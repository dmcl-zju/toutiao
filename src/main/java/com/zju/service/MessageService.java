package com.zju.service;

import java.util.List;

import com.zju.pojo.Message;

public interface MessageService {
	
	//新增消息
	public int insMessage(Message message);
	//读取会话内容
	public List<Message> getConversationDetail(String conversationId,int offset,int limit);
	//读取会话列表
	public List<Message> getConversationList(int userId,int offset,int limit);
	//读取指定会话未读信息
	public int getConversationUnreadCount(int userId,String conversationId);
}
