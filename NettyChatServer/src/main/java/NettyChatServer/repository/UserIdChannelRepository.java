package NettyChatServer.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import NettyChatServer.service.LoginService;
import NettyChatServer.service.MessageService;
import io.netty.channel.Channel;

public class UserIdChannelRepository {
	private LoginService loginService;
	private MessageService messageService;

	private final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

	private UserIdChannelRepository() {}
	
	public static UserIdChannelRepository getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private static class LazyHolder{
		private static final UserIdChannelRepository INSTANCE = new UserIdChannelRepository();
	}
	
	public Map<String, Channel> getUserIdChannelMap() {
		return userIdChannelMap;
	}
	
	public void writeAndFlush(String returnMessage) throws Exception {
		userIdChannelMap.values().parallelStream().forEach(channel -> {
			if (!channel.isActive()) {
				loginService.removeUser(channel);
				channel.close();
				return;
			}
			channel.writeAndFlush(messageService.returnMessage(returnMessage));
		});
	}
}
