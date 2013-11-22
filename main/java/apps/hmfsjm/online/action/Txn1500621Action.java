package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.txn.Tia3002;
import apps.hmfsjm.gateway.domain.txn.Toa3002;
import apps.hmfsjm.online.service.Txn1500621Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//	��ī����ά���ʽ��˿�ȷ��
@Component
public class Txn1500621Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500621Action.class);
    private Txn1500621Service txn1500621Service = new Txn1500621Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // �˿�����
        String refundNo = fieldArray[0];

        logger.info("[1500621][3002][hmfsjm�˿����˿�ȷ��][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [�˿�����] " + refundNo);

        try {
            // ���׷���
            Toa3002 toa = (Toa3002) txn1500621Service.process(msg.tellerID, msg.branchID, msg.serialNo, refundNo);
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);
        } catch (Exception e) {
            logger.error("[1500621][3002][hmfsjm�˿�˿�ȷ��]ʧ��", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa3002 toa3002) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3002.BODY.REFUND_BILLNO).append("|")                // �˿�����
                .append(nullToEmpty(toa3002.BODY.BILL_STS_CODE)).append("|")     // �˿״̬����
                .append(nullToEmpty(toa3002.BODY.BILL_STS_TITLE)).append("|")    // �˿״̬˵��
                .append(nullToEmpty(toa3002.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
