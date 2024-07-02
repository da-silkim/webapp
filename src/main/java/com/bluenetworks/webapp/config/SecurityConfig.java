package com.bluenetworks.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bluenetworks.webapp.app.member.mapper.MemberMapper;
import com.bluenetworks.webapp.interceptor.LoginFailureHandler;
import com.bluenetworks.webapp.interceptor.LoginSuccessHandler;
import com.bluenetworks.webapp.interceptor.LogoutSuccessHandler;
import com.bluenetworks.webapp.interceptor.RememberMeLoginSuccessHandler;
import com.bluenetworks.webapp.interceptor.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MemberMapper memberMapper) throws Exception{
        http
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/app/main").permitAll()
                .antMatchers("/app/set/index",
                        "/app/set/action",
                        "/app/charge/*",
                        "/app/customer/my_info",
                        "/app/customer/my_noti",
                        "/app/customer/my_question",
                        "/app/customer/modify",
                        "/app/customer/my_voc",
                        "/app/payment/payment",
                        "/app/payment/returnBillPay",
                        "/app/payment/pay_point",
                        "/app/payment/pay_credit",
                        "/app/payment/pay_roaming",
                        "/app/payment/point/action",
                        "/app/payment/member/action",
                        "/app/payment/my_register_01",
                        "/app/payment/my_register_02",
                        "/app/payment/my_register/action",
                        "/app/payment/delete_card/action",
                        "/app/payment/credit/action",
                        "/app/payment/payment_history",
                        "/app/find/setup"
                        ).authenticated()
                .antMatchers("/app/**",
                        "/resources/**", "/js/**",
                        "/j_spring_security_check",
                        "/resources/icon/favicon_blue.ico",
                        "/login/**",
                        "/error/**",
                        "/*.css",
                        "/*.ico",
                        "/*.js").permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/app/j_spring_security_check")
                .loginPage("/app/member/login")
                .defaultSuccessUrl("/app/main")
                .successHandler(loginSuccessHandler(memberMapper))
                .failureHandler(loginFailureHandler(memberMapper))
                .usernameParameter("userId")
                .passwordParameter("userPassword")
                .and()
                .logout()
                .logoutUrl("/app/member/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .and()
                .rememberMe()
                .key("bluenetworks")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("remember-me")
                .tokenValiditySeconds(3600*24*365)	//토큰 유지 기간 (1년)
                .authenticationSuccessHandler(rememberMeLoginSuccessHandler(memberMapper));

        return http.build();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(MemberMapper memberMapper){
        return new LoginSuccessHandler(memberMapper);
    }

    @Bean
    public RememberMeLoginSuccessHandler rememberMeLoginSuccessHandler(MemberMapper memberMapper){
        return new RememberMeLoginSuccessHandler(memberMapper);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(MemberMapper memberMapper){
        return new LoginFailureHandler(memberMapper);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new LogoutSuccessHandler();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberMapper memberMapper){
        return new UserDetailsServiceImpl(memberMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new MessageDigestPasswordEncoder("sha-256");
    }
}
