package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.online.service.Txn1500610Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	��ī����ά���ʽ�ɿ��ѯ
@Component
public class Txn1500610Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500610Action.class);
    private Txn1500610Service txn1500610Service = new Txn1500610Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // �ɿ�����
        String billNo = fieldArray[0];

        logger.info("[1500610�ɿ�����Ϣ��ѯ][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [�ɿ�����] " + billNo);


        try {

            Toa1001 toa = (Toa1001) txn1500610Service.process(msg.tellerID, msg.branchID, billNo);
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);

        } catch (Exception e) {
            logger.error("[1500610][1001][hmfsjm�ɿ��ѯ]ʧ��", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa1001 toa1001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa1001.BODY.PAY_BILLNO).append("|")                   // �ɿ�����
                .append(nullToEmpty(toa1001.BODY.BILL_STS_CODE)).append("|")     // �ɿ״̬����
                .append(nullToEmpty(toa1001.BODY.BILL_STS_TITLE)).append("|")    // �ɿ״̬˵��
                .append(nullToEmpty(toa1001.BODY.HOUSE_ID)).append("|")          // ���ݱ��
                .append(nullToEmpty(toa1001.BODY.HOUSE_LOCATION)).append("|")    // ��������
                .append(nullToEmpty(toa1001.BODY.HOUSE_AREA)).append("|")        // �������
                .append(nullToEmpty(toa1001.BODY.STANDARD)).append("|")          // �ɴ��׼
                .append(nullToEmpty(toa1001.BODY.TXN_AMT)).append("|")           // ���
                .append(nullToEmpty(toa1001.BODY.PAY_BANK)).append("|")          // �ɿ�����
                .append(nullToEmpty(toa1001.BODY.AREA_ACCOUNT)).append("|")      // ר���˺�
                .append(nullToEmpty(toa1001.BODY.HOUSE_ACCOUNT)).append("|")     // �ֻ��˺�
                .append(nullToEmpty(toa1001.BODY.CARD_TYPE)).append("|")         // ֤������
                .append(nullToEmpty(toa1001.BODY.CARD_NO)).append("|")           // ֤������
                .append(nullToEmpty(toa1001.BODY.OWNER)).append("|")             // ҵ������
                .append(nullToEmpty(toa1001.BODY.TEL)).append("|")               // ��ϵ�绰
                .append(nullToEmpty(toa1001.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
