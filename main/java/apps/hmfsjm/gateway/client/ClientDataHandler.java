package apps.hmfsjm.gateway.client;

import apps.hmfsjm.gateway.domain.base.Toa;
import common.utils.MD5Helper;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端报文解析
 */
public class ClientDataHandler extends SimpleChannelUpstreamHandler {
    private static Logger logger = LoggerFactory.getLogger(ClientDataHandler.class);
    private Toa toa;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent me) {
        try {
            byte[] bytes = (byte[]) me.getMessage();
            logger.info("[客户端接收]" + new String(bytes));

            String wsysid = new String(bytes, 4, 10).trim();
            String txnCode = new String(bytes, 14, 10).trim();
            String txnDate = new String(bytes, 24, 8).trim();
            String mac = new String(bytes, 32, 32);
            String msgdata = new String(bytes, 64, bytes.length - 64);
            String md5 = MD5Helper.getMD5String(msgdata + txnDate + wsysid);
            logger.info("交易日期：" + txnDate + " 系统ID:" + wsysid);
            if (!md5.equalsIgnoreCase(mac)) {
                logger.error("校验失败,MAC不一致.本地生成md5:" + md5 + " 服务端mac:" + mac);
                throw new RuntimeException("报文校验失败,MAC不一致");
            }
            Class clazz = Class.forName("apps.hmfsjm.gateway.domain.txn.Toa" + txnCode);
            Toa tmptoa = (Toa) clazz.newInstance();
            toa = tmptoa.toBean(msgdata);
        } catch (Exception e) {
            logger.error("报文解析异常", e);
            throw new RuntimeException("报文解析异常", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("客户端连接异常。", e.getCause());
        e.getChannel().close();
    }

    public Toa getToa() {
        return toa;
    }

    public void setToa(Toa toa) {
        this.toa = toa;
    }
}
