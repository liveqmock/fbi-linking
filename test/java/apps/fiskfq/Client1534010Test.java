package apps.fiskfq;

import apps.fiskfq.gateway.client.SyncSocketClient;
import apps.fiskfq.gateway.domain.txn.Tia9905;
import apps.fiskfq.gateway.domain.txn.Toa9906;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 开发区非税测试
 */
public class Client1534010Test {

    public static void main(String[] args) {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
