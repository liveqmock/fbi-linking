package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.domain.txn.Tia1001;
import apps.hmfsjm.gateway.domain.txn.Tia3001;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.gateway.domain.txn.Toa3001;
import apps.hmfsjm.gateway.service.ProtocolTxnService;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//	��ī����ά���ʽ��˿��ѯ
@Component
public class Txn1500620Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500620Action.class);
    @Autowired
    private ProtocolTxnService protocolTxnService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // �˿�����
        String billNo = fieldArray[0];

        logger.info("[1500620][3001][hmfsjm�˿��ѯ][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [�˿�����] " + billNo);

        Tia3001 tia = new Tia3001();
        tia.BODY.REFUND_BILLNO = billNo;
        try {
            Toa3001 toa = (Toa3001) protocolTxnService.execute("3001", tia);
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);
        } catch (Exception e) {
            logger.error("[1500620][3001][hmfsjm�˿��ѯ]ʧ��", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa3001 toa3001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3001.BODY.REFUND_BILLNO).append("|")                // �ɿ�����
                .append(nullToEmpty(toa3001.BODY.BILL_STS_CODE)).append("|")     // �ɿ״̬����
                .append(nullToEmpty(toa3001.BODY.BILL_STS_TITLE)).append("|")    // �ɿ״̬˵��
                .append(nullToEmpty(toa3001.BODY.RP_TYPE)).append("|")           // �˿����
                .append(nullToEmpty(toa3001.BODY.RP_MEMO)).append("|")           // �˿�����
                .append(nullToEmpty(toa3001.BODY.RP_MONEY)).append("|")          // �˿���
                .append(nullToEmpty(toa3001.BODY.PAY_BILL_NO)).append("|")       // �ɴ�֪ͨ����
                .append(nullToEmpty(toa3001.BODY.PAY_BANK)).append("|")          // �ɿ�����
                .append(nullToEmpty(toa3001.BODY.BANK_USER)).append("|")         // �����տ���
                .append(nullToEmpty(toa3001.BODY.BANK_CFM_DATE)).append("|")     // �����տ�����
                .append(nullToEmpty(toa3001.BODY.PAY_MONEY)).append("|")         // �����տ���
                .append(nullToEmpty(toa3001.BODY.HOUSE_ID)).append("|")          // ���ݱ��
                .append(nullToEmpty(toa3001.BODY.HOUSE_LOCATION)).append("|")    // ��������
                .append(nullToEmpty(toa3001.BODY.HOUSE_AREA)).append("|")        // �������
                .append(nullToEmpty(toa3001.BODY.STANDARD)).append("|")          // �ɴ��׼
                .append(nullToEmpty(toa3001.BODY.AREA_ACCOUNT)).append("|")      // ר���˺�
                .append(nullToEmpty(toa3001.BODY.HOUSE_ACCOUNT)).append("|")     // �ֻ��˺�
                .append(nullToEmpty(toa3001.BODY.CARD_TYPE)).append("|")         // ֤������
                .append(nullToEmpty(toa3001.BODY.CARD_NO)).append("|")           // ֤������
                .append(nullToEmpty(toa3001.BODY.OWNER)).append("|")             // ҵ������
                .append(nullToEmpty(toa3001.BODY.TEL)).append("|")               // ��ϵ�绰
                .append(nullToEmpty(toa3001.BODY.ACCEPT_DATE)).append("|")       // �ɴ���������
                .append(nullToEmpty(toa3001.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
