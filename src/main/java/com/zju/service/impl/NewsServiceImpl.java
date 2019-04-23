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
	
	//查询所有写资讯
	@Override
	public List<News> selLastestNews(int userid, int offset, int limit) {
		return newsMapper.selNewsByUserIdAndPage(userid, offset, limit);
	}

	@Override
	public String saveImage(MultipartFile file) throws IOException {
		int dotPos = file.getOriginalFilename().lastIndexOf(".");
		//说明上传错误
		if(dotPos<0) {		
			return null;
		}
		String fileExt = file.getOriginalFilename().substring(dotPos+1).toLowerCase();
		//简单验证后缀名看下是不是图片
		if(!ToutiaoUtil.isImage(fileExt)) {
			return null;
		}
		//生成文件名
		String fileName = UUID.randomUUID().toString().replaceAll("-", "")+fileExt;
		//将文件存入本地--参数：输入流+目标地址+重复替代
		Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(),
				   StandardCopyOption.REPLACE_EXISTING);
		//拼凑一个完整的访问地址给前端，这里其实是访问控制器的一个网址
		return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;
	}

	//查询当前id的资讯
	@Override
	public News selNewsDetial(int newsId) {
		return newsMapper.selNewsById(newsId);
	}
	//更新评论数
	@Override
	public int updCommentCount(int newsId, int count) {
		return newsMapper.updNewsCount(newsId, count);
	}

	@Override
	public int updLikeCount(int newsId, int count) {
		return newsMapper.updLikeCount(newsId, count);
	}

}
