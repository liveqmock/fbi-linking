package apps.hmfsjm.gateway.action;

import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Tia;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia3001;
import apps.hmfsjm.gateway.domain.txn.Toa3001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 3001-退款单信息查询
 */
@Component
public class Txn3001Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn3001Action.class);
    @Autowired
    private SyncSocketClient syncSocketClient;

    @Override
    public Toa process(Tia tia) throws Exception {

        Tia3001 tia3001 = (Tia3001) tia;
        tia3001.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        logger.info("[3001-退款单信息查询-请求] 流水号：" + tia3001.INFO.REQ_SN + " 单号：" + tia3001.BODY.REFUND_BILLNO);

        // 交易发起
        Toa3001 toa = (Toa3001) syncSocketClient.onRequest(tia3001);

        if (toa == null) throw new RuntimeException("网络异常。");

        logger.info("[3001-退款单信息查询-响应] 流水号：" + toa.INFO.REQ_SN +
                " 单号：" + toa.BODY.REFUND_BILLNO +
                " 状态码：" + toa.BODY.BILL_STS_CODE +
                " 状态说明：" + toa.BODY.BILL_STS_TITLE);
        return toa;
    }
}
