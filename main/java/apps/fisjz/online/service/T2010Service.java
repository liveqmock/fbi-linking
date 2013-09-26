package apps.fisjz.online.service;

import apps.fisjz.domain.staring.T2010Request.TIA2010;
import apps.fisjz.domain.staring.T2010Response.TOA2010;
import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesInfo;
import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesItem;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import apps.fisjz.repository.model.FsJzfPaymentItem;
import apps.fisjz.repository.model.FsJzfPaymentItemExample;
import common.dataformat.SeperatedTextDataFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 缴款书信息查询处理
 * User: zhanrui
 * Date: 13-9-23
 */
@Service
public class T2010Service {
    private static final Logger logger = LoggerFactory.getLogger(T2010Service.class);

    @Autowired
    private ServiceHelper helper;
    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    FsJzfPaymentItemMapper paymentItemMapper;


    //缴款书信息查询
    public FsJzfPaymentInfo selectPaymentInfo(Map paramMap) {
        TIA2010 tia = (TIA2010) paramMap.get("tia");
        String areacode = tia.getAreacode();
        String notescode = tia.getNotescode();
        String checkcode = tia.getCheckcode();
        String billtype = tia.getBilltype();

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(notescode)
                .andCheckcodeEqualTo(checkcode)
                .andBilltypeEqualTo(billtype)
                .andAreaCodeEqualTo(areacode)
                .andCanceldateEqualTo("99999999")    //非冲销记录
                .andArchiveFlagEqualTo("0");
        List<FsJzfPaymentInfo> recordList = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 1) {
            return recordList.get(0);
        } else if (recordList.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("重复记录！");
        }
    }

    //缴款书信息查询
    public List<FsJzfPaymentItem> selectPaymentItem(String mainId) {
        FsJzfPaymentItemExample example = new FsJzfPaymentItemExample();
        example.createCriteria()
                .andMainidEqualTo(mainId);
        return paymentItemMapper.selectByExample(example);
    }

    //业务逻辑处理：本地无数据，需远程获取
    @SuppressWarnings("unchecked")
    @Transactional
    public void processTxn(Map paramMap) {
        String branchId = (String) paramMap.get("branchId");
        String tellerId = (String) paramMap.get("tellerId");
        TIA2010 tia = (TIA2010) paramMap.get("tia");
        String areacode = tia.getAreacode();

        SeperatedTextDataFormat dataFormat;//与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List rtnlist = null;
        try {
            rtnlist = service.queryNontaxPayment(
                    helper.getApplicationidByAreaCode(tia.getAreacode()),
                    helper.getBankCodeByAreaCode(tia.getAreacode()),
                    tia.getYear(),
                    helper.getFinorgByAreaCode(tia.getAreacode()),
                    tia.getNotescode(),
                    tia.getCheckcode(),
                    tia.getBilltype());
        } catch (Exception e) {
            throw new RuntimeException("与财政局连接失败.", e);
        }

        //判断财政局响应结果
        if (!helper.getResponseResult(rtnlist)) {
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
            return;
        }

        //处理财政局响应报文 1：处理财政局返回的缴款书主信息
        TOA2010 toa = new TOA2010();
        TOA2010PaynotesInfo paynotesInfo = new TOA2010PaynotesInfo();
        Map responseContentMap = (Map) rtnlist.get(0);
        try {
            BeanUtils.populate(paynotesInfo, responseContentMap);
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }
        toa.setPaynotesInfo(paynotesInfo);

        //处理财政局响应报文 2：处理财政局返回的缴款书明细子项目信息
        List<TOA2010PaynotesItem> paynotesItems = new ArrayList<TOA2010PaynotesItem>();
        List details = (List) responseContentMap.get("details");
        for (Object detail : details) {
            TOA2010PaynotesItem item = new TOA2010PaynotesItem();
            try {
                BeanUtils.populate(item, (Map) detail);
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }
            paynotesItems.add(item);
        }

        //处理财政局响应报文 3：计算明细子项目条数
        toa.setPaynotesItems(paynotesItems);
        toa.setItemNum("" + paynotesItems.size());

        //处理财政局响应报文 4：本地数据库处理（若表中不存在则先保存）
        initPaymentInfoAndPaymentItem(areacode, branchId, tellerId, paynotesInfo, paynotesItems);

        //组特色平台响应报文
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        modelObjectsMap.put(toa.getPaynotesInfo().getClass().getName(), toa.getPaynotesInfo());
        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Response");
        String result = null;
        try {
            result = (String) dataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("响应报文处理错误");
        }

        paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
        paramMap.put("rtnMsg", result);
    }

    //本地已有信息，不再去财政局查询，直接读取本地数据
    @SuppressWarnings("unchecked")
    public void processTxn_LocalInfo(Map paramMap) {
        String branchId = (String) paramMap.get("branchId");
        String tellerId = (String) paramMap.get("tellerId");
        TIA2010 tia = (TIA2010) paramMap.get("tia");
        String areacode = tia.getAreacode();
        FsJzfPaymentInfo fsJzfPaymentInfo = selectPaymentInfo(paramMap);

        //处理缴款书主信息
        TOA2010 toa = new TOA2010();
        TOA2010PaynotesInfo paynotesInfo = new TOA2010PaynotesInfo();
        try {
            BeanUtils.copyProperties(paynotesInfo, fsJzfPaymentInfo);
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }
        toa.setPaynotesInfo(paynotesInfo);

        //处理缴款书明细子项目信息
        List<TOA2010PaynotesItem> paynotesItems = new ArrayList<TOA2010PaynotesItem>();
        List<FsJzfPaymentItem> details = selectPaymentItem(paynotesInfo.getBillid());
        for (FsJzfPaymentItem detail : details) {
            TOA2010PaynotesItem item = new TOA2010PaynotesItem();
            try {
                BeanUtils.copyProperties(item, detail);
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }
            paynotesItems.add(item);
        }

        //计算明细子项目条数
        toa.setPaynotesItems(paynotesItems);
        toa.setItemNum("" + paynotesItems.size());

        //组特色平台响应报文
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        modelObjectsMap.put(toa.getPaynotesInfo().getClass().getName(), toa.getPaynotesInfo());
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Response");
        String result = null;
        try {
            result = (String) dataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("响应报文处理错误");
        }

        paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
        paramMap.put("rtnMsg", result);
    }


    //初始化缴款书主信息和子项目信息
    private void initPaymentInfoAndPaymentItem(String areaCode, String branchId, String tellerId,
                                               TOA2010PaynotesInfo paynotesInfo,
                                               List<TOA2010PaynotesItem> paynotesItems) {

        //初始化主表
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        try {
            BeanUtils.copyProperties(fsJzfPaymentInfo, paynotesInfo);
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }
        insertPaymentInfo_init(areaCode, branchId, tellerId, fsJzfPaymentInfo);

        //初始化子项目明细表
        FsJzfPaymentItemExample itemExample = new FsJzfPaymentItemExample();
        itemExample.createCriteria()
                .andPkidMaininfoEqualTo(fsJzfPaymentInfo.getPkid())
                .andMainidEqualTo(fsJzfPaymentInfo.getBillid());
        List<FsJzfPaymentItem> itemList = paymentItemMapper.selectByExample(itemExample);
        if (itemList.size() > 0) {
            throw new RuntimeException("此BillId下的子项目信息已存在.");
        }
        for (TOA2010PaynotesItem paynotesItem : paynotesItems) {
            FsJzfPaymentItem fsJzfPaymentItem = new FsJzfPaymentItem();
            try {
                BeanUtils.copyProperties(fsJzfPaymentItem, paynotesItem);
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }
            fsJzfPaymentItem.setPkidMaininfo(fsJzfPaymentInfo.getPkid());
            paymentItemMapper.insert(fsJzfPaymentItem);
        }
    }

    //查询时初始化记录
    private void insertPaymentInfo_init(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo) {
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        paymentInfo.setHostBookFlag("0");
        paymentInfo.setHostChkFlag("0");

        paymentInfo.setFbBookFlag("0");
        paymentInfo.setFbChkFlag("0");

        //正常记录标志
        paymentInfo.setArchiveFlag("0");

        paymentInfo.setCanceldate("99999999");
        paymentInfo.setAreaCode(areaCode);

        paymentInfoMapper.insert(paymentInfo);
    }
}
