package apps.hmfsjm.gateway.action;

import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Tia;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia3002;
import apps.hmfsjm.gateway.domain.txn.Toa3002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 3002-退款确认
 */
@Component
public class Txn3002Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn3002Action.class);
    @Autowired
    private SyncSocketClient syncSocketClient;

    @Override
    public Toa process(Tia tia) throws Exception {

        Tia3002 tia3002 = (Tia3002) tia;
        logger.info("[3002-退款确认-请求] 流水号：" + tia3002.INFO.REQ_SN + " 单号：" + tia3002.BODY.REFUND_BILLNO);

        // 交易发起
        Toa3002 toa = (Toa3002) syncSocketClient.onRequest(tia3002);

        if (toa == null) throw new RuntimeException("网络异常。");

        logger.info("[3002-退款确认-响应] 流水号：" + toa.INFO.REQ_SN +
                " 单号：" + toa.BODY.REFUND_BILLNO +
                " 状态码：" + toa.BODY.BILL_STS_CODE +
                " 状态说明：" + toa.BODY.BILL_STS_TITLE);
        return toa;
    }
}
