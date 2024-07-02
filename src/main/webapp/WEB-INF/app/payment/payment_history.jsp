<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
    .basic_table3 {
        width:100%;
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

    var myGrid = null;
    $(function() {

        var startDate = new Date();
        startDate.setDate (startDate.getDate() - 6);
        $("#paymentDate1").datepicker().datepicker("setDate", startDate);
        $("#paymentDate2").datepicker().datepicker("setDate", new Date());
        $("#btnSearch").click(function(){
            paymentHist.init(1);
        });

        var opt = {
            gridId : "paymentHistBody"
            ,pageId : "dataGridPageNavi"
        };
        myGrid = gf_initMyGrid(opt);
        paymentHist.init(1);

    });
    var paymentHist = (function(){

        var obj = {};
        var page = 1;
        var contentHtml;

        var currentYear = (new Date()).getFullYear();
        var startYear = currentYear-5;
        var html = "";

        for(var i = currentYear; i >= startYear; i--){
            html +="<option value="+i+">"+i+"년</option>";
        }

        $("#year").html(html);

        var getParam = function(pageNo){
            var param = {};
            param.paymentDate1 = $("#paymentDate1").val();
            param.paymentDate2 = $("#paymentDate2").val();
            param.currPageNo = pageNo;
            param.searchType = $("#sysSelect").val();
            param.userId = userId;
            return param;
        };

        var ajax = function(pageNo){
            var param = getParam(pageNo);
            $.ajax({
                url : "/app/payment/payment_history_list"
                ,type : "get"
                ,dataType : "json"
                ,data : param
                ,success : function(data){
                    var listInfo = data.rows;
                    var pageInfo = data.pageInfo;

                    var html = "";

                    if( listInfo.length == 0 ) {
                        $("#dataGridPageNavi").html('');
                        el = '<tr><td colspan=5>결제 내역이 존재하지 않습니다</td></tr>';
                        $("#paymentHistBody").html(el);
                        return false;
                    }

                    data.rows.forEach(function(item){
                        html += "<tr>";
                        html +=     "<td>" + item.authTimestamp + "</td>";
                        html +=     "<td>" + $formatNumber(item.authAmount) + "</td>";
                        html +=     "<td>" + item.name + "</td>";
                        html +=     "<td>" + item.power + " kWh</td>";
                        html += "</tr>";
                    });
                    contentHtml(html);

                    $("#totalPayment").text($formatNumber(data.totalAmount));

                    if(listInfo.length != 0){
                        myGrid.makePageNvai(pageInfo, "paymentHist.init");
                    }
                }
            })
        }
        var contentNew = function(html){
            $("#paymentHistBody").html(html);
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
        obj.search = search;
        return obj;
    })();
</script>
<div class="contents-wrap cont-padding-typeA hisPay-wrap">

    <!-- 20220209: as is 활용코드 :s -->
    <form action="" method="post">

        <!-- 상단요약 :s -->
        <div class="summary-wrap">
            <div class="summary-point">
                <table>
                    <caption>총 결제 금액</caption>
                    <colgroup>
                        <col style="width:30%">
                        <col style="width:55%">
                        <col style="width:25%">
                    </colgroup>
                    <tbody>
                    <tr>
                        <th>총 결제 금액</th>
                        <td id="totalPayment"></td><td>원</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <ul class="guide-box2">
                <li>실시간, 후불(월) 결제 시 자동 적용됩니다.</li>
            </ul>
        </div>
        <!-- //상단요약 :s -->
        <!-- 본문 evWrap :s -->
        <!-- 검색영역 :s -->
        <div class="search-wrap">
            <div class="dateSearch">
                <span class="tit">기간</span>
                <span class="calendarWrap">
                    <span class="date-pick-wrap"><input type="text" id="paymentDate1" class="input-h40 date-pick" name='paymentDate' readonly="readonly"></span>
                    <span class="date-pick-wave">~</span>
                    <span class="date-pick-wrap"><input type="text" id="paymentDate2" class="input-h40 date-pick" name='paymentDate' readonly="readonly"></span>
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
                    <col style="width:19%">
                    <col style="width:25%">
                    <col style="width:31%">
                    <col style="width:25%">
                </colgroup>
                <thead>
                <tr>
                    <th>일시</th>
                    <th>충전소</th>
                    <th>충전요금</th>
                    <th>충전량</th>
                </tr>
                </thead>
                <tbody id="paymentHistBody">
                </tbody>
            </table>
            <!--충전이력리스트-->
            <div class="pageNavi" id="dataGridPageNavi">
            </div>
        </div>
    </form>
</div>