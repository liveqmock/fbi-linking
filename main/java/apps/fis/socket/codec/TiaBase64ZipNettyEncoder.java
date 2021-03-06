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

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-5-28
 * Time: ����9:29
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class TiaBase64ZipNettyEncoder extends OneToOneEncoder {
    private static final Logger logger = LoggerFactory.getLogger(TiaBase64ZipNettyEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext channelHandlerContext, Channel channel, Object o) throws Exception {

        Tia tia = (Tia) o;
        logger.info("[linking TiaBase64ZipEncoder]" + tia.toString());

        // base64����
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
        return ChannelBuffers.wrappedBuffer(baos.toByteArray());
    }
}
