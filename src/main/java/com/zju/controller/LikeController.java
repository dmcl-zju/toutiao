package com.zju.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zju.async.EventModel;
import com.zju.async.EventProducer;
import com.zju.async.EventType;
import com.zju.pojo.EntityType;
import com.zju.pojo.HostHolder;
import com.zju.service.LikeService;
import com.zju.service.NewsService;
import com.zju.utils.ToutiaoUtil;

@Controller
public class LikeController {
	
	@Resource
	private LikeService likeServiceImpl;
	@Resource
	private HostHolder hostHolder;
	@Resource
	private NewsService newsServiceImpl;
	@Resource
	private EventProducer eventProducer;
	
	
	@RequestMapping({"/like"})
	@ResponseBody
	public String like(@RequestParam("newsId") int newsId) {
		//System.out.println("������like");
		//���û����뼯���У�����ϲ��������
		long likeCount = likeServiceImpl.like(hostHolder.getUser().getId(), newsId, EntityType.ENTITY_NEWS);
		//System.out.println("������:"+likeCount);
		//�����������µ����ݿ�
		newsServiceImpl.updLikeCount(newsId, (int)likeCount);
		
		//�����첽����
		eventProducer.fireEvent(new EventModel(EventType.LIKE).
				setActorId(hostHolder.getUser().getId()).
				setEntityId(newsId).setEntityType(EntityType.ENTITY_NEWS).
				setEntityOwnerId(newsServiceImpl.selNewsDetial(newsId).getUserId()));
		
		return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
	}
	
	@RequestMapping({"/dislike"})
	@ResponseBody
	public String dislike(@RequestParam("newsId") int newsId) {
		//���û����뼯���У�����ϲ��������
		long likeCount = likeServiceImpl.dislike(hostHolder.getUser().getId(), newsId, EntityType.ENTITY_NEWS);
		newsServiceImpl.updLikeCount(newsId, (int)likeCount);
		return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
	}
}




