package apps.fiskfq.gateway.codec;

import apps.hmfsjm.PropertyManager;
import apps.hmfsjm.gateway.domain.base.Tia;
import common.utils.MD5Helper;
import common.utils.StringPad;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 编码
 */
public class XmlMessageEncoder extends OneToOneEncoder {
    private static Logger logger = LoggerFactory.getLogger(XmlMessageEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext channelHandlerContext, Channel channel, Object message) throws Exception {

        Tia tia = (Tia) message;
        String msgdata = tia.toString();
        String wsysid = PropertyManager.getProperty("socket.wsysid");
        String txnDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String txnCode = getSubstrBetweenStrs(msgdata, "<TXN_CODE>", "</TXN_CODE>");
        String mac = MD5Helper.getMD5String(msgdata + txnDate + wsysid);

        String msg = "1.00" + StringPad.rightPad4ChineseToByteLength(wsysid, 10, " ")
                + StringPad.rightPad4ChineseToByteLength(txnCode, 10, " ")
                + txnDate + mac + msgdata;
        int msglength = msg.getBytes().length + 8;
        String len = StringPad.rightPad4ChineseToByteLength("" + msglength, 8, " ");

        byte[] bytes = new byte[msglength];
        System.arraycopy(len.getBytes(), 0, bytes, 0, 8);
        System.arraycopy(msg.getBytes(), 0, bytes, 8, msglength - 8);
        logger.info("[本地客户端]已发送报文长度：" + msglength);
        logger.info("[本地客户端]发送报文：" + msg);
        return ChannelBuffers.wrappedBuffer(bytes);
    }

    private String getSubstrBetweenStrs(String fromStr, String startStr, String endStr) {
        int length = startStr.length();
        int start = fromStr.indexOf(startStr) + length;
        int end = fromStr.indexOf(endStr);
        return fromStr.substring(start, end);
    }
}
