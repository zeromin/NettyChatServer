package NettyChatServer.service;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

import io.netty.channel.Channel;

public class MessageService {
	private LoginService loginService = new LoginService(this);
	private SendService sendService = new SendService();
	private RoomService roomService = new RoomService(this, loginService);
	
	public void execute(Channel channel, Map<String, Object> data, Map<String, Object> result) throws Exception{
		String method = (String) data.getOrDefault("method", "");
		
		switch (method) {
		case "login":
			loginService.login(channel, method, data, result);
			break;
		case "send":
			sendService.send(channel, method, data, result);
			break;
		case "create_room":
			roomService.create(channel, method, result);
			break;
		case "enter_room":
			roomService.enter(channel, method, data, result);
			break;
		case "exit_room":
			roomService.exit(channel, method, result);
			break;
		case "send_room":
			roomService.send(channel, method, data, result);
			break;
		default:
			break;
		}
	}
	
	public void returnMessage(Channel channel, Map<String, Object> result, Throwable throwable, String status) throws Exception{
		result.put("status", status);
		result.put("message", ExceptionUtils.getStackTrace(throwable));
		
		channel.writeAndFlush(returnMessage(result));
	}
	
	void returnMessage(Channel channel, Map<String, Object> result, String method) {
		result.put("status", "0");
		result.put("method", method);
		
		try {
			channel.writeAndFlush(returnMessage(result));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Object returnMessage(Map<String, Object> result) throws Exception{
		return result + System.lineSeparator();
	}
	
	public Object returnMessage(String message) {
		return message + System.lineSeparator();
	}
}
