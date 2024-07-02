<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
    var _param = {};
    const userId = "${userid}";
    const userName = "${username}";
    const csId = "${csId}";

    $(document).ready(function() {
        $("#csId").val(csId);
        $("#userId").val(userId);

        init();
    });

    function init() {
        $.ajax({
            url: "/app/find/cs_detail_data",
            type: 'post',
            data: {csId: csId, userId: userId},
            dataType:"json",
            success : function(res) {
                setData(res);
            }
        });
    }

    function setData( data ) {
        var hList = data.histList;
        var el='';

        el = '';
        $(hList).each( function( i , item ) {
            var formatDate1 = dateFormat(item.begin);
            var formatDate2 = dateFormat(item.end);

            el += '<tr>';
            el += '<td>' + nullToStr ( item.cpName ) + '</td>';
            el += '<td>' + formatDate1.date + '<br>' + formatDate1.time + '</td>';
            el += '<td>' + formatDate2.date + '<br>' + formatDate2.time + '</td>';
            el += '</tr>';
        });
        $("#hList").html(el);
    }

    // 충전기 List
    var myGrid = null;

    $(function() {
        var opt = {
            gridId : "cpList"
            ,pageId : "dataGridPageNavi"
        };
        myGrid = gf_initMyGrid(opt);
        CpList.init(1);
    });

    var CpList = (function(){

        var obj = {};
        var page = 1;
        var url = "/app/find/cp_list";
        var contentHtml;

        var getParam = function( pageNo ){
            var param = {};
            param.currPageNo = pageNo;
            param.userId = userId;
            param.csId = csId;
            return param;
        };

        var ajax = function( pageNo ){
            var param = getParam( pageNo );
            $.ajax({
                url : url
                ,type : "get"
                ,dataType : "json"
                ,data : param
                , success : function(data){
                    var listInfo = data.rows;
                    var pageInfo = data.pageInfo;

                    var html = "";

                    if( listInfo.length == 0 ) {
                        $("#dataGridPageNavi").html('');
                        el = '<tr><td colspan=4>데이터가 없습니다</td></tr>';
                        $("#cpList").html(el);
                        return false;
                    }

                    data.rows.forEach(function(item){
                        html += '<tr>';
                        html += '<td>' + item.cpId + '</td>';
                        html += '<td>' + item.connectorId + '</td>';
                        html += '<td>' + item.connectorName + '</td>';
                        html += '<td>' + item.unitPrice + '원 / kWh</td>';
                        html += '<td>' + item.status + '</td>';
                        html += '</tr>';
                    });

                    contentHtml(html);

                    if(listInfo.length != 0){
                        myGrid.makePageNvai(pageInfo, "CpList.init");
                    }
                }
            })
        }

        var contentAdd = function(html){
            $("#cpList").append(html);
        }
        var contentNew = function(html){
            $("#cpList").html(html);
        }

        var init = function(pageNo){
            contentHtml = contentNew;
            ajax( pageNo );
        }

        var search = function(){
            contentHtml = contentNew;
            ajax(1);
        }

        obj.init = init;
        return obj;
    })();

    function dateFormat(date) {
        var dateObject = new Date(date);

        // 날짜 및 시간 추출
        var year = dateObject.getFullYear();
        var month = (dateObject.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
        var day = dateObject.getDate().toString().padStart(2, '0');
        var hours = dateObject.getHours().toString().padStart(2, '0');
        var minutes = dateObject.getMinutes().toString().padStart(2, '0');
        var seconds = dateObject.getSeconds().toString().padStart(2, '0');

        // 원하는 형식으로 조합
        var formattedDate = year + "-" + month + "-" + day;
        var formattedTime = hours + ":" + minutes + ":" + seconds;

        return {date:formattedDate, time:formattedTime};
    }

    function brokenPage() {
        location.href = "/app/customer/my_voc?csId="+csId;
    }
</script>
<div class="contents-wrap cont-padding-typeA station-detail-wrap">
    <div>
        <div class="table-wrap" style="padding: 0 0 0 0;">
            <table class="basic_table3">
                <caption>충전기 현황</caption>
                <colgroup>
                    <col style="width:25%">
                    <col style="width:25%">
                    <col style="width:25%">
                    <col style="width:25%">
                </colgroup>
                <tbody>
                    <tr>
                        <td style="font-size: 1.2rem;">사용가능 : ${available}</td>
                        <td style="font-size: 1.2rem;">충전중 : ${charging}</td>
                        <td style="font-size: 1.2rem;">충전예약 : ${chargingReserve}</td>
                        <td style="font-size: 1.2rem;">고장/수리 : ${unAvailable}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <!-- 충전기 리스트 :s-->
        <div class="table-wrap">
            <input type="hidden" name="userId" id="userId"/>
            <input type="hidden" name="csId" id="csId"/>
            <div class="tableTit">
                <h2>충전기 리스트</h2>
            </div>
            <table class="basic_table3">
                <colgroup>
                    <col style="width:35%">
                    <col style="width:15%">
                    <col style="width:15%">
                    <col style="width:15%">
                    <col style="width:20%">
                </colgroup>
                <thead>
                <tr>
                    <th>충전기</th>
                    <th>커넥터</th>
                    <th>충전<br>방식</th>
                    <th>충전<br>요금</th>
                    <th>충전기<br>상태</th>
                </tr>
                </thead>
                <tbody id="cpList">
                </tbody>
            </table>
            <div class="pageNavi" id="dataGridPageNavi">
            </div>
        </div>
        <!-- 충전기 리스트 :e-->

        <!-- 상세정보 :s-->
        <div class="table-wrap">
            <div class="tableTit">
                <h2>상세 정보</h2>
            </div>
            <table class="basic_table2">
                <colgroup>
                    <col style="width:35%">
                    <col style="width:65%">
                </colgroup>

                <tbody id="list">
                <tr>
                    <th>주소</th>
                    <td id="address">${address}</td>
                </tr>
                <tr>
                    <th>운영사</th>
                    <td id="serviceName">${serviceName}</td>
                </tr>
                <tr>
                    <th>운영 시간</th>
                    <td id="operatingTime">${operatingTime}</td>
                </tr>
                <tr>
                    <th>주차 요금</th>
                    <td id="parkingFee">${parkingFee}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <!-- 상세정보 :e-->

        <!-- 최근 충전 이력 :s-->
        <div class="table-wrap">
            <div class="tableTit">
                <h2>최근 충전이력</h2>
            </div>
            <table class="basic_table3">
                <caption>최근 충전 이력</caption>
                <colgroup>
                    <col style="width:40%">
                    <col style="width:30%">
                    <col style="width:30%">
                </colgroup>
                <thead>
                <tr>
                    <th>충전기</th>
                    <th>충전시작</th>
                    <th>충전종료</th>
                </tr>
                </thead>
                <tbody id="hList">
                </tbody>
            </table>
        </div>
        <!-- 고장 접수/신고 :s -->
        <div class="table-wrap">
            <div class="tableTit">
                <h2>고장 접수/신고</h2>
            </div>
            <div class="btn-wrap">
                <div class="btn-one" style="text-align: center;">
                    <a href="#" class="btn-h40 btn-color-main1" onclick="brokenPage();" style="width: 50%;">고장 신고</a>
                </div>
            </div>
        </div>
    </div>
</div>
