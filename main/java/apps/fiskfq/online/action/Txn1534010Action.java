package apps.fiskfq.online.action;

import apps.fiskfq.enums.TxnRtnCode;
import apps.fiskfq.gateway.domain.base.Attribute;
import apps.fiskfq.gateway.domain.base.ToaXml;
import apps.fiskfq.gateway.domain.txn.Tia2401;
import apps.fiskfq.gateway.domain.txn.Toa1401;
import apps.fiskfq.online.service.Txn1534010Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            ToaXml toa = (ToaXml) txn1534010Service.process(msg.tellerID, msg.branchID, tia);

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


    private String assembleStr(ToaXml toa) {
        StringBuilder strBuilder = new StringBuilder();
        Map<String, String> infoMap = toa.getMaininfoMap();
        // ƴ�ӱ���
        strBuilder.append(infoMap.get("CHR_ID")).append("|")                      // �ɿ���ID
                .append(nullToEmpty(infoMap.get("BILLTYPE_CODE"))).append("|")    // �ɿ�����ʽ����
                .append(nullToEmpty(infoMap.get("BILLTYPE_NAME"))).append("|")    // �ɿ�����ʽ����
                .append(nullToEmpty(infoMap.get("BILL_NO"))).append("|")          // Ʊ��
                .append(nullToEmpty(infoMap.get("MAKEDATE"))).append("|")         // ��Ʊ����
                .append(nullToEmpty(infoMap.get("IEN_CODE"))).append("|")         // ִ�յ�λҵ����
                .append(nullToEmpty(infoMap.get("IEN_NAME"))).append("|")         // ִ�յ�λ����
                .append(nullToEmpty(infoMap.get("CONSIGN_IEN_CODE"))).append("|") // ί�е�λҵ����
                .append(nullToEmpty(infoMap.get("CONSIGN_IEN_NAME"))).append("|") // ί�е�λ����
                .append(nullToEmpty(infoMap.get("PM_CODE"))).append("|")               // �ɿʽ����
                .append(nullToEmpty(infoMap.get("PM_NAME"))).append("|")              // �ɿʽ����
                .append(nullToEmpty(infoMap.get("CHEQUE_NO"))).append("|")          // �����
                .append(nullToEmpty(infoMap.get("PAYER"))).append("|")                   // �ɿ���ȫ��
                .append(nullToEmpty(infoMap.get("PAYERBANK"))).append("|")           // �ɿ����˻�������
                .append(nullToEmpty(infoMap.get("PAYERACCOUNT"))).append("|")     // �ɿ����˺�
                .append(nullToEmpty(infoMap.get("RECEIVER"))).append("|")              // �տ���ȫ��
                .append(nullToEmpty(infoMap.get("RECEIVERBANK"))).append("|")      // �տ����˻�������
                .append(nullToEmpty(infoMap.get("RECEIVERACCOUNT"))).append("|")  // �տ����˺�
                .append(nullToEmpty(infoMap.get("VERIFY_NO"))).append("|")            // ȫƱ��У����
                .append(nullToEmpty(infoMap.get("RG_CODE"))).append("|")               // ������
                .append(nullToEmpty(infoMap.get("RECEIVETYPE"))).append("|")        // ���շ�ʽ
                .append(nullToEmpty(infoMap.get("INPUTERNAME"))).append("|")       // ����������
                .append(nullToEmpty(infoMap.get("IS_CONSIGN"))).append("|")          // �Ƿ�ί��
                .append(nullToEmpty(infoMap.get("LATEFLAG"))).append("|")              // �Ƿ�¼
                .append(nullToEmpty(infoMap.get("NOSOURCE_IDS"))).append("|")     // ��������ID����
                .append(nullToEmpty(infoMap.get("SUPPLYTEMPLET_ID"))).append("|") // ��������ģ��ID
                .append(nullToEmpty(infoMap.get("REMARK"))).append("|");                // ��ע

        List<Map<String, String>> detailMapList = toa.getDetailMapList();

        strBuilder.append(String.valueOf(detailMapList.size())).append("|");               // ��ϸ��¼��

        for (Map<String, String> detail : detailMapList) {
            strBuilder.append(nullToEmpty(detail.get("CHR_ID"))).append(",")            // �ɿ�����ϸID
                    .append(nullToEmpty(detail.get("MAIN_ID"))).append(",")                // �ɿ���ID
                    .append(nullToEmpty(detail.get("IN_BIS_CODE"))).append(",")        // ������Ŀҵ����
                    .append(nullToEmpty(detail.get("IN_BIS_NAME"))).append(",")        // ������Ŀ����
                    .append(nullToEmpty(detail.get("MEASURE"))).append(",")               // ���յ�λ
                    .append(nullToEmpty(detail.get("CHARGENUM"))).append(",")           // ��������

                    .append(nullToEmpty(detail.get("CHARGESTANDARD"))).append(",")  // �շѱ�׼
                    .append(nullToEmpty(detail.get("CHARGEMONEY"))).append(",")        // ������
                    .append(nullToEmpty(detail.get("ITEM_CHKCODE"))).append("|");     // ��λ��ĿУ����
        }
        return strBuilder.toString();
    }


    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
