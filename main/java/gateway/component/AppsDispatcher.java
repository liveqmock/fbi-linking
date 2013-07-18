package gateway.component;

import apps.TxnHandler;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import startup.AppsStartManager;

/**
 * �����ķ��������Ӧ��
 */
public class AppsDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(AppsDispatcher.class);

    public LFixedLengthProtocol dispatch(LFixedLengthProtocol txn) throws Exception {

        if (StringUtils.isEmpty(txn.appID)) {
            throw new RuntimeException("9000|Ӧ�ñ�ʶ����Ϊ�ա�");
        }
        String appid = txn.appID.trim().toLowerCase();

        TxnHandler txnHandler = (TxnHandler) AppsStartManager.getBean(appid, appid + "TxnHandler");
        if (txnHandler == null) {
            String txnHandlerName = appid.substring(0, 1).toUpperCase()
                    + appid.substring(1, appid.length()).toLowerCase() + "TxnHandler";
            txnHandler = (TxnHandler) Class.forName("apps." + appid.toLowerCase() + "." + txnHandlerName).newInstance();
        }
        return txnHandler.handle(txn);
    }
}
