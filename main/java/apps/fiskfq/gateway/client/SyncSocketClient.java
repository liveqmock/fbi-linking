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
 * ͬ��Socket�ͻ���
 */
public class SyncSocketClient {
    private static final String SERVER_IP = PropertyManager.getProperty("socket.ip");
    private static final int SERVER_PORT = PropertyManager.getIntProperty("socket.port");
    private static final int SERVER_TIMEOUT = PropertyManager.getIntProperty("socket.timeout");
    private static final String BANK_ID = PropertyManager.getProperty("bank.id");

    private static Logger logger = LoggerFactory.getLogger(SyncSocketClient.class);

    private FskfqSysCtlService sysCtlService = new FskfqSysCtlService();

    // �Զ��� SocketClient
    public Toa onRequest(Tia tia) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        FsKfqSysCtl info = sysCtlService.getSysCtl(BANK_ID);
        Toa toa = doRequest(tia, info);

        return toa;
    }

    public Toa doRequest(Tia tia, FsKfqSysCtl info) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        socket.setSoTimeout(SERVER_TIMEOUT);
        OutputStream os = socket.getOutputStream();
        byte[] reqBytes = CustomCodeHandler.encode(tia,
                info.getSrcCode(), info.getDesCode(),
                info.getIsSign(), info.getAuthLen(), info.getAuthCode());
        os.write(reqBytes);
        os.flush();
        InputStream is = socket.getInputStream();
        byte[] lengthBytes = new byte[8];
        is.read(lengthBytes);
        logger.info("�����ձ����ܳ��ȣ�" + new String(lengthBytes));
        int toReadlength = Integer.parseInt(new String(lengthBytes)) - 8;
        /*byte[] dataBytes = new byte[toReadlength];
        int redlen = is.read(dataBytes);
        logger.info(new String(dataBytes));

        if (redlen != toReadlength) {

            throw new RuntimeException("���Ķ�ȡʧ�ܣ�����ȡ���ȣ�" + redlen + "/" + toReadlength);
        }
*/

        // TODO


        byte[] dataBytes = new byte[toReadlength];
        byte[] bytes = new byte[256];
        int index = 0;
        int curlen = 0;
        while ((curlen = is.read(bytes)) == 256) {
            System.arraycopy(bytes, 0, dataBytes, index, curlen);
            index += curlen;
            logger.info("���α���[" + index + "] " + new String(bytes));
        }
        if (curlen > 0) {
            logger.info("���һ������:" + curlen);
            logger.info("���α���[" + (curlen) + "] " + new String(bytes, 0,  curlen));

            System.arraycopy(bytes, 0, dataBytes, index, toReadlength - index);
        }
        return CustomCodeHandler.decode(dataBytes);

    }

}
