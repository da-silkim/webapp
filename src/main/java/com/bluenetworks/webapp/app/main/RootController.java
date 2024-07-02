package com.bluenetworks.webapp.app.main;

import java.util.Base64;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bluenetworks.webapp.common.NetUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RootController {

	@RequestMapping(value = {"","/"})
	public String root(HttpServletRequest request,  Locale locale, Model model, HttpServletResponse response) {

		String epn = request.getParameter("epn"); // 휴대폰번호(android)
		String uuid = request.getParameter("uuid"); // 휴대폰번호(ios)
		String Token = request.getParameter("Token"); // FCM token

		if(Token != null && !Token.equals("")){
			try {

				String uk = null;
				if(NetUtils.getSession(request, "user_key") != null){
					uk = String.valueOf( NetUtils.getSession(request, "user_key") );
				}
				//안드로이드 사용자라면
//				if(epn != null && !epn.equals("")){
//
//					byte[] decodedBytes = Base64.getDecoder().decode(epn);
//					String decodedPhone = new String(decodedBytes);
//					String phoneNumber = decodedPhone.replace("-", "").replace("+82", "0");
//
//					//android 인 경우 핸드폰번호로 token 매핑(로그인이 안되어있을때도 매핑됨)
//					//아이폰처럼 쿠키에 저장 시 보안 이슈가 있기 때문에 여기서 바로 매핑
//					if(uk == null){
//						JSONObject param = new JSONObject();
//						param.put("phoneno", phoneNumber);
//						JSONObject info = memberMapper.SELECT_COM_USER(param);
//						if( info != null ){
//							uk = String.valueOf(info.get("user_key"));
//						}
//					}
//
//					memberService.insertToken(epn, uk, "0", Token);
//				}
//				//아이폰 사용자라면
//				else if(uuid != null && !uuid.equals("")){
//					// uuid 쿠키에 저장해놓고 로그인 시 매핑
//					Cookie setCookie = new Cookie("uuid", uuid);
//					setCookie.setMaxAge(60 * 60 * 24 * 365 * 10);
//					response.addCookie(setCookie);
//
//					memberService.insertToken(uuid, uk, "1", Token);
//				}
			}
			catch (Exception e){
				log.info("token 저장 오류 발생");
				e.printStackTrace();
			}
		}
		return "redirect:/app/main";
	}
}
