package com.zju.interceptor;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zju.mapper.LoginTicketMapper;
import com.zju.mapper.UserMapper;
import com.zju.pojo.HostHolder;
import com.zju.pojo.LoginTicket;
import com.zju.pojo.User;


//���������Ŀ�Ĳ���������,ֻ��Ϊ��ȷ����ݺ�ȷ����ݺ�ʵ�����ݹ���
public class PassportInterceptor implements HandlerInterceptor{

	@Resource
	private LoginTicketMapper loginTicketMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private HostHolder hostHolder;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ticket = null;
		//System.out.println("������������");
		//���ж���û��cookie
		if(request.getCookies() != null) {
			//��cookie�Ļ�����cookie���鿴�Ƿ��ticket��ǩ
			for(Cookie cookie:request.getCookies()) {
				if(cookie.getName().equals("ticket")) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		if(ticket != null) {
			//���Я����ticket,��login_ticket���ݿ��в���,���Ƿ���ں���Ч
			LoginTicket loginTicket = loginTicketMapper.selByTicket(ticket);
			//�������˵�������ϵ�½����
			if(loginTicket==null || loginTicket.getStatus()!=0 || loginTicket.getExpired().before(new Date())) {
				return true;
			}
			//����˵���ǵ�½�û�,����ȡuser������Ϣ��ʵ���̹߳���
			User user = userMapper.selById(loginTicket.getUserId());
			//System.out.println(user);
			//������ �ŵ��߳��У�������ص�
			hostHolder.setUser(user);
			//System.out.println(hostHolder.getUser());
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// �����Ǻ�ǰ�˽����ĵط�
		if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		hostHolder.clear();
		
	}

}
