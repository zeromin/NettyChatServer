package NettyChatServer.domain.Repository;

public class UserRepository {

	private UserRepository() {}
	
	public static UserRepository getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private static class LazyHolder{
		private static final UserRepository INSTANCE = new UserRepository();
	}
}