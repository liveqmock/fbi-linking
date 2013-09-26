package apps.fisjz.online.service;

import apps.fisjz.domain.financebureau.FbPaynotesInfo;
import apps.fisjz.domain.financebureau.FbResponseChkInfo;
import apps.fisjz.domain.staring.T2011Request.TIA2011;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 缴款书缴款业务处理
 * User: zhanrui
 * Date: 13-9-23
 */
@Service
public class T2011Service {
    private static final Logger logger = LoggerFactory.getLogger(T2011Service.class);

    @Autowired
    private ServiceHelper helper;
    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    FsJzfPaymentItemMapper paymentItemMapper;

    //缴款书信息查询
    public FsJzfPaymentInfo selectPaymentInfo(Map paramMap){
        TIA2011 tia =  (TIA2011)paramMap.get("tia");
        String areacode = tia.getAreacode();
        String notescode = tia.getPaynotesInfo().getNotescode();
        String checkcode = tia.getPaynotesInfo().getCheckcode();
        String billtype = tia.getPaynotesInfo().getBilltype();

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(notescode)
                .andCheckcodeEqualTo(checkcode)
                .andBilltypeEqualTo(billtype)
                .andCanceldateEqualTo("99999999")    //非冲销记录
                .andAreaCodeEqualTo(areacode)
                .andArchiveFlagEqualTo("0");
        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 1) {
              return recordList.get(0);
        }else if (recordList.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("重复记录！");
        }
    }

    //普通缴款书缴款 需发起自动到账交易
    @SuppressWarnings("unchecked")
    @Transactional
    public void processTxn(Map paramMap){
        String branchId = (String)paramMap.get("branchId");
        String tellerId = (String)paramMap.get("tellerId");
        TIA2011 tia =  (TIA2011)paramMap.get("tia");
        String areacode = tia.getAreacode();

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        try {
            BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }

        paramList.add(fbPaynotesInfo);
        logger.info("[1532011缴款书缴款] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateNontaxPayment(helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        if (helper.getResponseResult(rtnlist)) { //缴款成功
            //检查返回的缴款信息是否与请求的报文一致
            Map responseContentMap = (Map) rtnlist.get(0);
            FbResponseChkInfo respInfo = new FbResponseChkInfo();
            try {
                BeanUtils.populate(respInfo, responseContentMap);
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }
            if (!fbPaynotesInfo.getBillid().equals(respInfo.getBillid()) ||
                    !fbPaynotesInfo.getPaynotescode().equals(respInfo.getPaynotescode()) ||
                    !fbPaynotesInfo.getNotescode().equals(respInfo.getNotescode())
                    ) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                paramMap.put("rtnMsg", "缴款交易失败!明细核对不符!");
                return;
            }

            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            String rtnMsg = helper.getResponseErrMsg(rtnlist);
            if (StringUtils.isEmpty(rtnMsg)) {
                paramMap.put("rtnMsg", "缴款成功.");
            } else {
                paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
            }

            //自动发起缴款确认交易
            rtnlist = processPayConfirmTxn(tia);
            if (!helper.getResponseResult(rtnlist)) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
            } else {
                //全部处理成功 更新标志和处理时间
                FsJzfPaymentInfo record = selectPaymentInfo(paramMap);
                stuffPaymentInfoBean_pay(areacode, branchId, tellerId, record);
                paymentInfoMapper.updateByPrimaryKey(record);
            }
        } else { //缴款失败
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
        }
    }

    //发起缴款确认交易
    private List processPayConfirmTxn(final TIA2011 tia) {
        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        try {
            BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }

        paramList.add(fbPaynotesInfo);
        logger.info("[1532012缴款书到帐确认] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist =  service.accountNontaxPayment(
                helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //判断财政局响应结果
        if (helper.getResponseResult(rtnlist)) { //到帐确认成功
            //业务逻辑处理
            FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
            try {
                BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }
            updatePaymentPayAccountFlag(tia.getAreacode(), fsJzfPaymentInfo);
        }
        return rtnlist;
    }

    //更新到账标志
    private void updatePaymentPayAccountFlag(String areaCode, FsJzfPaymentInfo paymentInfo){
        String notesCode = paymentInfo.getNotescode();
        if (StringUtils.isEmpty(notesCode)) {
            throw new RuntimeException("票据编号不能为空!");
        }

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        List<String> billTypes = new ArrayList<String>();
        billTypes.add("0");
        billTypes.add("1");
        example.createCriteria()
                .andNotescodeEqualTo(notesCode)
                .andBilltypeIn(billTypes)
                .andAreaCodeEqualTo(areaCode)
                .andArchiveFlagEqualTo("0");

        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() != 1) {
            throw new RuntimeException("此票据编号不能对应多条记录!");
        }

        FsJzfPaymentInfo record = recordList.get(0);
        record.setRecfeeflag("1");  //!!
        paymentInfoMapper.updateByPrimaryKey(record);
    }


    //=======private method=========================================
    //PaymentInfoBean 填写基本信息 for 初始记账
    private void stuffPaymentInfoBean_pay(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        //主机记账成功
        paymentInfo.setHostBookFlag("1");
        paymentInfo.setHostChkFlag("0");

        //财政记账成功
        paymentInfo.setFbBookFlag("1");
        paymentInfo.setFbChkFlag("0");

        //正常记录标志
        paymentInfo.setArchiveFlag("0");
        paymentInfo.setAreaCode(areaCode);
    }
}
