package com.zju.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.zju.pojo.News;

public interface NewsService {
	
	
	//新增一条消息
	public int insNews(News news);
	
	//更新评论数
	public int updCommentCount(int newsId,int count);
	
	//更新点赞数
	public int updLikeCount(int newsId,int count);
	
	//获得当前id的信息
	public News selNewsDetial(int newsId);
	
	//获得最新的信息
	public List<News> selLastestNews(int userid,int offset,int limit);
	
	//图片上传到本地存储，返回文件名
	public  String saveImage(MultipartFile file) throws IOException;
}
