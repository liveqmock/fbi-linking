package apps.hmfsjm.gateway.action;

import apps.hmfsjm.gateway.domain.base.Tia;
import apps.hmfsjm.gateway.domain.base.Toa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    @Transactional
    public Toa run(Tia tia) {
        try {
            Toa toa = process(tia);
            return toa;
        } catch (Exception e) {
            logger.error("交易异常", e);
            throw new RuntimeException(e.getMessage() == null ? "交易异常." : e.getMessage());
        }
    }

    abstract protected Toa process(Tia tia) throws Exception;
}
