package com.bluenetworks.webapp.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.bluenetworks.webapp.app.member.mapper.MemberMapper;
import com.bluenetworks.webapp.common.CommonUtil;
import com.bluenetworks.webapp.common.NetUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private final MemberMapper memberMapper;


	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException auth) throws IOException, ServletException {
		ObjectMapper om = new ObjectMapper();

		StringBuilder fail_info = new StringBuilder("");
		if(auth instanceof BadCredentialsException){
			//계정 정보 없음
			fail_info.append("아이디/비밀번호를 확인해주세요.");
		}else if(auth instanceof InternalAuthenticationServiceException){
			//시스템 에러, 매장 없음
			fail_info.append(auth.getMessage());
		}else if(auth instanceof AuthenticationCredentialsNotFoundException){
			//인증 요청 거부
			fail_info.append("인증이 거부되었습니다.");
		}else{
			fail_info.append("시스템 장애");
		}
		
		CommonUtil CommonUtil = new CommonUtil();
		String username = request.getParameter("userId");
		String password = request.getParameter("userPassword");
		password = CommonUtil.encryption_sha256(password);

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
		param2.put("userid", username);
		param2.put("userip", ip);
		param2.put("result", 1);
		param2.put("failreason", null);

		NetUtils.sessionInvalidate(request);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", "fail");
		map.put("fail_info", fail_info.toString());

		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		String jsonString = om.writeValueAsString(map);
		OutputStream out = response.getOutputStream();
		out.write(jsonString.getBytes());
	}
}
