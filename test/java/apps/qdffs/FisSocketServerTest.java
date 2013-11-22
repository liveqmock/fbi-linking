package apps.qdffs;

import apps.fis.domain.txn.Toa3001;
import apps.fis.socket.codec.B64Code;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-6-5
 * Time: ÏÂÎç3:53
 * To change this template use File | Settings | File Templates.
 */
public class FisSocketServerTest {
    public static void main(String[] args) throws ParseException {
        if (true) {
        System.out.println("2010-04-01".substring(8, 10));
        return;
        }
        try {
            ServerSocket ss = new ServerSocket(2001);
//            ss.setSoTimeout(5000);
            while (true) {
                Socket socket = ss.accept();
                System.out.println("New connection accepted" +
                        socket.getInetAddress() + ":" + socket.getPort());
                InputStream is = socket.getInputStream();
                System.out.println(" get input content.");
                byte[] req = readBytesFromInputStream(is);
                System.out.println(" get input content over.");

                System.out.println(new String(req));
                OutputStream os = socket.getOutputStream();

                Toa3001 toa = new Toa3001();
                toa.HEAD.STATUS = "0";
                toa.HEAD.MESSAGE = "success";
                // base64±àÂë
                byte[] bytes = B64Code.encode(toa.toString()).getBytes();
                // zip
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(baos);
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("data");
                zaos.putArchiveEntry(zipArchiveEntry);
                IOUtils.copy(bais, zaos);
                zaos.closeArchiveEntry();
                byte[] rtnbytes = baos.toByteArray();
                zaos.finish();
                zaos.close();
                os.write(rtnbytes);
                os.flush();
                os.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static byte[] readBytesFromInputStream(InputStream is) throws IOException {
        if (is != null) {
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] byteBuffer = new byte[100];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            len = bis.read(byteBuffer);
            baos.write(byteBuffer, 0, len);
            baos.flush();
//            bis.close();
//            is.close();
            return baos.toByteArray();
        } else
            return null;
    }

}
