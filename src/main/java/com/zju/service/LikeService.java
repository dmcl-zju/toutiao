package com.zju.service;

public interface LikeService {
	//��ȡϲ��״̬
	public int getLikeStatus(int userId,int entityId,int entityType);
	//��ӵ�ϲ������
	public long like(int userId,int entityId,int entityType);
	//��ӵ���ϲ������
	public long dislike(int userId,int entityId,int entityType);
}
