package com.zju.service;

import java.util.Map;

import com.zju.pojo.User;

public interface UserService {
	
	//ע��---��service����У�飬���ϲ��׳�����͵�½���map
	public Map<String,Object> insRegister(String username,String password);	
	//��½---��service����У�飬���ϲ��׳�����͵�½���map
	public Map<String,Object> loginUser(String username,String password);
	//�ǳ�
	public Map<String, Object> logout(String ticket);
	//ͨ��id�ҳ��û�
	public User selByid(int id);

}
