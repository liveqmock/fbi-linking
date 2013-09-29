package apps.fisjz.online.service;

import apps.fisjz.domain.financebureau.FbPaynotesInfo4Manual;
import apps.fisjz.domain.financebureau.FbPaynotesItem;
import apps.fisjz.domain.staring.T2013Request.TIA2013;
import apps.fisjz.domain.staring.T2013Request.TIA2013PaynotesItem;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import apps.fisjz.repository.model.FsJzfPaymentItem;
import apps.fisjz.repository.model.FsJzfPaymentItemExample;
import org.apache.commons.beanutils.BeanUtils;
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
 * �ɿ���ɿ�ҵ���� ���ֹ�Ʊ��
 * User: zhanrui
 * Date: 13-9-26
 */
@Service
public class T2013Service {
    private static final Logger logger = LoggerFactory.getLogger(T2013Service.class);

    @Autowired
    private ServiceHelper helper;
    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    FsJzfPaymentItemMapper paymentItemMapper;

    //�ɿ�����Ϣ��ѯ
    public FsJzfPaymentInfo selectPaymentInfo(Map paramMap) {
        TIA2013 tia = (TIA2013) paramMap.get("tia");
        String areacode = tia.getAreacode();
        String notescode = tia.getPaynotesInfo().getNotescode();
        String checkcode = tia.getPaynotesInfo().getCheckcode();
        String billtype = tia.getPaynotesInfo().getBilltype();

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(notescode)
                .andCheckcodeEqualTo(checkcode)
                .andBilltypeEqualTo(billtype)
                .andCanceldateEqualTo("99999999")    //�ǳ�����¼
                .andAreaCodeEqualTo(areacode)
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


    @SuppressWarnings("unchecked")
    @Transactional
    public void processTxn(Map paramMap) {
        String branchId = (String) paramMap.get("branchId");
        String tellerId = (String) paramMap.get("tellerId");
        TIA2013 tia = (TIA2013) paramMap.get("tia");
        String areacode = tia.getAreacode();

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo4Manual> paramList = new ArrayList<FbPaynotesInfo4Manual>();
        FbPaynotesInfo4Manual fbPaynotesInfo = new FbPaynotesInfo4Manual();
        try {
            BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("���Ĵ������.", e);
        }

        List<TIA2013PaynotesItem> tiaItems = tia.getPaynotesItems();
        List<FbPaynotesItem> fbItems = new ArrayList<FbPaynotesItem>();
        for (TIA2013PaynotesItem tiaItem : tiaItems) {
            FbPaynotesItem fbItem = new FbPaynotesItem();
            try {
                BeanUtils.copyProperties(fbItem, tiaItem);
            } catch (Exception e) {
                throw new RuntimeException("���Ĵ������.", e);
            }
            fbItems.add(fbItem);
        }
        fbPaynotesInfo.setDetails(fbItems);
        paramList.add(fbPaynotesInfo);

        logger.info("[1532013�ֹ��ɿ���ɿ�] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist = service.insertNontaxPayment(
                helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //�жϲ�������Ӧ���
        if (helper.getResponseResult(rtnlist)) { //�ɿ�ɹ�
            //���汾����Ϣ TODO ��Է��ص���ϸ���к˶ԣ�
            initPaymentInfoAndPaymentItem(areacode, branchId, tellerId, tia);
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            paramMap.put("rtnMsg", "�ֹ�Ʊ�ɿ�ɹ�");
        } else { //�ɿ�ʧ��
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
        }
    }


    //��ʼ���ɿ�������Ϣ������Ŀ��Ϣ
    private void initPaymentInfoAndPaymentItem(String areaCode, String branchId, String tellerId, TIA2013 tia) {
        //��ʼ������
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        try {
            BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("���Ĵ������.", e);
        }
        insertPaymentInfo(areaCode, branchId, tellerId, fsJzfPaymentInfo);

        //��ʼ������Ŀ��ϸ��
        FsJzfPaymentItemExample itemExample = new FsJzfPaymentItemExample();
        itemExample.createCriteria().andMainidEqualTo(fsJzfPaymentInfo.getBillid());
        List<FsJzfPaymentItem> itemList = paymentItemMapper.selectByExample(itemExample);
        if (itemList.size() > 0) {
            throw new RuntimeException("��BillId�µ�����Ŀ��Ϣ�Ѵ���.");
        }
        for (TIA2013PaynotesItem paynotesItem : tia.getPaynotesItems()) {
            FsJzfPaymentItem fsJzfPaymentItem = new FsJzfPaymentItem();
            try {
                BeanUtils.copyProperties(fsJzfPaymentItem, paynotesItem);
            } catch (Exception e) {
                throw new RuntimeException("���Ĵ������.", e);
            }
            paymentItemMapper.insert(fsJzfPaymentItem);
        }
    }

    private void insertPaymentInfo(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        paymentInfo.setHostBookFlag("1");
        paymentInfo.setHostChkFlag("0");

        paymentInfo.setFbBookFlag("1");
        paymentInfo.setFbChkFlag("0");

        paymentInfo.setArchiveFlag("0");    //������¼��־
        paymentInfo.setCanceldate("99999999");
        paymentInfo.setAreaCode(areaCode);
        paymentInfo.setHostAckFlag("0");  //��������ȷ�ϱ�־

        paymentInfoMapper.insert(paymentInfo);
    }
}
