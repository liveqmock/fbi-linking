package apps.fisjz.online.action;

import apps.fis.enums.TxnRtnCode;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    protected static String FISJZ_APPLICATIONID = "";
    protected static String FISJZ_BANK = "";

    public LFixedLengthProtocol run(LFixedLengthProtocol tia) {
        try {
            tia.rtnCode = "0000";

            //特殊处理：去掉报文的body区的首尾分隔符
            int length = tia.msgBody.length;
            if (length > 2) {
                byte[] buf = new byte[length - 2];
                System.arraycopy(tia.msgBody, 1, buf, 0, buf.length);
                tia.msgBody = buf;
            }
            return process(tia);
        } catch (Exception e) {
            logger.error("Action业务处理错误。", e);
            throw new RuntimeException(e.getMessage() == null ? TxnRtnCode.TXN_EXECUTE_FAILED.toRtnMsg() : e.getMessage());
        }
    }

    abstract protected LFixedLengthProtocol process(LFixedLengthProtocol tia) throws Exception;


    protected boolean getResponseResult(List<Map> rtnList) {
        for (Map map : rtnList) {
            String result = (String) map.get("RESULT");
            if (result != null) {
                if ("SUCCESS".equals(result.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getResponseErrMsg(List<Map> rtnList) {
        for (Map map : rtnList) {
            String result = (String) map.get("RESULT");
            if (result != null) {
                if (!"SUCCESS".equals(result.toUpperCase())) {
                    return (String) map.get("MESSAGE");
                }
            }
        }
        return "";
    }
}
