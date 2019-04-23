package com.zju.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zju.async.EventModel;
import com.zju.pojo.EntityType;
import com.zju.pojo.HostHolder;
import com.zju.pojo.News;
import com.zju.pojo.ViewObject;
import com.zju.service.LikeService;
import com.zju.service.NewsService;
import com.zju.service.UserService;


/*
 * 用于和首页相关的控制
 * */
@Controller
public class HomeController {
	@Resource
	private NewsService newsServiceImpl;
	@Resource
	private UserService userServiceImpl;
	@Resource
	private HostHolder hostHolder;
	@Resource
	private LikeService likeServiceImpl;
	
	//获取新闻---userid=0则获取所有人的，其他两个参数为分页需求
	private List<ViewObject> getNews(int userId,int offset,int limit){
		List<ViewObject> vos = new ArrayList<>();
		List<News> list = newsServiceImpl.selLastestNews(userId, offset, limit);
		if(null != list) {
			for(News news:list) {
				//这里vo存在的意义在于将查询出的咨询和发布人捆绑打包发送
				ViewObject vo = new ViewObject();
				vo.set("news", news);
				vo.set("user", userServiceImpl.selByid(news.getUserId()));
				if(null != hostHolder.getUser()) {
					vo.set("like",likeServiceImpl.getLikeStatus(hostHolder.getUser().getId(), news.getId(), EntityType.ENTITY_NEWS));
				}else {
					vo.set("like", 0);
				}
				vos.add(vo);
			}
		}
		return vos;
	}
	
	//显示所有人发布的信息---先直接返回结果
	@RequestMapping(value = {"/","index"})
	public String index(Model model,@RequestParam(value="pop",defaultValue="0") int pop) {
		
		List<ViewObject> vos = getNews(0,0,10);
		model.addAttribute("vos", vos);
		//如果已经登录就不发送pop信息
		if(hostHolder.getUser() == null) {
			model.addAttribute("pop", pop);
		}
		//System.out.println("主页的："+hostHolder.getUser());
		return "home";
	}
	
	//显示个人发布的信息
	@RequestMapping({"/user/{userId}"})
	public String userIndex(Model model,@PathVariable("userId") int userId) {
		List<ViewObject> vos = getNews(userId,0,10);
		model.addAttribute("vos", vos);
		return "home";
	}
}
