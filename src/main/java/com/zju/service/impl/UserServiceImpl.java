package com.zju.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.zju.mapper.LoginTicketMapper;
import com.zju.mapper.UserMapper;
import com.zju.pojo.LoginTicket;
import com.zju.pojo.User;
import com.zju.service.UserService;
import com.zju.utils.ToutiaoUtil;

@Service
public class UserServiceImpl implements UserService{
	@Resource
	private UserMapper userMapper;
	@Resource
	private LoginTicketMapper loginTicketMapper;
	

	@Override
	public User selByid(int id) {
		return userMapper.selById(id);
	}

	//���������Ҫ����ins�����ܱ��������
	@Override
	public Map<String, Object> insRegister(String username, String password) {
		//��Ȼǰ���Ѿ����˱���֤�������е�û��ͨ����ҳֱ���ڵ�ַ��ע��(����ע��)�����Ի���ҪУ�顣
		Map<String,Object> map = new HashMap<>();
		//�û���Ϊ��---isBlank�����жϳ�Ϊ�պ�null����true
		if(StringUtils.isBlank(username)) {
			map.put("msgname","�û�������Ϊ��");
			return map;
		}
		//����Ϊ��
		if(StringUtils.isBlank(password)) {
			map.put("msgpwd","���벻��Ϊ��");
			return map;
		}
		//�û����Ѿ���ע��
		User user= userMapper.selByName(username);
		if(user != null) {
			map.put("msgname", "�û����Ѿ���ע��");
			return map;
		}
		
		//���ﻹ������չ����������ǿ����֤�ȹ���
		
		//----------------------------------------------------------
		//����һ��˵���û���д��Ϣ��ȷ�������û�������
		user = new User();
		user.setName(username);
		//Ŀǰ�û�ͷ�����ϴ��ģ���ֱ��ָ����
		String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
		user.setHeadUrl(head);
		//������ܲ������ص�
		String salt = UUID.randomUUID().toString().substring(0, 5);
		user.setSalt(salt);
		//�������saltȻ����MD5����
		String newpwd = ToutiaoUtil.MD5((password+salt).toString());
		user.setPassword(newpwd);
		//�û���Ϣ�������ݿ���
		int index = userMapper.insUser(user);
		if(index<0) {
			map.put("msgreg", "ע��ʧ��");
			return map;
		}
		//ע��ɹ���ֱ�ӵ�¼
		//�Ȼ�ȡ��ǰע���û���id--��������ݿ��������ֶΣ������Ҫ���õ�
		int userId = userMapper.selByName(username).getId();
		String ticket = insLoginTicket(userId);
		if(ticket == null) {
			//˵������ticketʧ��
			map.put("msgticket", "����ticketʧ��");
		}
		//����ɹ�����ticket����
		map.put("ticket", ticket);
		return map;
	}

	//�û���½
	@Override
	public Map<String, Object> loginUser(String username, String password) {
		//��Ȼǰ���Ѿ����˱���֤�������е�û��ͨ����ҳֱ���ڵ�ַ��ע��(����ע��)�����Ի���ҪУ�顣
		Map<String,Object> map = new HashMap<>();
		//�û���Ϊ��---isBlank�����жϳ�Ϊ�պ�null����true
		if(StringUtils.isBlank(username)) {
			map.put("msgname","�û�������Ϊ��");
			return map;
		}
		//����Ϊ��
		if(StringUtils.isBlank(password)) {
			map.put("msgpwd","���벻��Ϊ��");
			return map;
		}
		//�û���������
		User user= userMapper.selByName(username);
		if(user == null) {
			map.put("msgname", "�û���������");
			return map;
		}
			
		//����һ��˵���û���д��Ϣ��ȷ,��ʼУ������
		String newPwd = password+user.getSalt();
		if(!ToutiaoUtil.MD5(newPwd).equals(user.getPassword())) {
			//�������
			map.put("msgpwd", "������󣬵�½ʧ��");
			return map;
		}
	
		//У��ɹ�,�����µ�ticket�������ݿ⣬������ticket
		String ticket = insLoginTicket(user.getId());
		if(ticket == null) {
			//˵������ticketʧ��
			map.put("msgticket", "����ticketʧ��");
		}
		//����ɹ�����ticket���ظ��û�
		map.put("ticket", ticket);
		map.put("userId", user.getId());
		return map;
	}
	
	@Override
	public Map<String, Object> logout(String ticket){
		Map<String,Object> map = new HashMap<>();
		int index = loginTicketMapper.updLoignTicket(ticket, 1);
		if(index<1) {
			map.put("msgout", "�˳���½ʧ��");
		}
		return map;
	}
	
	//����ticket---�ⲿ�����ص�
	private String insLoginTicket(int userId) {
		LoginTicket ticket = new LoginTicket();
		ticket.setUserId(userId);
		Date date = new Date();
		//������Ч����Ϊ��ǰ���ں��һ��
		date.setTime(date.getTime()+1000*3600*24);
		ticket.setExpired(date);
		ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		//0Ϊ��Ч
		ticket.setStatus(0);
		int index = loginTicketMapper.insLoginTicket(ticket);
		if(index>0) {
			return ticket.getTicket();
		}
		//�������ʧ����ô����Ҫ�Ż����ȷ���Ϊ��
		return null;
	}
	

}
