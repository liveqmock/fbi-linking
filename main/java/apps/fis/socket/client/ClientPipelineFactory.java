package apps.fis.socket.client;

import apps.fis.socket.codec.TiaBase64ZipNettyEncoder;
import apps.fis.socket.codec.ToaUnzipBase64NettyDecoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * 客户端设置
 */
@Deprecated
public class ClientPipelineFactory implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        // 编码器
        pipeline.addLast("tiaEncoder", new TiaBase64ZipNettyEncoder());

        // 解码器
        pipeline.addLast("toaDecoder", new ToaUnzipBase64NettyDecoder());

        pipeline.addLast("clientChannelHandler", new ClientChannelHandler());
        return pipeline;
    }
}
