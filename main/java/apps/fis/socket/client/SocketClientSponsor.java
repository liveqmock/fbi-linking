package apps.fis.socket.client;

import apps.fis.SystemParameter;
import apps.fis.domain.base.Tia;
import apps.fis.domain.base.Toa;
import apps.fis.socket.codec.TiaBase64ZipEncoder;
import apps.fis.socket.codec.ToaUnzipBase64Decoder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 发起客户端
 */
@Service
@Scope("prototype")
public class SocketClientSponsor {

    public Toa execute(Tia tia) throws Exception {
        OutputStream os = null;
        try {
            byte[] reqBytes = TiaBase64ZipEncoder.encode(tia);
            Socket socket = new Socket(SystemParameter.SERVER_SOCKET_IP, SystemParameter.SERVER_SOCKET_PORT);
            socket.setSoTimeout(SystemParameter.SERVER_TIMEOUT);
            os = socket.getOutputStream();
            os.write(reqBytes);
            os.flush();
            InputStream is = socket.getInputStream();
            byte[] resBytes = ToaUnzipBase64Decoder.readBytesFromInputStream(is);
            Toa toa = ToaUnzipBase64Decoder.decode(resBytes);
            return toa;
        } finally {
             if(os != null) {
                 os.close();
             }
        }
    }
}
