package apps.fisjz.online.action;

import apps.fisjz.enums.TxnRtnCode;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    //������������Ҫ���������뷽ʽ�����������ļ����ݵ����Ų�����
    protected static String THIRDPARTY_SERVER_CODING = "GBK";

    @Transactional
    public LFixedLengthProtocol run(LFixedLengthProtocol tia) {
        boolean isBizErr = false;
        try {
            tia.rtnCode = "0000";

            //���������ǳɹ����ף����ع�
            LFixedLengthProtocol toa = process(tia);
            if (!TxnRtnCode.TXN_EXECUTE_SECCESS.getCode().equals(toa.rtnCode)) {
                isBizErr = true;
                throw new RuntimeException(new String(toa.msgBody, "GBK"));
            }else{
                return toa;
            }
        } catch (Exception e) {
            if (!isBizErr) {
                logger.error("Actionҵ�������", e);
            }
            throw new RuntimeException(e.getMessage() == null ? TxnRtnCode.TXN_EXECUTE_FAILED.toRtnMsg() : e.getMessage());
        }
    }

    abstract protected LFixedLengthProtocol process(LFixedLengthProtocol tia) throws Exception;

}
