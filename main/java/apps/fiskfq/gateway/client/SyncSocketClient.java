package apps.fiskfq.gateway.client;


import apps.fiskfq.PropertyManager;
import apps.fiskfq.gateway.domain.base.Tia;
import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.txn.Toa9910;
import apps.fiskfq.online.service.FskfqSysCtlService;
import apps.fiskfq.repository.model.FsKfqSysCtl;
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
    private static final String BANK_ID = PropertyManager.getProperty("bank.id");

    private static Logger logger = LoggerFactory.getLogger(SyncSocketClient.class);

    private FskfqSysCtlService sysCtlService = new FskfqSysCtlService();

    // 自定义 SocketClient
    public Toa onRequest(Tia tia) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        FsKfqSysCtl info = sysCtlService.getSysCtl(BANK_ID);
        Toa toa = doRequest(tia, info);
        if (toa instanceof Toa9910) {
            // TODO 授权码无效
            throw new RuntimeException("授权码无效");
            //
        } else
            return toa;
    }

    public Toa doRequest(Tia tia, FsKfqSysCtl info) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        socket.setSoTimeout(Integer.parseInt(SERVER_TIMEOUT));
        OutputStream os = socket.getOutputStream();
        byte[] reqBytes = CustomCodeHandler.encode(tia,
                info.getSrcCode(), info.getDesCode(), info.getIsSign(), info.getAuthLen());
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

        return CustomCodeHandler.decode(dataBytes);

    }

}
