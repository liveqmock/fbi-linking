package apps.fisjz.online.action;

import apps.fisjz.enums.TxnRtnCode;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    //第三方服务器要求的请求编码方式，可在配置文件根据地区号参数化
    protected static String THIRDPARTY_SERVER_CODING = "GBK";

    @Transactional
    public LFixedLengthProtocol run(LFixedLengthProtocol tia) {
        boolean isBizErr = false;
        try {
            tia.rtnCode = "0000";

            //事务处理：若非成功交易，即回滚
            LFixedLengthProtocol toa = process(tia);
            if (!TxnRtnCode.TXN_EXECUTE_SECCESS.getCode().equals(toa.rtnCode)) {
                isBizErr = true;
                throw new RuntimeException(new String(toa.msgBody, "GBK"));
            }else{
                return toa;
            }
        } catch (Exception e) {
            if (!isBizErr) {
                logger.error("Action业务处理错误。", e);
            }
            throw new RuntimeException(e.getMessage() == null ? TxnRtnCode.TXN_EXECUTE_FAILED.toRtnMsg() : e.getMessage());
        }
    }

    abstract protected LFixedLengthProtocol process(LFixedLengthProtocol tia) throws Exception;

}
