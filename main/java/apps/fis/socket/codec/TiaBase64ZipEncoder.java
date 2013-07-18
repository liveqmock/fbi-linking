package apps.fis.socket.codec;

import apps.fis.domain.base.Tia;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-5-28
 * Time: ÏÂÎç9:29
 * To change this template use File | Settings | File Templates.
 */
public class TiaBase64ZipEncoder {
    private static final Logger logger = LoggerFactory.getLogger(TiaBase64ZipEncoder.class);

    public static byte[] encode(Tia tia) throws IOException {

        logger.info("[linking TiaBase64ZipEncoder] " + tia.toString());
        // base64±àÂë
        byte[] bytes = B64Code.encode(tia.toString()).getBytes();
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
        return baos.toByteArray();
    }
}
