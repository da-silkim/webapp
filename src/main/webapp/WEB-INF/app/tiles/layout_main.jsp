<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<!DOCTYPE html>
<html lang="ko">
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<meta name="google-site-verification" content="YEnqXLtMU0yLMhEE-bkqVU8Yljj1JcBkis5XYDX3dnU" />
<link rel="shortcut icon" href="/resources/icon/favicon_dongah.ico" id="faviconHref" />
<!-- <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css"> -->
<%
    String router = request.getParameter("router");
%>
<body>

<script>
    var _tryCnt = 0;
    var vChargeBoxSerial ="";
    var vConnectorId ="";
    let publicLatitude = "36.86286585496495";
    let publicLongitude = "127.14776055108835";

    $(document).ready(
        function() {
            $("#imgLogo").append("<h1><img src='/resources/images/dongah-white-v2.png' alt='DonAh' /></h1>");

            //예약현황 체크
            //paymentCheck();
            reseverCheck();
        }
    )

    function selectCp() {
        _param = {
            "csId": $("#comboCs option:selected").val()
        };
        gf_send( "/selectCp" , _param, selectCpCallBack );
    }

    function selectCpCallBack( data ) {
        $("#comboCp").empty();
        var ell = '';
        $(data.cpList).each(function(i, item){
            ell += "<option value='"+item.chargeBoxSerialNumber+"'>" + item.name + "</option>";
        });
        $("#comboCp").append(ell);
    }

    function goResPage() {
        location.href = "/mobile/chargeReservation";
    }


    function paymentCheck() {
        if( $("#mainCheck").text() == "false" )
            gf_send( "/json/mobile/payment/chkPaymentReg", "" , paymentCheckcallback ) ;
    }

    function paymentCheckcallback( data ) {
        if( data.result == 0 ) {
            alert("현재 고객님은 결제카드 미등록 상태입니다.\n결재관리 화면으로 이동합니다.");
            location.href = "/mobile/payment";
            return false;
        }else{
            //reseverCheck();
        }
    }

    function reseverCheck() {
        if( $("#mainCheck").text() == "false" )
            gf_send( "/json/mypage/chargestate/check", "" , callback ) ;
    }

    function callback( data ) {
        if( data.result > 0 ) {
            location.href = "/mobile/charge_state";
            return false;
        }
    }

    function closeWin() {
        if ( $('input:checkbox[id="notice"]').is(":checked") == true ) {
            setCookie("popup", "no" , 1);
        }
    }

    function ready() {
        alert("서비스 준비중입니다.");
        return;
    }
</script>
<tiles:insertAttribute name="header"/>
<div id="m-wrap">
    <span id="mainCheck" id="mainCheck" style="display:none;"></span>
    <input id="serviceCompany" id="serviceCompany" type="hidden"  />
    <div id="m-wrap">
        <tiles:insertAttribute name="content"/>
        <div class="mask"></div>
        <!-- popup :e -->
    </div>
    <footer id="mobileTemplateFooter">
        <tiles:insertAttribute name="navi"/>
        <tiles:insertAttribute name="footer"/>
    </footer>
</div>
</body>
</html>
