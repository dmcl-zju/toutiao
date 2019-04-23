package com.zju.configuration;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zju.interceptor.PassportInterceptor;


//×¢²áÀ¹½ØÆ÷----Ä¿Ç°»¹Ã»ÓÃ
@Component
public class WebConfiguration extends WebMvcConfigurerAdapter {
	
	@Resource
	private PassportInterceptor passportInterceptor;
	
	//×¢²áÀ¹½ØÆ÷
	 public void addInterceptors(InterceptorRegistry registry) {
	      //×¢²ápassportInterceptor
		 registry.addInterceptor(new PassportInterceptor());
	     super.addInterceptors(registry);
	 }
	
}
