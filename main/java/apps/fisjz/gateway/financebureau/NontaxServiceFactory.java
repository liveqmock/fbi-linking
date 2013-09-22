package apps.fisjz.gateway.financebureau;

import apps.fisjz.PropertyManager;
import com.caucho.burlap.client.BurlapProxyFactory;

import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: ÏÂÎç1:29
 */
public class NontaxServiceFactory {
    private static NontaxServiceFactory instance = new NontaxServiceFactory();
    private static BurlapProxyFactory burlapProxyFactory = new BurlapProxyFactory();


    public static NontaxServiceFactory getInstance() {
        return instance;
    }

    private NontaxServiceFactory() {
    }

    public NontaxBankService getElementService() {
        String url = PropertyManager.getProperty("thirdparty.nontax.jiaozhou.server");
        NontaxBankService elementService = null;
        try {
            elementService = (NontaxBankService) burlapProxyFactory.create(NontaxBankService.class, url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("½Ó¿Ú´íÎó!", e);
        }
        return elementService;
    }

}
