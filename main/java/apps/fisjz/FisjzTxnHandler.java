package apps.fisjz;

import apps.TxnHandler;
import apps.fisjz.online.action.AbstractTxnAction;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import startup.AppsStartManager;

@Component
public class FisjzTxnHandler extends TxnHandler {
    private static final Logger logger = LoggerFactory.getLogger(FisjzTxnHandler.class);

    @Override
    public LFixedLengthProtocol execute(LFixedLengthProtocol tia) throws Exception {
        try {
            AbstractTxnAction txnAction = (AbstractTxnAction) AppsStartManager.getBean(tia.appID.trim().toLowerCase(), "txn" + tia.txnCode.trim() + "Action");
            return txnAction.run(tia);
        } catch (Exception e) {
            logger.error("½»Ò×Ê§°Ü,", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
