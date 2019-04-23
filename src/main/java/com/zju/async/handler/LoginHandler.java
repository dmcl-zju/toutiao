package com.zju.async.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zju.async.EventHandler;
import com.zju.async.EventModel;
import com.zju.async.EventType;
import com.zju.pojo.Message;
import com.zju.service.MessageService;
import com.zju.utils.MailSender;

@Component
public class LoginHandler implements EventHandler {

	@Resource
	private MessageService messageServiceImpl;
	@Override
	public void doHandler(EventModel model) {
		
		//����վ��֪ͨ
		Message message = new Message();
		//22��Ŀǰ��Ϊϵͳ֪ͨ����id
		message.setFromId(22);
		message.setToId(model.getActorId());
		message.setContent("���"+model.getExt("username")+"����ӭ��½");
		message.setCreatedDate(new Date());
		message.setHasRead(0);
		int toId = model.getActorId();
		String conversationId = toId<22?toId+"-"+22:22+"-"+toId;
		message.setConversationId(conversationId);
		messageServiceImpl.insMessage(message);
		
}

	@Override
	public List<EventType> getSupportEnventTypes() {
		List<EventType> list = new ArrayList<>();
		list.add(EventType.LOGIN);
		return list;
	}
	
}
