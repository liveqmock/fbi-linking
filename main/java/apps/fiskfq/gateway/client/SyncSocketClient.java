package apps.fiskfq.gateway.client;


import apps.fiskfq.PropertyManager;
import apps.fiskfq.gateway.domain.base.Tia;
import apps.fiskfq.gateway.domain.base.Toa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 同步Socket客户端
 */
public class SyncSocketClient {
    private static final String SERVER_IP = PropertyManager.getProperty("socket.ip");
    private static final int SERVER_PORT = PropertyManager.getIntProperty("socket.port");
    private static final String SERVER_TIMEOUT = PropertyManager.getProperty("socket.timeout");

    private static Logger logger = LoggerFactory.getLogger(SyncSocketClient.class);

    // 自定义 SocketClient
    public Toa onRequest(Tia tia) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        socket.setSoTimeout(Integer.parseInt(SERVER_TIMEOUT));
        OutputStream os = socket.getOutputStream();
        byte[] reqBytes = CustomCodeHandler.encode(tia);
        os.write(reqBytes);
        os.flush();
        InputStream is = socket.getInputStream();
        byte[] lengthBytes = new byte[8];
        is.read(lengthBytes);
        int toReadlength = Integer.parseInt(new String(lengthBytes)) - 8;
        byte[] dataBytes = new byte[toReadlength];
        int redlen = is.read(dataBytes);
        logger.info(new String(dataBytes));

        if (redlen != toReadlength) {

            throw new RuntimeException("报文长度读取失败");
        }
        /*byte[] bytes = new byte[64];
        int index = 0;
        int curlen = 0;
        while ((curlen = is.read(bytes)) == 64) {
            System.arraycopy(bytes, 0, dataBytes, index, bytes.length);
            index += bytes.length;
        }
        if (curlen > 0) {
            System.arraycopy(bytes, 0, dataBytes, index, curlen);
        }*/
        return CustomCodeHandler.decode(dataBytes);
    }

}
