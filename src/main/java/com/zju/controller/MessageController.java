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
	
	//获取会话列表---前提是当前用户的列表，因此传入的参数直接从hostHolder中取，不用前端传入
		@RequestMapping({"msg/list"})
		public String conversationList(Model model) {
			//测试
			//System.out.println("进入控制器list");
			try {
				User hostUser = hostHolder.getUser();
				if(hostUser != null) {
					List<Message> conversationList = messageServiceImpl.getConversationList(hostUser.getId(), 0, 10);
					List<ViewObject> vos = new ArrayList<>();
					for(Message conversation:conversationList) {
						//测试一下
						//System.out.println(conversation);
						ViewObject vo = new ViewObject();
						vo.set("conversation", conversation);
						//这个是对方的id,因为查到的会话id是一致的，因此需要判断
						int targetId = conversation.getFromId()==hostUser.getId()?conversation.getToId():conversation.getFromId();
						User user = userServiceImpl.selByid(targetId);
						if(user==null) {
							continue;
						}
						vo.set("headUrl", user.getHeadUrl());
		                vo.set("userName", user.getName());
		                //用于点击头像跳转
		                vo.set("targetId", targetId);
		                //当前会话数目
		                vo.set("totalCount", conversation.getId());
		                vo.set("unreadCount", messageServiceImpl.getConversationUnreadCount(hostUser.getId(), conversation.getConversationId()));
		                vos.add(vo);
					}
					model.addAttribute("conversations", vos);
				}
				
			} catch (Exception e) {
				logger.error("获取站内会话表失败"+e.getMessage());
			}
			return "letter";
		}
	
	
	//获取会话详细内容
	@RequestMapping({"msg/detail"})
	public String conversationDetail(Model model,@RequestParam("conversationId") String conversationId) {
		//测试
		//System.out.println("进入控制器");
		try {
			List<Message> messages = messageServiceImpl.getConversationDetail(conversationId, 0, 10);		
			List<ViewObject> vos = new ArrayList<>();
			for(Message message:messages) {
				//测试一下
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
			logger.error("获取详细会话错误"+e.getMessage());
		}
		return "letterDetail";
	}
	
	
	//发送信息
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
				return ToutiaoUtil.getJSONString(0, "消息发送成功");
			}
			return ToutiaoUtil.getJSONString(1, "消息发送失败");
		} catch (Exception e) {
			logger.error("消息发送失败："+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "消息发送失败");
		}
	}
}
