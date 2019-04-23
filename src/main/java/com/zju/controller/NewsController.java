package com.zju.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zju.pojo.Comment;
import com.zju.pojo.EntityType;
import com.zju.pojo.HostHolder;
import com.zju.pojo.News;
import com.zju.pojo.ViewObject;
import com.zju.service.CommentService;
import com.zju.service.NewsService;
import com.zju.service.UserService;
import com.zju.service.impl.QiniuServiceImpl;
import com.zju.utils.ToutiaoUtil;

@Controller
public class NewsController {
	private Logger logger = Logger.getLogger(NewsController.class);
	
	@Resource
	private NewsService newsServiceImpl;
	
	@Resource
	private QiniuServiceImpl qiniuServiceImpl;
	
	@Resource
	private HostHolder hostHolder;
	
	@Resource
	private UserService userServiceImpl;
	//新闻详情
	@Resource
	private CommentService commentServiceImpl;
	
	
	//新增评论
	@RequestMapping(value={"/addComment"})
	public String addComment(@RequestParam("content") String content,
						     @RequestParam("newsId") int newsId) {
		
		try {
			//这里可以先做一下校验----
			
			//新建评论准备插入数据库
			Comment comment = new Comment();
			comment.setContent(content);
			//当前登录用户的id
			comment.setUserId(hostHolder.getUser().getId());
			comment.setEntiyId(newsId);
			//这里用于复用
			comment.setEntiyType(EntityType.ENTITY_NEWS);
			comment.setCreatedDate(new Date());
			comment.setStatus(0);
			//插入数据库中
			commentServiceImpl.insComment(comment);
			
			//同步实现news中的评论数量（后面通过异步改进）
			int count = commentServiceImpl.getCommentCount(newsId, EntityType.ENTITY_NEWS);
			newsServiceImpl.updCommentCount(newsId, count);
		} catch (Exception e) {
			logger.error("提交评论错误："+e.getMessage());
		}
		//重定向到消息详情页，将新增的评论显示出来
		return "redirect:/news/" + String.valueOf(newsId);
	}
	
	
	//显示资讯详情
	@RequestMapping(value= {"news/{newsId}"})
	public String newsDetial(Model model,@PathVariable("newsId") int newsId) {
		try {
			News news = newsServiceImpl.selNewsDetial(newsId);
			if(news!=null) {
				//将该信息下的所有评轮内容加载出来
				List<Comment> comments = commentServiceImpl.getCommentByEntity(newsId,EntityType.ENTITY_NEWS);
				//用ViewObject传给前端--每一条评论后面还要带上发起人的信息
				List<ViewObject> vos = new ArrayList<>();
				for(Comment comment:comments) {
					//System.out.println(comment.getCreatedDate());
					ViewObject commentVO = new ViewObject();
					//这里拓展功能就是增加判断；评论是否有效
					commentVO.set("comment", comment);
					//放user进去可以看评论人的头像和用户名
					commentVO.set("user", userServiceImpl.selByid(comment.getUserId()));
					//commentVO.set("news", newsServiceImpl.selNewsDetial(newsId));
					vos.add(commentVO);
				}
				//评论的内容
				model.addAttribute("comments",vos);
			}
			//要显示的资讯
			model.addAttribute("news", news);
			//显示资讯的发布人
			model.addAttribute("owner", userServiceImpl.selByid(news.getUserId()));
		} catch (Exception e) {
			logger.error("获取资讯异常"+e.getMessage());
		}
		return "detail";
	}
	
	//发布新闻---这里传入的image是前端先访问了 uploadImage 控制器得到的网址
	//这里直接返回发布是否成功就好了，如果成功前端会自动访问主页控制器，将新增内容显示出来
	@RequestMapping(value={"/user/addNews"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String addNews(@RequestParam("image") String image,
						  @RequestParam("title") String title,
						  @RequestParam("link") String link) {
		try {
			News news = new News();
			news.setCreatedDate(new Date());
			news.setTitle(title);
			news.setImage(image);
			news.setLink(link);
			//这里提供了游客发布的功能，用户可以不登录直接发布信息
			if(hostHolder.getUser() != null) {
				//获取已登录用户对象
				news.setUserId(hostHolder.getUser().getId());
			}else {
				//放置一个匿名用户
				news.setUserId(1);
			}
			newsServiceImpl.insNews(news);
			return ToutiaoUtil.getJSONString(0);
		} catch (Exception e) {
			logger.error("添加信息异常："+e.getMessage());
			return ToutiaoUtil.getJSONString(1,"信息发布失败");
		}
		
	}
	//上传图片---返回图片访问的网址
	@RequestMapping(value={"/uploadImage"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			//上传到本地
			//String fileUrl = newsServiceImpl.saveImage(file);
			//上传到七牛云
			String fileUrl = qiniuServiceImpl.saveImage(file);
			if(fileUrl == null) {
				return ToutiaoUtil.getJSONString(1, "图片上传失败");
			}
			return ToutiaoUtil.getJSONString(0, fileUrl);
		} catch (IOException e) {
			logger.error("图片上传失败："+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "图片上传失败");
		}
	}
	
	
	//下载图片---只有本地存储的时候需要,云存储的时候直接从云上取，目前不经过控制器
	@RequestMapping(value={"/image"})
	@ResponseBody
	public void getImage(@RequestParam("name") String imageName,HttpServletResponse resp) {
		try {
			resp.setContentType("image/jpeg");
			//ToutiaoUtil.IMAGE_DIR是本地的存储地址，在工具类中定义好了
			StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)), resp.getOutputStream());
		} catch (IOException e) {
			logger.error("图片下载失败："+e.getMessage());
		}
	}
	
	/*
	 *
	 //测试增加评论
	@RequestMapping(value= {"testadd"})
	@ResponseBody
	public String addcomment() {
		Comment comment = new Comment();
		for(int i=0;i<4;i++) {
			comment.setContent("评论"+i);
			comment.setEntiyId(i);
			comment.setEntiyType(0);
			comment.setCreateDate(new Date());
			comment.setStatus(1);
			//commentServiceImpl.insComment(comment);
		}
		List<Comment> comments = commentServiceImpl.getCommentByEntity(0, 0);
		for(Comment c:comments) {
			System.out.println(c);
		}
		return "haha---"+commentServiceImpl.getCommentCount(0, 0);
		//return "haha---";
	}*/
}
