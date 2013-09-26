package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2030Request.TIA2030;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.online.service.T2030Service;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 1532030 �ɿ��˸���ѯ
 * zhanrui  20130924
 */

@Component
public class Txn1532030Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532030Action.class);

    @Autowired
    private T2030Service service;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2030Request");
        TIA2030 tia = null;
        try {
            tia = (TIA2030) dataFormat.fromMessage(new String(msg.msgBody), "TIA2030");
        } catch (Exception e) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "���Ľ�������.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }
        Map paramMap = new HashMap();
        paramMap.put("branchId", msg.branchID);
        paramMap.put("tellerId", msg.tellerID);
        paramMap.put("tia", tia);

        //���ұ��ؼ�¼
        FsJzfPaymentInfo fsJzfPaymentInfo = service.selectPaymentInfo(paramMap);
        if (fsJzfPaymentInfo == null) {//����δ�鵽��Ϣ
            service.processTxn(paramMap); //ȡ�����ַ�������Ϣ
            msg.rtnCode = (String)paramMap.get("rtnCode");
            msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        } else {
            if ("0".equals(fsJzfPaymentInfo.getFbBookFlag())) {//�������г�ʼ��Ϣ����δ�ɿ�
                service.processTxn_LocalInfo(paramMap); //ȡ������Ϣ
                msg.rtnCode = (String)paramMap.get("rtnCode");
                msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
            } else { //�ظ��˸�
                msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
                msg.msgBody =  "�˱ʽɿ����˸�".getBytes(THIRDPARTY_SERVER_CODING);
            }
        }
        return msg;
    }
}
