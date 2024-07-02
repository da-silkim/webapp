// 우편번호 찾기 화면을 넣을 element
var element_layer = document.getElementById("layer");

function closeDaumPostcode() {
    // iframe을 넣은 element를 안보이게 한다.
    element_layer.style.display = "none";
}

function execDaumPostcode(gubun, zipCdObj, addrObj, focusObj) {
    new daum.Postcode({
        oncomplete: function(data) {
            // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullAddr = data.address; // 최종 주소 변수
            var extraAddr = ""; // 조합형 주소 변수

            // 기본 주소가 도로명 타입일때 조합한다.
            if (data.addressType === "R") {
                //법정동명이 있을 경우 추가한다.
                if (data.bname !== "") {
                    extraAddr += data.bname;
                }
                // 건물명이 있을 경우 추가한다.
                if (data.buildingName !== "") {
                    extraAddr += extraAddr !== "" ? ", " + data.buildingName : data.buildingName;
                }
                // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                fullAddr += extraAddr !== "" ? " (" + extraAddr + ")" : "";
            }

			var sigungu = data.sigunguCode;
			sigungu = sigungu.substr(0, 2);
			data.province =  sigungu;
			console.log(data);
			
			if( gubun == 'grid' ){
				data.fullAddr = fullAddr;
				//Google Geocoding 위/경도 API 추가
			  	var vUrl = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyAUh6pgB5_HmmATQKmfPOMbH7ETQgEcXeg&address="+fullAddr;
			  	//console.log(vUrl);
			    $.ajax({
			        crossOrigin: false,
			        url : vUrl,
			        success : function(geoData) {
			        	console.log(geoData.results[0].geometry);
			        	data.latitude = geoData.results[0].geometry.location.lat;
			        	data.longitude = geoData.results[0].geometry.location.lng;
			        	getPostData(data);
			        }
			    });
				
			} else if(gubun == 'csInfo') {
				if (zipCdObj) {
	                $("#"+zipCdObj).val(data.zonecode);
	                //$("#"+addrObj).val(fullAddr);
					$('#addressHidPop').val(fullAddr);
					$('#zoneHidPop').val(data.province);
					$('#cityHidPop').val(data.sigunguCode);
					$('#zoneNmHidPop').val(data.sido);
					$('#cityNmHidPop').val(data.sigungu);
	                
	                //Google Geocoding 위/경도 API 추가
				  	var vUrl = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyAUh6pgB5_HmmATQKmfPOMbH7ETQgEcXeg&address="+fullAddr;
				  	console.log(vUrl);
				    $.ajax({
				        crossOrigin: false,
				        url : vUrl,
				        success : function(geoData) {
				        	var lat = geoData.results[0].geometry.location.lat
				        	var lng = geoData.results[0].geometry.location.lng
				        	$('#latitudeHidPop').val(lat);
				        	$('#longitudeHidPop').val(lng);
							var name = $('#csNm').val();
				        	pop_open(2,'width_60',10, 83);
				        	kakaoMapPop(lat, lng, name, fullAddr);
				        	$("#"+focusObj).focus();
				        }
				    });
	            }
			} else {
	            // 우편번호와 주소 정보를 해당 필드에 넣는다.
	            if (zipCdObj) {
	                $("#"+zipCdObj).val(data.zonecode);
	                $("#"+addrObj).val(fullAddr);
	                $("#"+focusObj).focus();

	            } else {
	                document.getElementById("zipcode").value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById("address").value = fullAddr;		//기본주소
	                document.getElementById("province").value = sigungu;		//시도
	                document.getElementById("city").value = data.sigunguCode;	//시군구
	                if ( $("#latitude").length > 0 && $("#longitude").length > 0 ) {
                    	getLngLati(fullAddr);
                    }
	            }
			}


            // iframe을 넣은 element를 안보이게 한다.
            // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
            element_layer.style.display = "none";
        },
        width: "100%",
        height: "100%",
        maxSuggestItems: 5
    }).embed(element_layer);

    // iframe을 넣은 element를 보이게 한다.
    element_layer.style.display = "block";

    // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
    initLayerPosition();
}

function kakaoMapPop(lat, lng, name, fullAddr) {
	// 주소-좌표 변환 객체를 생성합니다
	var geocoder = new kakao.maps.services.Geocoder();
	
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
		mapOption = { 
		center: new kakao.maps.LatLng(lat, lng), // 지도의 중심좌표
		level: 2 // 지도의 확대 레벨
		};
	// KakaoMap 시작
	// 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
	var map = new kakao.maps.Map(mapContainer, mapOption);
	
	mapContainer.style.width = '100%';
	mapContainer.style.height = '655px';

	map.relayout();
	
	var positions = new Array();
	
	var csData = new Object();
   	csData.title = name;
   	csData.latlng = new kakao.maps.LatLng(lat, lng);
	positions.push(csData);
	
	// 마커 이미지의 이미지 주소입니다
	//var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
	var imageSrc = "./resources/images/common/map_maker1.png"

	
		// 마커 이미지의 이미지 크기 입니다
		var imageSize = new kakao.maps.Size(24, 35); 
		// 마커 이미지를 생성합니다    
		var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
		// 마커를 생성합니다
		var marker = new kakao.maps.Marker({
		    map: map, // 마커를 표시할 지도
		    position: positions[0].latlng, // 마커를 표시할 위치
		    title : positions[0].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
		    image : markerImage // 마커 이미지 
		});
		marker.setMap(map);
		
		var content = '<div class="customoverlay" style="width:100%; height:100%;">' + fullAddr + '</div>';
		var contentAddr = fullAddr;
		
		var customOverlay = new kakao.maps.CustomOverlay({
		    map: map,
		    position: positions[0].latlng,
		    content: content,
		    xAnchor: 0.5, // 커스텀 오버레이의 x축 위치입니다. 1에 가까울수록 왼쪽에 위치합니다. 기본값은 0.5 입니다
    		yAnchor: 2.0 // 커스텀 오버레이의 y축 위치입니다. 1에 가까울수록 위쪽에 위치합니다. 기본값은 0.5 입니다
		});
		customOverlay.setMap(map); // 지도에 올린다.
		
		// 지도에 클릭 이벤트를 등록합니다
		// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
		kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
			searchDetailAddrFromCoords(mouseEvent.latLng, function(result, status) {
				if (status === kakao.maps.services.Status.OK) {
				    var roadAddr = !!result[0].road_address ? result[0].road_address.address_name : '';
					var commonAddr = result[0].address.address_name;

					// 도로명주소
					var detailAddr = !!result[0].road_address ? '<div style="width:100%; height:100%;">도로명주소 : ' + result[0].road_address.address_name + '</div>' : '';
					
				    // 지번 주소
					detailAddr += '<div style="width:100%; height:100%;">지번 주소 : ' + commonAddr + '</div>';
					
				            
				    var clickContent = '<div class="customoverlay" style="width:100%; height:100%;">' + detailAddr + '</div>';
				    
					var clickLatLng = mouseEvent.latLng;
					
					map.setCenter(clickLatLng);
					
				    // 마커를 클릭한 위치에 표시합니다 
				    marker.setPosition(clickLatLng);
				    marker.setMap(map);
				
				    // 인포윈도우에 클릭한 위치에 대한 법정동 상세 주소정보를 표시합니다
					customOverlay.setContent(clickContent);
					customOverlay.setPosition(clickLatLng);
					customOverlay.setMap(map); // 지도에 올린다.
					
					if (contentAddr != roadAddr) {
						setTimeout(function() {
							if (!confirm("해당 위치로 선택 시 주소가 변경됩니다.\n그래도 진행하시겠습니까?")) {
								// 취소
								marker.setPosition(positions[0].latlng);
					            marker.setMap(map);
								customOverlay.setContent(content);
								customOverlay.setPosition(positions[0].latlng);		
							} else {
								// 확인
								$('#latitudeHidPop').val(clickLatLng.getLat());
						        $('#longitudeHidPop').val(clickLatLng.getLng());
								if(!result[0].road_address){
									$('#addressHidPop').val(commonAddr);
								} else {
									$('#addressHidPop').val(roadAddr);
									contentAddr = roadAddr;
								}
							}
						}, 100);
					}
				}   
			});
		});
	/** 
	function searchAddrFromCoords(coords, callback) {
	    // 좌표로 행정동 주소 정보를 요청합니다
	    geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);         
	}
	*/
	
	function searchDetailAddrFromCoords(coords, callback) {
	    // 좌표로 법정동 상세 주소 정보를 요청합니다
	    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
	}
}

// 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
// resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
// 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
function initLayerPosition() {
    var width = 600; //우편번호서비스가 들어갈 element의 width
    var height = 455; //우편번호서비스가 들어갈 element의 height
    var borderWidth = 5; //샘플에서 사용하는 border의 두께

    // 위에서 선언한 값들을 실제 element에 넣는다.
    element_layer.style.width = width + "px";
    element_layer.style.height = height + "px";
    element_layer.style.border = borderWidth + "px solid";
    // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
    element_layer.style.left = ((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth + "px";
    element_layer.style.top = ((window.innerHeight || document.documentElement.clientHeight) - height) / 2 - borderWidth + "px";
}
