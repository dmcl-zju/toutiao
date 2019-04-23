package com.zju.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zju.mapper.CommentMapper;
import com.zju.pojo.Comment;
import com.zju.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Resource
	private CommentMapper commentMapper;
	
	@Override
	public int insComment(Comment comment) {
		return commentMapper.insComment(comment);
	}

	@Override
	public List<Comment> getCommentByEntity(int entityId, int entityType) {
		return commentMapper.selByEntity(entityId, entityType);
	}

	@Override
	public int getCommentCount(int entityId, int entityType) {
		return commentMapper.getCommentCount(entityId, entityType);
	}

}
