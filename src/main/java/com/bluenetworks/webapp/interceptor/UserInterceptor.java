package com.bluenetworks.webapp.interceptor;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.bluenetworks.webapp.common.NetUtils;

public class UserInterceptor implements HandlerInterceptor {

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		//비회원이면 uuid 발급
		if(NetUtils.getSession(request, "user_key") == null){
			if(NetUtils.getCookie(request, "anonymous_key").equals("")){
				String uuid = UUID.randomUUID().toString();
				Cookie setCookie = new Cookie("anonymous_key", uuid);
				setCookie.setMaxAge(60 * 60 * 24 * 365 * 10);
				response.addCookie(setCookie);
			}
		}

		return true;
    }
}
