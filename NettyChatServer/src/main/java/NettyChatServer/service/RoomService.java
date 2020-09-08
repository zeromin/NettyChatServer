package NettyChatServer.service;

import java.util.Map;
import java.util.UUID;

import javax.print.attribute.standard.MediaSize.Other;

import NettyChatServer.domain.User;
import NettyChatServer.repository.ChannelIdUserIdRepository;
import NettyChatServer.repository.RoomIdUserIdRepository;
import NettyChatServer.repository.UserIdChannelRepository;
import NettyChatServer.repository.UserIdRoomIdRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

public class RoomService {	
	private MessageService messageService;
	private LoginService loginService;
	
	public RoomService(MessageService messageService, LoginService loginService) {
		this.messageService = messageService;
		this.loginService = loginService;
	}
	
	public void create(Channel channel, String method, Map<String, Object> result) throws Exception{
		ChannelId id = channel.id();
		String userId = ChannelIdUserIdRepository.getInstance().getChannelIdUserIdMap().get(id);

		if(UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().containsKey(id)){
			messageService.returnMessage(channel, result, new Exception("猷몄뿉 �엯�옣�빐�엳�뒗 �궗�슜�옄�엯�땲�떎."), "1006");
			return;
		}
		
		String roomId = UUID.randomUUID().toString();
		
		System.out.println("userId => " + userId);
		System.out.println("userIdRoomIdRepository => " + UserIdRoomIdRepository.getInstance());
		
		RoomIdUserIdRepository.getInstance().getRoomIdUserIdMap().put(roomId, userId);
		UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().put(userId, roomId);
		
		result.put("method", method);
		result.put("roomId", roomId);
		
		messageService.returnMessage(channel, result, method);
	}
	
	public void enter(Channel channel, String method, Map<String, Object> data, Map<String, Object> result) throws Exception{
		String userId = ChannelIdUserIdRepository.getInstance().getChannelIdUserIdMap().get(channel.id());
		
		if(UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().containsKey(userId)) {
			messageService.returnMessage(channel, result, new Exception("猷몄뿉 �엯�옣�빐�엳�뒗 �궗�슜�옄�엯�땲�떎."), "1006");
			return;
		}
		
		String roomId = (String) data.get("roomId");
		
		if(!RoomIdUserIdRepository.getInstance().getRoomIdUserIdMap().containsKey(roomId)) {
			messageService.returnMessage(channel, result, new Exception("議댁옱�븯吏� �븡�뒗 猷� �븘�씠�뵒�엯�땲�떎."), "1007");
			return;
		}
		
		RoomIdUserIdRepository.getInstance().getRoomIdUserIdMap().put(roomId, userId);
		UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().put(userId, roomId);
		
		result.put("method", method);
		result.put("roomId", roomId);
		
		messageService.returnMessage(channel, result, method);
	}
	
	public void exit(Channel channel, String method, Map<String, Object> result) throws Exception{
		String userId = ChannelIdUserIdRepository.getInstance().getChannelIdUserIdMap().get(channel.id());
		
		if(!UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().containsKey(userId)) {
			messageService.returnMessage(channel, result, new Exception("猷몄뿉 議댁옱�븯吏� �븡�뒿�땲�떎."), "1008");
			return;
		}
		
		String roomId = UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().get(userId);
		
		RoomIdUserIdRepository.getInstance().getRoomIdUserIdMap().remove(roomId, userId);
		UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().remove(userId);
		
		messageService.returnMessage(channel, result, method);
	}
	
	public void send(Channel channel, String method, Map<String, Object> data, Map<String, Object> result) throws Exception{
		String userId = ChannelIdUserIdRepository.getInstance().getChannelIdUserIdMap().get(channel.id());
		
		if(!UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().containsKey(userId)) {
			messageService.returnMessage(channel, result, new Exception("猷몄뿉 議댁옱�븯吏� �븡�뒿�땲�떎."), "1008");
			return;
		}
		
		User user = null;
		if(user == null) {
			messageService.returnMessage(channel, result, new Exception("�궗�슜�옄 �젙蹂대�� 議고쉶�븷 �닔 �뾾�뒿�땲�떎."), "1009");
		}
		
		String userName = user.getUserName();
		
		result.put("method", method);
		result.put("userId", userId);
		result.put("userName", userName);
		result.put("content", data.get("content"));
		
		String roomId = UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().get(userId);
		
		RoomIdUserIdRepository.getInstance().getRoomIdUserIdMap().getCollection(roomId).parallelStream().forEach(otherUserId -> {
			// 梨꾨꼸 媛��졇�삤湲�
			Channel otherChannel = UserIdChannelRepository.getInstance().getUserIdChannelMap().get(otherUserId);
			// 梨꾨꼸�씠 �솢�꽦�솕 �긽�깭媛� �븘�땲�씪硫� �궗�슜�옄瑜� �젣嫄�
			if (!otherChannel.isActive()) {

				loginService.removeUser(otherChannel);
				return;
			}
			messageService.returnMessage(otherChannel, result, method);
		});
	}
	
}
