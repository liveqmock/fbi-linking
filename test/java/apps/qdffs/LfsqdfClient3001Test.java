package apps.qdffs;

import apps.fis.socket.codec.B64Code;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

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
public class LfsqdfClient3001Test {
    public static void main(String[] args) {
        try {
            String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><ROOT>\n" +
                    "  <HEAD TYPE=\"0\" CODE=\"3001\">\n" +
                    "    <YHLB>08</YHLB>\n" +
                    "    <USERNAME>pfyh</USERNAME>\n" +
                    "    <PASSWORD>pfyh</PASSWORD>\n" +
                    "  </HEAD>\n" +
                    "  <BODY>\n" +
                    "    <DATA>\n" +
                    "      <PJZL>01</PJZL>\n" +
                    "      <JKSBH>121000000203</JKSBH>\n" +
                    "    </DATA>\n" +
                    "  </BODY>\n" +
                    "</ROOT>";
            System.out.println("发送报文：" + xml);
            Socket socket = new Socket("10.22.129.153", 2000);
            socket.setSoTimeout(61000);
            // base64编码
            byte[] bytes = B64Code.encode(xml).getBytes();
            // zip
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(baos);
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("data");
            zaos.putArchiveEntry(zipArchiveEntry);
            IOUtils.copy(bais, zaos);
            zaos.closeArchiveEntry();
            zaos.finish();
            zaos.close();
            OutputStream os = socket.getOutputStream();
            os.write(baos.toByteArray());
            os.flush();

            InputStream is = socket.getInputStream();
            byte[] readbytes = readBytesFromInputStream(is);
            System.out.println("返回报文[压缩]：" + new String(readbytes));
            // unzip
            ByteArrayInputStream rbais = new ByteArrayInputStream(readbytes);
            ZipArchiveInputStream zais = new ZipArchiveInputStream(rbais);
            zais.getNextZipEntry();
            byte[] contentBytes = readBytesFromInputStream(zais);
            // base64
            String strXml = B64Code.decode(new String(contentBytes));

            System.out.println("[返回报文[明文]]" + strXml);
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
