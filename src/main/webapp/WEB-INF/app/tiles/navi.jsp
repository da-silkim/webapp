<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<script>
	$(document).ready(function() {
        let loginHtml = "";
		if("${username}") {
			loginHtml += '<span id="customerName" id="customerNameT" class="customName"><i class="bi bi-person-circle mr-5"></i></span><span id="spCust" style="font-size:2.3rem; font-weight:600;">${username}님</span>';
			// loginHtml += '<button id="btnLogout" type="button" class="btn-gradient btn-h24-br1" onclick="fn_logout();">log out</button>';
            loginHtml += '<div class="mt-10"><i class="bi bi-box-arrow-right mr-5"></i><button id="btnLogout" type="button" onclick="fn_logout();">log out</button></div>';
            
        } else {
			loginHtml += '<span id="loginSpan" class="customName"><i class="bi bi-person-circle mr-5"></i>로그인</span><span id="spLoginCust">해주세요.</span>';
			// loginHtml += '<a id="btnLogIn" class="btn-gradient btn-h24-br1" onclick="fn_login();">log in</a>';
            loginHtml += '<div class="mt-10"><i class="bi bi-box-arrow-in-right mr-5"></i><a id="btnLogIn" onclick="fn_login();">log in</a></div>';

        }
		
		$(".login-state-subwrap").html(loginHtml);
    });

    function fn_login() {
        $('.aside').asidebar('close');
        location.href = '/app/member/login';
    }

    function fn_logout() {
        $('.aside').asidebar('close');



        bridgeUtil.setAutologinClear();

        location.href = '/app/member/logout';
    }
    function fn_promotion() {
    	alert("서비스 준비중입니다.");
    }

    function chargeNow() {
        $.ajax({
            url: "/app/main/charge_status",
            type: 'post',
            data: {customerId: customerId},
            async: false,
            success: function (res) {
                if (res == "") {
                    alert("현재 충전중인 현황이 없습니다.");
                } else {
                    location.href = "/app/charge/charge_now";
                }
            }
        });
    }

</script>
<body>
<div class="sideNavi-wrap">
    <div class="sideNavi-sub-wrap">
        <button type="button" class="btn-sidenavi-close">
            <img src="/resources/images/btn-sidenavi-close.png" alt="사이드 네비게이션 닫기 버튼">
        </button>
        <div class="login-state-wrap">
            <div class="login-state-subwrap"> </div>
        </div>
        <ul class="navi-menu-wrap">
            <li><a href="/app/find/find_map">주변 충전소</a></li>
            <li><a href="/app/charge/my_favorites">MY 충전소</a></li>
            <li><a onclick="chargeNow();">실시간 충전 상태</a></li>
            <li><a href="/app/charge/charge_history">충전 이력</a></li>
            <li><a href="/app/payment/payment_history">결제 이력</a></li>
            <li><a href="/app/payment/payment">결제 카드 관리</a></li>
            <li><a href="/app/customer/modify">내 정보 관리</a></li>
            <li><a href="/app/find/setup">설정 관리</a></li>
            <li><a onclick="fn_promotion();">프로모션 등록</a></li>
            <!-- <li><a href="/app/charge/charge_remote?csId=BNS0149&cpId=BNS014906&connectorId=1">원격충전</a></li> -->
            <li style="font-size: 11px;">(주)동아일렉콤<br/>경기도 용인시 처인구 양지면 남평로 134-14<br/>TEL: (031)330-5500</li>
        </ul>
    </div>
</div>
<div id="div_load_image" style="position:absolute; top:50%; left:50%;width:0px;height:0px; z-index:9999; background:#f0f0f0; opacity:alpha*0.5; margin:auto; padding:0; text-align:center">
    <img id="asxLoading" src="/resources/images/loading.gif" style="display:none; width:50px; height:50px; top : 50%; left :50% ; position:absolute ; margin-top:-25px; margin-left:-25px;opacity: 0.5;">
</div>
</body>
    
