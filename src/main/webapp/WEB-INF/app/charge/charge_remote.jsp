<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:eval var="kakaoKey" expression="@environment.getProperty('blue.kakao-key')" />
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=services"></script>
<script defer >
    var param = {};
    var preparingYn = "N";
    var cpId = "${cpId}";

    $(document).ready(function() {
    	var userId = "${userid}";

        var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
        var options = { //지도를 생성할 때 필요한 기본 옵션
            center: new kakao.maps.LatLng("${latitude}", "${longitude}"), //지도의 중심좌표.
            level: 3 //지도의 레벨(확대, 축소 정도)
        };


        var map = new kakao.maps.Map(container, options);

        var imageSrc = "/resources/images/state_dongah_01.png";

        var icon = new daum.maps.MarkerImage(imageSrc, new daum.maps.Size(51, 62), new daum.maps.Point(13, 34))

        new kakao.maps.Marker({
            position: new kakao.maps.LatLng("${latitude}", "${longitude}"),
            image: icon
        }).setMap(map);
        
    	$(document).on("click", "#btnChargeEnd", function(e) {
            e.preventDefault();
            var id = $(this).attr("id");
            
            ///custPaymentInfo
            
			$.ajax({
				url : "/app/charge/custPaymentInfo",
				type : 'post',
				data : param,
				success : function(res) {
					//console.log(res.cardCnt);
					//console.log(res.mainCnt);
					
					if( !isNull(res) ){
						let redirectUrlMsg = "";
						let redirectYn = false;

                        //TODO : 결제카드 연동 임시 주석처리 - ksi_edit.240702
                        // if( res.cardCnt == 0 ){
						// 	redirectUrlMsg = "현재 고객님은 결제카드 미등록 상태입니다.\n결제카드 관리 화면으로 이동하겠습니다. ";
						// 	redirectYn = true;
							
						// } else if( res.mainCnt == 0 ){
						// 	redirectUrlMsg = "현재 고객님은 주 결제카드 설정이 안되어있습니다.\n결제카드 관리 화면으로 이동하겠습니다. ";
						// 	redirectYn = true;
							
						// }
						
						if( redirectYn ){
							if(confirm(redirectUrlMsg)){
								fn_setCookie("mainRedirect", "true", 1);
								location.href='/app/payment/payment';
								
							}
						} else {
							if( id == "btnChargeEnd") {
				                if( confirm("원격 충전을 시작하시겠습니까?")) {
				                    prograssbarOpen();

				                    param.popCsId = "${csPkId}";
				                    param.popId = "${cpPkId}";
				                    param.modelId = "${cpModelId}";
				                    param.chargeBoxSerialNumber = "${chargeBoxSerialNumber}";
				                    param.idTag = "${idToken}";
				                    param.connectorId = $("#connectorId").val();
				                    param.controlType = "RemoteStartTransaction";
				                    param.controlReason = "원격 충전 시작";
				                    param.controlCause = "CAUSE1";

				                    $.ajax({
				                        url : "/app/charge/remote_charge_control",
				                        type : 'post',
				                        data : param,
				                        //async : false,
				                        success : function(res) {
				                            if (res.successYN == "Y") {
				                                param.stationName = "${stationName}";
				                                param.chargerName = "${chargerName}";

				                                popOpen();
				                            } else {
				                                if (res.errCd == "DuplicateKey") {
				                                    alert("중복된 토큰값입니다.");
				                                } else if (res.errCd == "InUse") {
				                                    alert("이미 충전중입니다.");
				                                } else if (res.errCd == "cpNotConnected") {
				                                    alert("충전기가 연결되어 있지 않습니다.");
				                                } else {
				                                    alert("충전 시작에 실패했습니다. 관리자에게 문의하세요.");
				                                }
				                            }
				                        },
				                        complete: function() {
				                            prograssbarClose();
				                        }
				                    });
				                }
				            }
						}
					}
				},
				complete: function() {
					prograssbarClose();
				}
           });
        });

        //setConnectorList();
	});

    /* function setConnectorList() {
        var DataParam = {};
        DataParam.csId = $("#csId").val();
        DataParam.cpId = $("#cpId").val();
        DataParam.modelId = $("#modelId").val();
        DataParam.connectorId = $("#connectorId").val();

        $.ajax({
            url: "/app/charge/connector_list",
            type: 'post',
            data: DataParam,
            success: function (res) {
                $("#connectorList").empty();
                var html = "";

                res.forEach(function(item) {
                    html += '<option value='+item.connectorId+'>'+item.name+'</option>';
                });

                $("#connectorList").append(html);
            }
        });
        
        $("#connectorList").val("3").trigger('change');
    } */

function popOpen() {
    var popup_height = $(".popupWrap").height()/2;
    $(".popupWrap").css("margin-top",-popup_height);
    $(".popupWrap, .mask").show();
    document.querySelector('header').style.position = 'static';

    startTimer(60, document.getElementById('timer'));
}

    function startTimer(duration, display) {
        var timer = duration, minutes, seconds;
        var interval = setInterval(function () {
            minutes = parseInt(timer / 60, 10);
            seconds = parseInt(timer % 60, 10);

            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;

            display.textContent = minutes + ":" + seconds;

            if (timer % 1 === 0) {
                $.ajax({
                    url: "/app/charge/charge_possible?cpId=" + cpId,
                    type: 'get',
                    success: function (res) {
                        if (res.result == "charging") {
                            location.href = "/app/charge/charge_now";

                        } else if (res.result == "preparing") {
                            if (preparingYn == "N") {
                                preparingYn = "Y";
                                $("#popupHeader").html("충전기 커넥터가 연결되었습니다. <br> 충전기와 차량을 연결 중입니다.");
                                clearInterval(interval);
                                startTimer(60, document.getElementById('timer'));
                            }
                        }
                    }
                });
            }

            if (--timer < 0) {
                clearInterval(interval); // Stop the timer
                endTime();
            }
        }, 1000);
    }

    function endTime() {
        $(".popupWrap, .mask").hide();
        document.querySelector('header').style.position = 'relative';

        alert("커넥터 연결 시간이 지났습니다. 다시 충전을 진행해 주시기 바랍니다.");
    }
</script>

<div class="contents-wrap bgGray cont-padding-typeA">
    <div class="chargeState_Wrap">
        <div style="width:99%; height: 300px; object-fit: contain; margin-bottom: 1rem; border: 2px solid;">
            <div id="map" style="width:100%; height: 100%;"></div>
        </div>

        <!-- 충전현황정보 :s -->
        <div class="table-wrap">
            <table class="basic_table2">
                <caption>충전현황정보</caption>
                <colgroup>
                    <col style="width:40%">
                    <col style="width:60%">
                </colgroup>
                <tbody>
                    <tr>
                        <th>충전소</th>
                        <td><span style="margin-left: 16px;">${stationName}</span></td>
                    </tr>
                    <tr>
                        <th>충전기</th>
                        <td><span style="margin-left: 16px;">${chargerName}</span></td>
                    </tr>
                    <tr>
                        <th>충전기 모델</th>
                        <td><span style="margin-left: 16px;">${chargerModelName}</span></td>
                    </tr>
                    <tr>
                        <th>충전요금 단가</th>
                        <td><span style="margin-left: 16px;">${unitPrice} 원</span></td>
                    </tr>
                    <tr>
                        <th>커넥터</th>
                        <td><span style="margin-left: 16px;">${connectorInfo.name}</span></td>
                        <!-- <td class="select-wrap">
                            <select class="select-one select-h40" style="width: 62%;" id="connectorList"></select>
                        </td> -->
                    </tr>
                </tbody>
            </table>
            <form method="post" name="frm" >
            	<input type="hidden" name="csId" id="csId" value="${csId}" />
            	<input type="hidden" name="cpId" id="cpId" value="${cpId}" />
            	<input type="hidden" name="modelId" id="modelId" value="${cpModelId}" />
            	<input type="hidden" name="connectorId" id="connectorId" value="${connectorId}" />
            </form>
        </div>
        <!-- 충전현황정보 :e -->

        <!-- 충전종료 버튼 :s -->
        <div class="btn-wrap" style="text-align: center;">
            <button type="submit" class="btn-one btn-h60 btn-gradient btn-rectangle" id="btnChargeEnd" style="width: 66%;">원격 충전 시작</button>
        </div>
        <!-- 충전종료 버튼 :e -->
    </div>
</div>
<!-- popup :s -->
<div>
    <input type="hidden" name="popUserId" id="popUserId"/>
    <div class="popupWrap">
        <h3 id="popupHeader">커넥터를 연결하고 기다려 주세요.</h3>
        <div style="margin-top: 5%; text-align: center; font-size: 20px;">
            <p>
                <span id="timer">01:00</span>
            </p>
        </div>
    </div>
    <div class="mask"></div>
</div>
<!-- popup :e -->

