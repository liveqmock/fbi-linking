package apps.fisjz.online.action;

import apps.fis.enums.TxnRtnCode;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    public LFixedLengthProtocol run(LFixedLengthProtocol tia) {
        try {
            tia.rtnCode = "0000";
            return process(tia);
        } catch (Exception e) {
            logger.error("Action业务处理错误。", e);
            throw new RuntimeException(e.getMessage() == null ? TxnRtnCode.TXN_EXECUTE_FAILED.toRtnMsg() : e.getMessage());
        }
    }

    abstract protected LFixedLengthProtocol process(LFixedLengthProtocol tia) throws Exception;

    protected boolean getResponseResult(List<Map> rtnList){
        for (Map map : rtnList) {
            String result = (String)map.get("RESULT");
            if (result != null) {
                if ("SUCCESS".equals(result.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    protected String getResponseErrMsg(List<Map> rtnList){
        for (Map map : rtnList) {
            String result = (String)map.get("RESULT");
            if (result != null) {
                if (!"SUCCESS".equals(result.toUpperCase())) {
                    return (String)map.get("MESSAGE");
                }
            }
        }
        return "";
    }
}
