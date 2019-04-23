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
		//系统给被点赞这发送站内通知
		Message message = new Message();
		User user = userServiceImpl.selByid(model.getActorId());
		//22号目前定为系统通知发送id
		message.setFromId(22);
		message.setToId(model.getEntityOwnerId());
		message.setContent("用户"+user.getName()+"点赞了你的资讯");
		message.setCreatedDate(new Date());
		message.setHasRead(0);
		int toId = model.getEntityOwnerId();
		String conversationId = toId<22?toId+"-"+22:22+"-"+toId;
		message.setConversationId(conversationId);
		messageServiceImpl.insMessage(message);
		//System.out.println("执行了喜欢相关操作");
		
	}

	@Override
	//目前只处理和喜欢事件相关的类型
	public List<EventType> getSupportEnventTypes() {
		List<EventType> list = new ArrayList<>();
		//需要关注什么就添加什么
		list.add(EventType.LIKE);
		return list;
	}

}
