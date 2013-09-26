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
 * �ɿ�����Ϣ��ѯ����
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


    //�ɿ�����Ϣ��ѯ
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
                .andCanceldateEqualTo("99999999")    //�ǳ�����¼
                .andArchiveFlagEqualTo("0");
        List<FsJzfPaymentInfo> recordList = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 1) {
            return recordList.get(0);
        } else if (recordList.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("�ظ���¼��");
        }
    }

    //�ɿ�����Ϣ��ѯ
    public List<FsJzfPaymentItem> selectPaymentItem(String mainId) {
        FsJzfPaymentItemExample example = new FsJzfPaymentItemExample();
        example.createCriteria()
                .andMainidEqualTo(mainId);
        return paymentItemMapper.selectByExample(example);
    }

    //ҵ���߼��������������ݣ���Զ�̻�ȡ
    @SuppressWarnings("unchecked")
    @Transactional
    public void processTxn(Map paramMap) {
        String branchId = (String) paramMap.get("branchId");
        String tellerId = (String) paramMap.get("tellerId");
        TIA2010 tia = (TIA2010) paramMap.get("tia");
        String areacode = tia.getAreacode();

        SeperatedTextDataFormat dataFormat;//�������ͨѶ
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
            throw new RuntimeException("�����������ʧ��.", e);
        }

        //�жϲ�������Ӧ���
        if (!helper.getResponseResult(rtnlist)) {
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
            return;
        }

        //�����������Ӧ���� 1����������ַ��صĽɿ�������Ϣ
        TOA2010 toa = new TOA2010();
        TOA2010PaynotesInfo paynotesInfo = new TOA2010PaynotesInfo();
        Map responseContentMap = (Map) rtnlist.get(0);
        try {
            BeanUtils.populate(paynotesInfo, responseContentMap);
        } catch (Exception e) {
            throw new RuntimeException("���Ĵ������.", e);
        }
        toa.setPaynotesInfo(paynotesInfo);

        //�����������Ӧ���� 2����������ַ��صĽɿ�����ϸ����Ŀ��Ϣ
        List<TOA2010PaynotesItem> paynotesItems = new ArrayList<TOA2010PaynotesItem>();
        List details = (List) responseContentMap.get("details");
        for (Object detail : details) {
            TOA2010PaynotesItem item = new TOA2010PaynotesItem();
            try {
                BeanUtils.populate(item, (Map) detail);
            } catch (Exception e) {
                throw new RuntimeException("���Ĵ������.", e);
            }
            paynotesItems.add(item);
        }

        //�����������Ӧ���� 3��������ϸ����Ŀ����
        toa.setPaynotesItems(paynotesItems);
        toa.setItemNum("" + paynotesItems.size());

        //�����������Ӧ���� 4���������ݿ⴦�������в��������ȱ��棩
        initPaymentInfoAndPaymentItem(areacode, branchId, tellerId, paynotesInfo, paynotesItems);

        //����ɫƽ̨��Ӧ����
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        modelObjectsMap.put(toa.getPaynotesInfo().getClass().getName(), toa.getPaynotesInfo());
        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Response");
        String result = null;
        try {
            result = (String) dataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("��Ӧ���Ĵ������");
        }

        paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
        paramMap.put("rtnMsg", result);
    }

    //����������Ϣ������ȥ�����ֲ�ѯ��ֱ�Ӷ�ȡ��������
    @SuppressWarnings("unchecked")
    public void processTxn_LocalInfo(Map paramMap) {
        String branchId = (String) paramMap.get("branchId");
        String tellerId = (String) paramMap.get("tellerId");
        TIA2010 tia = (TIA2010) paramMap.get("tia");
        String areacode = tia.getAreacode();
        FsJzfPaymentInfo fsJzfPaymentInfo = selectPaymentInfo(paramMap);

        //����ɿ�������Ϣ
        TOA2010 toa = new TOA2010();
        TOA2010PaynotesInfo paynotesInfo = new TOA2010PaynotesInfo();
        try {
            BeanUtils.copyProperties(paynotesInfo, fsJzfPaymentInfo);
        } catch (Exception e) {
            throw new RuntimeException("���Ĵ������.", e);
        }
        toa.setPaynotesInfo(paynotesInfo);

        //����ɿ�����ϸ����Ŀ��Ϣ
        List<TOA2010PaynotesItem> paynotesItems = new ArrayList<TOA2010PaynotesItem>();
        List<FsJzfPaymentItem> details = selectPaymentItem(paynotesInfo.getBillid());
        for (FsJzfPaymentItem detail : details) {
            TOA2010PaynotesItem item = new TOA2010PaynotesItem();
            try {
                BeanUtils.copyProperties(item, detail);
            } catch (Exception e) {
                throw new RuntimeException("���Ĵ������.", e);
            }
            paynotesItems.add(item);
        }

        //������ϸ����Ŀ����
        toa.setPaynotesItems(paynotesItems);
        toa.setItemNum("" + paynotesItems.size());

        //����ɫƽ̨��Ӧ����
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        modelObjectsMap.put(toa.getPaynotesInfo().getClass().getName(), toa.getPaynotesInfo());
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Response");
        String result = null;
        try {
            result = (String) dataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("��Ӧ���Ĵ������");
        }

        paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
        paramMap.put("rtnMsg", result);
    }


    //��ʼ���ɿ�������Ϣ������Ŀ��Ϣ
    private void initPaymentInfoAndPaymentItem(String areaCode, String branchId, String tellerId,
                                               TOA2010PaynotesInfo paynotesInfo,
                                               List<TOA2010PaynotesItem> paynotesItems) {

        //��ʼ������
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        try {
            BeanUtils.copyProperties(fsJzfPaymentInfo, paynotesInfo);
        } catch (Exception e) {
            throw new RuntimeException("���Ĵ������.", e);
        }
        insertPaymentInfo_init(areaCode, branchId, tellerId, fsJzfPaymentInfo);

        //��ʼ������Ŀ��ϸ��
        FsJzfPaymentItemExample itemExample = new FsJzfPaymentItemExample();
        itemExample.createCriteria()
                .andPkidMaininfoEqualTo(fsJzfPaymentInfo.getPkid())
                .andMainidEqualTo(fsJzfPaymentInfo.getBillid());
        List<FsJzfPaymentItem> itemList = paymentItemMapper.selectByExample(itemExample);
        if (itemList.size() > 0) {
            throw new RuntimeException("��BillId�µ�����Ŀ��Ϣ�Ѵ���.");
        }
        for (TOA2010PaynotesItem paynotesItem : paynotesItems) {
            FsJzfPaymentItem fsJzfPaymentItem = new FsJzfPaymentItem();
            try {
                BeanUtils.copyProperties(fsJzfPaymentItem, paynotesItem);
            } catch (Exception e) {
                throw new RuntimeException("���Ĵ������.", e);
            }
            fsJzfPaymentItem.setPkidMaininfo(fsJzfPaymentInfo.getPkid());
            paymentItemMapper.insert(fsJzfPaymentItem);
        }
    }

    //��ѯʱ��ʼ����¼
    private void insertPaymentInfo_init(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo) {
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        paymentInfo.setHostBookFlag("0");
        paymentInfo.setHostChkFlag("0");

        paymentInfo.setFbBookFlag("0");
        paymentInfo.setFbChkFlag("0");

        //������¼��־
        paymentInfo.setArchiveFlag("0");

        paymentInfo.setCanceldate("99999999");
        paymentInfo.setAreaCode(areaCode);

        paymentInfoMapper.insert(paymentInfo);
    }
}
