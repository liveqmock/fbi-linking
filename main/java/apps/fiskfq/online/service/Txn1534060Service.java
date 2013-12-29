package apps.fiskfq.online.service;

import apps.fiskfq.gateway.client.SyncSocketClient;
import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.txn.Tia2409;
import apps.fiskfq.gateway.domain.txn.Tia2425;
import apps.fiskfq.repository.MybatisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * ¶ÔÕË
 */
public class Txn1534060Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn1534060Service.class);
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, Tia2425 tia2425) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {



        tia2425.Head.src = "CCB-370211";
        tia2425.Head.des = "CZ-370211";
        Toa toa = new SyncSocketClient().onRequest(tia2425);

        // TODO¡¡ÒµÎñÂß¼­

        return toa;
    }

}
