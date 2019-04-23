package com.zju.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zju.mapper.NewsMapper;
import com.zju.pojo.News;
import com.zju.service.NewsService;
import com.zju.utils.ToutiaoUtil;

@Service
public class NewsServiceImpl implements NewsService {

	@Resource
	private NewsMapper newsMapper;
	
	@Override
	public int insNews(News news) {
		return newsMapper.insNews(news);
	}
	
	//��ѯ����д��Ѷ
	@Override
	public List<News> selLastestNews(int userid, int offset, int limit) {
		return newsMapper.selNewsByUserIdAndPage(userid, offset, limit);
	}

	@Override
	public String saveImage(MultipartFile file) throws IOException {
		int dotPos = file.getOriginalFilename().lastIndexOf(".");
		//˵���ϴ�����
		if(dotPos<0) {		
			return null;
		}
		String fileExt = file.getOriginalFilename().substring(dotPos+1).toLowerCase();
		//����֤��׺�������ǲ���ͼƬ
		if(!ToutiaoUtil.isImage(fileExt)) {
			return null;
		}
		//�����ļ���
		String fileName = UUID.randomUUID().toString().replaceAll("-", "")+fileExt;
		//���ļ����뱾��--������������+Ŀ���ַ+�ظ����
		Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(),
				   StandardCopyOption.REPLACE_EXISTING);
		//ƴ��һ�������ķ��ʵ�ַ��ǰ�ˣ�������ʵ�Ƿ��ʿ�������һ����ַ
		return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;
	}

	//��ѯ��ǰid����Ѷ
	@Override
	public News selNewsDetial(int newsId) {
		return newsMapper.selNewsById(newsId);
	}
	//����������
	@Override
	public int updCommentCount(int newsId, int count) {
		return newsMapper.updNewsCount(newsId, count);
	}

	@Override
	public int updLikeCount(int newsId, int count) {
		return newsMapper.updLikeCount(newsId, count);
	}

}
