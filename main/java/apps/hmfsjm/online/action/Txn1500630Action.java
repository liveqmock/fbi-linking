package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.online.service.Txn1500630Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	即墨房屋维修资金票据领用
@Component
public class Txn1500630Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500630Action.class);
    private Txn1500630Service txn1500630Service = new Txn1500630Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 起始编号
        String vchStartNo = fieldArray[0];
        // 结束编号
        String vchEndNo = fieldArray[1];

        logger.info("[1500630票据领用][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [起始编号] " + vchStartNo + "[结束编号]" + vchEndNo);
        try {
            long startNo = Long.parseLong(vchStartNo);
            long endNo = Long.parseLong(vchEndNo);
            if (startNo < endNo) {
                throw new RuntimeException("起始编号不能小于终止编号");
            } else {
                txn1500630Service.process(msg.branchID, msg.tellerID, startNo, endNo);
            }

        } catch (Exception e) {
            logger.error("[1500630][hmfsjm票据领用]失败", e);
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
