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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * �ɿ���ɿ�ҵ����
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


     //�ɿ�����Ϣ��ѯ
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
                .andCanceldateEqualTo("99999999")    //�ǳ�����¼
                .andAreaCodeEqualTo(areacode)
                .andArchiveFlagEqualTo("0");
        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 1) {
              return recordList.get(0);
        }else if (recordList.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("�ظ���¼��");
        }
    }

    //��ͨ�ɿ���ɿ� �跢���Զ����˽���
    //@Transactional  ������������
    @SuppressWarnings("unchecked")
    public void processTxn(Map paramMap){
        String branchId = (String)paramMap.get("branchId");
        String tellerId = (String)paramMap.get("tellerId");
        TIA2011 tia =  (TIA2011)paramMap.get("tia");
        String areacode = tia.getAreacode();

        List rtnlist = new ArrayList();
        FsJzfPaymentInfo fsJzfPaymentInfo = selectPaymentInfo(paramMap);
        String fbBookFlag = fsJzfPaymentInfo.getFbBookFlag();

        try {
            BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("���Ĵ������.", e);
        }

        if ("1".equals(fbBookFlag)) {  //�Ѽ��˵�δ���ˣ���ʱ���˱�־һ��Ϊ0
            //�Զ�����ɿ�ȷ�Ͻ���
            if (processPayConfirmTxn(fsJzfPaymentInfo, paramMap) == 0) {
                //ȫ������ɹ� ���±�־�ʹ���ʱ��
                fsJzfPaymentInfo.setRecfeeflag("1");  //!!
                paymentInfoMapper.updateByPrimaryKey(fsJzfPaymentInfo);
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
                paramMap.put("rtnMsg", "�ɿ�˴���ɹ���");
            }
            return;
        }

        //�������ͨѶ  �����ˡ����˱�־��Ϊ 0 �������
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        try {
            BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("���Ĵ������.", e);
        }

        paramList.add(fbPaynotesInfo);
        logger.info("[1532011�ɿ���ɿ�] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        rtnlist = service.updateNontaxPayment(helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        if (helper.getResponseResult(rtnlist)) { //�ɿ�ɹ�
            //��鷵�صĽɿ���Ϣ�Ƿ�������ı���һ��
            Map responseContentMap = (Map) rtnlist.get(0);
            FbResponseChkInfo respInfo = new FbResponseChkInfo();
            try {
                BeanUtils.populate(respInfo, responseContentMap);
            } catch (Exception e) {
                throw new RuntimeException("���Ĵ������.", e);
            }
            if (!fbPaynotesInfo.getBillid().equals(respInfo.getBillid()) ||
                    !fbPaynotesInfo.getPaynotescode().equals(respInfo.getPaynotescode()) ||
                    !fbPaynotesInfo.getNotescode().equals(respInfo.getNotescode())
                    ) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                paramMap.put("rtnMsg", "�ɿ��ʧ��!��ϸ�˶Բ���!");
                return;
            }

            //����������   ���±�־�ʹ���ʱ��
            stuffPaymentInfoBean_pay(areacode, branchId, tellerId, fsJzfPaymentInfo);
            paymentInfoMapper.updateByPrimaryKey(fsJzfPaymentInfo);

            //�Զ�����ɿ�ȷ�Ͻ���
            if (processPayConfirmTxn(fsJzfPaymentInfo, paramMap) == 0) { //��ʱ������ַ���1��������ظ����ף�
                //����ɹ����±�־
                fsJzfPaymentInfo.setRecfeeflag("1");  //!!
                paymentInfoMapper.updateByPrimaryKey(fsJzfPaymentInfo);
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
                paramMap.put("rtnMsg", "�ɿ�˴���ɹ���");
            }
        } else { //�ɿ�ʧ��
            String rtnMsg = helper.getResponseErrMsg(rtnlist);
            //TODO
            if (rtnMsg.contains("��ȷ���տ�����ظ�������")) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_PAY_REPEATED.getCode());
            } else {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            }
            paramMap.put("rtnMsg", rtnMsg);
        }
    }

    /**
     * ����ɿ�ȷ�Ͻ���  ���˷����в������ݿ⴦�������������
     * ���� 0�����˴���ɹ�   1������ʧ�ܣ�ʧ��ԭ�����ظ����˽���  -1�����״���ʧ��
     */
    @SuppressWarnings("unchecked")
    private int processPayConfirmTxn(FsJzfPaymentInfo fsJzfPaymentInfo, Map paramMap) {
        TIA2011 tia =  (TIA2011)paramMap.get("tia");

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        try {
            BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("���Ĵ������.", e);
        }

        paramList.add(fbPaynotesInfo);
        logger.info("[1532012�ɿ��鵽��ȷ��] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist =  service.accountNontaxPayment(
                helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //�жϲ�������Ӧ���
        if (helper.getResponseResult(rtnlist)) { //����ȷ�ϳɹ�
            //��鷵�صĽɿ���Ϣ�Ƿ�������ı���һ��
            Map responseContentMap = (Map) rtnlist.get(0);
            FbResponseChkInfo respInfo = new FbResponseChkInfo();
            try {
                BeanUtils.populate(respInfo, responseContentMap);
            } catch (Exception e) {
                throw new RuntimeException("���Ĵ������.", e);
            }
            if (!fbPaynotesInfo.getBillid().equals(respInfo.getBillid()) ||
                    !fbPaynotesInfo.getPaynotescode().equals(respInfo.getPaynotescode()) ||
                    !fbPaynotesInfo.getNotescode().equals(respInfo.getNotescode())
                    ) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                paramMap.put("rtnMsg", "���˽���ʧ��!��ϸ�˶Բ���!");
                return -1;
            }
            //ҵ��ɹ�
            return 0;
        }else{ //��������ʧ��
            String rtnMsg = helper.getResponseErrMsg(rtnlist);
            //TODO
            if (rtnMsg.contains("��ȷ���տ�����ظ�������")) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_PAY_REPEATED.getCode());
                paramMap.put("rtnMsg", rtnMsg);
                return 1;
            } else {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                paramMap.put("rtnMsg", rtnMsg);
                return -1;
            }
        }
    }

    //PaymentInfoBean ��д������Ϣ for ��ʼ����
    private void stuffPaymentInfoBean_pay(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        //�������˳ɹ�
        paymentInfo.setHostBookFlag("1");
        paymentInfo.setHostChkFlag("0");

        //�������˳ɹ�
        paymentInfo.setFbBookFlag("1");
        paymentInfo.setFbChkFlag("0");

        //������¼��־
        paymentInfo.setArchiveFlag("0");
        paymentInfo.setAreaCode(areaCode);
    }
}
