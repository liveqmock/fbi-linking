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
public class Txn1532013Test {
    public static void main(String[] args) {
        try {
            LFixedLengthProtocol t = new LFixedLengthProtocol();
            t.appID = "fisjz";
            t.ueserID = "FIS153";
            t.tellerID = "9999";
            t.branchID = "9999";
            t.txnCode = "1532013";
            t.txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            t.serialNo = "SPDB" + t.txnTime;

            t.msgBody = ("2013" +
                    "|CZBM" +
                    "|BANKNUM" +
                    "|NOTESCODE" +
                    "|CHECKCODE" +
                    "|123.45" +
                    "|NOTESKINDCODE" +
                    "|NOTESKINDNAME" +
                    "|PERFORMDEPTCODE" +
                    "|PERFORMDEPTNAME" +
                    "|PRINTDATE" +
                    "|AGENTBANKCODE" +
                    "|AGENTBANKNAME" +
                    "|PAYFEEMETHODCODE" +
                    "|PAYFEEMETHODNAME" +
                    "|PAYMETHODCODE" +
                    "|PAYMETHODNAME" +
                    "|PAYER" +
                    "|PAYERBANK" +
                    "|PAYERBANKACCT" +
                    "|REMARK" +
                    "|CREATETIME" +
                    "|CREATER" +
                    "|BANKRECDATE" +
                    "|BANKACCTDATE" +
                    "|ISPREAUDIT" +
                    "|RECFEEFLAG" +
                    "|BILLTYPE" +
                    "|LATEFEE" +
                    "|2" +
                    "|BANKNUM,NONTAXPROGRAMCODE,NONTAXPROGRAMNAME,UNITS,100,STANDARDKINDCODE,STANDARDKINDNAME,123.45" +
                    "|BANKNUM2,NONTAXPROGRAMCODE2,NONTAXPROGRAMNAME2,UNITS2,200,STANDARDKINDCODE2,STANDARDKINDNAME2,123456.45" +
                    "").getBytes();

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
