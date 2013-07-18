package apps.fis.socket.client;

import apps.fis.SystemParameter;
import apps.fis.domain.base.Tia;
import apps.fis.domain.base.Toa;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 发起客户端
 */
@Deprecated
public class NettyClientSponsor {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientSponsor.class);

    private String serverIP = SystemParameter.SERVER_SOCKET_IP;
    private int port = SystemParameter.SERVER_SOCKET_PORT;


    public Toa request(Tia tia) {
        ChannelFactory factory = new OioClientSocketChannelFactory(Executors.newSingleThreadExecutor());
        ClientBootstrap bootstrap = new ClientBootstrap(factory);
        bootstrap.setPipelineFactory(new ClientPipelineFactory());
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(serverIP, port));
        ClientChannelHandler handler = future.getChannel().getPipeline().get(ClientChannelHandler.class);
        future.getChannel().write(tia);
//        future.getChannel().getCloseFuture().syncUninterruptibly();
       future.getChannel().getCloseFuture().awaitUninterruptibly();
        future.getChannel().close();
        factory.releaseExternalResources();
        return handler.getRtnToa();
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
