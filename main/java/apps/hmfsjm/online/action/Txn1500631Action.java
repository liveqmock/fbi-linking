package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.enums.VoucherStatus;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.online.service.Txn1500631Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	即墨房屋维修资金票据使用和作废
@Component
public class Txn1500631Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500631Action.class);
    private Txn1500631Service txn1500631Service = new Txn1500631Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 票据编号
        String vchNo = fieldArray[1];
        // 票据状态
        String vchSts = fieldArray[2];
        // 缴款单编号
        String billNo = fieldArray[0];

        if (VoucherStatus.USED.getCode().equals(vchSts)) {
            if (StringUtils.isEmpty(billNo)) {
                msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
                msg.msgBody = "使用票据时必须输入缴款单号".getBytes(THIRDPARTY_SERVER_CODING);
                return msg;
            }
        }

        logger.info("[1500631票据使用与作废][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [票据编号] " + vchNo + "[票据状态]" + vchSts + "[缴款单编号]" + billNo);


        try {
            txn1500631Service.process(msg.branchID, msg.tellerID, vchNo, billNo, vchSts);
        } catch (Exception e) {
            logger.error("[1500631][hmfsjm票据使用与作废]失败", e);
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
