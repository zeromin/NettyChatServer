package NettyChatServer.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

public class ChatServer {
	ChatServerConfiguration configuration;
	ServerBootstrap serverBootstrap;
	Channel channel;
	
	public ChatServer(){
		configuration = new ChatServerConfiguration();
	}
	
	public void start() {
		try {
			serverBootstrap = configuration.serverBootstrap();
			channel = serverBootstrap.bind(configuration.port()).sync().channel().closeFuture().sync().channel();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}


