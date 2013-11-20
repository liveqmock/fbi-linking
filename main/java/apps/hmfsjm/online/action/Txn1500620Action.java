package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.txn.Tia3001;
import apps.hmfsjm.gateway.domain.txn.Toa3001;
import apps.hmfsjm.online.service.Txn1500620Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

//	即墨房屋维修资金退款查询
@Component
public class Txn1500620Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500620Action.class);
    private Txn1500620Service txn1500620Service = new Txn1500620Service();
    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 退款书编号
        String billNo = fieldArray[0];

        logger.info("[1500620][3001][hmfsjm退款单查询][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [退款书编号] " + billNo);

        try {
            Toa3001 toa = (Toa3001)txn1500620Service.process(msg.tellerID, billNo);
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);
        } catch (Exception e) {
            logger.error("[1500620][3001][hmfsjm退款单查询]失败", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa3001 toa3001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3001.BODY.REFUND_BILLNO).append("|")                // 缴款书编号
                .append(nullToEmpty(toa3001.BODY.BILL_STS_CODE)).append("|")     // 缴款单状态代码
                .append(nullToEmpty(toa3001.BODY.BILL_STS_TITLE)).append("|")    // 缴款单状态说明
                .append(nullToEmpty(toa3001.BODY.RP_TYPE)).append("|")           // 退款类别
                .append(nullToEmpty(toa3001.BODY.RP_MEMO)).append("|")           // 退款内容
                .append(nullToEmpty(toa3001.BODY.RP_MONEY)).append("|")          // 退款金额
                .append(nullToEmpty(toa3001.BODY.PAY_BILL_NO)).append("|")       // 缴存通知单号
                .append(nullToEmpty(toa3001.BODY.PAY_BANK)).append("|")          // 缴款银行
                .append(nullToEmpty(toa3001.BODY.BANK_USER)).append("|")         // 银行收款人
                .append(nullToEmpty(toa3001.BODY.BANK_CFM_DATE)).append("|")     // 银行收款日期
                .append(nullToEmpty(toa3001.BODY.PAY_MONEY)).append("|")         // 银行收款金额
                .append(nullToEmpty(toa3001.BODY.HOUSE_ID)).append("|")          // 房屋编号
                .append(nullToEmpty(toa3001.BODY.HOUSE_LOCATION)).append("|")    // 房屋坐落
                .append(nullToEmpty(toa3001.BODY.HOUSE_AREA)).append("|")        // 建筑面积
                .append(nullToEmpty(toa3001.BODY.STANDARD)).append("|")          // 缴存标准
                .append(nullToEmpty(toa3001.BODY.AREA_ACCOUNT)).append("|")      // 专户账号
                .append(nullToEmpty(toa3001.BODY.HOUSE_ACCOUNT)).append("|")     // 分户账号
                .append(nullToEmpty(toa3001.BODY.CARD_TYPE)).append("|")         // 证件类型
                .append(nullToEmpty(toa3001.BODY.CARD_NO)).append("|")           // 证件号码
                .append(nullToEmpty(toa3001.BODY.OWNER)).append("|")             // 业主姓名
                .append(nullToEmpty(toa3001.BODY.TEL)).append("|")               // 联系电话
                .append(nullToEmpty(toa3001.BODY.ACCEPT_DATE)).append("|")       // 缴存受理日期
                .append(nullToEmpty(toa3001.BODY.RESERVE)).append("|");          // 保留域
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
