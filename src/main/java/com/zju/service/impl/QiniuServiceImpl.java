package com.zju.service.impl;

import java.io.IOException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zju.utils.ToutiaoUtil;

@Service
public class QiniuServiceImpl {
	
	//记录日志
	private Logger logger = Logger.getLogger(QiniuServiceImpl.class);
	
	//一下根据七牛云提供的模板做-------------------------------------------------
	//构造一个带指定Zone对象的配置类
	Configuration cfg = new Configuration(Zone.zone0());
	//设置好账号和密码---注册账户会给
	String accessKey = "eUelo7i34ghXLdmrJW8tabdDFE1iL2o0XWhON1gK";
	String secretKey = "_tI5rghmshtG9ZNBF6qB-lVVniOelZCCkfQcQ_AA";
	//要上传的空间---先在云上创建好
	String bucket = "mydata";
	//秘钥配置
	Auth auth = Auth.create(accessKey, secretKey);
	//创建上传对象
	UploadManager uploadManager = new UploadManager(cfg);
	//访问的地址
	private static String QINIU_IMAGE_DOMAIN = "http://pp0rwkn5i.bkt.clouddn.com/";
	//简单上传使用默认策略
	String upToken = auth.uploadToken(bucket);

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
		try {
			//输入参数第一个是字节文件
		    Response response = uploadManager.put(file.getBytes(), fileName, upToken);
		    if(response.isOK()&&response.isJson()) {
		    	//返回访问的连接，response.bodyString()返回的是两个json,用fastjson解析，取key
		    	return QINIU_IMAGE_DOMAIN+JSONObject.parseObject(response.bodyString()).get("key");
		    }else {
		    	logger.error("七牛云异常："+response.bodyString());
		    	return null;
		    }
		} catch (QiniuException ex) {
			logger.error("七牛云异常："+ex.getMessage());
	    	return null;
		} 
	}	
}
