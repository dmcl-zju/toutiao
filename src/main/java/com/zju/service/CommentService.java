package com.zju.service;

import java.util.List;

import com.zju.pojo.Comment;

public interface CommentService {
	
	//��������
		int insComment(Comment comment);
	
	//��ȡ��Ӧ��Ϣ����������
	List<Comment> getCommentByEntity(int entityId,int entityType);
	
	//��ȡ��Ӧ��Ϣ��������
	int getCommentCount(int entityId,int entityType);
	
}
