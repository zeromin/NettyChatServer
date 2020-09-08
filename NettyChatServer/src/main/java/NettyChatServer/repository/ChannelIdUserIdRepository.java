package NettyChatServer.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelId;

public class ChannelIdUserIdRepository {
	private final Map<ChannelId, String> channelIdUserIdMap = new ConcurrentHashMap<>();

	private ChannelIdUserIdRepository() {}
	
	public static ChannelIdUserIdRepository getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private static class LazyHolder{
		private static final ChannelIdUserIdRepository INSTANCE = new ChannelIdUserIdRepository();
	}
	
	public Map<ChannelId, String> getChannelIdUserIdMap() {
		return channelIdUserIdMap;
	}
}
