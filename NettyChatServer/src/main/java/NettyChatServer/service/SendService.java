package NettyChatServer.service;

import java.util.Map;

import NettyChatServer.repository.ChannelIdUserIdRepository;
import NettyChatServer.repository.UserIdChannelRepository;
import io.netty.channel.Channel;

public class SendService {
	/*
	private ChannelIdUserIdRepository channelIdUserIdRepository;
	private UserIdChannelRepository userIdChannelRepository;
	private UserRepository userRepository;
	*/
	
	public void send(Channel channel, String method, Map<String, Object> data, Map<String, Object> result) throws Exception{
		String userId = ChannelIdUserIdRepository.getInstance().getChannelIdUserIdMap().get(channel.id());
		
		result.put("method", method);
		result.put("userId", userId);
		//�씠由� 
		result.put("content", data.get("content"));
		
		String resultMessage = result.toString();
		
		UserIdChannelRepository.getInstance().writeAndFlush(resultMessage);
	}
}
