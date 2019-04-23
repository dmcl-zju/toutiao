package com.zju.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zju.service.LikeService;
import com.zju.utils.JedisAdapter;
import com.zju.utils.RedisKeyUtil;

@Service
public class LikeServiceImpl implements LikeService {
	
	@Resource
	private JedisAdapter jedisAdapter;

	//��ȡϲ��״̬��1ϲ����-1��ϲ����0��״̬
	@Override
	public int getLikeStatus(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
		//���userId��ϲ���ļ�����
		if(jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
			return 1;
		}
		//����ϲ������
		return jedisAdapter.sismember(dislikeKey, String.valueOf(userId))?-1:0;
	}

	//��userId�������Ϣ��Ӧ��ϲ�������У�ͬʱ��Ӧ�ĴӲ�ϲ���ļ������Ƴ�������еĻ���
	//���շ���ϲ�����ϵ�Ԫ�ظ���
	@Override
	public long like(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
		//����ϲ������
		jedisAdapter.sadd(likeKey, String.valueOf(userId));
		//�Ƴ���ϲ������
		jedisAdapter.srem(dislikeKey, String.valueOf(userId));
		return jedisAdapter.scard(likeKey);
		
	}

	@Override
	public long dislike(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
		//���벻ϲ������
		jedisAdapter.sadd(dislikeKey, String.valueOf(userId));
		//�Ƴ�ϲ������
		jedisAdapter.srem(likeKey, String.valueOf(userId));
		return jedisAdapter.scard(likeKey);
	}
	
	
}
