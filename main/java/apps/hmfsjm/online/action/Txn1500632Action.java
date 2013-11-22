package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.enums.VoucherStatus;
import apps.hmfsjm.online.service.Txn1500631Service;
import apps.hmfsjm.online.service.Txn1500632Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	票据使用情况概览
@Component
public class Txn1500632Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500632Action.class);
    private Txn1500632Service txn1500632Service = new Txn1500632Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 日期
        String date8 = fieldArray[0];

        logger.info("[1500632票据使用情况查询][日期]" + date8 + " [网点]" + msg.branchID + "[柜员]" + msg.tellerID);

        try {
            String strVchs = txn1500632Service.process(date8);
            if (StringUtils.isEmpty(strVchs)) {
                msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
                msg.msgBody = "没有查询到该日期使用的票据".getBytes(THIRDPARTY_SERVER_CODING);
            } else {
                msg.msgBody = strVchs.getBytes(THIRDPARTY_SERVER_CODING);
            }
        } catch (Exception e) {
            logger.error("[1500632][hmfsjm票据使用情况查询]失败", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }
}
