package apps.fisjz.gateway.financebureau;

import apps.fisjz.PropertyManager;
import com.caucho.burlap.client.BurlapProxyFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * //���ӳ�ʱ����
 * User: zhanrui
 * Date: 13-9-25
 * Time: ����10:24
 */
public class NontaxBurlapProxyFactory extends BurlapProxyFactory {
    protected URLConnection openConnection(URL url) throws IOException {
        URLConnection conn = super.openConnection(url);
        int timeout = 0;
        try {
            timeout = Integer.parseInt(PropertyManager.getProperty("thirdparty.nontax.jiaozhou.server.timeout"));
        } catch (NumberFormatException e) {
            throw new RuntimeException("������ַ��������ӳ�ʱ���ô���");
        }
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        return conn;
    }
}
