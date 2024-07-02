package com.bluenetworks.webapp.app.member.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bluenetworks.webapp.app.customer.service.AppCustomerService;
import org.json.simple.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bluenetworks.webapp.app.member.service.AppMemberService;
import com.bluenetworks.webapp.common.CommonUtil;
import com.bluenetworks.webapp.common.ErrorMessage;
import com.bluenetworks.webapp.interceptor.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/app/member")
public class AppMemberController {

	private final AppMemberService memberService;

	private final CommonUtil commonUtil;
	
	private final LoginSuccessHandler LoginSuccessHandler;
	private final UserDetailsService userDetailsService;
	
	
	@RequestMapping(value = "/login")
    public String login(HttpServletRequest request, Model model) {
		model.addAttribute("routerName", "로그인");
		return "app/member/login.app_tiles";
    }
	
	@RequestMapping(value = "/join_terms")
	public String join_terms(HttpServletRequest request, Model model) {
        model.addAttribute("routerName", "약관 동의");

		return "app/member/join_terms.app_tiles";
	}
	
	@RequestMapping(value = "/find_id")
	public String find_id(HttpServletRequest request, Model model) {
        model.addAttribute("routerName", "아이디 / 비밀번호 찾기");
        
		return "app/member/find_id.app_tiles_footer";
	}


    /* 아이디 패스워드 찾기 */
    @RequestMapping(value = "/find_id/action", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Transactional
    public JSONObject find_id_action(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject result = new JSONObject();

        String flag = (String) paramMap.get("flag");

        try{
            int count = memberService.idCheck(paramMap);

            if (count == 0) {
                result.put("result", commonUtil.result("error", 003, "유저 정보 없음", "해당 유저 정보가 없습니다."));
            } else {
                if (flag.equals("id")) {
                    resultMap = memberService.getIdAndPw(paramMap);
                    result.put("result", commonUtil.result("success", 001, (String) resultMap.get("userId")));
                } else if (flag.equals("pw")) {
                    result.put("result", commonUtil.result("success", 001, "비밀번호 변경 진행"));
                } else {
                    result.put("result", commonUtil.result("noFlag", 001, "오류가 발생했습니다."));
                }
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.put("result", commonUtil.result("error", 001, "오류가 발생했습니다."));
        }

        if(commonUtil.is_error(result)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }
	
	@RequestMapping(value = "/find_password")
	public String find_password(HttpServletRequest request, Model model) {
		
		
		return "app/member/find_id.app_tiles_footer";
	}
	
    
    @RequestMapping(value = "/join")
	public String join(HttpServletRequest request, Model model) {
        model.addAttribute("routerName", "회원 가입");
		return "app/member/join.app_tiles";
	}
    
    /*
	아이디 중복확인
	 */
    @RequestMapping(value = "/check_id/action", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Transactional
    public JSONObject check_id_action(@RequestBody Map param, HttpServletRequest request, Model model) {

        JSONObject result = new JSONObject();

        String userid = (String) param.get("userid");
        System.out.println("userid ::::::: "+userid);
        System.out.println("param ::::::: "+param);

        try{

            if(userid == null || userid.equals("")){
                result.put("result", commonUtil.result("error", 002, "아이디를 입력해주세요."));
            }
            else {

                userid = userid.toLowerCase();

                //정규식 체크
                boolean is_ok1 = commonUtil.check_pattern("id", userid);

                //중복 체크
                boolean is_ok2 = memberService.checkId(userid);
                System.out.println("정규식 체크 ::::::: "+is_ok1);
                System.out.println("중복 체크 :::::::   "+is_ok2);

                if(!is_ok1){
                    result.put("result", commonUtil.result("idForm", 003, "아이디 형식 오류", "아이디는 영문+숫자 5~12자리로 입력하시기 바랍니다."));
                }
                else if(!is_ok2){
                    result.put("result", commonUtil.result("duplicatedId", 003, "중복확인 알림", "중복된 아이디 입니다.<br/>다른 아이디를 입력해주세요."));
                }
                else{
                    result.put("result", commonUtil.result("success", 200, "중복확인 알림", "사용가능한 아이디 입니다.<br/>나머지 정보도 입력해주세요."));
                }

            }


        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.put("result", commonUtil.result("error", 001, "오류가 발생했습니다."));
        }

        if(commonUtil.is_error(result)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }
    
    /*
	회원가입 액션
	 */
    @RequestMapping(value = "/join/action", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Transactional
    public JSONObject join_action(HttpServletRequest request, Model model, HttpServletResponse response) {
        JSONObject result = new JSONObject();

        //사용자 입력 파라미터
        String customerName = request.getParameter("customerName");
        String userId = request.getParameter("userId");
        String customerType = request.getParameter("customerType");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String brithDay = request.getParameter("brithDay");
        String mobile = request.getParameter("mobile");
        String check_id = request.getParameter("check_id");
        String email = request.getParameter("email");
        String carNo = request.getParameter("carNo");

        //주소
        String zipcode = request.getParameter("zipcode");
        String address = request.getParameter("address");
        String addressDetail = request.getParameter("addressDetail");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String joinDate = sdf.format(c1.getTime());

        try{
            int carNumberCheck = 0;
            if (customerType.equals("P")) {
                carNumberCheck = memberService.selectCarNumberCheck(carNo);
            }

            String idToken = "";

            while(true) {
                idToken = generateRandom16Digits();

                int count = memberService.userAuthCheck(idToken);

                if (count == 0) {
                    break;
                }
            }

            if(userId == null || userId.equals("")){
                result.put("result", commonUtil.result("error", 002, "아이디를 입력해주세요."));
            } else if(!commonUtil.check_pattern("id", userId)){
                result.put("result", commonUtil.result("error", 003, "다른 아이디를 입력해주세요."));
            } else if(!memberService.checkId(userId)){
                result.put("result", commonUtil.result("error", 003, "다른 아이디를 입력해주세요."));
            } else if(password == null || password.equals("")){
                result.put("result", commonUtil.result("error", 003, "비밀번호를 입력 해주세요."));
            } else if(!commonUtil.check_pattern("password", password)){
                result.put("result", commonUtil.result("error", 004, "잘못된 비밀번호예요.", "비밀번호는 영문(대,소문자 가능), 숫자, 특수문자를 포함하여 등록해주세요."));
            } else if(passwordConfirm == null || passwordConfirm.equals("")){
                result.put("result", commonUtil.result("error", 003, "비밀번호 확인을 입력 해주세요."));
            } else if(!password.equals(passwordConfirm)){
                result.put("result", commonUtil.result("error", 003, "비밀번호가 일치하지 않습니다."));
            } else if(customerName == null || customerName.equals("")){
                result.put("result", commonUtil.result("error", 003, "이름을 입력 해주세요."));
            } else if(carNumberCheck > 0){
                result.put("result", commonUtil.result("error", 003, "차량번호 중복", "이미 존재하는 차량번호입니다."));
            } else {

                userId = userId.toLowerCase();
                email = email.toLowerCase();

                password = commonUtil.encryption_sha256(password);

                String number = memberService.selectIdMax();
                String id = "BNS" + number;

                JSONObject param = new JSONObject();
                param.put("id", id);
                param.put("idToken", idToken);
                param.put("userId", userId);
                param.put("companyId", 1);
                param.put("password", password);
                param.put("customerType", customerType);
                param.put("customerName", customerName);
                param.put("status", "P");
                param.put("mobile", mobile);
                param.put("email", email);
                param.put("cardno", null);
                param.put("zipcode", zipcode);
                param.put("address", address);
                param.put("addressDetail", addressDetail);
                param.put("point", 0);
                param.put("brithDay", brithDay);
                param.put("autologin", null);
                param.put("carNo", carNo);
                param.put("joinDate", joinDate);

                // 회원가입 insert
                int success1 = memberService.insertUser(param);

                // 회원가입 insert
                int success2 = memberService.insertUserAuth(param);

                // 차량등록 insert
                int success3 = memberService.insertCustomerEv(param);

                // 스프링 시큐리티 로그인
                UserDetails ckUserDetails = userDetailsService.loadUserByUsername(userId);
                Authentication authentication = new UsernamePasswordAuthenticationToken(ckUserDetails, password,
                        ckUserDetails.getAuthorities());

                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                LoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

                result.put("result", commonUtil.result("success", 200, "성공"));

            }


        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.put("result", commonUtil.result("error", 001, "오류가 발생했습니다."));
        }

        if(commonUtil.is_error(result)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }

    @RequestMapping(value = "/passwordChange")
    public String passwordChange(HttpServletRequest request, Model model) {
        return "app/member/popup/password_change";
    }

    /* 아이디 패스워드 찾기 */
    @RequestMapping(value = "/password_change/action", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Transactional
    public JSONObject password_change_action(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject result = new JSONObject();

        String password = (String) paramMap.get("changePassword");
        password = commonUtil.encryption_sha256(password);

        resultMap.put("password", password);
        resultMap.put("userId", paramMap.get("userId"));

        try{
            int successYn = memberService.passwordUpdate(resultMap);

            if (successYn == 0) {
                result.put("result", commonUtil.result("error", 001, "오류가 발생했습니다."));
            } else {
                result.put("result", commonUtil.result("success", 200, "정상적으로 변경되었습니다."));
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.put("result", commonUtil.result("error", 001, "오류가 발생했습니다."));
        }

        if(commonUtil.is_error(result)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }
    
    private static String generateRandom16Digits() {
        // Random 객체 생성
        Random random = new Random();

        // 16자리 숫자를 담을 StringBuilder 생성
        StringBuilder sb = new StringBuilder(16);

        // 16자리 랜덤 숫자 생성
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10); // 0부터 9까지의 랜덤 숫자 생성
            sb.append(digit);
        }

        return sb.toString();
    }

}
