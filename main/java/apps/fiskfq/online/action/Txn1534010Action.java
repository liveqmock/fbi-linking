package apps.fiskfq.online.action;

import apps.fiskfq.enums.TxnRtnCode;
import apps.fiskfq.gateway.domain.txn.Tia2401;
import apps.fiskfq.gateway.domain.txn.Toa1401;
import apps.fiskfq.online.service.Txn1534010Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	�ɿ��ѯ
@Component
public class Txn1534010Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1534010Action.class);
    private Txn1534010Service txn1534010Service = new Txn1534010Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        /*
        billtype_code	�ɿ�����ʽ����
bill_no	Ʊ��
verify_no	ȫƱ��У����
bill_money	�տ���
set_year	���
         */

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // �ɿ�����ʽ����
        String typeCode = fieldArray[0];
        // Ʊ��
        String billNo = fieldArray[1];
        // ȫƱ��У����
        String verifyNo = fieldArray[2];
        // �տ���
        String billMoney = fieldArray[3];
        // ���
        String setYear = fieldArray[4];

        logger.info("[1534010�ɿ���Ϣ��ѯ][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [��ʽ��]" + typeCode + "  [�ɿ�����] " + billNo);


        try {
            Tia2401 tia = new Tia2401();
            tia.Body.Object.Record.billtype_code = typeCode;
            tia.Body.Object.Record.bill_no = billNo;
            tia.Body.Object.Record.verify_no = verifyNo;
            tia.Body.Object.Record.bill_money = billMoney;
            tia.Body.Object.Record.set_year = setYear;
            tia.Head.msgId = msg.txnTime + msg.serialNo;
            tia.Head.workDate = msg.txnTime.substring(0, 8);

            Toa1401 toa = (Toa1401) txn1534010Service.process(msg.tellerID, msg.branchID, tia);
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);


        } catch (Exception e) {
            logger.error("[1534010][fiskfqӦ�����ݲ�ѯ]ʧ��", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_EXECUTE_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa1401 toa1401) {
        StringBuilder strBuilder = new StringBuilder();
        // ƴ�ӱ���
        strBuilder.append(toa1401.Body.Object.Record.chr_id).append("|")                      // �ɿ���ID
                .append(nullToEmpty(toa1401.Body.Object.Record.billtype_code)).append("|")    // �ɿ�����ʽ����
                .append(nullToEmpty(toa1401.Body.Object.Record.billtype_name)).append("|")    // �ɿ�����ʽ����
                .append(nullToEmpty(toa1401.Body.Object.Record.bill_no)).append("|")          // Ʊ��
                .append(nullToEmpty(toa1401.Body.Object.Record.makedate)).append("|")         // ��Ʊ����
                .append(nullToEmpty(toa1401.Body.Object.Record.ien_code)).append("|")         // ִ�յ�λҵ����
                .append(nullToEmpty(toa1401.Body.Object.Record.ien_name)).append("|")         // ִ�յ�λ����
                .append(nullToEmpty(toa1401.Body.Object.Record.consign_ien_code)).append("|") // ί�е�λҵ����
                .append(nullToEmpty(toa1401.Body.Object.Record.consign_ien_name)).append("|") // ί�е�λ����
                .append(nullToEmpty(toa1401.Body.Object.Record.pm_code)).append("|")          // �ɿʽ����
                .append(nullToEmpty(toa1401.Body.Object.Record.pm_name)).append("|")          // �ɿʽ����
                .append(nullToEmpty(toa1401.Body.Object.Record.cheque_no)).append("|")        // �����
                .append(nullToEmpty(toa1401.Body.Object.Record.payer)).append("|")            // �ɿ���ȫ��
                .append(nullToEmpty(toa1401.Body.Object.Record.payerbank)).append("|")        // �ɿ����˻�������
                .append(nullToEmpty(toa1401.Body.Object.Record.payeraccount)).append("|")     // �ɿ����˺�
                .append(nullToEmpty(toa1401.Body.Object.Record.receiver)).append("|")         // �տ���ȫ��
                .append(nullToEmpty(toa1401.Body.Object.Record.receiverbank)).append("|")     // �տ����˻�������
                .append(nullToEmpty(toa1401.Body.Object.Record.receiveraccount)).append("|")  // �տ����˺�
                .append(nullToEmpty(toa1401.Body.Object.Record.verify_no)).append("|")        // ȫƱ��У����
                .append(nullToEmpty(toa1401.Body.Object.Record.rg_code)).append("|")          // ������
                .append(nullToEmpty(toa1401.Body.Object.Record.receivetype)).append("|")      // ���շ�ʽ
                .append(nullToEmpty(toa1401.Body.Object.Record.inputername)).append("|")      // ����������
                .append(nullToEmpty(toa1401.Body.Object.Record.is_consign)).append("|")       // �Ƿ�ί��
                .append(nullToEmpty(toa1401.Body.Object.Record.lateflag)).append("|")         // �Ƿ�¼
                .append(nullToEmpty(toa1401.Body.Object.Record.nosource_ids)).append("|")     // ��������ID����
                .append(nullToEmpty(toa1401.Body.Object.Record.supplytemplet_id)).append("|") // ��������ģ��ID
                .append(nullToEmpty(toa1401.Body.Object.Record.supplytemplet_id)).append("|") // ��������ģ��ID
                .append(nullToEmpty(toa1401.Body.Object.Record.remark)).append("|");          // ��ע

        strBuilder.append(String.valueOf(toa1401.Body.Object.Record.Object.size())).append("|");// ��ϸ��¼��

        for (Toa1401.Record.DetailRecord record : toa1401.Body.Object.Record.Object) {
            strBuilder.append(nullToEmpty(record.chr_id)).append(",")           // �ɿ�����ϸID
                    .append(nullToEmpty(record.main_id)).append(",")            // �ɿ���ID
                    .append(nullToEmpty(record.in_bis_code)).append(",")        // ������Ŀҵ����
                    .append(nullToEmpty(record.in_bis_name)).append(",")        // ������Ŀ����
                    .append(nullToEmpty(record.measure)).append(",")            // ���յ�λ
                    .append(nullToEmpty(record.chargenum)).append(",")          // ��������
                    .append(nullToEmpty(record.chargestandard)).append(",")     // �շѱ�׼
                    .append(nullToEmpty(record.chargemoney)).append(",")        // ������
                    .append(nullToEmpty(record.item_chkcode)).append("|");      // ��λ��ĿУ����
        }

        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}