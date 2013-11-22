package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.enums.VoucherStatus;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.online.service.Txn1500631Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	��ī����ά���ʽ�Ʊ��ʹ�ú�����
@Component
public class Txn1500631Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500631Action.class);
    private Txn1500631Service txn1500631Service = new Txn1500631Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // Ʊ�ݱ��
        String vchNo = fieldArray[1];
        // Ʊ��״̬
        String vchSts = fieldArray[2];
        // �ɿ���
        String billNo = fieldArray[0];

        if (VoucherStatus.USED.getCode().equals(vchSts)) {
            if (StringUtils.isEmpty(billNo)) {
                msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
                msg.msgBody = "ʹ��Ʊ��ʱ��������ɿ��".getBytes(THIRDPARTY_SERVER_CODING);
                return msg;
            }
        }

        logger.info("[1500631Ʊ��ʹ��������][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [Ʊ�ݱ��] " + vchNo + "[Ʊ��״̬]" + vchSts + "[�ɿ���]" + billNo);


        try {
            txn1500631Service.process(msg.branchID, msg.tellerID, vchNo, billNo, vchSts);
        } catch (Exception e) {
            logger.error("[1500631][hmfsjmƱ��ʹ��������]ʧ��", e);
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
