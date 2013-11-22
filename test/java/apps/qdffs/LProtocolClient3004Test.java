package apps.qdffs;

import gateway.domain.LFixedLengthProtocol;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-3-28
 * Time: ����3:17
 * To change this template use File | Settings | File Templates.
 */
// �������ϴ�
public class LProtocolClient3004Test {
    public static void main(String[] args) {
        try {
            LFixedLengthProtocol t = new LFixedLengthProtocol();
            t.appID = "fis";
            t.ueserID = "FIS153";
            t.tellerID = "9999";
            t.branchID = "9999";
            t.txnCode = "1533004";
            t.txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            t.serialNo = t.txnTime;
//            t.msgBody = ("370200|BMK081001|112233445566|04||" + t.txnTime.substring(0, 8) + "|101.1|").getBytes();
//            t.msgBody = ("370200|BMK081002|112233445566|04||" + t.txnTime.substring(0, 8) + "|300.58|").getBytes();
//            t.msgBody = ("370200|BMK081003|112233445566|04||" + t.txnTime.substring(0, 8) + "|100|").getBytes();
//            t.msgBody = ("370200|BMK081004|112233445566|04||" + t.txnTime.substring(0, 8) + "|200|").getBytes();
//            t.msgBody = ("370200|BMK081005|112233445566|04||" + t.txnTime.substring(0, 8) + "|300|").getBytes();
            t.msgBody = ("370200|BMK081006|112233445566|04||" + t.txnTime.substring(0, 8) + "|400|").getBytes();
            System.out.println("���ͱ��ģ�" + new String(t.toByteArray()));
//            Socket socket = new Socket("48.135.44.51", 60001);
            Socket socket = new Socket("10.22.0.45", 60001);
            socket.setSoTimeout(61000);
            OutputStream os = socket.getOutputStream();
            os.write(t.toByteArray());
            os.flush();

            InputStream is = socket.getInputStream();
            byte[] bytes = readBytesFromInputStream(is);
            System.out.println("���ر��ģ�" + new String(bytes));
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] readBytesFromInputStream(InputStream is) throws IOException {
        if (is != null) {
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] byteBuffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            while ((len = bis.read(byteBuffer)) != -1) {
                baos.write(byteBuffer, 0, len);
            }
            baos.flush();
            bis.close();
            is.close();
            return baos.toByteArray();
        } else
            return null;
    }

    public static String appendStrToLength(String srcStr, String appendStr, int length) {
        int appendLength = length - srcStr.getBytes().length;
        StringBuilder strBuilder = new StringBuilder(srcStr);
        for (int i = 1; i <= appendLength; i++) {
            strBuilder.append(appendStr);
        }
        return strBuilder.toString();
    }
}