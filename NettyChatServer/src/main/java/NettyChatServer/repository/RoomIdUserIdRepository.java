package NettyChatServer.repository;

import org.apache.commons.collections.map.MultiValueMap;


public class RoomIdUserIdRepository {
	private final MultiValueMap roomIdUserIdMap = new MultiValueMap();

	private RoomIdUserIdRepository() {}
	
	public static RoomIdUserIdRepository getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private static class LazyHolder{
		private static final RoomIdUserIdRepository INSTANCE = new RoomIdUserIdRepository();
	}
	
	public MultiValueMap getRoomIdUserIdMap() {
		return roomIdUserIdMap;
	}
}
