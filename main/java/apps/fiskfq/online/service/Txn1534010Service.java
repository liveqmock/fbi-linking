package apps.fiskfq.online.service;

import apps.fiskfq.gateway.client.SyncSocketClient;
import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.ToaXml;
import apps.fiskfq.gateway.domain.txn.Tia2401;
import apps.fiskfq.repository.MybatisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 应收数据查询
 */
public class Txn1534010Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn1534010Service.class);
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, Tia2401 tia2401) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {


        /*// 交易发起
        Toa1401 toa = (Toa1401) new SyncSocketClient().onRequest(tia2401);

        if (toa == null) throw new RuntimeException("网络异常。");

        logger.info("[1534010-应收数据查询-响应] MsgRef：" + toa.Head.msgRef +
                " 缴款书ID：" + toa.Body.Object.Record.chr_id +
                " 票号：" + toa.Body.Object.Record.bill_no +
                " 明细数：" + toa.Body.Object.Record.Object.size());*/
        // TODO 从数据库或配置文件读取以下字段值
        tia2401.Head.src = "CCB-370211";
        tia2401.Head.des = "CZ-370211";
        ToaXml toa = (ToaXml)new SyncSocketClient().onRequest(tia2401);

        // TODO　业务逻辑

        return toa;
    }

}
