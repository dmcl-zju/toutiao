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
	//��������
	@Resource
	private CommentService commentServiceImpl;
	
	
	//��������
	@RequestMapping(value={"/addComment"})
	public String addComment(@RequestParam("content") String content,
						     @RequestParam("newsId") int newsId) {
		
		try {
			//�����������һ��У��----
			
			//�½�����׼���������ݿ�
			Comment comment = new Comment();
			comment.setContent(content);
			//��ǰ��¼�û���id
			comment.setUserId(hostHolder.getUser().getId());
			comment.setEntiyId(newsId);
			//�������ڸ���
			comment.setEntiyType(EntityType.ENTITY_NEWS);
			comment.setCreatedDate(new Date());
			comment.setStatus(0);
			//�������ݿ���
			commentServiceImpl.insComment(comment);
			
			//ͬ��ʵ��news�е���������������ͨ���첽�Ľ���
			int count = commentServiceImpl.getCommentCount(newsId, EntityType.ENTITY_NEWS);
			newsServiceImpl.updCommentCount(newsId, count);
		} catch (Exception e) {
			logger.error("�ύ���۴���"+e.getMessage());
		}
		//�ض�����Ϣ����ҳ����������������ʾ����
		return "redirect:/news/" + String.valueOf(newsId);
	}
	
	
	//��ʾ��Ѷ����
	@RequestMapping(value= {"news/{newsId}"})
	public String newsDetial(Model model,@PathVariable("newsId") int newsId) {
		try {
			News news = newsServiceImpl.selNewsDetial(newsId);
			if(news!=null) {
				//������Ϣ�µ������������ݼ��س���
				List<Comment> comments = commentServiceImpl.getCommentByEntity(newsId,EntityType.ENTITY_NEWS);
				//��ViewObject����ǰ��--ÿһ�����ۺ��滹Ҫ���Ϸ����˵���Ϣ
				List<ViewObject> vos = new ArrayList<>();
				for(Comment comment:comments) {
					//System.out.println(comment.getCreatedDate());
					ViewObject commentVO = new ViewObject();
					//������չ���ܾ��������жϣ������Ƿ���Ч
					commentVO.set("comment", comment);
					//��user��ȥ���Կ������˵�ͷ����û���
					commentVO.set("user", userServiceImpl.selByid(comment.getUserId()));
					//commentVO.set("news", newsServiceImpl.selNewsDetial(newsId));
					vos.add(commentVO);
				}
				//���۵�����
				model.addAttribute("comments",vos);
			}
			//Ҫ��ʾ����Ѷ
			model.addAttribute("news", news);
			//��ʾ��Ѷ�ķ�����
			model.addAttribute("owner", userServiceImpl.selByid(news.getUserId()));
		} catch (Exception e) {
			logger.error("��ȡ��Ѷ�쳣"+e.getMessage());
		}
		return "detail";
	}
	
	//��������---���ﴫ���image��ǰ���ȷ����� uploadImage �������õ�����ַ
	//����ֱ�ӷ��ط����Ƿ�ɹ��ͺ��ˣ�����ɹ�ǰ�˻��Զ�������ҳ��������������������ʾ����
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
			//�����ṩ���οͷ����Ĺ��ܣ��û����Բ���¼ֱ�ӷ�����Ϣ
			if(hostHolder.getUser() != null) {
				//��ȡ�ѵ�¼�û�����
				news.setUserId(hostHolder.getUser().getId());
			}else {
				//����һ�������û�
				news.setUserId(1);
			}
			newsServiceImpl.insNews(news);
			return ToutiaoUtil.getJSONString(0);
		} catch (Exception e) {
			logger.error("�����Ϣ�쳣��"+e.getMessage());
			return ToutiaoUtil.getJSONString(1,"��Ϣ����ʧ��");
		}
		
	}
	//�ϴ�ͼƬ---����ͼƬ���ʵ���ַ
	@RequestMapping(value={"/uploadImage"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			//�ϴ�������
			//String fileUrl = newsServiceImpl.saveImage(file);
			//�ϴ�����ţ��
			String fileUrl = qiniuServiceImpl.saveImage(file);
			if(fileUrl == null) {
				return ToutiaoUtil.getJSONString(1, "ͼƬ�ϴ�ʧ��");
			}
			return ToutiaoUtil.getJSONString(0, fileUrl);
		} catch (IOException e) {
			logger.error("ͼƬ�ϴ�ʧ�ܣ�"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "ͼƬ�ϴ�ʧ��");
		}
	}
	
	
	//����ͼƬ---ֻ�б��ش洢��ʱ����Ҫ,�ƴ洢��ʱ��ֱ�Ӵ�����ȡ��Ŀǰ������������
	@RequestMapping(value={"/image"})
	@ResponseBody
	public void getImage(@RequestParam("name") String imageName,HttpServletResponse resp) {
		try {
			resp.setContentType("image/jpeg");
			//ToutiaoUtil.IMAGE_DIR�Ǳ��صĴ洢��ַ���ڹ������ж������
			StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)), resp.getOutputStream());
		} catch (IOException e) {
			logger.error("ͼƬ����ʧ�ܣ�"+e.getMessage());
		}
	}
	
	/*
	 *
	 //������������
	@RequestMapping(value= {"testadd"})
	@ResponseBody
	public String addcomment() {
		Comment comment = new Comment();
		for(int i=0;i<4;i++) {
			comment.setContent("����"+i);
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
