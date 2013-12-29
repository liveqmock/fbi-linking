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

//	缴款查询
@Component
public class Txn1534010Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1534010Action.class);
    private Txn1534010Service txn1534010Service = new Txn1534010Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        /*
        billtype_code	缴款书样式编码
bill_no	票号
verify_no	全票面校验码
bill_money	收款金额
set_year	年度
         */

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 缴款书样式编码
        String typeCode = fieldArray[0];
        // 票号
        String billNo = fieldArray[1];
        // 全票面校验码
        String verifyNo = fieldArray[2];
        // 收款金额
        String billMoney = fieldArray[3];
        // 年度
        String setYear = fieldArray[4];

        logger.info("[1534010缴款信息查询][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [样式码]" + typeCode + "  [缴款书编号] " + billNo);


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
            logger.error("[1534010][fiskfq应收数据查询]失败", e);
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
        // 拼接报文
        strBuilder.append(infoMap.get("CHR_ID")).append("|")                      // 缴款书ID
                .append(nullToEmpty(infoMap.get("BILLTYPE_CODE"))).append("|")    // 缴款书样式编码
                .append(nullToEmpty(infoMap.get("BILLTYPE_NAME"))).append("|")    // 缴款书样式名称
                .append(nullToEmpty(infoMap.get("BILL_NO"))).append("|")          // 票号
                .append(nullToEmpty(infoMap.get("MAKEDATE"))).append("|")         // 开票日期
                .append(nullToEmpty(infoMap.get("IEN_CODE"))).append("|")         // 执收单位业务码
                .append(nullToEmpty(infoMap.get("IEN_NAME"))).append("|")         // 执收单位名称
                .append(nullToEmpty(infoMap.get("CONSIGN_IEN_CODE"))).append("|") // 委托单位业务码
                .append(nullToEmpty(infoMap.get("CONSIGN_IEN_NAME"))).append("|") // 委托单位名称
                .append(nullToEmpty(infoMap.get("PM_CODE"))).append("|")               // 缴款方式编码
                .append(nullToEmpty(infoMap.get("PM_NAME"))).append("|")              // 缴款方式名称
                .append(nullToEmpty(infoMap.get("CHEQUE_NO"))).append("|")          // 结算号
                .append(nullToEmpty(infoMap.get("PAYER"))).append("|")                   // 缴款人全称
                .append(nullToEmpty(infoMap.get("PAYERBANK"))).append("|")           // 缴款人账户开户行
                .append(nullToEmpty(infoMap.get("PAYERACCOUNT"))).append("|")     // 缴款人账号
                .append(nullToEmpty(infoMap.get("RECEIVER"))).append("|")              // 收款人全称
                .append(nullToEmpty(infoMap.get("RECEIVERBANK"))).append("|")      // 收款人账户开户行
                .append(nullToEmpty(infoMap.get("RECEIVERACCOUNT"))).append("|")  // 收款人账号
                .append(nullToEmpty(infoMap.get("VERIFY_NO"))).append("|")            // 全票面校验码
                .append(nullToEmpty(infoMap.get("RG_CODE"))).append("|")               // 区划码
                .append(nullToEmpty(infoMap.get("RECEIVETYPE"))).append("|")        // 征收方式
                .append(nullToEmpty(infoMap.get("INPUTERNAME"))).append("|")       // 经办人姓名
                .append(nullToEmpty(infoMap.get("IS_CONSIGN"))).append("|")          // 是否委托
                .append(nullToEmpty(infoMap.get("LATEFLAG"))).append("|")              // 是否补录
                .append(nullToEmpty(infoMap.get("NOSOURCE_IDS"))).append("|")     // 待查收入ID集合
                .append(nullToEmpty(infoMap.get("SUPPLYTEMPLET_ID"))).append("|") // 批量代扣模板ID
                .append(nullToEmpty(infoMap.get("REMARK"))).append("|");                // 备注

        List<Map<String, String>> detailMapList = toa.getDetailMapList();

        strBuilder.append(String.valueOf(detailMapList.size())).append("|");               // 明细记录数

        for (Map<String, String> detail : detailMapList) {
            strBuilder.append(nullToEmpty(detail.get("CHR_ID"))).append(",")            // 缴款书明细ID
                    .append(nullToEmpty(detail.get("MAIN_ID"))).append(",")                // 缴款书ID
                    .append(nullToEmpty(detail.get("IN_BIS_CODE"))).append(",")        // 收入项目业务码
                    .append(nullToEmpty(detail.get("IN_BIS_NAME"))).append(",")        // 收入项目名称
                    .append(nullToEmpty(detail.get("MEASURE"))).append(",")               // 计收单位
                    .append(nullToEmpty(detail.get("CHARGENUM"))).append(",")           // 收入数量

                    .append(nullToEmpty(detail.get("CHARGESTANDARD"))).append(",")  // 收费标准
                    .append(nullToEmpty(detail.get("CHARGEMONEY"))).append(",")        // 收入金额
                    .append(nullToEmpty(detail.get("ITEM_CHKCODE"))).append("|");     // 单位项目校验码
        }
        return strBuilder.toString();
    }


    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
