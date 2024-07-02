<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	$(document).ready(function () {
        var chargeSpeedType = "${chargeSpeedType}";
        var soc = "${soc}";
        var unitPrice = "${unitPrice}";
        var error = "${error}";
        var chargePower = ${chargePower};
        var price = ${price};

        if (chargePower) {
            let number = chargePower;
            let power = number.toFixed(2);

            $("#chargePower").text(power);
        }

        if (price) {
            // 소수점 두번째 자리까지
            let number = price;
            price = number.toFixed(2);
            // 3자리마다 , 추가
            var formatPrice = $formatNumber(price);

            $("#price").text(formatPrice);
        }

        if (error) {
            alert("현재 충전중이 아니라 메인으로 이동합니다.");
            location.href = "/app/main";
        }

        setData(chargeSpeedType, soc);

        $('.chargeRate_bg').easyPieChart({
            barColor: function (percent) {
                var startColor = [5, 242, 242]; // RGB values for #B4DFE9
                var endColor = [3, 57, 166];  // RGB values for #0C419A

                var r = Math.round(startColor[0] + (endColor[0] - startColor[0]) * percent / 100);
                var g = Math.round(startColor[1] + (endColor[1] - startColor[1]) * percent / 100);
                var b = Math.round(startColor[2] + (endColor[2] - startColor[2]) * percent / 100);

                return 'rgb(' + r + ', ' + g + ', ' + b + ')';
            },
            trackColor: '#e5e5e5',  // 차트가 그려지는 트랙의 기본 배경색(chart1 의 회색부분)
            lineCap: 'round', // 차트 선의 모양 chart1 butt / chart2 round / chart3 square
            lineWidth: 35, // 차트 선의 두께
            size: 300, // 차트크기
            animate: 2000, // 그려지는 시간
            onStart: $.noop,
            onStop: $.noop
        });


        $(document).on("click", "#btnChargeEnd", function(e) {
            e.preventDefault();
            var id = $(this).attr("id");

            if( id == "btnChargeEnd") {
                if( confirm("원격 충전을 종료하시겠습니까?")) {
                    prograssbarOpen();

                    var param = {};
                    param.controlType = "RemoteStopTransaction";
                    param.chargeBoxSerialNumber = "${chargeBoxSerialNumber}";


                    $.ajax({
                        url : "/app/charge/remote_charge_control",
                        type : 'post',
                        data : param,
                        success : function(res) {
                            if (res.successYN == "Y") {
                                alert("원격 충전이 종료되었습니다.");

                                location.href = "/app/main";
                            } else {
                                alert("원격 충전 종료에 실패했습니다. 관리자에게 문의해 주세요.");
                            }
                        },
                        complete: function() {
                            prograssbarClose();
                        }
                    });
                }
            }
        });

        function setData(type, percent) {
            var el ='';
            el += '<div class="chargeRate_Wrap">';
            el += ' <div class="chargeRate_subWrap">';
            el += '     <div class="chargeRate_thirdWrap">';
            if(type == "L") {
                el += '     <div class="chargeRate_bg" data-percent="' + 100 + '"></div>';
                el += '     <div class="white-bg"></div>';
                el += '         <p class="chargRate_number"><span>완속</span></p>';
            } else {
                el += '     <div class="chargeRate_bg" data-percent="' + percent + '"></div>';
                el += '     <div class="white-bg"></div>';
                el += '         <p class="chargRate_number"><span>' + percent + '</span> %</p>';
            }
            el += '     </div>';
            el += ' </div>';
            el += '</div>';

            $("#chargeRate_Wrap").html(el);

            var el2 ='';

            if(type == "F") {
                var remainStopTs = "${remainStopTs}";

                el2 += '     <th>충전 시작 시간</th>';
                el2 += '     <td id="endTimestamp">' + remainStopTs + '</td>';
            }

            $("#chargingEndTime").html(el2);
        }

        function callApi() {
            $.ajax({
                url: "/app/charge/charging_info_refresh",
                type: 'get',
                success: function (res) {
                    // 소수점 두번째자리까지 표현
                    //let number = res.chargePower;
                    //let power = number.toFixed(2);
                    // , 추가
                    //var price = unitPrice * res.chargePower;
                    //var formatPrice = $formatNumber(price);
                    if(isNull(res)){
                    	alert("충전이 종료되었습니다. 메인화면으로 이동하겠습니다.");
                    	location.href = "/app/main";
                    }

                    $("#chargePower").text(res.chargePower);
                    //$("#price").text(formatPrice);

                    if (chargeSpeedType != "L") {
                        setData(chargeSpeedType, res.soc)
                    }
                }
            });
        }

        setInterval(callApi, 10000);
        
        function triggerMsgCallApi() {
        	var param = {};
            param.chargeBoxSerialNumber = "${chargeBoxSerialNumber}";
        	param.connectorId = "${connectorId}";
            $.ajax({
                url : "/app/charge/triggerMessage",
                type : 'post',
                data : param,
                //async : false,
                success : function(res) {
                    
                },
                complete: function() {
                }
            });
        }

        setInterval(triggerMsgCallApi, 30000);
        
    });
</script>
<div class="contents-wrap bgGray cont-padding-typeA">

    <div class="chargeState_Wrap">

        <!-- 충전률 :s -->
        <div id="chargeRate_Wrap">
        </div>
        <!-- 충전률 :e -->

        <!-- 충전현황정보 :s -->
        <div class="table-wrap">
            <table class="basic_table2">
                <caption>충전현황정보</caption>
                <colgroup>
                    <col style="width:52%">
                    <col style="width:48%">
                </colgroup>
                <tbody>
                <tr>
                    <th>충전 시작 시간</th>
                    <td id="startTimestamp">${startTimestamp}</td>
                </tr>
                <tr id="chargingEndTime"></tr>
                <tr>
                    <th>충전소</th>
                    <td id="stationName">${stationName}</td>
                    <input type="hidden" id="csId" value="${csId}">
                </tr>
                <tr>
                    <th>충전기</th>
                    <td id="chargerName">${chargerName}</td>
                    <input type="hidden" id="cpId" value="${cpId}">
                    <input type="hidden" id="modelId" value="${modelId}">
                    <input type="hidden" id="connectorId" value="${connectorId}">
                </tr>
                <tr>
                    <th>실시간 충전량</th>
                    <td><span id="chargePower"></span>(kWh)</td>
                </tr>
                <tr>
                    <th>예상 충전금액</th>
                    <td><span id="price"></span> 원</td>
                </tr>
                </tbody>
            </table>
        </div>
        <!-- 충전현황정보 :e -->

        <!-- 충전종료 버튼 :s -->
        <div class="btn-wrap">
            <button type="submit" class="btn-one btn-h60 btn-gradient btn-rectangle" id="btnChargeEnd">충전 종료</button>
        </div>
        <!-- 충전종료 버튼 :e -->

    </div>

</div>