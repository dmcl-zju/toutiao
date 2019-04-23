package com.zju.utils;

//���ڲ���һ�������µ�redis keyֵ
public class RedisKeyUtil {
	private static String SPLIT=":";
	private static String LIKE = "LIKE";
	private static String DISLIKE = "DISLIKE";
	private static String EVENT = "EVENT";
	
	
	//�¼����е�key
	public static String getEVENT() {
		return EVENT;
	}

	//��ȡlike���ϵ�key
	public static String getLikeKey(int entityId,int entityType) {
		return LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
	}
	
	//��ȡdislike���ϵ�key
	public static String getDislikeKey(int entityId,int entityType) {
		return DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
	}

}
