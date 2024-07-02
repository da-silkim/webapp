package com.bluenetworks.webapp.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.bluenetworks.webapp.app.member.mapper.MemberMapper;
import com.bluenetworks.webapp.common.NetUtils;
import com.bluenetworks.webapp.app.member.model.UserVO;

import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private final MemberMapper memberMapper;

	public LoginSuccessHandler(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException {

		String url = request.getRequestURI();

		ObjectMapper om = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", "success");
		if(url.startsWith("/web")){
			map.put("return_url", "/web/main"); // 로그인 이후 이동될 주소
		} else {
			map.put("return_url", "/app/main"); // 로그인 이후 이동될 주소
		}

		UserVO user =(UserVO)auth.getPrincipal();
		NetUtils.setSession(request,"userid", user.getUserId());
		NetUtils.setSession(request,"customerId", user.getId());
		NetUtils.setSession(request,"username", user.getName());
		NetUtils.setSession(request,"companyId", user.getCompanyId());

		Cookie cookie = new Cookie("anonymous_key", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		JSONObject param = new JSONObject();
		param.put("userid", user.getUserId());
		param.put("logindt", "CURRENT_TIMESTAMP");
//		memberMapper.UPDATE_COM_CUSTOMER(param);


		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.isEmpty() || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("Proxy-Client-IP");
	    }
	    
	    if (ip == null || ip.isEmpty() || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    
	    if (ip == null || ip.isEmpty() || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    
	    if (ip == null || ip.isEmpty() || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    
	    if (ip == null || ip.isEmpty() || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("X-Real-IP");
	    }
	    
	    if (ip == null || ip.isEmpty() || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("X-RealIP");
	    }
	    
	    if (ip == null || ip.isEmpty() || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("REMOTE_ADDR");
	    }
	    
	    if (ip == null || ip.isEmpty() || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getRemoteAddr();
	    }    
	    
	    String[] arrIP = ip.split(",");
	    if (arrIP.length > 0) {
	    	ip = arrIP[0];
	    }
	    
		JSONObject param2 = new JSONObject();
		param2.put("userid", user.getId());
		param2.put("userip", ip);
		param2.put("result", 0);
		param2.put("failreason", null);

		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		String jsonString = om.writeValueAsString(map);
		OutputStream out = response.getOutputStream();
		out.write(jsonString.getBytes());
	}

}
