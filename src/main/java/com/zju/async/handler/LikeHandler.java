package com.zju.async.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zju.async.EventHandler;
import com.zju.async.EventModel;
import com.zju.async.EventType;
import com.zju.pojo.Message;
import com.zju.pojo.User;
import com.zju.service.MessageService;
import com.zju.service.UserService;

@Component
public class LikeHandler implements EventHandler{

	@Resource
	private MessageService messageServiceImpl;
	@Resource
	private UserService userServiceImpl;
	@Override
	public void doHandler(EventModel model) {
		//ϵͳ���������ⷢ��վ��֪ͨ
		Message message = new Message();
		User user = userServiceImpl.selByid(model.getActorId());
		//22��Ŀǰ��Ϊϵͳ֪ͨ����id
		message.setFromId(22);
		message.setToId(model.getEntityOwnerId());
		message.setContent("�û�"+user.getName()+"�����������Ѷ");
		message.setCreatedDate(new Date());
		message.setHasRead(0);
		int toId = model.getEntityOwnerId();
		String conversationId = toId<22?toId+"-"+22:22+"-"+toId;
		message.setConversationId(conversationId);
		messageServiceImpl.insMessage(message);
		//System.out.println("ִ����ϲ����ز���");
		
	}

	@Override
	//Ŀǰֻ�����ϲ���¼���ص�����
	public List<EventType> getSupportEnventTypes() {
		List<EventType> list = new ArrayList<>();
		//��Ҫ��עʲô�����ʲô
		list.add(EventType.LIKE);
		return list;
	}

}
