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
		//System.out.println("进入了like");
		//将用户插入集合中，返回喜欢的总数
		long likeCount = likeServiceImpl.like(hostHolder.getUser().getId(), newsId, EntityType.ENTITY_NEWS);
		//System.out.println("点赞数:"+likeCount);
		//将点赞数更新到数据库
		newsServiceImpl.updLikeCount(newsId, (int)likeCount);
		
		//进行异步处理
		eventProducer.fireEvent(new EventModel(EventType.LIKE).
				setActorId(hostHolder.getUser().getId()).
				setEntityId(newsId).setEntityType(EntityType.ENTITY_NEWS).
				setEntityOwnerId(newsServiceImpl.selNewsDetial(newsId).getUserId()));
		
		return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
	}
	
	@RequestMapping({"/dislike"})
	@ResponseBody
	public String dislike(@RequestParam("newsId") int newsId) {
		//将用户插入集合中，返回喜欢的总数
		long likeCount = likeServiceImpl.dislike(hostHolder.getUser().getId(), newsId, EntityType.ENTITY_NEWS);
		newsServiceImpl.updLikeCount(newsId, (int)likeCount);
		return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
	}
}




