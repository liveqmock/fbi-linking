package apps.fisjz.gateway.financebureau;

import apps.fisjz.PropertyManager;
import com.caucho.burlap.client.BurlapProxyFactory;

import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: ����1:29
 */
public class NontaxServiceFactory {
    private static NontaxServiceFactory instance = new NontaxServiceFactory();
    private static BurlapProxyFactory burlapProxyFactory = new NontaxBurlapProxyFactory();


    public static NontaxServiceFactory getInstance() {
        return instance;
    }

    private NontaxServiceFactory() {
    }

    public NontaxBankService getNontaxBankService() {
        String url = PropertyManager.getProperty("thirdparty.nontax.server.530003");
        NontaxBankService service = null;
        try {
            service = (NontaxBankService) burlapProxyFactory.create(NontaxBankService.class, url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("�ӿڴ���!", e);
        }
        return service;
    }

}
