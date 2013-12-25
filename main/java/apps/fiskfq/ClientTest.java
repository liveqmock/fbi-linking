package apps.fiskfq;

import apps.fiskfq.gateway.client.CustomCodeHandler;
import apps.fiskfq.gateway.client.SyncSocketClient;
import apps.fiskfq.gateway.domain.txn.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 开发区非税测试
 */
public class ClientTest {

    public static void main(String[] args) {

        try {
//            System.out.println("DVJfrWuqXA5NGtGJNYRUsTiKVKL1UmaliXROT7vSkb4=".length());
//            test9905();
            testStaring();
//            test9907();
//            Toa9910 toa = new Toa9910();        370211
//            System.out.println(toa.toString());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void testStaring() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        String str = "148   1.017631             99991534010371986106371986106021fis154FISKFQ201312251109103B77D28F5AE5184F                101|101000000201|5057|100|2013|";
        Socket socket = new Socket("127.0.0.1", 60005);
        socket.setSoTimeout(10000);
        OutputStream os = socket.getOutputStream();
        byte[] reqBytes = str.getBytes();
        os.write(reqBytes);
        os.flush();
        InputStream is = socket.getInputStream();
        byte[] lengthBytes = new byte[6];
        is.read(lengthBytes);
        System.out.println("待接收报文总长度：" + new String(lengthBytes));
        int toReadlength = Integer.parseInt(new String(lengthBytes).trim()) - 6;
        byte[] dataBytes = new byte[toReadlength];
        int readlen = is.read(dataBytes);
        System.out.println(new String(dataBytes));

        if (readlen != toReadlength) {

            throw new RuntimeException("报文长度读取失败");
        }

    }


    public static void test9905() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Tia9905 tia = new Tia9905();
        tia.Body.Object.Record.user_code = "ccb01";
        tia.Body.Object.Record.password = "000000";
        tia.Body.Object.Record.new_password = "";
        tia.Head.src = "CCB-370211";
        tia.Head.des = "CZ-370211";
        tia.Head.dataType = "9905";
        Toa9906 toa = (Toa9906) new SyncSocketClient().onRequest(tia);
        System.out.println(toa.Body.Object.Record.login_result);
        System.out.println(toa.Body.Object.Record.accredit_code);
        System.out.println(toa.Body.Object.Record.add_word);
    }

}
