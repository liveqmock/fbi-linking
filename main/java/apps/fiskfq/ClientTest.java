package apps.fiskfq;

import apps.fiskfq.gateway.client.SyncSocketClient;
import apps.fiskfq.gateway.domain.txn.*;

import java.io.IOException;

/**
 * ª∆µ∫∑«À∞≤‚ ‘
 */
public class ClientTest {

    public static void main(String[] args) {

        try {
//            test9905();
            test9907();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void test9905() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Tia9905 tia = new Tia9905();
        tia.Head.dataType = "9905";
        tia.Body.Object.Record.user_code = "ccb01";
        tia.Body.Object.Record.password = "000000";
        tia.Body.Object.Record.new_password = "";
        Toa9906 toa = (Toa9906) new SyncSocketClient().onRequest(tia);
        System.out.println(toa.Body.Object.Record.login_result);
        System.out.println(toa.Body.Object.Record.accredit_code);
        System.out.println(toa.Body.Object.Record.add_word);
    }

    public static void test9907() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Tia9907 tia = new Tia9907();
        tia.Head.dataType = "9907";
        tia.Body.Object.Record.password = "000000";
        Toa9908 toa = (Toa9908) new SyncSocketClient().onRequest(tia);
        System.out.println(toa.Body.Object.Record.login_result);
        System.out.println(toa.Body.Object.Record.add_word);
    }
}
