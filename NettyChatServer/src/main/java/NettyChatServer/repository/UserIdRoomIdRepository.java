package NettyChatServer.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserIdRoomIdRepository {
	private final Map<String, String> userIdRoomIdMap = new ConcurrentHashMap<>();

	private UserIdRoomIdRepository() {}
	
	public static UserIdRoomIdRepository getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private static class LazyHolder{
		private static final UserIdRoomIdRepository INSTANCE = new UserIdRoomIdRepository();
	}
	
	public Map<String, String> getUserIdRoomIdMap() {
		return userIdRoomIdMap;
	}

}
