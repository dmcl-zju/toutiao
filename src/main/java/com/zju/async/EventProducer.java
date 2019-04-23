package com.zju.async;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zju.utils.JedisAdapter;
import com.zju.utils.RedisKeyUtil;

//传来的对象序列化，并加入redis的事件队列中
@Service
public class EventProducer {
	
	@Resource
	private JedisAdapter jedisAdapter;
	
	public boolean fireEvent(EventModel model) {
		try {
			//将对象序列化成json串
			String json = JSONObject.toJSONString(model);
			String key = RedisKeyUtil.getEVENT();
			jedisAdapter.lpush(key, json);
			return true;
		} catch (Exception e) {
			//加入日志记录
			return false;
		}
		
	}
}
