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

	//这里的命名要包含ins，才能被事务管理
	@Override
	public Map<String, Object> insRegister(String username, String password) {
		//虽然前端已经有了表单验证，但是有的没有通过网页直接在地址栏注册(恶意注册)，所以还是要校验。
		Map<String,Object> map = new HashMap<>();
		//用户名为空---isBlank可以判断出为空和null都是true
		if(StringUtils.isBlank(username)) {
			map.put("msgname","用户名不能为空");
			return map;
		}
		//密码为空
		if(StringUtils.isBlank(password)) {
			map.put("msgpwd","密码不能为空");
			return map;
		}
		//用户名已经被注册
		User user= userMapper.selByName(username);
		if(user != null) {
			map.put("msgname", "用户名已经被注册");
			return map;
		}
		
		//这里还可以扩展功能如密码强度验证等功能
		
		//----------------------------------------------------------
		//到这一步说明用户填写信息正确，插入用户到表中
		user = new User();
		user.setName(username);
		//目前用户头像不是上传的，是直接指定的
		String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
		user.setHeadUrl(head);
		//密码加密部分是重点
		String salt = UUID.randomUUID().toString().substring(0, 5);
		user.setSalt(salt);
		//将密码加salt然后再MD5加密
		String newpwd = ToutiaoUtil.MD5((password+salt).toString());
		user.setPassword(newpwd);
		//用户信息插入数据库中
		int index = userMapper.insUser(user);
		if(index<0) {
			map.put("msgreg", "注册失败");
			return map;
		}
		//注册成功，直接登录
		//先获取当前注册用户的id--这个是数据库自增的字段，因此需要查表得到
		int userId = userMapper.selByName(username).getId();
		String ticket = insLoginTicket(userId);
		if(ticket == null) {
			//说明插入ticket失败
			map.put("msgticket", "增加ticket失败");
		}
		//如果成功，将ticket返回
		map.put("ticket", ticket);
		return map;
	}

	//用户登陆
	@Override
	public Map<String, Object> loginUser(String username, String password) {
		//虽然前端已经有了表单验证，但是有的没有通过网页直接在地址栏注册(恶意注册)，所以还是要校验。
		Map<String,Object> map = new HashMap<>();
		//用户名为空---isBlank可以判断出为空和null都是true
		if(StringUtils.isBlank(username)) {
			map.put("msgname","用户名不能为空");
			return map;
		}
		//密码为空
		if(StringUtils.isBlank(password)) {
			map.put("msgpwd","密码不能为空");
			return map;
		}
		//用户名不存在
		User user= userMapper.selByName(username);
		if(user == null) {
			map.put("msgname", "用户名不存在");
			return map;
		}
			
		//到这一步说明用户填写信息正确,开始校验密码
		String newPwd = password+user.getSalt();
		if(!ToutiaoUtil.MD5(newPwd).equals(user.getPassword())) {
			//密码错误
			map.put("msgpwd", "密码错误，登陆失败");
			return map;
		}
	
		//校验成功,生成新的ticket插入数据库，并返回ticket
		String ticket = insLoginTicket(user.getId());
		if(ticket == null) {
			//说明插入ticket失败
			map.put("msgticket", "增加ticket失败");
		}
		//如果成功，将ticket返回给用户
		map.put("ticket", ticket);
		map.put("userId", user.getId());
		return map;
	}
	
	@Override
	public Map<String, Object> logout(String ticket){
		Map<String,Object> map = new HashMap<>();
		int index = loginTicketMapper.updLoignTicket(ticket, 1);
		if(index<1) {
			map.put("msgout", "退出登陆失败");
		}
		return map;
	}
	
	//插入ticket---这部分是重点
	private String insLoginTicket(int userId) {
		LoginTicket ticket = new LoginTicket();
		ticket.setUserId(userId);
		Date date = new Date();
		//设置有效期限为当前日期后的一天
		date.setTime(date.getTime()+1000*3600*24);
		ticket.setExpired(date);
		ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		//0为有效
		ticket.setStatus(0);
		int index = loginTicketMapper.insLoginTicket(ticket);
		if(index>0) {
			return ticket.getTicket();
		}
		//如果插入失败怎么处理还要优化，先返回为空
		return null;
	}
	

}
