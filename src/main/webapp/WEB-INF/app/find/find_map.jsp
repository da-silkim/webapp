<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:eval var="kakaoKey" expression="@environment.getProperty('blue.kakao-key')" />
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=,clusterer,drawing"></script>
<style>
	.charg_search {
		position: absolute;
		top: 10px;
		left: 10px;
		z-index: 1;
	}
</style>
<div id="contents-wrap map-wrap">
	<div class="detail_map">
		<input type="hidden" id="id">
		<input type="number" id="lat" style="display: none;">
		<input type="number" id="lon" style="display: none;">

		<!--지도 -->
		<div id="map" style="height:900px;"></div>

		<!-- 1.내주변/지역별 충전소 :s -->
		<div class="charg_search-wrap">
			<ul>
				<li><a class="btn-h28 btn-color-sub2 btn-shadow" href="/app/find/near_by_station">내 주변 충전소</a></li>
				<li><a class="btn-h28 btn-color-sub2 btn-shadow" href="/app/find/by_region_station">지역별 충전소</a></li>
			</ul>
		</div>

		<!-- 3.확대/축소 보기 버튼 :s -->
		<div class="change-mapSize-wrap">
			<ul>
				<li>
					<button type="button" id="zoomIn">
						<img src="/resources/images/plus_map.png" alt="확대 보기">
					</button>
				</li>
				<li>
					<button type="button" id="zoomOut">
						<img src="/resources/images/minus_map.png" alt="축소 보기">
					</button>
				</li>
			</ul>
		</div>

		<!-- 4.충전상태 분류 안내 :s -->
		<div class="stateGuide-wrap">
			<button type="button" class="btn_view-stateList">
				<img src="/resources/images/map_on.png" alt="충전상태 분류안내 보기">
			</button>
			<ul class="stateList">
				<li>
					<img src="/resources/images/state_01.png" alt="사용 가능">
				</li>
				<li>
					<img src="/resources/images/state_02.png" alt="사용중">
				</li>
				<li>
					<img src="/resources/images/state_03.png" alt="예약중">
				</li>
				<li>
					<img src="/resources/images/state_04.png" alt="고장">
				</li>
				<li>
					<img src="/resources/images/state_05.png" alt="끊김">
				</li>
			</ul>
		</div>
		<!-- // 4.충전상태 분류 안내 :e -->

		<!-- 5.충전소검색 레이어 :s -->
		<div class="charSearch-wrap">
			<button class="btn_open-charSearch" type="button">
				<img src="/resources/images/btn_search-state.png" alt="충전소 검색 버튼">
			</button>
			<!-- <button type="button" id="curPos" class="btn-cur-pos">
				<img src="/resources/images/btn_nowPosition.png" alt="내 위치 찾기">
			</button> -->
			<div id="charSearch-popup" class="slide-popup-wrap">
				<button type="button" class="btn_close-slidePopup">
					<img src="/resources/images/btn-popup-close.png" alt="닫기 버튼">
				</button>
				<div class="mainTit">충전소 검색</div>
				<ul class="searchOption-wrap">
					<li>
						<p class="subTit">충전기 유형</p>
						<ul class="option-wrap" id="cpTypeList"></ul>
					</li>
					<li>
						<p class="subTit">커넥터 유형</p>
						<ul class="option-wrap" id="connectorTypeList"></ul>
					</li>
					<li>
						<p class="subTit">충전 사업자</p>
						<ul class="option-wrap" id="serviceCompanyList"></ul>
					</li>
				</ul>
			</div>
		</div>
		<!-- // 5.충전소검색 레이어 :e -->
		<div class="charInfo-wrap bottom_banner"></div>

		<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
		<!-- Swiper -->
		<script src="/resources/js/map.js" type="text/javascript"></script>
		<!-- Map -->
	</div>
</div>
<script>
	var banner = $(".bottom_banner");
	var searchCs = $(".bottom_search");
	var userId = "${userid}";
	var longitude = "${longitude}";
	var latitude = "${latitude}";
	var pressSearchButton = 0;

	$(document).ready(function () {
		searchList();
	});
	
	var initMap = (function (){
		var la = $("#lat").val();
		var lo = $("#lon").val();
		var csId = $("#csId").val();

		let cookie_lat = getCookie("init_lat");
		let cookie_lng = getCookie("init_lng");

		if ("${id}") {
			csId = "${id}";
			publicLatitude = "${latitude}";
			publicLongitude = "${longitude}";
		} else {
			if( cookie_lat ){
				publicLatitude = cookie_lat;
				publicLongitude = cookie_lng;
			}
		}
		var obj = {};

		var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스

		var options = { //지도를 생성할 때 필요한 기본 옵션
			center: new daum.maps.LatLng(publicLatitude, publicLongitude), //지도의 중심좌표.
			level: 3 //지도의 레벨(확대, 축소 정도)
		};

		var map = new daum.maps.Map(container, options); //지도 생성 및 객체 리턴
		
		// 지도 최대 축소 설정
		map.setMaxLevel(8);
		
		// 마커 클러스터러 객체 생성
		// var clusterer = new daum.maps.MarkerClusterer({
		// 	map: map, // 클러스터를 표시할 지도 객체
		// 	averageCenter: true, // 클러스터의 중심을 평균으로 설정
		// 	minLevel: 8 // 클러스터 할 최소 지도 레벨
		// });

		// 사용자 위치 찾기
		// geolocationPos();
		function geolocationPos() {
			if(navigator.geolocation) {
				// Geolocation을 이용해서 접속 위치를 얻어온다
				navigator.geolocation.getCurrentPosition(function(position) {
					var curr_lat = position.coords.latitude;
					var curr_lon = position.coords.longitude;

					var locPosition = new daum.maps.LatLng(curr_lat, curr_lon), 	// 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성
						message = '<div style="padding: 5px";>위치 테스트입니다.</div>'; // infoWindow 화면에 표시될 메시지
					
					// marker, infoWindow 표시
					displayMarker(locPosition, message);
				});
			} else {
				var locPosition = new daum.maps.LatLng(37.47996066791427, 126.87810219015276),
					message = 'geolocation을 사용할 수 없습니다';

				displayMarker(locPosition, message);
			}
		}
		// 사용자의 위치를 마커로 표시
		function displayMarker(locPosition, message) {
			// create marker
			// var marker = new daum.maps.Marker({
			// 	map: map,
			// 	position: locPosition
			// });

			var iwContent = message, // 인포윈도우에 표시할 내용
        		iwRemoveable = true;

			// 인포윈도우를 생성합니다
			var infowindow = new kakao.maps.InfoWindow({
				content : iwContent,
				removable : iwRemoveable
			});
			
			// 인포윈도우를 마커위에 표시합니다 
			// infowindow.open(map, marker);
			
			// 지도 중심좌표를 접속위치로 변경합니다
			map.setCenter(locPosition);
		}

		// 마커들을 담을 배열
		var markers = [];
		// 마커 옵션을 담을 배열
		var markerOptions = [];

		// 마커 정보들을 찾는다
		markerInfoAjax("echaeum");

		var positions = [];

		// 충전소 정보
		function markerInfoAjax(gubun){
			positions =[]; // positions 초기화

			prograssbarOpen();
			$.ajax({
				url:"/app/find/station_list",
				type:"get",
				dataType : "json",
				data : {gubun : gubun},
				success:function(result){
					for(var i=0; i<result.list.length; i++){
						var csName = result.list[i].name||"입력된 데이터가 없습니다.";
						var connectorTypes = result.list[i].connectorTypes||"입력된 데이터가 없습니다.";
						var fullAddr = result.list[i].fullAddr||"입력된 데이터가 없습니다.";

						// 충전소 정보 팝업 content
						var content = "";
						var _openStartTime = '00:00';
						var _openEndTime = '00:00';

						if (result.list[i].serviceCompanyId == "1") {
							content += 	'<div id="charInfo-popup" class="slide-popup-wrap"><button type="button" class="btn_close-slidePopup" id="charInfo_close"><img src="/resources/images/btn-popup-close.png" alt="닫기 버튼"></button><div class="station-info-wrap">'
							content += 		'<div class="tit"><p class="operator-name echaeum"><img src="/resources/images/dongah-large.png" alt="로고"></p>';
							content +=     	'<p class="station-name"><span>'+result.list[i].name+'</span>';

							if(result.list[i].favState > 0){
								content +=     '<button type="button" class="btn-bookmark favDel" name="favDel_'+(i+1)+'" data-id="'+result.list[i].favState+'" data-csId="'+result.list[i].id+'"><img src="/resources/images/bookmark_on.png" alt="즐겨찾기"></button>';
							}else{
								content +=     '<button type="button" class="btn-bookmark favReg" name="favReg_'+(i+1)+'" data-csId="'+result.list[i].id+'"><img src="/resources/images/bookmark_off.png" alt="즐겨찾기"></button>';
							}
							content +=		'</p></div>';

							content +=     '<ul class="detail"><li class="address">'+result.list[i].address+'</li>';
							// 주차요금
							if(result.list[i].parkingFee > 0){
								content +=     '<li class="parkPrice">주차요금 : <span>'+result.list[i].parkingFee+'</span></li>';
							} else{
								content +=     '<li class="parkPrice">주차요금 : <span>없음</span></li>';
							}
							// 운영시간
							if(result.list[i].openStartTime != null && result.list[i].openStartTime.length == 4){
								var _openStartTime = result.list[i].openStartTime[0] + result.list[i].openStartTime[1] + ':' + result.list[i].openStartTime[2] + result.list[i].openStartTime[3];
							}
							if(result.list[i].openEndTime != null && result.list[i].openEndTime.length == 4){
								var _openEndTime = result.list[i].openEndTime[0] + result.list[i].openEndTime[1] + ':' + result.list[i].openEndTime[2] + result.list[i].openEndTime[3];
							}

							content +=		'<li class="time">운영시간 : '+_openStartTime+' ~ '+_openEndTime+'</li>';
							content +=		'<li class="chargeType">';
							// 충전기유형 충전기단가(kWh)
							if(result.list[i].cpCount1 > 0){
								content +=		'<span>'+result.list[i].cpType1+'<span class="num"> '+result.list[i].cpCount1+'기 </span></span>';
							}
							if(result.list[i].cpCount2 > 0){
								content +=		'<span>'+result.list[i].cpType2+'<span class="num"> '+result.list[i].cpCount2+'기 </span></span>';
							}
							if(result.list[i].cpCount3 > 0){
								content +=		'<span>'+result.list[i].cpType3+'<span class="num"> '+result.list[i].cpCount3+'기 </span></span>';
							}
							if(result.list[i].cpCount4 > 0){
								content +=		'<span>'+result.list[i].cpType4+'<span class="num"> '+result.list[i].cpCount4+'기 </span></span>';
							}
							content +=		'</li></ul>';
							content +=         '<div class="btn-wrap"><a class="btn-one btn-h40 btn-color-main1" style="margin-bottom: 2%;" href="/app/find/cs_detail?id='+result.list[i].id+'" > 상세보기</a>';
							content +=         '<a class="btn-one btn-h40 btn-color-main1 reverse" href="/app/customer/my_voc?csId='+result.list[i].id+'" > 고장신고</a></div></div></div>';
						} else if (result.list[i].serviceCompanyId == "ME") {
							// 환경부 충전기인 경우
							content += 	'<div id="charInfo-popup" class="slide-popup-wrap"><button type="button" class="btn_close-slidePopup" id="charInfo_close"><img src="/resources/images/btn-popup-close.png" alt="닫기 버튼"></button><div class="station-info-wrap">'
							content += 		'<div class="tit"><p class="operator-name environmentMinistry"><img src="/resources/images/logo_han.png" alt="로고"></p>';
							content +=     	'<p class="station-name"><span>'+result.list[i].name+'</span>';
							content +=		'</p></div>';

							content +=     '<ul class="detail"><li class="address">'+result.list[i].address+'</li>';
							content +=		'<li class="time">운영시간 : '+result.list[i].useTime+'</li>';
							content +=		'<li class="chargeType">';
							// 충전기유형 충전기단가(kWh)
							if(result.list[i].type == '01'){
								content +=		'<span>DC차데모 ('+result.list[i].fee+'원/kWh)</span>';
							}
							if(result.list[i].type == '02'){
								content +=		'<span>승용차 AC완속 ('+result.list[i].fee+'원/kWh)</span>';
							}
							if(result.list[i].type == '03'){
								content +=		'<span>DC차데모+AC3상 ('+result.list[i].fee+'원/kWh)</span>';
							}
							if(result.list[i].type == '04'){
								content +=		'<span>DC콤보 ('+result.list[i].fee+'원/kWh)</span>';
							}
							if(result.list[i].type == '05'){
								content +=		'<span>DC차데모+DC콤보 ('+result.list[i].fee+'원/kWh)</span>';
							}
							if(result.list[i].type == '06'){
								content +=		'<span>DC차데모+AC3상+DC콤보 ('+result.list[i].fee+'원/kWh)</span>';
							}
							if(result.list[i].type == '07'){
								content +=		'<span>AC급속3상 ('+result.list[i].fee+'원/kWh)</span>';
							}
							content +=		'</li></ul>';
						}


						var imageSrc = "/resources/images/state_dongah_01.png";
						if(result.list[i].serviceCompanyId == "1"){
							if(result.list[i].csStatus=="DISCON"){
								imageSrc = "/resources/images/state_dongah_off.png";
							}else if(result.list[i].csStatus=="AVAIL"){
								imageSrc = "/resources/images/state_dongah_01.png";
							}else if(result.list[i].csStatus=="ERROR"){
								imageSrc = "/resources/images/state_dongah_04.png";
							}else if(result.list[i].csStatus=="CHARG"){
								imageSrc = "/resources/images/state_dongah_02.png";
							}else if(result.list[i].csStatus=="RSRV"){
								imageSrc = "/resources/images/state_dongah_03.png";
							}
						} else if(result.list[i].serviceCompanyId == "ME") {
							if(result.list[i].status=="0" || result.list[i].status=="4" || result.list[i].status=="5"
								|| result.list[i].status=="6" || result.list[i].status=="9"){
								imageSrc = "/resources/images/state_en_off.png";
							}else if(result.list[i].status=="2"){
								imageSrc = "/resources/images/state_en_01.png";
							}else if(result.list[i].status=="1"){
								imageSrc = "/resources/images/state_en_04.png";
							}else if(result.list[i].status=="3"){
								imageSrc = "/resources/images/state_en_02.png";
							}else if(result.list[i].status=="6"){
								imageSrc = "/resources/images/state_en_03.png";
							}
						}

						positions.push({
							"content" : content,
							"latlng" : new daum.maps.LatLng(result.list[i].latitude, result.list[i].longitude),
							"image" : new daum.maps.MarkerImage(imageSrc, new daum.maps.Size(51, 62), new daum.maps.Point(13, 34)),
							"imageSrc" : imageSrc,
							"csStatus" : result.list[i].csStatus,
							"csId" : result.list[i].id,
							"serviceCompanyId" : result.list[i].serviceCompanyId,
							"cpCount1" : result.list[i].cpCount1,
							"cpCount2" : result.list[i].cpCount2,
							"cpCount3" : result.list[i].cpCount3,
							"cpCount4" : result.list[i].cpCount4,
							"connCount1" : result.list[i].connCount0,
							"connCount2" : result.list[i].connCount1,
							"connCount3" : result.list[i].connCount2,
							"connCount4" : result.list[i].connCount3,
							"connCount5" : result.list[i].connCount4,
							"serviceSel" : "Y", // 사업자 포지션
							"cpTypeSel" : "Y", // 충전기 유형 포지션
							"connectorTypeSel" : "Y" // 커넥터 타입 포지션
						});
					}
					createMarker();
				},
				complete: function() {
					prograssbarClose();
				}
			});
		}

		//즐겨찾기 등록
		$(document).on("click", ".favReg", function(){
			var name = $(this).attr("name");
			var csId = $(this).attr("data-csId");

			if ( userId ) {
				$.ajax({
					url:"/app/find/favorites_add",
					type:"get",
					dataType:"json",
					data:{csId:csId, userId:userId},
					success:function(data){
						if(data.result == "success"){
							$("button[name="+name+"]").attr("class", "btn-bookmark favDel");
							$("button[name="+name+"]").attr("data-id", data.favId);
							$("button[name="+name+"]").find("img").attr("src","/resources/images/bookmark_on.png");

							positions = [];
							markerInfoAjax("echaeum");
						}else{
							alert(data.content);
						}
					}
				});
			} else {
				alert("로그인 후 이용이 가능합니다.");
				location.href="/app/member/login";
			}
		});

		//즐겨찾기 삭제
		$(document).on("click", ".favDel", function(){
			var name = $(this).attr("name");
			var id = $(this).attr("data-id");
			var csId = $(this).attr("data-csId");

			if ( userId ) {
				$.ajax({
					url:"/app/find/favorites_delete",
					type:"post",
					dataType:"json",
					data:{favId:id,csId:csId},
					success:function(data){
						if(data.result.message == "success"){
							$("button[name="+name+"]").attr("class", "btn-bookmark favReg");
							$("button[name="+name+"]").removeAttr("data-id");
							$("button[name="+name+"]").find("img").attr("src","/resources/images/bookmark_off.png");

							positions = [];
							markerInfoAjax("echaeum");
						}else{
							alert(data.result.content);
						}
					}
				});
			} else {
				alert("로그인 후 이용이 가능합니다.");
				location.href="/app/member/login";
			}
		});

		function clearMarkers() {
			for (var i = 0; i < markers.length; i++) {
				markers[i].setMap(null);
			}
			markers = []; // Clear the markers array
			markerOptions = []; // Clear the markerOptions array
		}

		// 마커 생성 function
		function createMarker() {
			clearMarkers();

			var index= 0;
			for (var i = 0; i < positions.length; i ++) {
				var marker;

				if(markers.length<1) {	// markers 배열이 비어있을 경우
					// 마커를 생성합니다
					marker = new daum.maps.Marker({
						map: map, // 마커를 표시할 지도
						position: positions[i].latlng, // 마커를 표시할 위치
						image : positions[i].image, // 마커 이미지
						title : positions[i].csId
					});
					daum.maps.event.addListener(marker, 'click', makeOverListener(map, marker, positions[i].content));
					daum.maps.event.addListener(map, 'click', makeOutListener());
					markers.push(marker);
					markerOptions.push(positions[i]);
				} else {
					var csIdArr = [];
					markers.forEach(function(element){	// 마커의 충전소id 배열로 생성
						csIdArr.push(element.getTitle());
					});

					if(!(csIdArr.indexOf(positions[i].csId.toString())>-1)){ // 새롭게 조회된 충전소일 경우 마커 생성 및 마커배열에 추가
						// 마커를 생성합니다
						marker = new daum.maps.Marker({
							map: map, // 마커를 표시할 지도
							position: positions[i].latlng, // 마커를 표시할 위치
							image : positions[i].image, // 마커 이미지
							title : positions[i].csId
						});
						daum.maps.event.addListener(marker, 'click', makeOverListener(map, marker, positions[i].content));
						daum.maps.event.addListener(map, 'click', makeOutListener());
						markers.push(marker);
						markerOptions.push(positions[i]);
					}
				}

				//특정 충전소 지도보기 클릭하였을 경우 csId와 동일한 충전소 index를 담아줌
				if(csId != undefined){
					if(positions[i].csId == csId) {
						index = i;
					}
				}
			}

			// 충전소 정보를 표시하는 팝업 클로저 open을 만드는 함수
			function makeOverListener(map, marker, content) {
				return function() {
					$("#charSearch-popup").css("display", "none");

					var charInfo = $(".charInfo-wrap");
					var thisMark = $(this);

					if( charInfo.is(":visible") ){
						charInfo.slideUp();
						$(".mark > button").removeClass("off");
						$(thisMark).addClass("off");
					}else{
						charInfo.slideDown();
						$(".mark > button").addClass("off");
						$(thisMark).removeClass("off");
					}

					banner.empty();
					banner.append(content);
				};
			}

			// 충전소 정보를 표시하는 팝업 클로저 close를  만드는 함수
			function makeOutListener() {
				return function() {
					$("#charSearch-popup").css("display", "none");

					var charInfo = $(".charInfo-wrap");
					var thisMark = $(this);

					if( charInfo.is(":visible") ){
						charInfo.slideUp();
						$(".mark > button").removeClass("off");
						$(thisMark).addClass("off");
					}else{
						charInfo.slideDown();
						$(".mark > button").addClass("off");
						$(thisMark).removeClass("off");
					}
				};
			}

			// 특정충전소 지도보기 클릭하였을경우 해당 충전소 마커 자동클릭
			if(csId != undefined){
				kakao.maps.event.trigger(markers[index], 'click', makeOverListener(map, markers[index], positions[index]));
			}

			// createMarker 함수에서 클러스터러에 마커 추가할 때 주석 처리된 부분 수정
			// 클러스터러에 마커 추가
			//clusterer.addMarkers(markers);

			// 클러스터러를 지도에 표시
			//clusterer.setMap(map);
		}

		$(document).on("click", ".search_option", function(){
			searchOption();
		});

		$(document).on("click", "#ME", function(){
			if ( pressSearchButton < 1 ) {
				pressSearchButton ++;
				markerInfoAjax("ME");
			}
		});

		//검색조건 적용
		function searchOption (){
			//clusterer.clear();
			//clusterer._markers = [];

			//조건 옵션 색상 및 선택유무 조건
			// 마커 포지션 데이터 분기처리
			var searchOptions = $(".search_option");
			for (var i = 0; i < markerOptions.length; i++) {
				searchOptions.each(function(){
					//사업자 옵션
					if($(this).attr('data-type') =='businessType'){
						//선택되지 않은 사업자일 경우 사업자 포지션 N
						if (this.id == "echaeum") {
							if( !$(this).is(":checked") && markerOptions[i].serviceCompanyId == 1){
								markerOptions[i].serviceSel = "N";
								// 선택된 사업자일 경우 사업자 포지션 Y
							} else if( $(this).is(":checked") && markerOptions[i].serviceCompanyId == 1){
								markerOptions[i].serviceSel = "Y";
							}
						} else if (this.id == "ME") {
							if( !$(this).is(":checked") && markerOptions[i].serviceCompanyId == "ME"){
								markerOptions[i].serviceSel = "N";
								// 선택된 사업자일 경우 사업자 포지션 Y
							} else if( $(this).is(":checked") && markerOptions[i].serviceCompanyId == "ME"){
								markerOptions[i].serviceSel = "Y";
							}
						}
					}

					//충전기 유형 옵션
					if($(this).attr('data-type') =='cpType'){
						var count = 0;
						$("#cpTypeList>li>input").each(function(){

							if( $(this).is(":checked") ){
								if(this.id =='01'){
									count += markerOptions[i].cpCount1;
								}
								if(this.id =='02'){
									count += markerOptions[i].cpCount2;
								}
								if(this.id =='03'){
									count += markerOptions[i].cpCount3;
								}
								if(this.id =='04'){
									count += markerOptions[i].cpCount4;
								}
							}
						});
						if(count < 1){
							markerOptions[i].cpTypeSel = "N";
						} else{
							markerOptions[i].cpTypeSel = "Y";
						}
					}
					//커넥터 타입 옵션
					if($(this).attr('data-type') =='connectorType'){
						var count = 0;
						$("#connectorTypeList>li>input").each(function(){
							if( $(this).is(":checked") ){
								if(this.id =='0'){
									count += markerOptions[i].connCount0;
								}
								if(this.id =='1'){
									count += markerOptions[i].connCount1;
								}
								if(this.id =='2'){
									count += markerOptions[i].connCount2;
								}
								if(this.id =='3'){
									count += markerOptions[i].connCount3;
								}
								if(this.id =='4'){
									count += markerOptions[i].connCount4;
								}
							}
						});
						if(count < 1){
							markerOptions[i].connectorTypeSel = "N";
						} else{
							markerOptions[i].connectorTypeSel = "Y";
						}
					}
				});
			}
			// 지도에 마커표시 분기처리
			for (var i =0; i< markerOptions.length; i++){
				// 사업자 포지션 , 충전기 유형 포지션, 커넥터 타입 포지션이 모두 Y인 경우에 지도에 마커표시 ( default 값은 모두 Y )
				if(markerOptions[i].serviceSel =="Y"
						&& markerOptions[i].cpTypeSel =="Y"
						&& markerOptions[i].connectorTypeSel =="Y"){
					markers[i].setMap(map);
					// 포지션중 하나라도 N일 경우 지도에 표시 X

					//clusterer.addMarkers(markers);
					//clusterer.setMap(map);
				} else{
					markers[i].setMap(null);
				}
			}
		};

		// 지도 확대/축소 시 마커 이벤트(이미지 변경)
		function zoomChangedMarker(positions){
			daum.maps.event.addListener(map, 'zoom_changed', function() {
				var imageSrc = "";
				var size1, size2;
				var point1, point2;
			});
		}

		zoomChangedMarker(positions);

		//마커 On&Off
		var isMarkerOff = false;
		$(document).on("click", ".btn_view-stateList", function(){
			var imageSrc = "";
			for(var i=0; i<markers.length; i++){
				if(!isMarkerOff){
					imageSrc = "/resources/images/state_s_off.png";
				} else {
					//imageSrc = "/resources/images/state_s_01.png";
					imageSrc = positions[i].imageSrc;
				}
				var markerImage = new daum.maps.MarkerImage(imageSrc, new daum.maps.Size(31, 42), new daum.maps.Point(13, 34));

				markers[i].setImage(markerImage);
			}

			if (isMarkerOff) {
				isMarkerOff = false;
				$("a[name=markerOn]").css('display','');
				$("a[name=markerOff]").css('display','none');
			} else {
				isMarkerOff = true;
				$("a[name=markerOn]").css('display','none');
				$("a[name=markerOff]").css('display','');
			}
		});

		// 확대 보기
		$(document).on("click", "#zoomIn", function(){
			map.setLevel(map.getLevel() - 1);
		});

		//축소 보기
		$(document).on("click", "#zoomOut", function(){
			map.setLevel(map.getLevel() + 1);
		});

		//충전소 정보 팝업 close
		$(document).on("click", "#charInfo_close", function(){

			$("#charSearch-popup").css("display", "none");

			var charInfo = $(".charInfo-wrap");
			var thisMark = $(this);

			if( charInfo.is(":visible") ){
				charInfo.slideUp();
				$(".mark > button").removeClass("off");
				$(thisMark).addClass("off");
			}else{
				charInfo.slideDown();
				$(".mark > button").addClass("off");
				$(thisMark).removeClass("off");
			}
		});

		// 현재 사용자 위치
		$(document).on("click", "#curPos", function(){
			geolocationPos();
		});

		obj.map = map;
		return obj;
	}());

	//검색조건에 리스트 init
	function searchList() {
		var paramMap = {};

		$.ajax({
			url: "/app/find/option",
			type: "get",
			dataType: "json",
			data: paramMap,
			success: function (result) {
				//검색조건 충전기유형 추가
				for (var i = 0; i < result.cpTypeList.length; i++) {
					$("#cpTypeList").append('<li><input type="checkbox"  data-type="cpType" id="' + result.cpTypeList[i].id + '" name="businessType" class="search_option"><label for="' + result.cpTypeList[i].id + '">' + result.cpTypeList[i].name + '</label></li>');
				}
				//검색조건 커넥터타입 추가
				for (var i = 0; i < result.connectorTypeList.length; i++) {
					$("#connectorTypeList").append('<li><input type="checkbox"  data-type="connectorType" id="' + result.connectorTypeList[i].id + '" name="businessType" class="search_option">' +
							'<label for="' + result.connectorTypeList[i].id + '">' + result.connectorTypeList[i].name + '</label></li>');
				}
				//검색조건 사업자리스트 추가
				$("#serviceCompanyList").append('<li><input type="checkbox" id="echaeum" name="businessType" data-type="businessType" value="" checked="on" class="search_option"><label for="echaeum">동아</label></li>');
				$("#serviceCompanyList").append('<li><input type="checkbox" id="ME" name="businessType" data-type="businessType" value="" class="search_option"><label for="ME">환경부</label></li>');
			},
			complete: function () {
				$("input[name='businessType']").prop("checked", true);
				$("input[id='ME']").prop("checked", false);
			}
		});
	};

	//충전소 검색 조건 함수
	function search_option(){
		banner.css('display','none');
		searchCs.css("display","block");
	}
</script>
