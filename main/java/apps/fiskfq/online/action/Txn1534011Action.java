package apps.fiskfq.online.action;

import apps.fiskfq.enums.TxnRtnCode;
import apps.fiskfq.gateway.domain.base.ToaXml;
import apps.fiskfq.gateway.domain.txn.Tia2401;
import apps.fiskfq.gateway.domain.txn.Tia2402;
import apps.fiskfq.gateway.domain.txn.Toa1401;
import apps.fiskfq.online.service.Txn1534010Service;
import apps.fiskfq.online.service.Txn1534011Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	缴款确认
@Component
public class Txn1534011Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1534011Action.class);
    private Txn1534011Service txn1534011Service = new Txn1534011Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {


        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 缴款书样式编码
        String chrid = fieldArray[0];

        String billtype_code = fieldArray[1];
        String billno = fieldArray[2];
        String billMoney = fieldArray[3];
        String bank_indate = fieldArray[4];
        String incomestatus = fieldArray[5];
        String pm_code = fieldArray[6];
        String cheque_no = fieldArray[7];
        String payerbank = fieldArray[8];
        String payeraccount = fieldArray[9];
        String set_year = fieldArray[10];
        String route_user_code = fieldArray[11];
        String license = fieldArray[12];
        String business_id = fieldArray[13];


        logger.info("[1534011缴款确认][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [样式码]" + billtype_code + "  [缴款书编号] " + billno + "[金额]" + billMoney);


        try {
            Tia2402 tia = new Tia2402();
            tia.Body.Object.Record.chr_id = chrid;
            tia.Body.Object.Record.billtype_code = billtype_code;
            tia.Body.Object.Record.bill_no = billno;
            tia.Body.Object.Record.bill_money = billMoney;
            tia.Body.Object.Record.bank_indate = bank_indate;
            tia.Body.Object.Record.incomestatus = incomestatus;
            tia.Body.Object.Record.pm_code = pm_code;
            tia.Body.Object.Record.cheque_no = cheque_no;
            tia.Body.Object.Record.payerbank = payerbank;
            tia.Body.Object.Record.payeraccount = payeraccount;

            tia.Body.Object.Record.set_year = set_year;
            tia.Body.Object.Record.route_user_code = route_user_code;
            tia.Body.Object.Record.license = license;
            tia.Body.Object.Record.business_id = business_id;
            tia.Head.msgId = msg.txnTime + msg.serialNo;
            tia.Head.workDate = msg.txnTime.substring(0, 8);
            ToaXml toa = (ToaXml)txn1534011Service.process(msg.tellerID, msg.branchID, tia);
             // TODO 交款成功确认

        } catch (Exception e) {
            logger.error("[1534011][fiskfq应收数据确认]失败", e);
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
