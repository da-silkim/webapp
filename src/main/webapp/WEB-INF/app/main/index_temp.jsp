<%@page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<script>
	const userId = "${userid}";
	const userName = "${username}";
	const customerId = "${customerId}";

	$(document).ready(function(){
		let indexHtml = "";
		if("${username}") {
			
		}
		
		//QR코드 스캔후 존재하지 않는 충전기일때 result값을 가져온다.
		if( !isNull('${result}') ){
			let returnResult = JSON.parse('${result}');
			if( returnResult.message == "fail" ){
				alert("조회하신 충전기가 존재하지 않습니다. ");
			}
		}
		
		// 앱이 돌고 있다면 메인화면 접근 시에 위치 정보 가지고 오기.
		if(bridgeUtil.getAppRunning()){
			fn_deleteCookie("init_lat");
			fn_deleteCookie("init_lng");
			fn_deleteCookie("isLatLngInit");
			bridgeUtil.getCurrentLocation();

			// 만약 아이디가 없으면 자동로그인까지.
			if(!userId){
				bridgeUtil.setAutoLogin();
			}
		}
		
		var swiper = new Swiper(".mySwiper2", {
		    pagination: {
		      el: ".swiper-pagination",
		      type: "fraction",
		    },
		    autoplay: {
		      delay: 3000,
		      disableOnInteraction: false,
		    },
		    loop: true,
		});
		
		fn_initProc();
		
	});
	
	function fn_initProc(){
		let cookie_lat = getCookie("init_lat");
		let cookie_lng = getCookie("init_lng");
		if( !cookie_lat ){
			cookie_lat = publicLatitude;
			cookie_lng = publicLongitude;
		}
		const initMap = {"latitude":cookie_lat, "longitude":cookie_lng};
		$.ajax({
			url: "/app/main/list",
			type: 'post',
			data: initMap,
			async: false,
			success : function(res) {
				let number = res.power;
				let power = number.toFixed(2); // 소수점 두 자리까지 반올림하여 문자열로 변환

				$("#charging").text(power + " kWh");
				$("#totalPrice").text($formatNumber(res.totalPrice) + " 원");

				let notiList = res.notiList;
				let notiHtml = "";
				for(i=0;i<notiList.length;i++){
					notiHtml += '<div class="swiper-slide" id="slideDv">';
					notiHtml += '	<a href="/app/customer/my_noti_detail?boardId='+ notiList[i].id +'">';
					notiHtml += '		<div class="slide-tit" id="subject">'+ notiList[i].subject +'</div>';
					notiHtml += '		<div class="slide-cont" id="content">'+ notiList[i].content +'</div>';
					notiHtml += '	</a>';
					notiHtml += '</div>';
				}
				$("#notiList").html(notiHtml);
				
				let stationList = res.stationList;
				let stationHtml = "";
				for(i=0;i<stationList.length;i++){
					stationHtml += '<a href="/app/find/find_map?id='+ stationList[i].id +'">';
					stationHtml += '	<div class="main-list-item">';
					stationHtml += '		<div class="main-item-image"><img src="/resources/images/005-location.png" style="height: 40px; width: 40px;"></div>';
					stationHtml += '		<div class="main-list-contents">';
					stationHtml += '			<div class="main-list-title">'+ stationList[i].name +'</div>';
					stationHtml += '			<div class="main-list-text">'+ stationList[i].distance +' kw</div>';
					stationHtml += '		</div>';

					if (stationList[i].csStatus == "DISCON") {
						stationHtml += '		<div class="main-list-button disable">충전불가능</div>';
					} else {
						stationHtml += '		<div class="main-list-button">충전가능</div>';
					}
					stationHtml += '	</div>';
					stationHtml += '</a>';
				}
				$("#stationList").html(stationHtml);
				if( res.customerId != null ){
					$("#stationTitle").text("마이 충전소");
					
				} else {
					$("#stationTitle").text("주변 충전소");
				}
				
				//TODO : 결제 임시 주석처리 - ksi_edit.240702
				// //결제카드 확인하여 결제카드 관리 화면이동처리
				// if( !isNull(res.paymentUrl) ){
				// 	//let mainRedirect = fn_getCookie("mainRedirect");
				// 	//if( mainRedirect == "true" ){
				// 		//fn_deleteCookie(mainRedirect");
				// 	//} else {
				// 		let redirectUrlMsg = "";
				// 		let redirectYn = false;
				// 		if( res.paymentUrl.cardCnt == 0 ){
				// 			redirectUrlMsg = "현재 고객님은 결제카드 미등록 상태입니다.\n결제카드 관리 화면으로 이동하겠습니다. ";
				// 			redirectYn = true;
							
				// 		} else if( res.paymentUrl.mainCnt == 0 ){
				// 			redirectUrlMsg = "현재 고객님은 주 결제카드 설정이 안되어있습니다.\n결제카드 관리 화면으로 이동하겠습니다. ";
				// 			redirectYn = true;
							
				// 		}
						
				// 		if( redirectYn ){
				// 			if(confirm(redirectUrlMsg)){
				// 				fn_setCookie("mainRedirect", "true", 1);
				// 				location.href='/app/payment/payment';
								
				// 			}
				// 		}
				// 	//}
					
				// }
			},
			error: function (res){
			}
		});
	}

	function fn_go_payment(){
		if( $("#chargerNo").val() === ""){
			pop_alert("fail", "충전기번호를 입력해주세요.", "충전기번호를 입력해주세요.");
			$("#chargerNo").focus();
		}
		else{
			const chargerNo = parseInt($("#chargerNo").val())
			$.ajax({
				url: "/app/payment/check_charger/action",
				type: 'post',
				data: JSON.stringify({chargerNo}),
				async: false,
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Content-type","application/json; charset=utf-8;");
				},
				success : function(res) {
					fnOpenPopup("/app/payment/main?chargerNo="+String(chargerNo), null, true)
				},
				error: function (res){
					pop_alert('fail', res['responseJSON'].message, res['responseJSON'].description);
				}
			});
		}
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

	// 2024.06.19 add
	const swiper = new Swiper('.swiper', {
	// Optional parameters
	direction: 'vertical',
	loop: true,

	// If we need pagination
	pagination: {
		el: '.swiper-pagination',
	},

	// Navigation arrows
	navigation: {
		nextEl: '.swiper-button-next',
		prevEl: '.swiper-button-prev',
	},

	// And if we need scrollbar
	scrollbar: {
		el: '.swiper-scrollbar',
	},
	});
</script>

<link
  rel="stylesheet"
  href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"
/>

<script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
<head>
	<meta name="google-site-verification" content="YEnqXLtMU0yLMhEE-bkqVU8Yljj1JcBkis5XYDX3dnU" />
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
	<title>Dongah</title>
</head>
<header class="header-wrap main" id="imgLogo" style="background-color: #365486;"></header>

<!-- contents -->
<div class="contents-wrap">
    <section class="indivi-area" style="background-color: #365486;  padding-top: 2rem; padding-bottom: 1rem;">
        <ul>
            <li>
				<div id="month">당월 이용내역</div>
				<div id="monthHistory">
                	<div id="monthCharging">
	                	<!-- <div class="img-center"><img src="/resources/images/blue/ico_main09.png"></div> -->
	                	<div class="title">충전량</div>
		                <div class="value" id="charging"></div>
                	</div>
                </div>
				<div id="monthHistory" style="padding-top: 14px;">
                	<div id="monthPayment">
	                	<!-- <div class="img-center"><img src="/resources/images/blue/ico_main02.png"></div> -->
	                	<div class="title">사용금액</div>
		                <div class="value" id="totalPrice">30,000원</div>
                	</div>
                </div>
				<div id="monthHistory" style="padding-top: 14px;">
                	<div id="monthPayment">
	                	<!-- <div class="img-center"><img src="/resources/images/blue/ico_main02.png"></div> -->
	                	<div class="title">충전횟수</div>
		                <div class="value" id="totalCount">3 회</div>
                	</div>
                </div>
            </li>
            <li>
                <!-- <div id="monthHistory">
                	<div id="monthPayment">
	                	<div class="img-center"><img src="/resources/images/blue/ico_main02.png"></div>
	                	<div class="title">당월 사용금액</div>
		                <div class="value" id="totalPrice">30,000원</div>
                	</div>
                </div> -->
				<div style="padding-bottom: 14px; height: 100%;">
					<a onclick="bridgeUtil.fn_qrScanProc();">
						<img src="/resources/images/blue/ico_main07.png" style="height: 90px; object-fit: contain; margin-bottom: .7rem;">
						<p>QR 충전하기</p>
					</a>
				</div>
				<!-- <div>
					<a onclick="location.href='/app/charge/charge_history'">
						<img src="/resources/images/blue/ico_main05.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>충전이력</p>
					</a>
				</div>
				<div>
					<a onclick="chargeNow();">
						<img src="/resources/images/blue/ico_main08.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>충전현황</p>
					</a>
				</div> -->
            </li>
			
        </ul>
    </section>
	<!-- style="display: grid; grid-template-columns: 1.05fr 1fr;" -->
    <section style="display: grid; grid-template-columns: 1fr 1fr 1fr; padding: 0px 5.5%; place-items: stretch; border-radius: 0 0 15px 15px; background-color: #365486;">
        <div class="indivi3-child3 pr-10">
	        <ul>
	            <li class="main-item">
	                <a onclick="location.href='/app/charge/my_favorites'">
	                	<img src="/resources/images/blue/ico_main03.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>나의 충전소</p>
	                </a>
	            </li>
			</ul>
			<!-- <ul>
	            <li>
	                <a onclick="location.href='/app/find/find_map'">
						<img src="/resources/images/blue/ico_main04.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>주변충전소</p>
					</a>
	            </li>	           
			</ul> -->
        </div>
		<div class="indivi3-child3 plr-5">
			<ul>
	            <li class="main-item">
	                <a onclick="location.href='/app/charge/charge_history'">
						<img src="/resources/images/blue/ico_main05.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>충전이력</p>
					</a>
	            </li>
	        </ul>
		</div>
		<div class="indivi3-child3 pl-10">
			<ul>
	            <li class="main-item">
	                <a onclick="chargeNow();">
						<img src="/resources/images/blue/ico_main08.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>충전현황</p>
					</a>
	            </li>
	        </ul>
		</div>
		
        <!-- <div class="indivi3-area" style="width: 100%;">
        	<ul style="margin: 0; height: 100%; box-sizing: border-box; padding-bottom: 1rem;">
        		<li style="width: 105%; height: 100%; box-sizing: border-box;  display: flex; justify-content: center; align-items: center;">
        			<a onclick="bridgeUtil.fn_qrScanProc();">
						<img src="/resources/images/blue/ico_main07.png" style="height: 90px; object-fit: contain; margin-bottom: .7rem;">
						<p>QR 충전하기</p>
					</a>
        		</li>
        	</ul>
        </div> -->
    </section>

	<!-- 2024.06.20 add -->
	<!-- <section style="display: grid; grid-template-columns: 1fr 1fr 1fr; padding: 0px 5.5%; place-items: stretch; border-radius: 0 0 15px 15px; background-color: #365486;">
        <div class="indivi3-child3 pr-10">
	        <ul>
	            <li class="main-item">
	                <a onclick="location.href='/app/charge/my_favorites'">
	                	<img src="/resources/images/blue/ico_main03.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>나의 충전소</p>
	                </a>
	            </li>
			</ul>
        </div>
		<div class="indivi3-child3 plr-5">
			<ul>
	            <li class="main-item">
	                <a onclick="location.href='/app/charge/charge_history'">
						<img src="/resources/images/blue/ico_main05.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>충전이력</p>
					</a>
	            </li>
	        </ul>
		</div>
		<div class="indivi3-child3 pl-10">
			<ul>
	            <li class="main-item">
	                <a onclick="chargeNow();">
						<img src="/resources/images/blue/ico_main08.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>충전현황</p>
					</a>
	            </li>
	        </ul>
		</div>
		<div class="indivi3-child3 pl-10">
			<ul>
	            <li class="main-item">
	                <a onclick="bridgeUtil.fn_qrScanProc();">
						<img src="/resources/images/blue/ico_main07.png" style="height: 40px; object-fit: contain; margin-bottom: .7rem;">
						<p>QR 충전하기</p>
					</a>
	            </li>
	        </ul>
		</div> -->
        <!-- <div class="indivi3-area" style="width: 100%;">
        	<ul style="margin: 0; height: 100%; box-sizing: border-box; padding-bottom: 1rem;">
        		<li style="width: 105%; height: 100%; box-sizing: border-box;  display: flex; justify-content: center; align-items: center;">
        			<a onclick="bridgeUtil.fn_qrScanProc();">
						<img src="/resources/images/blue/ico_main07.png" style="height: 90px; object-fit: contain; margin-bottom: .7rem;">
						<p>QR 충전하기</p>
					</a>
        		</li>
        	</ul>
        </div> 
    </section>-->
	<!-- notice list area -->
    <!-- <section class="common-area" >
        <div class="swiper mySwiper2 notice-area typeB">
            <div class="swiper-wrapper" id="notiList"></div>
            <div class="swiper-pagination"></div>
        </div>
    </section> -->

	<section class="common-area">
		<a href="/app/customer/my_noti">
			<div class="swiper" style="box-sizing: border-box; border-radius: 15px; width: 100%; margin-top: 20px; padding: 20px 20px; background-color: #fff; box-shadow: 0 0 6px #0000000D;">
				<!-- <div class="swiper-wrapper">
					<div class="swiper-slide">Slide 1</div>
					<div class="swiper-slide">Slide 2</div>
					<div class="swiper-slide">Slide 3</div>
				</div>
				<div class="swiper-pagination"></div>
	
				<div class="swiper-button-prev"></div>
				<div class="swiper-button-next"></div>
	
				<div class="swiper-scrollbar"></div> -->
				<!-- <span style="background-color: #eee; border-radius: 5px; margin-right: 2rem; padding: 4px 6px;">
					<i class="bi bi-megaphone"></i> 공지
				</span> -->
				
				<span class="fa-xl" style="margin-right: 2rem;"><i class="bi bi-megaphone fa-xl"></i></span>
				<span>
					테스트 내용입니다.
				</span>
			</div>
		</a>
		
	</section>
    <section class="common-area" >
	    <div id="mainNearCharger" class="main-item" style="box-sizing: border-box; margin-bottom: 80px;">
	        <div class="main-item-head">
	            <div class="main-item-title" id="stationTitle"></div>
	            <a class="more" onclick="location.href='/app/find/find_map'">
					<span style="white-space: nowrap;">더보기</span>
					<!-- <img src="/resources/images/more.png" style="object-fit: contain;"> -->
					<!-- <i class="bi bi-plus"></i> -->
					<i class="bi bi-plus-circle"></i>
				</a>
	        </div>
	        <div class="main-list" id="stationList"></div>
	    </div>
    </section>
</div>
