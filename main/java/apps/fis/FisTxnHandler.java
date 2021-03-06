package apps.fis;

import apps.TxnHandler;
import apps.fis.online.action.AbstractTxnAction;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import startup.AppsStartManager;

/**
 *
 */
@Component
public class FisTxnHandler extends TxnHandler {
    private static final Logger logger = LoggerFactory.getLogger(FisTxnHandler.class);

    @Override
    public LFixedLengthProtocol execute(LFixedLengthProtocol tia) throws Exception {
        try {
//            AbstractTxnAction txnAction = (AbstractTxnAction)Class.forName("apps.fis.online.action.Txn" + tia.txnCode.trim() + "Action").newInstance();
            AbstractTxnAction txnAction = (AbstractTxnAction) AppsStartManager.getBean(tia.appID.trim().toLowerCase(), "txn" + tia.txnCode.trim() + "Action");
            return txnAction.run(tia);
        } catch (Exception e) {
            logger.error("����ʧ��,", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
