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

	//获取喜欢状态，1喜欢，-1不喜欢，0无状态
	@Override
	public int getLikeStatus(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
		//如果userId在喜欢的集合中
		if(jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
			return 1;
		}
		//不再喜欢当中
		return jedisAdapter.sismember(dislikeKey, String.valueOf(userId))?-1:0;
	}

	//将userId加入该信息对应的喜欢集合中，同时相应的从不喜欢的集合中移除（如果有的话）
	//最终返回喜欢集合的元素个数
	@Override
	public long like(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
		//加入喜欢集合
		jedisAdapter.sadd(likeKey, String.valueOf(userId));
		//移出不喜欢集合
		jedisAdapter.srem(dislikeKey, String.valueOf(userId));
		return jedisAdapter.scard(likeKey);
		
	}

	@Override
	public long dislike(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
		//加入不喜欢集合
		jedisAdapter.sadd(dislikeKey, String.valueOf(userId));
		//移出喜欢集合
		jedisAdapter.srem(likeKey, String.valueOf(userId));
		return jedisAdapter.scard(likeKey);
	}
	
	
}
