package NettyChatServer;

import NettyChatServer.netty.ChatServer;

public class App 
{
	public static void main( String[] args )
    {
		ChatServer chatServer = new ChatServer();
		chatServer.start();
    }
}
