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
 * ���ں���ҳ��صĿ���
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
	
	//��ȡ����---userid=0���ȡ�����˵ģ�������������Ϊ��ҳ����
	private List<ViewObject> getNews(int userId,int offset,int limit){
		List<ViewObject> vos = new ArrayList<>();
		List<News> list = newsServiceImpl.selLastestNews(userId, offset, limit);
		if(null != list) {
			for(News news:list) {
				//����vo���ڵ��������ڽ���ѯ������ѯ�ͷ���������������
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
	
	//��ʾ�����˷�������Ϣ---��ֱ�ӷ��ؽ��
	@RequestMapping(value = {"/","index"})
	public String index(Model model,@RequestParam(value="pop",defaultValue="0") int pop) {
		
		List<ViewObject> vos = getNews(0,0,10);
		model.addAttribute("vos", vos);
		//����Ѿ���¼�Ͳ�����pop��Ϣ
		if(hostHolder.getUser() == null) {
			model.addAttribute("pop", pop);
		}
		//System.out.println("��ҳ�ģ�"+hostHolder.getUser());
		return "home";
	}
	
	//��ʾ���˷�������Ϣ
	@RequestMapping({"/user/{userId}"})
	public String userIndex(Model model,@PathVariable("userId") int userId) {
		List<ViewObject> vos = getNews(userId,0,10);
		model.addAttribute("vos", vos);
		return "home";
	}
}
