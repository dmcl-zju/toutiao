package com.zju.service;

import java.util.Map;

import com.zju.pojo.User;

public interface UserService {
	
	//注册---在service层做校验，向上层抛出检验和登陆结果map
	public Map<String,Object> insRegister(String username,String password);	
	//登陆---在service层做校验，向上层抛出检验和登陆结果map
	public Map<String,Object> loginUser(String username,String password);
	//登出
	public Map<String, Object> logout(String ticket);
	//通过id找出用户
	public User selByid(int id);

}
