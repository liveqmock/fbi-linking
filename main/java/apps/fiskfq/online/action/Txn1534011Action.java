package apps.fiskfq.online.action;

import apps.fiskfq.enums.TxnRtnCode;
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
            txn1534011Service.process(msg.tellerID, msg.branchID, tia);

//            Toa1401 toa = (Toa1401) txn1534010Service.process(msg.tellerID, msg.branchID, tia);
//            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);


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

    private String assembleStr(Toa1401 toa1401) {
        StringBuilder strBuilder = new StringBuilder();
        // 拼接报文
        strBuilder.append(toa1401.Body.Object.Record.chr_id).append("|")                      // 缴款书ID
                .append(nullToEmpty(toa1401.Body.Object.Record.billtype_code)).append("|")    // 缴款书样式编码
                .append(nullToEmpty(toa1401.Body.Object.Record.billtype_name)).append("|")    // 缴款书样式名称
                .append(nullToEmpty(toa1401.Body.Object.Record.bill_no)).append("|")          // 票号
                .append(nullToEmpty(toa1401.Body.Object.Record.makedate)).append("|")         // 开票日期
                .append(nullToEmpty(toa1401.Body.Object.Record.ien_code)).append("|")         // 执收单位业务码
                .append(nullToEmpty(toa1401.Body.Object.Record.ien_name)).append("|")         // 执收单位名称
                .append(nullToEmpty(toa1401.Body.Object.Record.consign_ien_code)).append("|") // 委托单位业务码
                .append(nullToEmpty(toa1401.Body.Object.Record.consign_ien_name)).append("|") // 委托单位名称
                .append(nullToEmpty(toa1401.Body.Object.Record.pm_code)).append("|")          // 缴款方式编码
                .append(nullToEmpty(toa1401.Body.Object.Record.pm_name)).append("|")          // 缴款方式名称
                .append(nullToEmpty(toa1401.Body.Object.Record.cheque_no)).append("|")        // 结算号
                .append(nullToEmpty(toa1401.Body.Object.Record.payer)).append("|")            // 缴款人全称
                .append(nullToEmpty(toa1401.Body.Object.Record.payerbank)).append("|")        // 缴款人账户开户行
                .append(nullToEmpty(toa1401.Body.Object.Record.payeraccount)).append("|")     // 缴款人账号
                .append(nullToEmpty(toa1401.Body.Object.Record.receiver)).append("|")         // 收款人全称
                .append(nullToEmpty(toa1401.Body.Object.Record.receiverbank)).append("|")     // 收款人账户开户行
                .append(nullToEmpty(toa1401.Body.Object.Record.receiveraccount)).append("|")  // 收款人账号
                .append(nullToEmpty(toa1401.Body.Object.Record.verify_no)).append("|")        // 全票面校验码
                .append(nullToEmpty(toa1401.Body.Object.Record.rg_code)).append("|")          // 区划码
                .append(nullToEmpty(toa1401.Body.Object.Record.receivetype)).append("|")      // 征收方式
                .append(nullToEmpty(toa1401.Body.Object.Record.inputername)).append("|")      // 经办人姓名
                .append(nullToEmpty(toa1401.Body.Object.Record.is_consign)).append("|")       // 是否委托
                .append(nullToEmpty(toa1401.Body.Object.Record.lateflag)).append("|")         // 是否补录
                .append(nullToEmpty(toa1401.Body.Object.Record.nosource_ids)).append("|")     // 待查收入ID集合
                .append(nullToEmpty(toa1401.Body.Object.Record.supplytemplet_id)).append("|") // 批量代扣模板ID
                .append(nullToEmpty(toa1401.Body.Object.Record.supplytemplet_id)).append("|") // 批量代扣模板ID
                .append(nullToEmpty(toa1401.Body.Object.Record.remark)).append("|");          // 备注

        strBuilder.append(String.valueOf(toa1401.Body.Object.Record.Object.size())).append("|");// 明细记录数

        for (Toa1401.Record.DetailRecord record : toa1401.Body.Object.Record.Object) {
            strBuilder.append(nullToEmpty(record.chr_id)).append(",")           // 缴款书明细ID
                    .append(nullToEmpty(record.main_id)).append(",")            // 缴款书ID
                    .append(nullToEmpty(record.in_bis_code)).append(",")        // 收入项目业务码
                    .append(nullToEmpty(record.in_bis_name)).append(",")        // 收入项目名称
                    .append(nullToEmpty(record.measure)).append(",")            // 计收单位
                    .append(nullToEmpty(record.chargenum)).append(",")          // 收入数量
                    .append(nullToEmpty(record.chargestandard)).append(",")     // 收费标准
                    .append(nullToEmpty(record.chargemoney)).append(",")        // 收入金额
                    .append(nullToEmpty(record.item_chkcode)).append("|");      // 单位项目校验码
        }

        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
