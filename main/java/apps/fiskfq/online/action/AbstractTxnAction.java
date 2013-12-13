package apps.fiskfq.online.action;

import apps.fis.enums.TxnRtnCode;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);
    //第三方服务器要求的请求编码方式，可在配置文件根据地区号参数化
    protected static String THIRDPARTY_SERVER_CODING = "GBK";

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
}
