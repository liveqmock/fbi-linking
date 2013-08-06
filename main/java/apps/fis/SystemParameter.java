package apps.fis;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-6-5
 * Time: ÉÏÎç9:40
 * To change this template use File | Settings | File Templates.
 */
public class SystemParameter {
    public static final String YHLB = PropertyManager.getProperty("dept.id");
    public static final String USERNAME = PropertyManager.getProperty("socket.dept.userid");
    public static final String PASSWORD = PropertyManager.getProperty("socket.dept.password");
    public static final String SERVER_SOCKET_IP = PropertyManager.getProperty("server.fis.socket.ip");
    public static final int SERVER_SOCKET_PORT = PropertyManager.getIntProperty("server.fis.socket.port");
    public static final int SERVER_TIMEOUT = PropertyManager.getIntProperty("server.fis.socket.timeout");
    public static final int CHKACT_FIS_SOCKET_TIMEOUT = PropertyManager.getIntProperty("chkact.fis.socket.timeout");
    public static final String SERVER_FTP_IP = PropertyManager.getProperty("server.fis.ftp.ip");
    public static final String SERVER_FTP_USERID = PropertyManager.getProperty("ftp.dept.userid");
    public static final String SERVER_FTP_PASSWORD = PropertyManager.getProperty("ftp.dept.password");
    public static final String LOCAL_FTP_FILE_PATH = PropertyManager.getProperty("upload.ftp.local.file.path");
}
