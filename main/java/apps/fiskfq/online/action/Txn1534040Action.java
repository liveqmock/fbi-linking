package apps.fiskfq.online.action;

import apps.fiskfq.enums.TxnRtnCode;
import apps.fiskfq.gateway.domain.base.ToaXml;
import apps.fiskfq.gateway.domain.txn.Tia2402;
import apps.fiskfq.gateway.domain.txn.Tia2409;
import apps.fiskfq.online.service.Txn1534011Service;
import apps.fiskfq.online.service.Txn1534040Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	�ɿ��
@Component
public class Txn1534040Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1534040Action.class);
    private Txn1534040Service txn1534040Service = new Txn1534040Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {


        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        String chrid = fieldArray[0];
        String billtype_code = fieldArray[1];
        String billno = fieldArray[2];
        String set_year = fieldArray[3];


        logger.info("[1534040����][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [��ʽ��]" + billtype_code + "  [�ɿ���ID] " + chrid + "[Ʊ��]" + billno);


        try {
            Tia2409 tia = new Tia2409();
            tia.Body.Object.Record.chr_id = chrid;
            tia.Body.Object.Record.billtype_code = billtype_code;
            tia.Body.Object.Record.bill_no = billno;
            tia.Body.Object.Record.set_year = set_year;
            tia.Head.msgId = msg.txnTime + msg.serialNo;
            tia.Head.workDate = msg.txnTime.substring(0, 8);
            ToaXml toa = (ToaXml)txn1534040Service.process(msg.tellerID, msg.branchID, tia);
             // TODO ����ɹ�ȷ��

        } catch (Exception e) {
            logger.error("[1534040][fiskfq�����ɿ�]ʧ��", e);
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


        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
