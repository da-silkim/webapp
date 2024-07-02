package com.bluenetworks.webapp.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class NetUtils {

	/*
	 * session 초기화
	 */
	public static void sessionInvalidate(HttpServletRequest request){
		request.getSession().invalidate();
	}

	/*
	 * session 속성값 삭제
	 */
	public static void removeSession(HttpServletRequest request, String key){
		request.getSession().removeAttribute(key);
	}

	/*
	 * session scope에 값 저장
	 */
	public static void setSession(HttpServletRequest request, String key, Object value){
		request.getSession().setAttribute(key, value);
	}

	/*
	 * session scope에 저장된 값 반환
	 */
	public static Object getSession(HttpServletRequest request, String key){
		return request.getSession().getAttribute(key);
	}

	public static String getDomain(HttpServletRequest request){
		return request.getRequestURL().toString().replaceAll(request.getRequestURI(),"");
	}

	public static String getReferer(HttpServletRequest request) {
		return request.getHeader("referer").replaceAll(getDomain(request),"");
	}


	public static String getCookie(HttpServletRequest request, String cookiename) {
		String result = "";
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				String cName = c.getName();
				if (cName.equals(cookiename))
					result = c.getValue();
			}
		}

		return result;
	}

}
