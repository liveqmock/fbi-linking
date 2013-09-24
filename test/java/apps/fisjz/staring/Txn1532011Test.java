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
public class Txn1532011Test {
    public static void main(String[] args) {
        try {
            LFixedLengthProtocol t = new LFixedLengthProtocol();
            t.appID = "fisjz";
            t.ueserID = "FIS153";
            t.tellerID = "9999";
            t.branchID = "9999";
            t.txnCode = "1532011";
            t.txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            t.serialNo = "SPDB" + t.txnTime;


            t.msgBody = ("|2013" +
                    "|CZBM" +
                    "|BANKNUM" +
                    "|BILLID" +
                    "|PAYNOTESCODE" +
                    "|NOTESCODE" +
                    "|CHECKCODE" +
                    "|123.45" +
                    "|NOTESKINDCODE" +
                    "|NOTESKINDNAME" +
                    "|AGENTBANKCODE" +
                    "|AGENTBANKNAME" +
                    "|BANKRECDATE" +
                    "|BANKACCTDATE" +
                    "|ISPREAUDIT" +
                    "|RECFEEFLAG" +
                    "|BILLTYPE" +
                    "|LATEFEE" +
                    "|BANKNOTESCODE" +
                    "|").getBytes();

//            t.msgBody = ("|1|2|3|5300013092076|2013000000002|130000010002|VPR955|52000|00000022||||20130923|20130923|1|0|0|0.00||").getBytes();
            t.msgBody = ("2013|530001|2702|2076|2013000000002|130000010002|VPR955|52000|00000022||106001|建设银行胶州市支行|20130924|20130924|1|0|0|0.00||").getBytes();

            System.out.println("发送报文：" + new String(t.toByteArray()));
            //Socket socket = new Socket("10.22.0.45", 60001);
            Socket socket = new Socket("localhost", 60001);
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
