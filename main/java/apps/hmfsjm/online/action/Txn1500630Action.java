package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.online.service.Txn1500630Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	��ī����ά���ʽ�Ʊ������
@Component
public class Txn1500630Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500630Action.class);
    private Txn1500630Service txn1500630Service = new Txn1500630Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // ��ʼ���
        String vchStartNo = fieldArray[0];
        // �������
        String vchEndNo = fieldArray[1];

        logger.info("[1500630Ʊ������][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [��ʼ���] " + vchStartNo + "[�������]" + vchEndNo);
        try {
            long startNo = Long.parseLong(vchStartNo);
            long endNo = Long.parseLong(vchEndNo);
            if (startNo < endNo) {
                throw new RuntimeException("��ʼ��Ų���С����ֹ���");
            } else {
                txn1500630Service.process(msg.branchID, msg.tellerID, startNo, endNo);
            }

        } catch (Exception e) {
            logger.error("[1500630][hmfsjmƱ������]ʧ��", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

}
