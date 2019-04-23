package com.zju.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zju.mapper.MessageMapper;
import com.zju.pojo.Message;
import com.zju.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Resource
	private MessageMapper messageMapper;
	@Override
	//新增信息
	public int insMessage(Message message) {
		return messageMapper.insMessage(message);
	}
	@Override
	public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
		return messageMapper.getConversationDetail(conversationId, offset, limit);
	}
	@Override
	public List<Message> getConversationList(int userId, int offset, int limit) {
		return messageMapper.getConversationList(userId, offset, limit);
	}
	@Override
	public int getConversationUnreadCount(int userId, String conversationId) {
		return messageMapper.getConversationUnreadCount(userId, conversationId);
	}

}
