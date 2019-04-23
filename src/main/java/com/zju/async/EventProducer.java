package com.zju.async;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zju.utils.JedisAdapter;
import com.zju.utils.RedisKeyUtil;

//�����Ķ������л���������redis���¼�������
@Service
public class EventProducer {
	
	@Resource
	private JedisAdapter jedisAdapter;
	
	public boolean fireEvent(EventModel model) {
		try {
			//���������л���json��
			String json = JSONObject.toJSONString(model);
			String key = RedisKeyUtil.getEVENT();
			jedisAdapter.lpush(key, json);
			return true;
		} catch (Exception e) {
			//������־��¼
			return false;
		}
		
	}
}
