package apps.hmfsjm.gateway.action;

import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Tia;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia2001;
import apps.hmfsjm.gateway.domain.txn.Toa2001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 2001-缴款确认
 */
@Component
public class Txn2001Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn2001Action.class);
    @Autowired
    private SyncSocketClient syncSocketClient;

    @Override
    public Toa process(Tia tia) throws Exception {

        Tia2001 tia2001 = (Tia2001) tia;
        logger.info("[2001-缴款确认-请求] 流水号：" + tia2001.INFO.REQ_SN + " 单号：" + tia2001.BODY.PAY_BILLNO);

        // 交易发起
        Toa2001 toa = (Toa2001) syncSocketClient.onRequest(tia2001);

        if (toa == null) throw new RuntimeException("网络异常。");

        logger.info("[2001-缴款确认-响应] 流水号：" + toa.INFO.REQ_SN +
                " 单号：" + toa.BODY.PAY_BILLNO +
                " 状态码：" + toa.BODY.BILL_STS_CODE +
                " 状态说明：" + toa.BODY.BILL_STS_TITLE);
        return toa;
    }
}
