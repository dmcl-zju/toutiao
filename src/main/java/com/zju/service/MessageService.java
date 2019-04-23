package com.zju.service;

import java.util.List;

import com.zju.pojo.Message;

public interface MessageService {
	
	//������Ϣ
	public int insMessage(Message message);
	//��ȡ�Ự����
	public List<Message> getConversationDetail(String conversationId,int offset,int limit);
	//��ȡ�Ự�б�
	public List<Message> getConversationList(int userId,int offset,int limit);
	//��ȡָ���Ựδ����Ϣ
	public int getConversationUnreadCount(int userId,String conversationId);
}
