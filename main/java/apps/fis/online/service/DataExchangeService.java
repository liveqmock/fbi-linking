package apps.fis.online.service;

import apps.fis.SystemParameter;
import apps.fis.domain.base.Tia;
import apps.fis.domain.base.Toa;
import apps.fis.enums.TxnRtnCode;
import apps.fis.ftp.FtpClient;
import apps.fis.socket.client.SocketClientSponsor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-5-14
 * Time: ÏÂÎç3:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DataExchangeService {
    private static Logger logger = LoggerFactory.getLogger(DataExchangeService.class);
    @Autowired
    private SocketClientSponsor client;

    public Toa process(Tia tia) {
        try {
            return client.execute(tia);
        } catch (Exception e) {
            if (e.getMessage() == null) {
                logger.error(TxnRtnCode.MSG_SEND_ERROR.toRtnMsg(), e);
                throw new RuntimeException(TxnRtnCode.MSG_SEND_ERROR.toRtnMsg());
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public Toa process(Tia tia, int timeout) {
        try {
            return client.execute(tia, timeout);
        } catch (Exception e) {
            if (e.getMessage() == null) {
                logger.error(TxnRtnCode.MSG_SEND_ERROR.toRtnMsg(), e);
                throw new RuntimeException(TxnRtnCode.MSG_SEND_ERROR.toRtnMsg());
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public String getFtpfileData(String fileName) throws IOException {
        FtpClient ftpClient = new FtpClient(SystemParameter.SERVER_FTP_IP, SystemParameter.SERVER_FTP_USERID, SystemParameter.SERVER_FTP_PASSWORD);
        String data = ftpClient.readFile(null, fileName);
        ftpClient.logout();
        return data;
    }

    public boolean uploadFtpfileData(String localPath, String fileName) throws IOException {
        FtpClient ftpClient = new FtpClient(SystemParameter.SERVER_FTP_IP, SystemParameter.SERVER_FTP_USERID, SystemParameter.SERVER_FTP_PASSWORD);
        boolean isUploaded = ftpClient.uploadFile(null, localPath, fileName);
        ftpClient.logout();
        return isUploaded;
    }
}
