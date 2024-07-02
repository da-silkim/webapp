<%@page import="javax.crypto.BadPaddingException"%>
<%@page import="javax.crypto.IllegalBlockSizeException"%>
<%@page import="java.security.InvalidAlgorithmParameterException"%>
<%@page import="java.security.InvalidKeyException"%>
<%@page import="javax.crypto.NoSuchPaddingException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.IvParameterSpec"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="java.security.spec.AlgorithmParameterSpec"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.commons.codec.digest.DigestUtils"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="com.bluenetworks.webapp.common.PayUtil"%>
<%@page import="com.bluenetworks.webapp.common.SmartroPayUtil"%>
<%@page import="com.bluenetworks.webapp.common.StringUtils"%>
<%
/******************************************************************************
*	
*	@ SYSTEM NAME		: 간편결제 빌링키 테스트 응답 결과 페이지
*	@ PROGRAM NAME		: returnBillPay.jsp
*	@ MAKER				: 스마트로 PG개발실
*	@ MAKE DATE			: 2018.03.02
*	@ PROGRAM CONTENTS	: 
*
************************** 변 경 이 력 *****************************************
* 번호	작업자		작업일				변경내용
*	1	스마트로	2018.03.02			최초작성
*	2	yehwang    2020.02.05			연동규격서와 파라미터 맞춤
*******************************************************************************/
%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");

String merchantKey="6W+56/N221QLcqVp9kJsfGSUBsI5THbQ21OWLqlKGCfeQy57lFxn06mOFpKUD58Ux5uNLz/BOC+zqqr2Rk4RCA==";
String passWd ="SMARTRO!@#";
final String mKey = merchantKey.substring(0,32);

String key = "";
String value = "";

String encBillTokenKey = request.getParameter("BillTokenKey");
String MID = request.getParameter("Mid");
String DisplayCardNo = StringUtils.cleanXSS(request.getParameter("DisplayCardNo"));
String ResultCode    = StringUtils.cleanXSS(request.getParameter("ResultCode"));	
String VerifyValue    = StringUtils.cleanXSS(request.getParameter("VerifyValue"));
String CardExpire    = StringUtils.cleanXSS(request.getParameter("CardExpire"));
String IssuerCardNm    = StringUtils.cleanXSS(request.getParameter("IssuerCardNm"));
String IssuerCardCd = StringUtils.cleanXSS(request.getParameter("IssuerCardCd"));
String ResultMsg    = StringUtils.cleanXSS(request.getParameter("ResultMsg"));	

String BillTokenKey = null;

System.out.println("ResultCode :" + ResultCode);
System.out.println("VerifyValue :" + VerifyValue);
System.out.println("DisplayCardNo :" + DisplayCardNo);
System.out.println("CardExpire :" + CardExpire);
System.out.println("IssuerCardNm :" + IssuerCardNm);
System.out.println("IssuerCardCd :" + IssuerCardCd);
System.out.println("ResultMsg :" + ResultMsg);

if( ResultCode != null && ResultCode.equals("3001") ) {
	if (encBillTokenKey != null && mKey != null) {
		BillTokenKey = PayUtil.AESDecode(encBillTokenKey, mKey);
	}
	
	System.out.println("BillTokenKey :" + BillTokenKey);
	
	
	//위변조 검증 처리
	String tempVerifyValueString = MID + BillTokenKey + DisplayCardNo + ResultCode + passWd;
	//System.out.println("tempVerifyValueString :" + tempVerifyValueString);
	
	String tempVerifyValue = SmartroPayUtil.encodeSHA256Base64(tempVerifyValueString);	
	//System.out.println("tempVerifyValue :" + tempVerifyValue);
	
	Boolean result = tempVerifyValue.equals(VerifyValue) ? true : false;
	//System.out.println("result :" + result);
			
	//빌링토큰키 AES456 복호화 처리
	
}

%> 
<html>
<head>
	<link rel="stylesheet" href="/resources/css/fakeLoader.min.css">
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script type="text/javascript" src="/resources/js/fakeLoader.min.js"></script>
	<script type="text/javascript" src="/resources/js/ecsc_util.js"></script>
	<script type="text/javascript" src="/resources/js/common.js"></script>
</head>
<script>
$(document).ready(function() {
	
	fakeLoader();
	
	let resultCode = "<%=ResultCode%>";
	let resultMsg = "<%= ResultMsg%>";
	
	let _param = {};
	_param.cardNo = "<%= DisplayCardNo %>";
	_param.issuerCardCd = "<%= IssuerCardCd %>";
	_param.billTokenKey = "<%= BillTokenKey %>";
	_param.cardAlias = "<%= IssuerCardNm %>";
	_param.validYymm = "<%= CardExpire %>";
		
	if( "3001" == resultCode ){
		$("[name=result]").val("success");
		callAjax("/app/payment/billTokenUpdate", _param, ajaxCustomSucessCallBack);
		
	} else {
		//alert("카드 등록에 실패되었습니다");
		location.href="/app/payment/payment";
	}
	
	
});

function ajaxCustomSucessCallBack(res){
	alert("카드가 등록 되었습니다");
	location.href="/app/payment/payment";
	
}


</script>
<div class="fakeLoader">
</div>
</html>
