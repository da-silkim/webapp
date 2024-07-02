<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

	<script language="JavaScript">
		
		$(document).ready(function() {
   			
			$("#login_btn").click(function(){
   				login_action();
   			});
   			
   			$("input[name=userPassword]").keyup(function(e) {
                 if (e.keyCode == 13) {
                 	login_action();
                 }
			});
	   			
		});
		
		function login_action() {
			if (check_validation() == false) {
                return false;
            }
            
			$.ajax({
				type: 'POST',
				url: '/app/j_spring_security_check',
				data: $("#loginForm").serialize(),
				dataType: 'json',
				async: false,
				success: function (res) {
					switch (res.result){

						case 'success': {
                            let isAutoLoginChecked =  $("#autoLogin").is(":checked");
                            if(bridgeUtil.getAppRunning()){
                                let param = {
                                    "user_id" : $("#userId").val(),
                                    "user_pw" : $("#userPassword").val(),
                                    "remember-me" : isAutoLoginChecked?"on":"off"
                                };

                                let params = {
                                    pluginId : "autoLogin"
                                    , params : param
                                    , callBack : "window.setAutoLogin"
                                };
                                bridgeUtil.postMessage(params);
                            }
                            location.href = res.return_url
                        };
							break;
						default:
                            alert("아이디 또는 비밀번호를 확인해주세요.");
					}
				},
				error: function (res){
                    alert("아이디 또는 비밀번호를 확인해주세요.");
				}
			});
		}
		
		function check_validation() {
            if ($("#userId").val() == "") {
            	$("#checkId").css("display","");
                return false;
            } else {
            	$("#checkId").css("display","none");
            }

            if ($("#userPassword").val() == "") {
            	$("#checkPass").css("display","");
                return false;
            } else {
            	$("#checkPass").css("display","none");
            }

            return true;
        }

        // function getAppRunning(){
        //     let agt = navigator.userAgent.toLowerCase();
        //     if( agt.indexOf("app_running_ios") > 0 || agt.indexOf("app_running_aos") > 0 ){
        //         return true;
        //
        //     } else {
        //         return false;
        //     }
        // }

		
	
	</script>
<div class="contents-wrap cont-padding-typeD">
    <input id="serviceCompany" id="serviceCompany" type="hidden" value="HNS" name="panel:serviceCompany">
    <h2 class="login-tit-wrap">
        <img src="/resources/images/dongah.png" alt="로고" style="margin-bottom: 10%; margin-top: 15%;">
    </h2>

    <div class="login-form-wrap">
        <fieldset>
            <legend>로그인</legend>
            <form id="loginForm" name="loginForm" method="post" onsubmit="return false;" class="flex-body login-form gap15 mb25">
                <div class="login-input-memInfo input-wrap" style="margin-bottom: 1%;">
                    <input type="text" name="userId" id="userId" placeholder="아이디" class="inputID input-one input-h50">
                    <p id="checkId" style="display:none;margin:0px 0px 10px 30px;color: #FF0000;">* 아이디를 입력하시기 바랍니다.</p>
                    <input type="password" name="userPassword" id="userPassword" placeholder="비밀번호" class="inputPW input-one input-h50">
                    <p id="checkPass" style="display:none;margin:10px 0px 10px 30px;color: #FF0000;">* 비밀번호를 입력하시기 바랍니다.</p>
                </div>
                <div class="auto-login">
                    <input type="checkbox" name="remember-me" id="autoLogin">
                    <label for="autoLogin" class="text">자동 로그인</label>
                </div>
            </form>
            <div class="btn-wrap login">
                <button type="submit" class="btn-gradient btn-one btn-h50" id="login_btn" style="margin-bottom: 3%;">로그인</button>
            </div>
            <ul class="login-util">
                <li><a onclick="location.href='/app/member/find_id'" class="link-findInfo">아이디/비밀번호 찾기</a></li>
                <li><a onclick="location.href='/app/member/join_terms'" class="link-join">회원가입</a></li>
            </ul>
        </fieldset>
    </div>
</div>

