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
	
	//��¼��־
	private Logger logger = Logger.getLogger(QiniuServiceImpl.class);
	
	//һ�¸�����ţ���ṩ��ģ����-------------------------------------------------
	//����һ����ָ��Zone�����������
	Configuration cfg = new Configuration(Zone.zone0());
	//���ú��˺ź�����---ע���˻����
	String accessKey = "eUelo7i34ghXLdmrJW8tabdDFE1iL2o0XWhON1gK";
	String secretKey = "_tI5rghmshtG9ZNBF6qB-lVVniOelZCCkfQcQ_AA";
	//Ҫ�ϴ��Ŀռ�---�������ϴ�����
	String bucket = "mydata";
	//��Կ����
	Auth auth = Auth.create(accessKey, secretKey);
	//�����ϴ�����
	UploadManager uploadManager = new UploadManager(cfg);
	//���ʵĵ�ַ
	private static String QINIU_IMAGE_DOMAIN = "http://pp0rwkn5i.bkt.clouddn.com/";
	//���ϴ�ʹ��Ĭ�ϲ���
	String upToken = auth.uploadToken(bucket);

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
		try {
			//���������һ�����ֽ��ļ�
		    Response response = uploadManager.put(file.getBytes(), fileName, upToken);
		    if(response.isOK()&&response.isJson()) {
		    	//���ط��ʵ����ӣ�response.bodyString()���ص�������json,��fastjson������ȡkey
		    	return QINIU_IMAGE_DOMAIN+JSONObject.parseObject(response.bodyString()).get("key");
		    }else {
		    	logger.error("��ţ���쳣��"+response.bodyString());
		    	return null;
		    }
		} catch (QiniuException ex) {
			logger.error("��ţ���쳣��"+ex.getMessage());
	    	return null;
		} 
	}	
}
