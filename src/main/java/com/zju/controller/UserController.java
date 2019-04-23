package com.zju.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zju.pojo.User;
import com.zju.service.UserService;



@Controller
public class UserController {
	@Resource
	private UserService userServiceImpl;
	
	
	@RequestMapping(value={"/set"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String setting() {
		return "ϵͳ����ҳ��";
	}
	
	
	@RequestMapping("login/{pass}")
	@ResponseBody
	public User login(@PathVariable("pass") String password,
					  @RequestParam(value="uname",defaultValue="�ŷ�") String name
					  ) {
		
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		System.out.println(name+"--"+password);
//		User seluser = userServiceImpl.selUser(user);
//		if(null != seluser) {
//			System.out.println(seluser);
//			return seluser;
//		}
		return null;
	}
	
	@RequestMapping("get")
	@ResponseBody
	public User getUser(@RequestParam(value="id",defaultValue="1") int id
					  ) {
		System.out.println("�õ�������"+id);
		User user = userServiceImpl.selByid(id);
		System.out.println(user);
		if(null != user) {
			return user;
		}
		return null;
	}
	
	@RequestMapping("add")
	@ResponseBody
	public String addUser() {
		
		User user = new User();
		user.setName("�ܲ�");
		user.setPassword("123");
		user.setSalt("123");
		user.setHeadUrl("123");
		//int index = userServiceImpl.insUser(user);
		int index = 0;
		if(index>0) {
			System.out.println("��ӳɹ�");
			return "success";
			
		}else {
			System.out.println("���ʧ��");
			return "fail";
		}
	}
	/*
	 @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "nowcoder") String key) {
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}", groupId, userId, type, key);
    }
	*/
}
