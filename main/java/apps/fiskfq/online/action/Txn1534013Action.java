package apps.fiskfq.online.action;

import apps.fiskfq.enums.TxnRtnCode;
import apps.fiskfq.gateway.domain.base.ToaXml;
import apps.fiskfq.gateway.domain.txn.Tia2402;
import apps.fiskfq.gateway.domain.txn.Tia2457;
import apps.fiskfq.online.service.Txn1534011Service;
import apps.fiskfq.online.service.Txn1534013Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	�ֹ�Ʊ�ɿ�
@Component
public class Txn1534013Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1534013Action.class);
    private Txn1534013Service txn1534030Service = new Txn1534013Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {


        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
       
        /*
rg_code	������	NString	[1,10]		M
billtype_code	�ɿ�����ʽ����	String	[1,20]	��Ӧ��Ʊ����ʽ����	M
bill_no	Ʊ��	String	[1,42]	�ɿ����ӡˢƱ��	M
verify_no	ȫƱ��У����	String	[1,20]	ΪƱ��bill_no�ĺ���λ	M
pm_code	�ɿʽ����	String	[1,10]	001Ϊ�ֽ�002Ϊת��	M
ien_code	ִ�յ�λҵ����	NString	[1,42]	��λ����	M
ien_name	ִ�յ�λ����	GBString	[1,80]	��λ����	M
consign_ien_code	ί�е�λ����	NString			O
consign_ien_name	ί�е�λ����	GBString			O
bill_money	�տ���	Currency		��λ��Ԫ	M
set_year	���	Integer	4	ҵ�����	M
bank_user	������	GBString	[1,38]	���в���Ա	M
Bank_no	���б���	NString	[1,10]		M
payer	�ɿ���	GBString			M
payerbank	�ɿ��˿�����	GBString			O
payeraccount	�ɿ����˺�	String			O
receiver	�տ���ȫ��	GBString	[1,100]		O

receiverbank	�տ����˻�������	GBString	[1,100]		O
receiveraccount	�տ����˺�	String	[1,42]		O
is_consign	�Ƿ�ί��	Boolean		false����  true����	M
remark	��ע	String	[1,200]		O
	��ϸ��¼��
         */
        Tia2457 tia = new Tia2457();

        tia.Body.Object.Record.rg_code = fieldArray[0];
        tia.Body.Object.Record.billtype_code = fieldArray[1];
        tia.Body.Object.Record.bill_no = fieldArray[2];
        tia.Body.Object.Record.verify_no = fieldArray[3];
        tia.Body.Object.Record.pm_code = fieldArray[4];
        tia.Body.Object.Record.ien_code = fieldArray[5];
        tia.Body.Object.Record.ien_name = fieldArray[6];
        tia.Body.Object.Record.consign_ien_code = fieldArray[7];
        tia.Body.Object.Record.consign_ien_name = fieldArray[8];
        tia.Body.Object.Record.bill_money = fieldArray[9];
        tia.Body.Object.Record.set_year = fieldArray[10];
        tia.Body.Object.Record.bank_user = fieldArray[11];
        tia.Body.Object.Record.bank_no = fieldArray[12];
        tia.Body.Object.Record.payer = fieldArray[13];
        tia.Body.Object.Record.payerbank = fieldArray[14];
        tia.Body.Object.Record.payeraccount = fieldArray[15];
        tia.Body.Object.Record.receiver = fieldArray[16];
        tia.Body.Object.Record.receiverbank = fieldArray[17];
        tia.Body.Object.Record.receiveraccount = fieldArray[18];
        tia.Body.Object.Record.is_consign = fieldArray[19];
        tia.Body.Object.Record.remark = fieldArray[20];

        tia.Head.msgId = msg.txnTime + msg.serialNo;
        tia.Head.workDate = msg.txnTime.substring(0, 8);

        int detailCnt = Integer.parseInt(fieldArray[21]);
        /*
        ��ʶ��	�ֶ�����	����	����	��ע	ǿ��/��ѡ
in_bis_code	������Ŀҵ����	NString	[1,42]		M
in_bis_name	������Ŀ����	GBString	[1,600]		M
chargenum	��������	Currency			M
chargemoney	������	Currency		��λ��Ԫ	M
         */
        for (int i = 0; i < detailCnt; i++) {
            String[] detailFields = StringUtils.splitByWholeSeparatorPreserveAllTokens(fieldArray[22 + i], ",");
            Tia2457.BodyRecord.DetailRecord dr = new Tia2457.BodyRecord.DetailRecord();
            dr.in_bis_code = detailFields[0];
            dr.in_bis_name = detailFields[1];
            dr.chargenum = detailFields[2];
            dr.chargemoney = detailFields[3];
            tia.Body.Object.Record.Object.add(dr);
        }


        logger.info("[1534030�ֹ�Ʊ�ɿ�][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "  [��ʽ��]" + tia.Body.Object.Record.billtype_code +
                "  [�ɿ�����] " + tia.Body.Object.Record.bill_no + "[���]" + tia.Body.Object.Record.bill_money);


        try {

            ToaXml toa = (ToaXml) txn1534030Service.process(msg.tellerID, msg.branchID, tia);
            // TODO ����ɹ�ȷ��

        } catch (Exception e) {
            logger.error("[1534030][�ֹ�Ʊ�ɿ�]ʧ��", e);
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
