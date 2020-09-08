package NettyChatServer.netty;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import NettyChatServer.service.MessageService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
	private MessageService messageService = new MessageService();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> data;
		
		Channel channel = ctx.channel();
		try {
			data = objectMapper.readValue(msg, new TypeReference<Map<String, Object>>() {});
		}catch (Exception e) {
			e.printStackTrace();
			messageService.returnMessage(channel, result, e, "1001");
			return;
		}
		
		messageService.execute(channel, data, result);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
