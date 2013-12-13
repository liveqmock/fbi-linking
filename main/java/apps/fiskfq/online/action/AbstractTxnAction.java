package apps.fiskfq.online.action;

import apps.fis.enums.TxnRtnCode;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);
    //������������Ҫ���������뷽ʽ�����������ļ����ݵ����Ų�����
    protected static String THIRDPARTY_SERVER_CODING = "GBK";

    public LFixedLengthProtocol run(LFixedLengthProtocol tia) {
        try {
            tia.rtnCode = "0000";
            return process(tia);
        } catch (Exception e) {
            logger.error("Actionҵ�������", e);
            throw new RuntimeException(e.getMessage() == null ? TxnRtnCode.TXN_EXECUTE_FAILED.toRtnMsg() : e.getMessage());
        }
    }

    abstract protected LFixedLengthProtocol process(LFixedLengthProtocol tia) throws Exception;
}
