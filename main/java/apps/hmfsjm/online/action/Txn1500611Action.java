package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.BillTxnStatus;
import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.domain.txn.Toa2001;
import apps.hmfsjm.online.service.Txn1500611Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	��ī����ά���ʽ�ɿ�ȷ��
@Component
public class Txn1500611Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500611Action.class);
    private Txn1500611Service txn1500611Service = new Txn1500611Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // �ɿ�����
        String billNo = fieldArray[0];

        logger.info("[1500611][2001][hmfsjm�ɿ���ɿ�ȷ��][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [�ɿ�����] " + billNo);

        // TODO ������ �ظ�����

        if (true) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(billNo).append("|")                   // �ɿ�����
                    .append(nullToEmpty("0000")).append("|")     // �ɿ״̬����
                    .append(nullToEmpty("�ɿ�ɹ�")).append("|")    // �ɿ״̬˵��
                    .append(nullToEmpty("")).append("|");          // ������
            msg.msgBody = strBuilder.toString().getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }

        try {

            Toa2001 toa = (Toa2001) txn1500611Service.process(msg.tellerID, msg.serialNo, billNo);
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);
            if (!BillTxnStatus.PAYED_SECCESS.getCode().equals(toa.BODY.BILL_STS_CODE)
                    && !BillTxnStatus.CONFIRMED.getCode().equals(toa.BODY.BILL_STS_CODE)) {
                throw new RuntimeException(StringUtils.isEmpty(toa.BODY.BILL_STS_CODE + toa.BODY.BILL_STS_TITLE) ?
                        (toa.INFO.RET_CODE + toa.INFO.ERR_MSG) : (toa.BODY.BILL_STS_CODE + toa.BODY.BILL_STS_TITLE));
            }
        } catch (Exception e) {
            logger.error("[1500611][2001][hmfsjm�ɿ�ɿ�ȷ��]ʧ��", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa2001 toa2001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa2001.BODY.PAY_BILLNO).append("|")                   // �ɿ�����
                .append(nullToEmpty(toa2001.BODY.BILL_STS_CODE)).append("|")     // �ɿ״̬����
                .append(nullToEmpty(toa2001.BODY.BILL_STS_TITLE)).append("|")    // �ɿ״̬˵��
                .append(nullToEmpty(toa2001.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
