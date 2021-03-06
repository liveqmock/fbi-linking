package apps.fisjz.staring;

import gateway.domain.LFixedLengthProtocol;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: zhanrui
 * Date: 13-9-22
 */
public class Txn1532010Test {
    public static void main(String[] args) {
        try {
            LFixedLengthProtocol t = new LFixedLengthProtocol();
            t.appID = "fisjz";
            t.ueserID = "FIS153";
            t.tellerID = "9999";
            t.branchID = "9999";
            t.txnCode = "1532010";
            t.txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            t.serialNo = "SPDB" + t.txnTime;

            t.msgBody = "2013|530003|PJBH001|YZM001|PJLX001|".getBytes();
            t.msgBody = "2013|530003|130000010001|FM5EH6|0|".getBytes();

            System.out.println("发送报文：" + new String(t.toByteArray()));
            //Socket socket = new Socket("10.22.0.45", 60001);
            Socket socket = new Socket("localhost", 60001);
            socket.setSoTimeout(60000);
            OutputStream os = socket.getOutputStream();
            os.write(t.toByteArray());
            os.flush();

            InputStream is = socket.getInputStream();
            byte[] bytes = readBytesFromInputStream(is);
            System.out.println("返回报文：" + new String(bytes));
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
