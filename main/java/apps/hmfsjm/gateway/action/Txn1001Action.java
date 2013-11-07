package apps.hmfsjm.gateway.action;

import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Tia;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia1001;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1001-�ɿ��Ϣ��ѯ
 */
@Component
public class Txn1001Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1001Action.class);
    @Autowired
    private SyncSocketClient syncSocketClient;

    @Override
    public Toa process(Tia tia) throws Exception {

        Tia1001 tia1001 = (Tia1001) tia;
        tia1001.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        logger.info("[1001-�ɿ��Ϣ��ѯ-����] ��ˮ�ţ�" + tia1001.INFO.REQ_SN + " ���ţ�" + tia1001.BODY.PAY_BILLNO);

        // ���׷���
        Toa1001 toa = (Toa1001) syncSocketClient.onRequest(tia1001);

        if (toa == null) throw new RuntimeException("�����쳣��");

        logger.info("[1001-�ɿ��Ϣ��ѯ-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                " ���ţ�" + toa.BODY.PAY_BILLNO +
                " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                " ״̬˵����" + toa.BODY.BILL_STS_TITLE);
        return toa;
    }
}
