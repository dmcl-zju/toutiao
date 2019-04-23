package com.zju.utils;

//用于产生一定规则下的redis key值
public class RedisKeyUtil {
	private static String SPLIT=":";
	private static String LIKE = "LIKE";
	private static String DISLIKE = "DISLIKE";
	private static String EVENT = "EVENT";
	
	
	//事件队列的key
	public static String getEVENT() {
		return EVENT;
	}

	//获取like集合的key
	public static String getLikeKey(int entityId,int entityType) {
		return LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
	}
	
	//获取dislike集合的key
	public static String getDislikeKey(int entityId,int entityType) {
		return DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
	}

}
