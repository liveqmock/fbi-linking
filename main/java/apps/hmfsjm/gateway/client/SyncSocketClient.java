package apps.hmfsjm.gateway.client;

import apps.hmfsjm.PropertyManager;
import apps.hmfsjm.gateway.codec.XmlMessageDecoder;
import apps.hmfsjm.gateway.codec.XmlMessageEncoder;
import apps.hmfsjm.gateway.domain.base.Tia;
import apps.hmfsjm.gateway.domain.base.Toa;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 同步Socket客户端
 */
@Component
@Scope("prototype")
public class SyncSocketClient {
    private static final String SERVER_IP = PropertyManager.getProperty("socket.ip");
    private static final int SERVER_PORT = PropertyManager.getIntProperty("socket.port");
    private static final String SERVER_TIMEOUT = PropertyManager.getProperty("socket.timeout");

    public Toa onRequest(Tia tia) throws Exception {
        ClientBootstrap bootstrap = new ClientBootstrap(
                new OioClientSocketChannelFactory(Executors.newSingleThreadExecutor()));
        final ClientDataHandler clientDataHandler = new ClientDataHandler();
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new XmlMessageDecoder());
                pipeline.addLast("encoder", new XmlMessageEncoder());
                pipeline.addLast("handler", clientDataHandler);
                return pipeline;
            }
        });
        bootstrap.setOption("socket.timeout", SERVER_TIMEOUT);
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
        future.getChannel().write(tia);
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        return clientDataHandler.getToa();
    }
}
