package apps.hmfsjm.gateway.service;

import apps.hmfsjm.gateway.action.AbstractTxnAction;
import apps.hmfsjm.gateway.domain.base.Tia;
import apps.hmfsjm.gateway.domain.base.Toa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 接口协议交易执行
 */
@Service
public class ProtocolTxnService {
    private static Logger logger = LoggerFactory.getLogger(ProtocolTxnService.class);

    public Toa execute(String txnCode, Tia tia) {
        WebApplicationContext springContext = ContextLoader.getCurrentWebApplicationContext();
        AbstractTxnAction txnAction = (AbstractTxnAction) springContext.getBean("txn" + txnCode + "Action");
        return txnAction.run(tia);
    }
}
