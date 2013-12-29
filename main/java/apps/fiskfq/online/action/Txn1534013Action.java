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

//	手工票缴款
@Component
public class Txn1534013Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1534013Action.class);
    private Txn1534013Service txn1534030Service = new Txn1534013Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {


        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
       
        /*
rg_code	区划码	NString	[1,10]		M
billtype_code	缴款书样式编码	String	[1,20]	对应的票据样式编码	M
bill_no	票号	String	[1,42]	缴款书的印刷票号	M
verify_no	全票面校验码	String	[1,20]	为票号bill_no的后四位	M
pm_code	缴款方式编码	String	[1,10]	001为现金，002为转账	M
ien_code	执收单位业务码	NString	[1,42]	单位代码	M
ien_name	执收单位名称	GBString	[1,80]	单位名称	M
consign_ien_code	委托单位编码	NString			O
consign_ien_name	委托单位名称	GBString			O
bill_money	收款金额	Currency		单位：元	M
set_year	年度	Integer	4	业务年度	M
bank_user	创建人	GBString	[1,38]	银行操作员	M
Bank_no	银行编码	NString	[1,10]		M
payer	缴款人	GBString			M
payerbank	缴款人开户行	GBString			O
payeraccount	缴款人账号	String			O
receiver	收款人全称	GBString	[1,100]		O

receiverbank	收款人账户开户行	GBString	[1,100]		O
receiveraccount	收款人账号	String	[1,42]		O
is_consign	是否委托	Boolean		false：否  true：是	M
remark	备注	String	[1,200]		O
	明细记录数
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
        标识符	字段名称	类型	长度	备注	强制/可选
in_bis_code	收入项目业务码	NString	[1,42]		M
in_bis_name	收入项目名称	GBString	[1,600]		M
chargenum	收入数量	Currency			M
chargemoney	收入金额	Currency		单位：元	M
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


        logger.info("[1534030手工票缴款][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [样式码]" + tia.Body.Object.Record.billtype_code +
                "  [缴款书编号] " + tia.Body.Object.Record.bill_no + "[金额]" + tia.Body.Object.Record.bill_money);


        try {

            ToaXml toa = (ToaXml) txn1534030Service.process(msg.tellerID, msg.branchID, tia);
            // TODO 交款成功确认

        } catch (Exception e) {
            logger.error("[1534030][手工票缴款]失败", e);
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
