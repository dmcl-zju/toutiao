package com.zju.utils;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class JedisAdapter implements InitializingBean{
	
	Logger logger =Logger.getLogger(JedisAdapter.class);
	
	//private Jedis jedis;
	private JedisPool pool;
	
	//当bean生成的时候会进入这个函数，初始化pool
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		pool = new JedisPool("localhost",6379);
		
	}
	
//-----------将一些常用的函数包装起来-----------------------
	//字符串添加和获取
	public void set(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error("发生异常："+e.getMessage());
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.get(key);
		} catch (Exception e) {
			logger.error("发生异常："+e.getMessage());
			return null;
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	//集合添加、删除、长度、是否包含
	//集合增加
	public long sadd(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sadd(key, value);
		} catch (Exception e) {
			logger.error("发生异常："+e.getMessage());
			return 0;
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	//集合删除
	public long srem(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.srem(key, value);
		} catch (Exception e) {
			logger.error("发生异常："+e.getMessage());
			return 0;
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	//判断是否包含
	public boolean sismember(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sismember(key, value);
		} catch (Exception e) {
			logger.error("发生异常："+e.getMessage());
			return false;
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	//集合长度
	public long scard(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.scard(key);
		} catch (Exception e) {
			logger.error("发生异常："+e.getMessage());
			return 0;
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	
	//redis中的list--返回操作过后list长度
	public long lpush(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("发生异常："+e.getMessage());
			return 0;
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	//阻塞式弹出，从尾部弹出
	public List<String> brpop(int timeout,String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.brpop(timeout, key);
		} catch (Exception e) {
			logger.error("发生异常："+e.getMessage());
			return null;
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	

//------------------------------------以下函数为测试用例------------------------------------------	
	//显示输出函数
	public static void print(int index,Object obj) {
		System.out.println(index+": "+obj.toString());
	}
	
	//测试使用redis
	public static void main(String[] args) throws InterruptedException {
		
//		Jedis jedis = new Jedis();
//		jedis.lpush("list1", "a");
//		jedis.lpush("list1", "b");
//		jedis.lpush("list1", "c");
//		jedis.lpush("list1", "d");
//		
//		jedis.lpush("list2", "e");
//		jedis.lpush("list2", "f");
//		jedis.lpush("list2", "g");
//		jedis.lpush("list2", "h");
		
//		System.out.println(jedis.llen("list1")+":"+jedis.llen("list2"));
//		List<String> brpop = jedis.brpop(0,"list1","list2");
//		for(String s:brpop) {
//			System.out.print(s+"----");
//		}
		//System.out.println(jedis.rpop("list1"));
		
		
		
		
		/*StringBuffer sb = new StringBuffer();
		sb.append("java");
		System.out.println(sb);
		sb.setCharAt(0, 'h');
		sb.setCharAt(2, 'h');
		System.out.println(sb);*/
		
		/*
		Jedis jedis = new Jedis();
		jedis.set("hh", "lala");
		print(1,jedis.get("hh"));
		jedis.rename("hh", "haha");
		System.out.println(jedis.keys("*"));
		print(2,jedis.get("haha"));+
//		jedis.psetex("hello", 1000, "world");
//		System.out.println(jedis.keys("*"));
//		//延时
//		Thread.currentThread();
//		Thread.sleep(1001);
//		System.out.println(jedis.keys("*"));
		
		// hash, 可变字段
		String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "18666666666");
        print(12, jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
		
        // 集合，点赞用户群, 共同好友
        String likeKey1 = "newsLike1";
        String likeKey2 = "newsLike2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * 2));
        }
        print(20, jedis.smembers(likeKey1));
        print(21, jedis.smembers(likeKey2));
		
        // 排序集合，有限队列，排行榜
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "Jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 75, "Lucy");
        jedis.zadd(rankKey, 80, "Mei");
        print(30, jedis.zcard(rankKey));
        print(31, jedis.zcount(rankKey, 61, 100));
        // 改错卷了
        print(32, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Lucy");
        print(33, jedis.zscore(rankKey, "Lucy"));*/
	}

}
