<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
    const userId = "${userid}";
    const userName = "${username}";

    $(document).ready(function () {
        var param = {};
        var lat;
        var lon;
        var distance = 5; // 거리 기본값
        param.distance = 5;
        param.userId = userId;

        let cookie_lat = getCookie("init_lat");
        let cookie_lng = getCookie("init_lng");
        if( cookie_lat ){
            param.lat = cookie_lat;
            param.lon = cookie_lng;

            stationList(param);
        } else {
            param.lat = 36.862879347070475;
            param.lon = 127.14778020220608;

            stationList(param);
        }
        /*if (navigator.geolocation) {

            // GeoLocation을 이용해서 접속 위치를 얻어옵니다
            navigator.geolocation.getCurrentPosition(function(position) {

                //TODO SSL 설치 후 연동
                lat = position.coords.latitude; // 위도
                lon = position.coords.longitude; // 경도

                param.lat = lat;
                param.lon = lon;
                param.distance = 5; // 거리 기본값
                param.userId = userId;

                stationList(param);
            });

        } else { // HTML5의 GeoLocation을 사용할 수 없을때
            var locPosition = new daum.maps.LatLng(37.65034152681641, 126.79456522115323);

            map.setCenter(locPosition);

            //TODO SSL 설치 후 연동 제거 시작
            lat = 37.65034152681641;
            lon = 126.79456522115323;

            param.lat = lat;
            param.lon = lon;
            param.distance = 5; // 거리 기본값 5 -> 2

            stationList(param);
            //TODO SSL 설치 후 연동 제거 종료
        }*/

        $("#selDistance").change(function(){
            $("#selDistance").prop("selected", true);
            param.distance = $("#selDistance option:selected").val();

            stationList(param);
        });

        $(document).on("click", ".favReg", function(){
            var name = $(this).attr("name");
            var csId = $(this).attr("data-id");

            if ( userId ) {
                $.ajax({
                    url:"/app/find/favorites_add",
                    type:"get",
                    dataType:"json",
                    data:{csId:csId, userId: userId},
                    success:function(data){
                        if(data.result == "success"){
                            $("button[name="+name+"]").attr("class", "btn-bookmark favDel");
                            $("button[name="+name+"]").attr("data-id", data.favId);
                            $("button[name="+name+"]").find("img").attr("src","/resources/images/bookmark_on.png");

                            stationList(param);
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

        $(document).on("click", ".favDel", function(){
            var name = $(this).attr("name");
            var id = $(this).attr("data-id");
            var csId = $(this).attr("data-csId");

            if ( userId ) {
                $.ajax({
                    url:"/app/find/favorites_delete",
                    type:"get",
                    dataType:"json",
                    data:{favId:id,csId:csId},
                    success:function(data){
                        if(data.result.message == "success"){
                            $("button[name="+name+"]").attr("class", "btn-bookmark favReg");
                            $("button[name="+name+"]").removeAttr("data-id");
                            $("button[name="+name+"]").find("img").attr("src","/resources/images/bookmark_off.png");

                            stationList(param);
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

        function stationList(param) {
            $.ajax({
                url:"/app/find/near_by_station_list",
                type:"get",
                dataType:"json",
                data:param,
                success:function(data){
                    var csInfo = data.list;
                    createCsDiv(csInfo);
                }
            });
        }

        function createCsDiv(info){
            $("#csList").html("");

            if(info.length > 0){
                for(var i=0; i<info.length; i++){
                    var html = "";
                    html += '<div class="station-info-wrap list">';
                    html += '<div class="info-subwrap">';
                    html += '<div class="tit">';

                    html += '<p class="station-name">';
                    html += '<span>'+info[i].name+'</span>';
                    if(info[i].favState > 0){
                        html += '<button type="button" class="btn-bookmark favDel" name="favDel_'+(i+1)+'" data-id="'+info[i].favState+'" data-csId="'+info[i].id+'"><img src="/resources/images/bookmark_on.png" alt="즐겨찾기"></button>';
                    } else {
                        html += '<button type="button" class="btn-bookmark favReg" name="favReg_'+(i+1)+'" data-id="'+info[i].id+'"><img src="/resources/images/bookmark_off.png" alt="즐겨찾기"></button>';
                    }

                    html += '</p></div>';

                    html += '<ul class="detail">';
                    html += '<li class="distance"><span>'+info[i].distance+' km</span></li>';
                    html += '<li class="address">'+info[i].fullAddr+'</li>';

                    if(info[i].parkingFeeYn == "Y"){
                        html += '<li class="parkPrice">주차요금 : <span>'+info[i].parkingFee+' 원</span></li>';
                    } else{
                        html += '<li class="parkPrice">주차요금 : <span>없음</span></li>';
                    }

                    var _openStartTime = "00:00"
                    var _openEndTime = "00:00"
                    if(info[i].openStartTime != null && info[i].openStartTime.length == 4){
                        _openStartTime = info[i].openStartTime[0] + info[i].openStartTime[1] + ':' + info[i].openStartTime[2] + info[i].openStartTime[3];
                    }
                    if(info[i].openEndTime != null && info[i].openEndTime.length == 4){
                        _openEndTime = info[i].openEndTime[0] + info[i].openEndTime[1] + ':' + info[i].openEndTime[2] + info[i].openEndTime[3];
                    }
                    html += '<li class="time">운영시간 : '+_openStartTime+' ~ '+_openEndTime+'</li>';
                    html += '<li class="charType">';
                    if(info[i].cpCount1 > 0){
                        html +=		'<span>'+info[i].cpType1+' '+'<span class="num">'+info[i].cpCount1+'</span><span>기 </span></span>';
                    }
                    if(info[i].cpCount2 > 0){
                        html +=		'<span>'+info[i].cpType2+' '+'<span class="num">'+info[i].cpCount2+'</span><span>기 </span></span>';
                    }
                    if(info[i].cpCount3 > 0){
                        html +=		'<span>'+info[i].cpType3+' '+'<span class="num">'+info[i].cpCount3+'</span><span>기 </span></span>';
                    }
                    if(info[i].cpCount4 > 0){
                        html +=		'<span>'+info[i].cpType4+' '+'<span class="num">'+info[i].cpCount4+'</span><span>기 </span></span>';
                    }
/*                     if(info[i].cpCount1 > 0){
                        html +=		'<span>'+info[i].cpType1+' ('+info[i].cpPrice+'원/kWh)<span class="num">'+info[i].cpCount1+'</span><span>기 </span></span>';
                    }
                    if(info[i].cpCount2 > 0){
                        html +=		'<span>'+info[i].cpType2+' ('+info[i].cpPrice+'원/kWh)<span class="num">'+info[i].cpCount2+'</span><span>기 </span></span>';
                    }
                    if(info[i].cpCount3 > 0){
                        html +=		'<span>'+info[i].cpType3+' ('+info[i].cpPrice+'원/kWh)<span class="num">'+info[i].cpCount3+'</span><span>기 </span></span>';
                    }
                    if(info[i].cpCount4 > 0){
                        html +=		'<span>'+info[i].cpType4+' ('+info[i].cpPrice+'원/kWh)<span class="num">'+info[i].cpCount4+'</span><span>기 </span></span>';
                    } */
                    html += '</li></ul>';

                    html += '<div class="btn-wrap flex">';
                    html += '<a href="/app/find/find_map?id='+info[i].id+'" class="btn-three btn-h40 btn-color-main1">지도 보기</a>';
                    html += '<a href="/app/find/cs_detail?id='+info[i].id+'" class="btn-three btn-h40 btn-color-main1 reverse">상세 보기</a>';
                    html += '<a href="/app/customer/my_voc?csId='+info[i].id+'" class="btn-three btn-h40 btn-color-main1">고장 신고</a>';
                    html += '</div>';
                    html += '</div></div>';

                    $("#csList").append(html);
                }
            } else {
                var html = "";
                html += '<div style="position:absolute; top:50%; left:50%; transform:translate(-50%, -50%);"><img src = "/resources/images/no-data.png">';
                html += 	'<p style="margin-top:10px;text-align:center;font-size: 15px;"> 검색결과가 없습니다. </p>';
                html += '</div>';
                $("#csList").append(html);
            }
        }
    })
</script>
<!-- contents -->
<div class="contents-wrap cont-padding-typeA localStation-wrap">

    <!-- 20220211: as is 활용코드 :s -->

    <!-- 검색창 및 셀렉트박스 :s -->
    <div class="searchForm ">
        <div class="select-wrap">
            <select class="select-one select-h40 mb10" name="panel:selDistance" id="selDistance">
                <option selected="" value="5">5km</option>
                <option value="10">10km</option>
                <option value="15">15km</option>
            </select>
        </div>
    </div>
    <!-- //검색창 및 셀렉트박스 :e -->

    <!-- 충전소 리스트 :s -->
    <div id="csList">
    </div>
    <!-- //contents -->
</div>
