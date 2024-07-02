package com.bluenetworks.webapp.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bluenetworks.webapp.app.member.mapper.MemberMapper;
import com.bluenetworks.webapp.app.member.model.UserVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberMapper memberMapper;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{

		//DB에서 회원정보 조회
		UserVO user = new UserVO();
		try{
			JSONObject param = new JSONObject();
			param.put("userid", userId);
			param.put("status", "normal");
			JSONObject info = memberMapper.SELECT_COM_CUSTOMER(param);
			System.out.println("info = " + info);

			if(info == null || info.isEmpty()){
				throw new UsernameNotFoundException("유저 정보가 없습니다.");
			}
			else{
				user = new UserVO();
				user.setId((String) info.get("id"));
				user.setUserId(userId);
				user.setPwd((String) info.get("password"));
				user.setName((String) info.get("customerName"));
				user.setAutoLogin((String) info.get("autoLogin"));
				user.setEmail((String) info.get("email"));
				user.setBirth((String) info.get("brithDay"));
				user.setCompanyId(Math.toIntExact((Long) info.get("companyId")));
				user.setStatus((String) info.get("status"));
				user.setMobile((String) info.get("mobile"));
				user.setAddress((String) info.get("address"));
				user.setAuth("USER");

				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority("USER"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return user;
	}
}
