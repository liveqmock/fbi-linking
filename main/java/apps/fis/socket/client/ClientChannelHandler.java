package apps.fis.socket.client;

import apps.fis.domain.base.Tia;
import apps.fis.domain.base.Toa;
import org.jboss.netty.channel.*;

/**
 * 客户端数据处理辅助类
 */
@Deprecated
public class ClientChannelHandler extends SimpleChannelUpstreamHandler {

    private Toa rtnToa = null;
//    public static final ChannelLocal<Toa> data = new ChannelLocal<Toa>();

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        // 得到外联系统的响应TOA
        Toa toa = (Toa)e.getMessage();
        rtnToa = toa;
        e.getChannel().close();
    }

    public Toa getRtnToa() {
        return rtnToa;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
