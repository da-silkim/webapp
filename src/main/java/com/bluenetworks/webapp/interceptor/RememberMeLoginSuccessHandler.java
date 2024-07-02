package com.bluenetworks.webapp.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.bluenetworks.webapp.app.member.mapper.MemberMapper;
import com.bluenetworks.webapp.common.NetUtils;
import com.bluenetworks.webapp.app.member.model.UserVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RememberMeLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private final MemberMapper memberMapper;

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		System.out.println("RememberMeLoginSuccessHandler");

		UserVO user =(UserVO)auth.getPrincipal();
		NetUtils.setSession(request,"user_key", user.getUser_key());
		NetUtils.setSession(request,"userid", user.getId());
		NetUtils.setSession(request,"username", user.getName());
		NetUtils.setSession(request,"companyId", user.getCompanyId());

		Cookie cookie = new Cookie("anonymous_key", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		JSONObject param = new JSONObject();
		param.put("userid", user.getId());
		param.put("logindt", "CURRENT_TIMESTAMP");
//		memberMapper.UPDATE_COM_CUSTOMER(param);


		//ios 인 경우 쿠키에 저장되어있는 uuid로 token 매핑
		String uuid = NetUtils.getCookie(request, "uuid");
		if(uuid != null){
			JSONObject param2 = new JSONObject();
			param2.put("device_key", uuid);
			param2.put("user_key", user.getUser_key());
			param2.put("ostype", null);
			param2.put("msgkey", null);
		}

		response.sendRedirect(request.getRequestURI());
	}

}
