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
 * ��Ҫ���ܣ�
 * ���ȳ�ʼ����ʱ��ÿ���¼����ͺ���Ҫ���������hanlder���й�����һ�Զࣩ
 * ����һ���µ��̣߳����¼�������ȡ���¼������з����л��õ���Ҫ������¼�model
 * �������Ӧ��handlerȥ����
 * @author lin
 *
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {
	
	//���¼��Ͳ����������γ�һ�Զ��ӳ��
	Map<EventType,List<EventHandler>> eventMap = new HashMap<>();
	private ApplicationContext applicationContext;
	@Resource
	private JedisAdapter jedisAdapter;
	//�ڳ�ʼ��bean���Զ�����
	@Override
	public void afterPropertiesSet() throws Exception {
		//���Ȼ�ȡʵ����EventHandler�����ж���
		Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
		if(beans != null) {
			//�����ҵ�������ʵ�ֶ���
			for(Map.Entry<String, EventHandler> entry:beans.entrySet()) {
				//ȡ��ÿ��handlerҪ������¼��б�
				List<EventType> eventTypes = entry.getValue().getSupportEnventTypes();
				for(EventType eventType:eventTypes) {
					if(!eventMap.containsKey(eventType)) {
						//�������¼�����֮ǰû�д������½�һ��list
						eventMap.put(eventType, new ArrayList<EventHandler>());
					}
					//����ǰ�¼�������Ӧ���͵�ӳ����
					eventMap.get(eventType).add(entry.getValue());
				}
			}
		}
		
		//���ԣ�����һ�µ�ǰmap���
		//System.out.println("����һ�£�"+eventMap);
		for(Map.Entry<EventType,List<EventHandler>> entry:eventMap.entrySet()) {
			System.out.println(entry.getKey());
		}
		//���ԣ�����һ�µ�ǰmap���
		
		
		
		
		//���ϴ���ӳ�����
		
		//�½��̴߳�������е�����
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				//һֱѭ����ȡ
				while(true) {
					String key = RedisKeyUtil.getEVENT();
					List<String> msgs = jedisAdapter.brpop(0, key);
					//���ڸ�ʽ���⣬��ȡ�ĵ�һ�����ݱ���
					for(String msg:msgs) {
						//�����������
						if(msg.equals(key)) {
							continue;
						}
						//���з����л��õ�EventModel
						EventModel eventModel = JSON.parseObject(msg,EventModel.class);
						//System.out.println("consumer�㣺"+eventModel);
						//Ȼ�������������õ�ӳ���ϵ�������¼�,�ȿ������ʱ���ǲ����ڴ���Χ��
						if(!eventMap.containsKey(eventModel.getType())) {
							System.out.println("�޷�ʶ���¼�:"+eventModel.getType());
							continue;
						}
						//����˵���¼���ӳ�䷶Χ��
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

	//��ȡspring����
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
