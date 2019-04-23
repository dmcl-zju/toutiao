package com.zju.service;

public interface LikeService {
	//获取喜欢状态
	public int getLikeStatus(int userId,int entityId,int entityType);
	//添加到喜欢集合
	public long like(int userId,int entityId,int entityType);
	//添加到不喜欢集合
	public long dislike(int userId,int entityId,int entityType);
}
