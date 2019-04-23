package com.zju.configuration;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zju.interceptor.PassportInterceptor;


//ע��������----Ŀǰ��û��
@Component
public class WebConfiguration extends WebMvcConfigurerAdapter {
	
	@Resource
	private PassportInterceptor passportInterceptor;
	
	//ע��������
	 public void addInterceptors(InterceptorRegistry registry) {
	      //ע��passportInterceptor
		 registry.addInterceptor(new PassportInterceptor());
	     super.addInterceptors(registry);
	 }
	
}
