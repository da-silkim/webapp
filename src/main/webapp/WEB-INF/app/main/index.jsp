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
			// on: {
            //     slideChangeTransitionEnd: function() {
            //         // Check if we're on the duplicated slide
            //         if (this.realIndex === this.slides.length - 1) {
            //             // Jump to the actual first slide without animation
            //             this.slideTo(0, 0, false);
            //         }
            //     },
            //     slideChange: function() {
            //         // If we're on the last slide, move to the duplicated first slide
            //         if (this.realIndex === this.slides.length - 2) {
            //             this.autoplay.stop();
            //             setTimeout(() => {
            //                 this.slideNext(); // Move to the duplicate slide with animation
            //                 this.autoplay.start();
            //             }, 3000); // Autoplay delay for next slide transition
            //         }
            //     },
            // },
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
				$("#totalCount").text(res.totCount + " 회");

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
</script>


<header class="header-wrap main bg-color-primary" id="imgLogo"></header>

<!-- contents -->
<div class="contents-wrap">
    <section class="indivi-child">
        <div class="bg-color-secondary">
			<div id="month">당월 이용내역</div>
			<div style="display: grid; grid-template-columns: 1fr 1fr 1fr;">
				<div id="monthHistory">
					<div id="monthCharging">
						<!-- <div class="img-center"><img src="/resources/images/blue/ico_main09.png"></div> -->
						<div class="title">충전량</div>
						<div class="value" id="charging"></div>
					</div>
				</div>
				<div id="monthHistory">
					<div id="monthPayment">
						<!-- <div class="img-center"><img src="/resources/images/blue/ico_main02.png"></div> -->
						<div class="title">사용금액</div>
						<div class="value" id="totalPrice">30,000원</div>
					</div>
				</div>
				<div id="monthHistory">
					<div id="monthCount">
						<!-- <div class="img-center"><img src="/resources/images/blue/ico_main02.png"></div> -->
						<div class="title">충전횟수</div>
						<div class="value" id="totalCount">3 회</div>
					</div>
				</div>
			</div>
		</div>
    </section>
    <section style="display: grid; grid-template-columns: 1fr 1fr 1fr 1fr; padding: 0px 5.3%; place-items: stretch; margin-bottom: 20px; background-color: #365486; border-radius: 0 0 15px 15px; padding-bottom: 1rem;">
        <div class="indivi4-child4 plr-5">
	        <ul>
	            <li class="main-item">
	                <a onclick="location.href='/app/charge/my_favorites'">
	                	<img src="/resources/images/blue/ico_main03.png" class="main-img">
						<p>My충전소</p>
	                </a>
	            </li>
			</ul>
        </div>
		<div class="indivi4-child4 plr-5">
			<ul>
	            <li class="main-item">
	                <aa onclick="bridgeUtil.fn_qrScanProc();" >
						<img src="/resources/images/blue/ico_main07.png" class="main-img">
						<p>QR 충전</p>
					</a>
	            </li>
	        </ul>
		</div>
		<div class="indivi4-child4 plr-5">
			<ul>
	            <li class="main-item">
	                <a onclick="location.href='/app/charge/charge_history'">
						<img src="/resources/images/blue/ico_main05.png" class="main-img">
						<p>충전이력</p>
					</a>
	            </li>
	        </ul>
		</div>

		<div class="indivi4-child4 pl-5">
			<ul>
	            <li class="main-item">
	                <a onclick="chargeNow();">
						<img src="/resources/images/blue/ico_main08.png" class="main-img">
						<p>충전현황</p>
					</a>
	            </li>
	        </ul>
		</div>		
    </section>

	<!-- notice list area -->
	<section class="common-area" >
		<!-- <div style="margin-left: 2%; margin-bottom: 5px; font-size: large;">
			<i class="bi bi-megaphone" style="margin-right: 2%;"></i>공지
		</div> -->
        <div class="swiper mySwiper2 notice-area typeB">
            <div class="swiper-wrapper" id="notiList"></div>
            <div class="swiper-pagination"></div>
        </div>
    </section>

    <section class="common-area" >
	    <div id="mainNearCharger" class="main-item" style="box-sizing: border-box; margin-bottom: 80px;">
	        <div class="main-item-head">
	            <div class="main-item-title" id="stationTitle"></div>
	            <a class="more" onclick="location.href='/app/find/find_map'">
					<span style="white-space: nowrap;">더보기</span>
					<!-- <img src="/resources/images/more.png" style="object-fit: contain;"> -->
					<i class="bi bi-plus-circle"></i>
				</a>
	        </div>
	        <div class="main-list" id="stationList"></div>
	    </div>
    </section>
</div>
