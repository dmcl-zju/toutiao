package com.zju.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zju.utils.JedisAdapter;
import com.zju.utils.RedisKeyUtil;

/**
 * 主要功能：
 * 首先初始化的时候，每个事件类型和需要对其操作的hanlder进行关联（一对多）
 * 开辟一个新的线程，从事件队列中取出事件，进行反序列化得到需要处理的事件model
 * 分配给相应的handler去处理
 * @author lin
 *
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {
	
	//将事件和操作关联，形成一对多的映射
	Map<EventType,List<EventHandler>> eventMap = new HashMap<>();
	private ApplicationContext applicationContext;
	@Resource
	private JedisAdapter jedisAdapter;
	//在初始化bean后自动调用
	@Override
	public void afterPropertiesSet() throws Exception {
		//首先获取实现了EventHandler的所有对象
		Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
		if(beans != null) {
			//遍历找到的所有实现对象
			for(Map.Entry<String, EventHandler> entry:beans.entrySet()) {
				//取出每个handler要处理的事件列表
				List<EventType> eventTypes = entry.getValue().getSupportEnventTypes();
				for(EventType eventType:eventTypes) {
					if(!eventMap.containsKey(eventType)) {
						//如果这个事件类型之前没有创建，新建一个list
						eventMap.put(eventType, new ArrayList<EventHandler>());
					}
					//将当前事件加入相应类型的映射中
					eventMap.get(eventType).add(entry.getValue());
				}
			}
		}
		
		//测试：遍历一下当前map情况
		//System.out.println("测试一下："+eventMap);
		for(Map.Entry<EventType,List<EventHandler>> entry:eventMap.entrySet()) {
			System.out.println(entry.getKey());
		}
		//测试：遍历一下当前map情况
		
		
		
		
		//以上创建映射完毕
		
		//新建线程处理队列中的内容
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				//一直循环读取
				while(true) {
					String key = RedisKeyUtil.getEVENT();
					List<String> msgs = jedisAdapter.brpop(0, key);
					//由于格式问题，读取的第一个内容表名
					for(String msg:msgs) {
						//如果读到表名
						if(msg.equals(key)) {
							continue;
						}
						//进行反序列化得到EventModel
						EventModel eventModel = JSON.parseObject(msg,EventModel.class);
						//System.out.println("consumer层："+eventModel);
						//然后利用上面做好的映射关系来处理事件,先看下这个时间是不是在处理范围内
						if(!eventMap.containsKey(eventModel.getType())) {
							System.out.println("无法识别事件:"+eventModel.getType());
							continue;
						}
						//到这说明事件在映射范围内
						List<EventHandler> handlers = eventMap.get(eventModel.getType());
						for(EventHandler handler:handlers) {
							handler.doHandler(eventModel);
						}
					}
				}
				
			}
			
		});
		thread.start();
		
	}

	//获取spring容器
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
