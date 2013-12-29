package apps.fiskfq.online.service;

import apps.fiskfq.gateway.client.SyncSocketClient;
import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.txn.Tia2409;
import apps.fiskfq.gateway.domain.txn.Tia2457;
import apps.fiskfq.repository.MybatisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 手工票
 */
public class Txn1534013Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn1534013Service.class);
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, Tia2457 tia2457) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {


        tia2457.Head.src = "CCB-370211";
        tia2457.Head.des = "CZ-370211";
        Toa toa = new SyncSocketClient().onRequest(tia2457);

        // TODO　业务逻辑

        return toa;
    }

}
