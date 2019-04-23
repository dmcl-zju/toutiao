package com.zju.service;

import java.util.List;

import com.zju.pojo.Comment;

public interface CommentService {
	
	//增加评论
		int insComment(Comment comment);
	
	//获取对应信息的所有评论
	List<Comment> getCommentByEntity(int entityId,int entityType);
	
	//获取对应信息的评论数
	int getCommentCount(int entityId,int entityType);
	
}
