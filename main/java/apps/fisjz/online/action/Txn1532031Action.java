package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2031Request.TIA2031;
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
 * 1532031 �ɿ��˸�ȷ��
 * zhanrui  20130924
 */
@Component
public class Txn1532031Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532031Action.class);

    @Autowired
    private T2030Service service;

    @Override
    @SuppressWarnings("unchecked")
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2031Request");
        TIA2031 tia = null;
        try {
            tia = (TIA2031) dataFormat.fromMessage(new String(msg.msgBody), "TIA2031");
        } catch (Exception e) {
            logger.error("���Ľ�������:", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "���Ľ�������.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }
        Map paramMap = new HashMap();
        paramMap.put("branchId", msg.branchID);
        paramMap.put("tellerId", msg.tellerID);
        paramMap.put("tia", tia);

        //�������ݼ��
        FsJzfPaymentInfo fsJzfPaymentInfo = service.selectPaymentInfo(paramMap);
        if (fsJzfPaymentInfo == null) {//δ�鵽��¼
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "���Ȳ�ѯ�˸��ɿ��Ϣ.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }else {
            if ("1".equals(fsJzfPaymentInfo.getFbBookFlag())) {
                msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
                msg.msgBody = ("�˽ɿ���˸�,����:" + fsJzfPaymentInfo.getBankrecdate()).getBytes(THIRDPARTY_SERVER_CODING);
                return msg;
            }
        }

        //ҵ���߼�����
        service.processTxn(paramMap);

        msg.rtnCode = (String)paramMap.get("rtnCode");
        msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        return msg;
    }
}
