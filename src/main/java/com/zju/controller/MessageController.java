package com.zju.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zju.pojo.HostHolder;
import com.zju.pojo.Message;
import com.zju.pojo.User;
import com.zju.pojo.ViewObject;
import com.zju.service.MessageService;
import com.zju.service.UserService;
import com.zju.utils.ToutiaoUtil;

@Controller
public class MessageController {
	private Logger logger = Logger.getLogger(MessageController.class);
	@Resource
	private MessageService messageServiceImpl;
	@Resource
	private UserService userServiceImpl;
	@Resource
	private HostHolder hostHolder;
	
	//��ȡ�Ự�б�---ǰ���ǵ�ǰ�û����б���˴���Ĳ���ֱ�Ӵ�hostHolder��ȡ������ǰ�˴���
		@RequestMapping({"msg/list"})
		public String conversationList(Model model) {
			//����
			//System.out.println("���������list");
			try {
				User hostUser = hostHolder.getUser();
				if(hostUser != null) {
					List<Message> conversationList = messageServiceImpl.getConversationList(hostUser.getId(), 0, 10);
					List<ViewObject> vos = new ArrayList<>();
					for(Message conversation:conversationList) {
						//����һ��
						//System.out.println(conversation);
						ViewObject vo = new ViewObject();
						vo.set("conversation", conversation);
						//����ǶԷ���id,��Ϊ�鵽�ĻỰid��һ�µģ������Ҫ�ж�
						int targetId = conversation.getFromId()==hostUser.getId()?conversation.getToId():conversation.getFromId();
						User user = userServiceImpl.selByid(targetId);
						if(user==null) {
							continue;
						}
						vo.set("headUrl", user.getHeadUrl());
		                vo.set("userName", user.getName());
		                //���ڵ��ͷ����ת
		                vo.set("targetId", targetId);
		                //��ǰ�Ự��Ŀ
		                vo.set("totalCount", conversation.getId());
		                vo.set("unreadCount", messageServiceImpl.getConversationUnreadCount(hostUser.getId(), conversation.getConversationId()));
		                vos.add(vo);
					}
					model.addAttribute("conversations", vos);
				}
				
			} catch (Exception e) {
				logger.error("��ȡվ�ڻỰ��ʧ��"+e.getMessage());
			}
			return "letter";
		}
	
	
	//��ȡ�Ự��ϸ����
	@RequestMapping({"msg/detail"})
	public String conversationDetail(Model model,@RequestParam("conversationId") String conversationId) {
		//����
		//System.out.println("���������");
		try {
			List<Message> messages = messageServiceImpl.getConversationDetail(conversationId, 0, 10);		
			List<ViewObject> vos = new ArrayList<>();
			for(Message message:messages) {
				//����һ��
				//System.out.println(message);
				ViewObject vo = new ViewObject();
				vo.set("message", message);
				User user = userServiceImpl.selByid(message.getFromId());
				if(user==null) {
					continue;
				}
				vo.set("headUrl", user.getHeadUrl());
				vo.set("userName", user.getName());
				vo.set("userId", user.getId());
				vos.add(vo);
			}
			model.addAttribute("messages", vos);
		} catch (Exception e) {
			logger.error("��ȡ��ϸ�Ự����"+e.getMessage());
		}
		return "letterDetail";
	}
	
	
	//������Ϣ
	@RequestMapping(value={"msg/addmessage"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId,
						     @RequestParam("toId") int toId,
						     @RequestParam("content") String content) {
		try {
			Message message = new Message();
			message.setFromId(fromId);
			message.setToId(toId);
			message.setContent(content);
			message.setCreatedDate(new Date());
			String conversationId = fromId<toId?fromId+"-"+toId:toId+"-"+fromId;
			message.setConversationId(conversationId);
			
			int index = messageServiceImpl.insMessage(message);
			if(index>0) {
				return ToutiaoUtil.getJSONString(0, "��Ϣ���ͳɹ�");
			}
			return ToutiaoUtil.getJSONString(1, "��Ϣ����ʧ��");
		} catch (Exception e) {
			logger.error("��Ϣ����ʧ�ܣ�"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "��Ϣ����ʧ��");
		}
	}
}
