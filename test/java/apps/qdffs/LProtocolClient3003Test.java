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
 * Time: 下午3:17
 * To change this template use File | Settings | File Templates.
 */
public class LProtocolClient3003Test {
    public static void main(String[] args) {
        try {
            LFixedLengthProtocol t = new LFixedLengthProtocol();
            t.appID = "fis";
            t.ueserID = "FIS153";
            t.tellerID = "9999";
            t.branchID = "9999";
            t.txnCode = "1533003";
            t.txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            t.serialNo = t.txnTime;
            // TODO
            t.msgBody = "370200|20130731|37101986610050011692|584875.6|103|76021,150660.00      ,D|76025,39600.00       ,D|76027,72.00          ,D|76031,3300.00        ,D|76033,3300.00        ,D|76035,2500.00        ,D|76038,2500.00        ,D|76040,2500.00        ,D|94430,392.00         ,D|94431,216.00         ,D|94434,3300.00        ,D|94435,82350.00       ,D|94437,2500.00        ,D|94439,2500.00        ,D|94441,2500.00        ,D|94443,3300.00        ,D|94445,3300.00        ,D|94447,2500.00        ,D|94449,3300.00        ,D|94451,2500.00        ,D|94453,2500.00        ,D|94456,2500.00        ,D|94458,5300.00        ,D|94460,3300.00        ,D|94463,3300.00        ,D|94465,4500.00        ,D|94467,3300.00        ,D|94471,3300.00        ,D|94473,2500.00        ,D|94475,3300.00        ,D|140295,72.00          ,D|140297,144.00         ,D|140299,15569.60       ,D|140301,3300.00        ,D|140302,3300.00        ,D|140302,3300.00        ,D|94443,3300.00        ,D|94445,3300.00        ,D|94447,2500.00        ,D|94449,3300.00        ,D|94451,2500.00        ,D|94453,2500.00        ,D|94456,2500.00        ,D|94458,5300.00        ,D|94460,3300.00        ,D|94463,3300.00        ,D|94465,4500.00        ,D|94467,3300.00        ,D|94471,3300.00        ,D|94473,2500.00        ,D|94475,3300.00        ,D|140295,72.00          ,D|140297,144.00         ,D|140299,15569.60       ,D|140301,3300.00        ,D|140302,3300.00        ,D|140302,3300.00        ,D".getBytes();

            System.out.println("发送报文：" + new String(t.toByteArray()));
//            Socket socket = new Socket("48.135.44.51", 60001);
//            Socket socket = new Socket("10.22.0.45", 60001);
            Socket socket = new Socket("127.0.0.1", 60001);
            socket.setSoTimeout(61000);
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
