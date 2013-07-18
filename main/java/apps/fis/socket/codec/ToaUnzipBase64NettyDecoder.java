package apps.fis.socket.codec;

import apps.fis.domain.base.Toa;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 *  base64 -> xml -> Toa
 */
@Deprecated
public class ToaUnzipBase64NettyDecoder extends OneToOneDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ToaUnzipBase64NettyDecoder.class);

    @Override
    protected Toa decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

        ChannelBuffer channelBuffer = (ChannelBuffer) msg;
        byte[] bytes = channelBuffer.array();
        // unzip
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ZipArchiveInputStream zais = new ZipArchiveInputStream(bais);
        zais.getNextZipEntry();
        byte[] contentBytes = readBytesFromInputStream(zais);
        // base64
        String strXml = B64Code.decode(new String(contentBytes));

        logger.info("[linking ToaUnzipBase64Decoder]" + strXml);
        String code = getStrBetweenStrs(strXml, "CODE=\"", "\">");
        Toa toa = (Toa) Class.forName("apps.fis.domain.txn.Toa" + code).newInstance();
        return toa.getToa(strXml);
    }

    public static byte[] readBytesFromInputStream(InputStream is) throws IOException {
        if (is != null) {
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] byteBuffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
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

    protected static String getStrBetweenStrs(String fromStr, String startStr, String endStr) {
        int length = startStr.length();
        int start = fromStr.indexOf(startStr) + length;
        int end = fromStr.indexOf(endStr);
        return fromStr.substring(start, end);
    }
}
