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


//这个拦截器目的不在于拦截,只是为了确认身份和确认身份后实现数据共享
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
		//System.out.println("进入了拦截器");
		//先判断有没有cookie
		if(request.getCookies() != null) {
			//有cookie的话遍历cookie数组看是否带ticket标签
			for(Cookie cookie:request.getCookies()) {
				if(cookie.getName().equals("ticket")) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		if(ticket != null) {
			//如果携带了ticket,从login_ticket数据库中查找,看是否存在和有效
			LoginTicket loginTicket = loginTicketMapper.selByTicket(ticket);
			//下面情况说明不符合登陆条件
			if(loginTicket==null || loginTicket.getStatus()!=0 || loginTicket.getExpired().before(new Date())) {
				return true;
			}
			//到这说明是登陆用户,将获取user所用信息以实现线程共享
			User user = userMapper.selById(loginTicket.getUserId());
			//System.out.println(user);
			//将对象 放到线程中，这个是重点
			hostHolder.setUser(user);
			//System.out.println(hostHolder.getUser());
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 这里是和前端交互的地方
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
