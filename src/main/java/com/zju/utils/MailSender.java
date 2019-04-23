package com.zju.utils;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.fileupload.util.mime.MimeUtility;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

//@Service
public class MailSender implements InitializingBean {
	private Logger logger = Logger.getLogger(MailSender.class);
	 private JavaMailSenderImpl mailSender;
	 @Resource
	 private VelocityEngine velocityEngine;
	
	 //初始化邮件参数
	@Override
	public void afterPropertiesSet() throws Exception {
		mailSender = new JavaMailSenderImpl();
        // 请输入自己的邮箱和密码，用于发送邮件
        mailSender.setUsername("969136791@qq.com");
        mailSender.setPassword("lin19940110");
        mailSender.setHost("smtp.qq.com");
        // 请配置自己的邮箱和密码
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
	}
	
	 public boolean sendWithHTMLTemplate(String to, String subject,
		             String template, Map<String, Object> model) {
		try {
			String nick = MimeUtility.decodeText("牛客中级课");
			InternetAddress from = new InternetAddress(nick + "<course@nowcoder.com>");
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			String result = VelocityEngineUtils
			.mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
			
			
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(result, true);
			mailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			logger.error("发送邮件失败" + e.getMessage());
			return false;
		}
	  }

}
