<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
    .basic_table3 {
        width:150%;
    }
    .wrapper-scroll{
        overflow-x:auto;
    }
    .tg-scroll{
        min-width: 40rem;
    }
</style>

<script>
    var userId = "${userid}";

    $(function() {
        var chargeHist = (function(){

            var startDate = new Date();
            startDate.setDate (startDate.getDate() - 6);
            $("#chargehistoryDate1").datepicker().datepicker("setDate", startDate);
            $("#chargehistoryDate2").datepicker().datepicker("setDate", new Date());

            var obj = {};
            var url = "/app/charge/charge_history_list";

            var currentYear = (new Date()).getFullYear();
            var startYear = currentYear-5;
            var html = "";

            for(var i = currentYear; i >= startYear; i--){
                html +="<option value="+i+">"+i+"년</option>";
            }

            $("#year").html(html);

            var getParam = function(){
                var param = {};
// 			var year = $("#year option:selected").val();
// 			param.year = year;
                param.begin = $("#chargehistoryDate1").val();
                param.end = $("#chargehistoryDate2").val();
                param.userId = userId;

                return param;
            };

            var ajax = function(){
                var param = getParam();
                $.ajax({
                    url : url
                    ,type : "get"
                    ,dataType : "json"
                    ,data : param
                    ,success : function(data){
                        var html = "";
                        if(data.rows.length > 0){
                            //일시, 시작시간, 종료시간, 사용시간, 사용량, 요금)
                            data.rows.forEach(function(item){
                                html += "<tr>";
                                html += "<td>" + item.csName + "</td>";
                                html += "<td>" + item.begin + "</td>";
                                html += "<td>" + item.end + "</td>";
                                html += "<td>" + item.unixTime + "</td>";
                                html += "<td>" + item.chargeAmount + "</td>";
                                html += "<td class='minusPoint'>" + item.totalPrice + "</td>";
                                html += "<td>" + Math.round(item.discountPrice) + "</td>";
                                html += "<td>" + item.paymentPrice + "</td>";
                                html += "</tr>";
                            });
                        }else{
                            html += "<tr><td colspan='8' style='text-align:center;'>충전 내역이 존재하지 않습니다.</td></tr>";
                        }
                        $("#cardHistBody").html(html);

                        $("#totalChargeTime").text(data.total.totalChargeTime);
                        $("#totalChargeAmount").text(data.total.totalChargeAmount);
                        $("#totalChargePrice").text(data.total.totalChargePrice);
                        $("#totalDiscountPrice").text(data.total.totalDiscountPrice);
                        $("#totalPaymentPrice").text(data.total.totalPaymentPrice);
                    }
                })
            }
            obj.getParam = getParam;
            obj.ajax = ajax;
            return obj;
        })();


        chargeHist.ajax();

        $("#btnSearch").click(function(){
            chargeHist.ajax();
        });

    });
</script>
<div id="content" class="chargeAdminWrap">
    <!-- contents -->
    <div class="contents-wrap cont-padding-typeA hisCharge-wrap">

        <!-- 20220209: as is 활용코드 :s -->
        <form action="" method="post">

            <!-- 상단요약 :s -->
            <div class="summary-wrap">
                <ul class="guide-box2">
                    <li>전기차 충전서비스를 이용한 내역을 확인하실 수 있습니다.</li>
                    <!-- <li>1년 이상의 내역은 조회가 불가능 할 수 있습니다.</li> 홈앤서비스 요청 주석처리 kjk -->
                </ul>
            </div>
            <!-- //상단요약 :s -->
            <!-- 본문 evWrap :s -->
            <!-- 검색영역 :s -->
            <div class="search-wrap">
                <div class="dateSearch">
                  <span class="calendarWrap">
                    <span class="date-pick-wrap"><input type='text' id='chargehistoryDate1' class="input-h40 date-pick" name='chargehistoryDate' readonly="readonly"></span>
                    <span class="date-pick-wave">~</span>
                    <span class="date-pick-wrap"><input type='text'  id='chargehistoryDate2' class="input-h40 date-pick" name='chargehistoryDate' readonly="readonly"></span>
                  </span>
                    <div class="btn-wrap">
                        <button type="button" class="btn-one btn-h40 btn-color-sub1" id="btnSearch">조회</button>
                    </div>
                </div>
            </div>
            <!-- 검색영역 :e -->

            <!-- 충전이력 리스트 :s -->
            <div class="chargeHisList-wrap wrapper-scroll">
                <table class="basic_table3 tg-scroll">
                    <colgroup>
                        <col style="width:20%">
                        <col style="width:15%">
                        <col style="width:15%">
                        <col style="width:10%">
                        <col style="width:10%">
                        <col style="width:10%" class='minusPoint'>
                        <col style="width:10%">
                        <col style="width:10%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>충전소<br>명</th>
                        <th>시작<br>시간</th>
                        <th>종료<br>시간</th>
                        <th>사용<br>시간(분)</th>
                        <th>사용량<br>(kW)</th>
                        <th>충전금액<br>(원)</th>
                        <th>할인금액<br>(원)</th>
                        <th>결제금액<br>(원)</th>
                    </tr>
                    </thead>
                    <tbody id="cardHistBody">
                    <tr>
                        <td id=csName></td>
                        <td id=begin></td>
                        <td id=end></td>
                        <td id=unixTime></td>
                        <td id=chargeAmount></td>
                        <td id=totalPrice></td>
                        <td id=discountPrice></td>
                        <td id=paymentPrice></td>
                    </tr>
                    </tbody>
                    <tfoot>
                    <tr>
                        <th colspan="3">합계</th>
                        <td id="totalChargeTime"></td>
                        <td id="totalChargeAmount"></td>
                        <td id="totalChargePrice"></td>
                        <td id="totalDiscountPrice"></td>
                        <td id="totalPaymentPrice"></td>
                    </tr>
                    </tfoot>
                </table>
                <!--충전이력리스트-->
            </div>
        </form>
    </div>
</div>