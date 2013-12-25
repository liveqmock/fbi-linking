package apps.fiskfq.online.service;

import apps.fiskfq.gateway.client.SyncSocketClient;
import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.txn.Tia2401;
import apps.fiskfq.gateway.domain.txn.Tia2402;
import apps.fiskfq.repository.MybatisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 应收数据确认
 */
public class Txn1534011Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn1534011Service.class);
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, Tia2402 tia2402) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {


        /*// 交易发起
        Toa1401 toa = (Toa1401) new SyncSocketClient().onRequest(tia2401);

        if (toa == null) throw new RuntimeException("网络异常。");

        logger.info("[1534010-应收数据查询-响应] MsgRef：" + toa.Head.msgRef +
                " 缴款书ID：" + toa.Body.Object.Record.chr_id +
                " 票号：" + toa.Body.Object.Record.bill_no +
                " 明细数：" + toa.Body.Object.Record.Object.size());*/
        new SyncSocketClient().onRequest(tia2402);

        // TODO　业务逻辑

        return null;
    }

}
