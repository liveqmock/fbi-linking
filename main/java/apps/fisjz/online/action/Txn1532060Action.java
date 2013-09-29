package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2060Request.TIA2060;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.online.service.T2060Service;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 1532060 ����
 * zhanrui  20130923
 */
@Component
public class Txn1532060Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532060Action.class);

    @Autowired
    private T2060Service service;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2060Request");
        TIA2060 tia = null;
        try {
            tia = (TIA2060)dataFormat.fromMessage(new String(msg.msgBody), "TIA2060");
        } catch (Exception e) {
            logger.error("���Ľ�������:", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "���Ľ�������.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }
        Map paramMap = new HashMap();
        paramMap.put("branchId", msg.branchID);
        paramMap.put("tellerId", msg.tellerID);
        paramMap.put("tia", tia);

        //ҵ���߼�����
        service.processTxn(paramMap);

        msg.rtnCode = (String)paramMap.get("rtnCode");
        msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        return msg;
    }
}
