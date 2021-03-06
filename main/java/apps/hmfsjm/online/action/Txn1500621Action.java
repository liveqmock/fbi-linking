package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.txn.Tia3002;
import apps.hmfsjm.gateway.domain.txn.Toa3002;
import apps.hmfsjm.online.service.Txn1500621Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//	即墨房屋维修资金退款确认
@Component
public class Txn1500621Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500621Action.class);
    private Txn1500621Service txn1500621Service = new Txn1500621Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 退款书编号
        String refundNo = fieldArray[0];

        logger.info("[1500621][3002][hmfsjm退款书退款确认][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [退款书编号] " + refundNo);

        try {
            // 交易发起
            Toa3002 toa = (Toa3002) txn1500621Service.process(msg.tellerID, msg.branchID, msg.serialNo, refundNo);
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);
        } catch (Exception e) {
            logger.error("[1500621][3002][hmfsjm退款单退款确认]失败", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa3002 toa3002) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3002.BODY.REFUND_BILLNO).append("|")                // 退款书编号
                .append(nullToEmpty(toa3002.BODY.BILL_STS_CODE)).append("|")     // 退款单状态代码
                .append(nullToEmpty(toa3002.BODY.BILL_STS_TITLE)).append("|")    // 退款单状态说明
                .append(nullToEmpty(toa3002.BODY.RESERVE)).append("|");          // 保留域
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
