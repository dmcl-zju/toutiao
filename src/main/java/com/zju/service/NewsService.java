package com.zju.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.zju.pojo.News;

public interface NewsService {
	
	
	//����һ����Ϣ
	public int insNews(News news);
	
	//����������
	public int updCommentCount(int newsId,int count);
	
	//���µ�����
	public int updLikeCount(int newsId,int count);
	
	//��õ�ǰid����Ϣ
	public News selNewsDetial(int newsId);
	
	//������µ���Ϣ
	public List<News> selLastestNews(int userid,int offset,int limit);
	
	//ͼƬ�ϴ������ش洢�������ļ���
	public  String saveImage(MultipartFile file) throws IOException;
}
