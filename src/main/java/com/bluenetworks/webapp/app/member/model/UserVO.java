package com.bluenetworks.webapp.app.member.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserVO implements UserDetails {
	
	private static final long serialVersionUID = 3453553029997200521L;
			
	private int user_key;
	private String id;
	private String pwd;
	private String name;
	private String auth;
	private String userId;

	private String iMaxSoc;
	private String carNo;
	private String eMinSoc;
	private String grade;
	private String autoLogin;
	private int companyId;
	private String email;
	private String mobile;
	private String birth;
	private String address;
	private String status;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		auth.add(new SimpleGrantedAuthority("USER"));
		return auth;
	}

	@Override
	public String getPassword() {
		return this.getPwd();
	}

	@Override
	public String getUsername() {
		return this.getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}