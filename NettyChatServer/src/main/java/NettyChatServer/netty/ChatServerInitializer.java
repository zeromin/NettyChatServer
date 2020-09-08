package NettyChatServer.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

public class ChatServerInitializer extends ChannelInitializer<Channel> {
	private static final StringDecoder STRING_DECODER = new StringDecoder(CharsetUtil.UTF_8);
	private static final StringEncoder STRING_ENCODER = new StringEncoder(CharsetUtil.UTF_8);
	
	private String logLevelPipeLine = "INFO";
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline channelPipeline = ch.pipeline();
		
		channelPipeline
						.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE))
						.addLast(STRING_DECODER)
						.addLast(STRING_ENCODER)
						.addLast(new LoggingHandler(LogLevel.valueOf(logLevelPipeLine)))
						.addLast(new ChatServerHandler());
		
	}

}
