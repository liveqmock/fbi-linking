package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.enums.VoucherStatus;
import apps.hmfsjm.online.service.Txn1500631Service;
import apps.hmfsjm.online.service.Txn1500632Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	Ʊ��ʹ���������
@Component
public class Txn1500632Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500632Action.class);
    private Txn1500632Service txn1500632Service = new Txn1500632Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // ����
        String date8 = fieldArray[0];

        logger.info("[1500632Ʊ��ʹ�������ѯ][����]" + date8 + " [����]" + msg.branchID + "[��Ա]" + msg.tellerID);

        try {
            String strVchs = txn1500632Service.process(date8);
            if (StringUtils.isEmpty(strVchs)) {
                msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
                msg.msgBody = "û�в�ѯ��������ʹ�õ�Ʊ��".getBytes(THIRDPARTY_SERVER_CODING);
            } else {
                msg.msgBody = strVchs.getBytes(THIRDPARTY_SERVER_CODING);
            }
        } catch (Exception e) {
            logger.error("[1500632][hmfsjmƱ��ʹ�������ѯ]ʧ��", e);
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
