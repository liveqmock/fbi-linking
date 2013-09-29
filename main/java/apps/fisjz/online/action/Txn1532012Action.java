package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2012Request.TIA2012;
import apps.fisjz.enums.TxnRtnCode;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 1532012缴款书到帐确认 (废弃，改成由linking自动发起)
 * zhanrui  20130923
 */
@Component
public class Txn1532012Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532012Action.class);

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2012Request");
        TIA2012 tia = null;
        try {
            tia = (TIA2012) dataFormat.fromMessage(new String(msg.msgBody), "TIA2012");
        } catch (Exception e) {
            logger.error("报文解析错误:", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "报文解析错误.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }
        logger.info("[1532012缴款书到帐确认] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 缴款书编号:" + tia.getPaynotesInfo().getNotescode());

        msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
        msg.msgBody =  "不支持此交易.".getBytes(THIRDPARTY_SERVER_CODING);
        return msg;
    }
}
