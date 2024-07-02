package com.bluenetworks.webapp.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.bluenetworks.webapp.common.NetUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String url = request.getRequestURI();

		NetUtils.sessionInvalidate(request);

		Cookie cookie = new Cookie("remember-me", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		response.sendRedirect("/app/main");
	}

}
