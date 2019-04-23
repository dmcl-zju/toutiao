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
	
	//�û�ע��
	@RequestMapping(value={"/reg"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String regist(Model model,@RequestParam("username")String username,
									 @RequestParam("password")String password,
									 @RequestParam(value="rember" ,defaultValue="0") int rememberme,
									 HttpServletResponse resp) {
		
		try {
			Map<String,Object> map = userServiceImpl.insRegister(username, password);
			if(map.containsKey("ticket")) {
				//��ticket����cookie��
				Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
				//����cookie����Ч·��
				cookie.setPath("/");
				//����û�����˼�ס���룬���趨��Чʱ��Ϊ2�죬����ر�����������
				if(rememberme>0) {
					cookie.setMaxAge(3600*24*2);
				}
				resp.addCookie(cookie);
				return ToutiaoUtil.getJSONString(0,"ע��ɹ�");
			}else {
				//ע��ʧ�ܾͷ���ʧ����Ϣ
				return ToutiaoUtil.getJSONString(1, map);
			}
		} catch (Exception e) {
			logger.error("ע���쳣"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "ע���쳣");
		}
		
	}
	
	//�û���¼
	@RequestMapping(value={"/login"},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String login(Model model,@RequestParam("username")String username,
									 @RequestParam("password")String password,
									 @RequestParam(value="rember" ,defaultValue="0") int rememberme,
									 HttpServletResponse resp) {
		
		try {
			Map<String,Object> map = userServiceImpl.loginUser(username, password);
			if(map.containsKey("ticket")) {
				//��ticket����cookie��
				Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
				//����cookie����Ч·��
				cookie.setPath("/");
				if(rememberme>0) {
					//�������˼�ס��������Ч��2��cookie������ر��������Ҫ���µ�½
					cookie.setMaxAge(3600*24*2);
				}
				resp.addCookie(cookie);
				//���ԣ������½���쳣֪ͨ����
				EventModel evm = new EventModel(EventType.LOGIN);
				evm.setActorId((int)map.get("userId"));
				evm.setExt("username", username);
				evm.setExt("to", "969136791@qq.com");
				eventProducer.fireEvent(evm);
				
				return ToutiaoUtil.getJSONString(0,"��½�ɹ�");
			}else {
				//��½ʧ�ܾͷ���ʧ����Ϣ
				return ToutiaoUtil.getJSONString(1, map);
			}
		} catch (Exception e) {
			logger.error("��½�쳣"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "��½�쳣");
		}
		
	}
	
	//�û��˳���¼---��ticket���ó���ЧȻ���ض�����ҳ
	@RequestMapping(value={"/logout"},produces = "application/json;charset=utf-8")
	public String logout(Model model,@CookieValue("ticket") String ticket) {
			userServiceImpl.logout(ticket);
			return "redirect:/";
	}
}
