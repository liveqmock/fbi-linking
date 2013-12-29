package apps.fiskfq.online.service;

import apps.fiskfq.gateway.client.SyncSocketClient;
import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.txn.Tia2402;
import apps.fiskfq.gateway.domain.txn.Tia2409;
import apps.fiskfq.repository.MybatisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 缴款撤销
 */
public class Txn1534040Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn1534040Service.class);
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, Tia2409 tia2409) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {



        tia2409.Head.src = "CCB-370211";
        tia2409.Head.des = "CZ-370211";
        Toa toa = new SyncSocketClient().onRequest(tia2409);

        // TODO　业务逻辑

        return toa;
    }

}
