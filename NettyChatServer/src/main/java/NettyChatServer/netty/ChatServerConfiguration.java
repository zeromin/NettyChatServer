package NettyChatServer.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class ChatServerConfiguration {
	private int PORT = Integer.parseInt(System.getProperty("port", "24424"));
	private int countBoss = 1;
	private int countWorker = 1;
	
	private String LogLevel = "INFO";
	
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(countBoss);
	}
	
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(countWorker);
	}
	
	public InetSocketAddress port() {
		return new InetSocketAddress(PORT);
	}
	
	public ServerBootstrap serverBootstrap() {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap
				.group(bossGroup(), workerGroup())
				.handler(new LoggingHandler(LogLevel.valueOf(LogLevel)))
				.childHandler(new ChatServerInitializer());

		serverBootstrap.channel(NioServerSocketChannel.class);
		return serverBootstrap;
	}
	
	public ChannelInboundHandlerAdapter handler() {
		return new ChatServerHandler();
	}
}
