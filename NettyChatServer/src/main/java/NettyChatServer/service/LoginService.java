package NettyChatServer.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import NettyChatServer.repository.ChannelIdUserIdRepository;
import NettyChatServer.repository.RoomIdUserIdRepository;
import NettyChatServer.repository.UserIdChannelRepository;
import NettyChatServer.repository.UserIdRoomIdRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

public class LoginService {
	/*
	private UserRepository userRepository;
	private ChannelIdUserIdRepository channelIdUserIdRepository;
	private UserIdChannelRepository userIdChannelRepository;
	private UserIdRoomIdRepository userIdRoomIdRepository;
	private RoomIdUserIdRepository roomIdUserIdRepository;
	*/
	
	private MessageService messageService;
	
	public LoginService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	public void login(Channel channel,
	                  String method,
	                  Map<String, Object> data,
	                  Map<String, Object> result) throws Exception {

		// �궗�슜�옄 �씤利� 泥섎━
		String userId = (String) data.get("userId");
		String password = (String) data.get("password");

		if (userId == null || password == null) {

			messageService.returnMessage(channel, result, new Exception("�궗�슜�옄 �븘�씠�뵒 �샊�� 鍮꾨�踰덊샇媛� 鍮꾩뼱�엳�뒿�땲�떎."), "1002");
			return;
		}
		
		/*
		User user = userRepository.findOne(userId);
		if (user == null) {
			messageService.returnMessage(channel, result, new Exception("�궗�슜�옄 �븘�씠�뵒媛� 議댁옱�븯吏� �븡�뒿�땲�떎."), "1003");
			return;
		} else if (!password.equals(user.getPassword())) {
			messageService.returnMessage(channel, result, new Exception("鍮꾨�踰덊샇媛� �씪移섑븯吏� �븡�뒿�땲�떎."), "1004");
			return;
		}
		*/
		
		// �궗�슜�옄 �젙蹂� �엯�젰
		ChannelIdUserIdRepository.getInstance().getChannelIdUserIdMap().put(channel.id(), userId);
		UserIdChannelRepository.getInstance().getUserIdChannelMap().put(userId, channel);

		messageService.returnMessage(channel, result, method);
	}
	
	public void removeUser(Channel channel) {
		ChannelId channelId = channel.id();
		Map<ChannelId, String> channelIdUserIdMap = ChannelIdUserIdRepository.getInstance().getChannelIdUserIdMap();
		String userId = channelIdUserIdMap.get(channelId);
		// �궗�슜�옄 �젙蹂� �젣嫄�
		if (!StringUtils.isEmpty(userId)) {
			UserIdChannelRepository.getInstance().getUserIdChannelMap().remove(userId);
			String roomId = UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().get(userId);
			// 猷� �젙蹂� �젣嫄�
			if (!StringUtils.isEmpty(roomId)) {
				RoomIdUserIdRepository.getInstance().getRoomIdUserIdMap().remove(roomId, userId);
				UserIdRoomIdRepository.getInstance().getUserIdRoomIdMap().remove(userId);
			}
			channelIdUserIdMap.remove(channelId);
		}
	}
}
