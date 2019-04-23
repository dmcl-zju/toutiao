package com.zju.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zju.async.EventModel;
import com.zju.async.EventProducer;
import com.zju.async.EventType;
import com.zju.service.UserService;
import com.zju.utils.ToutiaoUtil;

@Controller
public class LoginController {
	
	private static final Logger logger = Logger.getLogger(LoginController.class);
	@Resource
	private UserService userServiceImpl;
	@Resource
	private EventProducer eventProducer;
	
	//用户注册
	@RequestMapping(value={"/reg"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String regist(Model model,@RequestParam("username")String username,
									 @RequestParam("password")String password,
									 @RequestParam(value="rember" ,defaultValue="0") int rememberme,
									 HttpServletResponse resp) {
		
		try {
			Map<String,Object> map = userServiceImpl.insRegister(username, password);
			if(map.containsKey("ticket")) {
				//将ticket放入cookie中
				Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
				//设置cookie的有效路径
				cookie.setPath("/");
				//如果用户点击了记住密码，就设定有效时间为2天，否则关闭浏览器就清除
				if(rememberme>0) {
					cookie.setMaxAge(3600*24*2);
				}
				resp.addCookie(cookie);
				return ToutiaoUtil.getJSONString(0,"注册成功");
			}else {
				//注册失败就返回失败信息
				return ToutiaoUtil.getJSONString(1, map);
			}
		} catch (Exception e) {
			logger.error("注册异常"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "注册异常");
		}
		
	}
	
	//用户登录
	@RequestMapping(value={"/login"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String login(Model model,@RequestParam("username")String username,
									 @RequestParam("password")String password,
									 @RequestParam(value="rember" ,defaultValue="0") int rememberme,
									 HttpServletResponse resp) {
		
		try {
			Map<String,Object> map = userServiceImpl.loginUser(username, password);
			if(map.containsKey("ticket")) {
				//将ticket放入cookie中
				Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
				//设置cookie的有效路径
				cookie.setPath("/");
				if(rememberme>0) {
					//如果点击了记住密码则有效期2天cookie，否则关闭浏览器就要重新登陆
					cookie.setMaxAge(3600*24*2);
				}
				resp.addCookie(cookie);
				//测试：加入登陆发异常通知功能
				EventModel evm = new EventModel(EventType.LOGIN);
				evm.setActorId((int)map.get("userId"));
				evm.setExt("username", username);
				evm.setExt("to", "969136791@qq.com");
				eventProducer.fireEvent(evm);
				
				return ToutiaoUtil.getJSONString(0,"登陆成功");
			}else {
				//登陆失败就返回失败信息
				return ToutiaoUtil.getJSONString(1, map);
			}
		} catch (Exception e) {
			logger.error("登陆异常"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "登陆异常");
		}
		
	}
	
	//用户退出登录---将ticket设置成无效然后重定向到主页
	@RequestMapping(value={"/logout"},produces = "application/json;charset=utf-8")
	public String logout(Model model,@CookieValue("ticket") String ticket) {
			userServiceImpl.logout(ticket);
			return "redirect:/";
	}
}
