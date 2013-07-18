package startup;

import apps.fis.domain.txn.Toa3001;
import common.utils.PropertyManager;
import gateway.component.LocalServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * start linking
 */
public class Bootstrap {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        try {
            // �������ط����
            logger.info("Linking's ServerSocket����......");
            int portForStaring = PropertyManager.getIntProperty("linking.socket.port.fis");
            LocalServerBootstrap localServer = new LocalServerBootstrap(portForStaring);
            localServer.start();

            AppsStartManager.init();

            logger.info("Linking�������......");
        } catch (Exception e) {
            logger.error("Linking��ʼ������", e);
            System.exit(-1);
        }
    }
}
