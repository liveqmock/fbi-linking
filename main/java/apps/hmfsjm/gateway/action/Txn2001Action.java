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
 * 2001-�ɿ�ȷ��
 */
@Component
public class Txn2001Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn2001Action.class);
    @Autowired
    private SyncSocketClient syncSocketClient;

    @Override
    public Toa process(Tia tia) throws Exception {

        Tia2001 tia2001 = (Tia2001) tia;
        logger.info("[2001-�ɿ�ȷ��-����] ��ˮ�ţ�" + tia2001.INFO.REQ_SN + " ���ţ�" + tia2001.BODY.PAY_BILLNO);

        // ���׷���
        Toa2001 toa = (Toa2001) syncSocketClient.onRequest(tia2001);

        if (toa == null) throw new RuntimeException("�����쳣��");

        logger.info("[2001-�ɿ�ȷ��-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                " ���ţ�" + toa.BODY.PAY_BILLNO +
                " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                " ״̬˵����" + toa.BODY.BILL_STS_TITLE);
        return toa;
    }
}
