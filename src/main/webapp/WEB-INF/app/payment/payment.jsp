<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMMdd" var="now" />
<script src="https://mpay.smartropay.co.kr/asset/js/SmartroPAY-1.0.min.js?version=${now}"></script>
<style>
	.delPayCard {
		position: absolute; top: 1rem; right: 3rem; display: inline-block; width: 2.48rem;
	}
</style>
<script>
    let cardList = {};
    $(document).ready(function(){
    	let swiper1 = new Swiper(".mySwiper1", {
    		pagination: {
				el: ".swiper-pagination",
				type: "progressbar",
			},
			pagination: {
		          el: ".swiper-pagination",
		          type: 'bullets',
		          clickable: true,
		        },
			on: {
				slideChange: function () {
					fn_changePayCard(this.realIndex);
				}
			}
		});
    	
    	fn_getCardList();
    });
    
    function fn_changePayCard(idx){
    	let index = idx+1;
    	let selCard = cardList.find(e => e.rownum === index);
    	if( typeof selCard != "undefined" ){
    		$.ajax({
    			url: "/app/payment/payment/changeMainCard",
    			type: 'post',
    			data: selCard,
    			async: false,
    			success : function(res) {
    			
    			},
    			error: function (res){
    				console.log("fail: ", res)
    			}
    		});
    	}
    }
    
    
    function fn_getCardList(){
    	$("#cardList").empty();
    	let cardHtml = "";
    	$.ajax({
			url: "/app/payment/payment/cardList",
			type: 'post',
			async: false,
			success : function(res) {
				cardList = res.cardList;
		    	for(i=0;i<cardList.length;i++){
		    		cardHtml += '<div id="'+ cardList[i].issuerCardCd +'" class="swiper-slide card-one after-addCard">';
		    		cardHtml += '	<div style="position: relative;" ><p class="cardTit">'+ cardList[i].cardAlias +'</p>';
		    		cardHtml += '	<p class="delPayCard" onclick="fn_cardDelete('+ cardList[i].cardId +');"><img src="/resources/images/blue/btn_delete.png" alt="카드삭제"></p></div>';
		    		cardHtml += '	<p class="cardMoney"><span class="cardPrice">'+ cardList[i].cardNo +'</span></p>';
		    		cardHtml += '</div>';
		    	}
			},
			error: function (res){
				console.log("fail: ", res)
			}
		});
    	
    	cardHtml += '<div class="swiper-slide card-one after-addCard" id="addCard">';
    	cardHtml += '	<a onclick="fn_cardAdd();" id="btnCardReg"><p class="addCard-img"><img src="/resources/images/btn_payment_addCard.png" alt="카드 추가하기 버튼"></p>';
    	cardHtml += '	<p class="addCard-txt">간편결제 추가하기</p></a>';
   		cardHtml += '</div>';
    	
    	$("#cardList").html(cardHtml);
    	
    	
    }
    
    function fn_cardDelete(cardId){
    	if(confirm("결제카드를 삭제하시겠습니까?")){
	    	$.ajax({
				url: "/app/payment/deleteBillkeyReq",
				type: 'post',
				data: JSON.stringify(cardList.find(e => e.cardId === cardId)),
				async: false,
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Content-type","application/json; charset=utf-8;");
				},
				success : function(res) {
					fn_getCardList();
				},
				error: function (res){
					console.log("fail: ", res);
				}
			});
    	}
    }
    
    function fn_cardAdd(){
    	goPay();
    }
    
    function goPay() {
        // 스마트로페이 초기화
        smartropay.init({
            mode: 'REAL',		// STG: 테스트, REAL: 운영(운영서버 전환 시 변경 필수!)
            actionUri: '/ssb/interface.do'
        });
        // 스마트로페이 빌링키 발급요청
        smartropay.payment({
            FormId: 'tranMgr'				// 폼ID
        });
    }

</script>
<div class="contents-wrap bgGray cont-padding-typeA payment-wrap">
	<form id="tranMgr" name="tranMgr" method="post">
        <input type="hidden" name="PayMethod" value="CARD" />
        <input type="hidden" name="Mid" maxlength="10" value="${Mid}" />
        <input type="hidden" name="Moid" maxlength="40" value="${Moid}" />
        <input type="hidden" name="MallUserId" maxlength="20" value="${MallUserId}" />
        <input type="hidden" name="MallIp" maxlength="20" value="10.0.0.1" />
        <input type="hidden" name="UserIp" maxlength="20" value="10.0.0.1" />
        <input type="hidden" name="ReturnUrl" class="input" value="${ReturnUrl}" />
        <input type="hidden" name="VerifyValue" maxlength="100" class="input" value="${EncryptData}" />
        <input type="hidden" name="EncodingType" maxlength="14" value="utf8" />
        <input type="hidden" name="SspMallId" maxlength="20" value="${SspMallId}" />
        <input type="hidden" name="EdiDate" maxlength="14" value="${EdiDate}" />
    </form>
    <div class="payment-manage-wrap">
        <!-- 결제 수단 :s -->
        <div class="payMethod-wrap table-wrap">
            <div class="tableTit">
              	<h2>결제 수단</h2>
            </div>
            <div class="card-area">
				<div class="swiper mySwiper1 card-list-wrap after">
					<div class="swiper-wrapper card-list-subwrap" id="cardList"></div>
					<div class="swiper-pagination"></div>
				</div>
			</div>
		</div>
    </div>
    <fieldset class="chageCard-wrap">
		<legend>간편 결제 카드</legend>
			<div class="table-wrap">
				<div class="tableTit">
                	<h2>간편 결제 카드</h2>
                </div>
                <div class="table-subwrap">
                	<div class="guide-box">
                    	<p>전자금융거래 이용약관</p>
						<p>
						제1조 (목적)<br>
						이 약관은 (주)DongAh충전서비스 주식회사(이하 '회사'라 합니다)가 제공하는 전자지급결제대행서비스 및 결제대금예치서비스를 이용자가 이용함에 있어 회사와 이용자 사이의 전자금융거래에 관한 기본적인 사항을 정함을 목적으로 합니다.<br><br>
						제2조 (용어의 정의)<br>
						이 약관에서 정하는 용어의 정의는 다음과 같습니다.<br>
						1. '전자금융거래'라 함은 회사가 전자적 장치를 통하여 전자지급결제대행서비스 및 결제대금예치서비스(이하 '전자금융거래 서비스'라고 합니다)를 제공하고, 이용자가 회사의 종사자와 직접 대면하거나 의사소통을 하지 아니하고 자동화된 방식으로 이를 이용하는 거래를 말합니다.<br>
						2. '전자지급결제대행서비스'라 함은 전자적 방법으로 재화의 구입 또는 용역의 이용에 있어서 지급결제정보를 송신하거나 수신하는 것 또는 그 대가의 정산을 대행하거나 매개하는 서비스를 말합니다.<br>
						3. '결제대금예치서비스'라 함은 이용자가 재화의 구입 또는 용역의 이용에 있어서 그 대가(이하 '결제대금'이라 한다)의 전부 또는 일부를 재화 또는 용역(이하 '재화 등'이라 합니다)을 공급받기 전에 미리 지급하는 경우, 회사가 이용자의 물품수령 또는 서비스 이용 확인 시점까지 결제대금을 예치하는 서비스를 말합니다.<br>
						4. '이용자'라 함은 이 약관에 동의하고 회사가 제공하는 전자금융거래 서비스를 이용하는 자를 말합니다.<br>
						5. '접근매체'라 함은 전자금융거래에 있어서 거래지시를 하거나 이용자 및 거래내용의 진실성과 정확성을 확보하기 위하여 사용되는 수단 또는 정보로서 전자식 카드 및 이에 준하는 전자적 정보(신용카드번호를 포함한다), '전자서명법'상의 인증서, 회사에 등록된 이용자번호, 이용자의 생체정보, 이상의 수단이나 정보를<br> 사용하는데 필요한 비밀번호 등 전자금융거래법 제2조 제10호에서 정하고 있는 것을 말합니다.<br>
						6. '거래지시'라 함은 이용자가 본 약관에 의하여 체결되는 전자금융거래계약에 따라 회사에 대하여 전자금융거래의 처리를 지시하는 것을 말합니다.<br>
						7. '오류'라 함은 이용자의 고의 또는 과실 없이 전자금융거래가 전자금융거래계약 또는 이용자의 거래지시에 따라 이행되지 아니한 경우를 말합니다.<br><br>
						제3조 (약관의 명시 및 변경)<br>
						1. 회사는 이용자가 전자금융거래 서비스를 이용하기 전에 이 약관을 게시하고 이용자가 이 약관의 중요한 내용을 확인할 수 있도록 합니다.<br>
						2. 회사는 이용자의 요청이 있는 경우 전자문서의 전송방식에 의하여 본 약관의 사본을 이용자에게 교부합니다.<br>
						3. 회사가 약관을 변경하는 때에는 그 시행일 1월 전에 변경되는 약관을 회사가 제공하는 전자금융거래 서비스 이용 초기화면 및 회사의 홈페이지에 게시함으로써 이용자에게 공지합니다.<br>
						4. 회사는 제3항의 공지를 할 경우 "이용자가 변경에 따라 변경에 동의하지 아니한 경우 공지 받은 날로부터 30일 이내에 계약을 해지할 수 있으며, 해지의사표시를 하지 아니한 경우 동의한 것으로 본다."라는 내용을 통지합니다.<br><br>
						제4조 (전자지급결제대행서비스의 종류)<br>
						회사가 제공하는 전자지급결제대행서비스는 지급결제수단에 따라 다음과 같이 구별됩니다.<br>
						1. 신용카드결제대행서비스: 이용자가 결제대금의 지급을 위하여 제공한 지급결제수단이 신용카드인 경우로서, 회사가 전자결제시스템을 통하여 신용카드 지불정보를 송, 수신하고 결제대금의 정산을 대행하거나 매개하는 서비스를 말합니다.<br>
						2. 계좌이체결제대행서비스: 이용자가 결제대금을 회사의 전자결제시스템을 통하여 금융기관에 등록한 자신의 계좌에서 출금하여 원하는 계좌로 이체할 수 있는 실시간 송금 서비스를 말합니다.<br>
						3. 가상계좌결제대행서비스: 이용자가 결제대금을 현금으로 결제하고자 경우 회사의 전자결제시스템을 통하여 자동으로 이용자만의 고유한 일회용 계좌의 발급을 통하여 결제대금의 지급이 이루어지는 서비스를 말합니다.<br>
						4. 간편결제서비스 : 이용자가 결제대금의 지급을 위하여 제공한 지급결제수단이 신용카드, 계좌이체인 경우로서, 정보를 매번 입력할 필요 없이 관련 정보의 한번 등록만으로 상품 결제가 가능한 서비스를 말합니다. 단, 간편결제서비스 신청 시 회사가 정하는 이용자의 본인확인 절차가 반드시 필요하며, 회사의 인증 및 승낙이 있어야 서비스를 이용할 수 있습니다.<br>
						5. 기타: 회사가 제공하는 서비스로서 지급결제수단의 종류에 따라 '휴대폰 결제대행서비스', '상품권결제대행서비스', 등이 있습니다.<br><br>
						제5조 (결제대금예치서비스의 내용)<br>
						1. 이용자(이용자의 동의가 있는 경우에는 재화 등을 공급받을 자를 포함합니다. 이하 본 조에서 같습니다)는 재화 등을 공급받은 사실을 재화 등을 공급받은 날부터 3영업일 이내에 회사에 통보하여야 합니다.<br>
						2. 회사는 이용자로부터 재화 등을 공급받은 사실을 통보 받은 후 회사와 통신판매업자간 사이에서 정한 기일 내에 통신판매업자에게 결제대금을 지급합니다.<br>
						3. 회사는 이용자가 재화 등을 공급받은 날부터 3영업일이 지나도록 정당한 사유의 제시 없이 그 공급받은 사실을 회사에 통보하지 아니하는 경우에는 이용자의 동의 없이 통신판매업자에게 결제대금을 지급할 수 있습니다.<br>
						4. 회사는 통신판매업자에게 결제대금을 지급하기 전에 이용자에게 결제대금을 환급 받을 사유가 발생한 경우에는 그 결제대금을 소비자에게 환급합니다.<br>
						5. 회사는 이용자와의 결제대금예치서비스 이용과 관련된 구체적인 권리, 의무를 정하기 위하여 본 약관과는 별도로 결제대금예치서비스이용약관을 제정할 수 있습니다.<br><br>
						제6조 (이용시간)<br>
						1. 회사는 이용자에게 연중무휴 1일 24시간 전자금융거래 서비스를 제공함을 원칙으로 합니다. 단, 금융기관 기타 결제수단 발행업자의 사정에 따라 달리 정할 수 있습니다. 계좌이체결제대행서비스, 가상계좌결제대행서비스, 간편결제서비스는 은행사의 사정에 따라 “00:30~ 23:30” 까지 전자금융거래 서비스를 제공함을 원칙으로 합니다.<br>
						2. 회사는 정보통신설비의 보수, 점검 기타 기술상의 필요나 금융기관 기타 결제수단 발행업자의 사정에 의하여 서비스 중단이 불가피한 경우, 서비스 중단 3일 전까지 게시 가능한 전자적 수단을 통하여 서비스 중단 사실을 게시한 후 서비스를 일시 중단할 수 있습니다. 다만, 시스템 장애복구, 긴급한 프로그램 보수, 외부요인 등 불가피한 경우에는 사전 게시 없이 서비스를 중단할 수 있습니다.<br><br>
						제7조 (접근매체의 선정과 사용 및 관리)<br>
						1. 회사는 전자금융거래 서비스 제공 시 접근매체를 선정하여 이용자의 신원, 권한 및 거래지시의 내용 등을 확인할 수 있습니다.<br>
						2. 이용자는 접근매체를 제3자에게 대여하거나 사용을 위임하거나 양도, 양수 또는 담보 목적으로 제공할 수 없습니다.<br>
						3. 이용자는 자신의 접근매체를 제3자에게 누설 또는 노출하거나 방치하여서는 안되며, 접근매체의 도용이나 위조 또는 변조를 방지하기 위하여 충분한 주의를 기울여야 합니다.<br>
						4. 회사는 이용자로부터 접근매체의 분실이나 도난 등의 통지를 받은 때에는 그 때부터 제3자가 그 접근매체를 사용함으로 인하여 이용자에게 발생한 손해를 배상할 책임이 있습니다.<br><br>
						제8조 (거래내용의 확인)<br>
						1. 회사는 이용자와 미리 약정한 전자적 방법을 통하여 이용자의 거래내용(이용자의 '오류정정 요구사실 및 처리결과에 관한 사항'을 포함합니다)을 확인할 수 있도록 하며, 이용자의 요청이 있는 경우에는 요청을 받은 날로부터 2주 이내에 모사전송 등의 방법으로 거래내용에 관한 서면을 교부합니다.<br>
						2. 회사는 제1호에 따른 이용자의 거래내용 서면교부 요청을 받은 경우 전자적 장치의 운영장애, 그 밖의 사유로 거래내용을 제공할 수 없는 때에는 즉시 이용자에게 전자문서 전송(전자우편을 이용한 전송을 포함합니다)의 방법으로 그러한 사유를 알려야 하며, 전자적 장치의 운영장애 등의 사유로 거래내용을 제공할 수 없는 기간은 제1호의 거래내용에 관한 서면의 교부기간에 산입하지 아니합니다.<br>
						3. 회사가 이용자에게 제공하는 거래내용 중 거래계좌의 명칭 또는 번호, 거래의 종류 및 금액, 거래상대방을 나타내는 정보, 거래일자, 전자적 장치의 종류 및 전자적 장치를 식별할 수 있는 정보와 해당 전자금융거래와 관련한 전자적 장치의 접속기록, 회사가 전자금융거래의 대가로 받은 수수료, 이용자의 출금 동의에 관한 사항, 전자금융거래의 신청 및 조건의 변경에 관한 사항, 건당 거래금액이 1만원을 초과하는 전자금융거래에 관한 기록은 5년간, 건당 거래금액이 1만원 이하인 소액 전자금융거래에 관한 기록, 전자지급수단 이용 시 거래승인에 관한 기록, 이용자의 오류정정 요구사실 및 처리결과에 관한 사항은 1년간의 기간을 대상으로 하되, 회사가 전자지급결제대행 서비스 제공의 대가로 수취한 수수료에 관한 사항은 제공하는 거래내용에서 제외됩니다.<br>
						4. 이용자가 제1항에서 정한 서면교부를 요청하고자 할 경우 다음의 주소 및 전화번호로 요청할 수 있습니다.<br><br>
						- 주소: 서울시 금천구 가산디지털1로 119<br>
						- 전화번호: 1666-9114<br>
						- FAX: 02-2109-9196~7<br><br>
						제9조 (오류의 정정 등)<br>
						1. 이용자는 전자금융거래 서비스를 이용함에 있어 오류가 있음을 안 때에는 회사에 대하여 그 정정을 요구할 수 있습니다.<br>
						2. 회사는 전항의 규정에 따른 오류의 정정요구를 받은 때에는 이를 즉시 조사하여 처리한 후 정정요구를 받은 날부터 2주 이내에 그 결과를 이용자에게 알려 드립니다.<br><br>
						제10조 (회사의 책임)<br>
						1. 회사는 접근매체의 위조나 변조로 발생한 사고로 인하여 이용자에게 발생한 손해에 대하여 배상책임이 있습니다. 다만 이용자가 제7조 제2항에 위반하거나 제3자가 권한 없이 이용자의 접근매체를 이용하여 전자금융거래를 할 수 있음을 알았거나 알 수 있었음에도 불구하고 이용자가 자신의 접근매체를 누설 또는 노출하거나 방치한 경우 그 책임의 전부 또는 일부를 이용자가 부담하게 할 수 있습니다.<br>
						2. 회사는 계약체결 또는 거래지시의 전자적 전송이나 처리과정에서 발생한 사고로 인하여 이용자에게 그 손해가 발생한 경우에는 그 손해를 배상할 책임이 있습니다. 다만 본 조 제1항 단서에 해당하거나 법인('중소기업기본법' 제2조 제2항에 의한 소기업을 제외합니다)인 이용자에게 손해가 발생한 경우로서 회사가 사고를 방지하기 위하여 보안절차를 수립하고 이를 철저히 준수하는 등 합리적으로 요구되는 충분한 주의의무를 다한 경우 그 책임의 전부 또는 일부를 이용자가 부담하게 할 수 있습니다.<br>
						3. 회사는 전자금융거래를 위한 전자적 장치 또는 ‘정보통신망 이용촉진 및 정보보호 등에 관한 법률’ 제2조제1항제1호에 따른 정보통신망에 침입하여 거짓이나 그 밖의 부정한 방법으로 획득한 접근매체의 이용으로 발생한 사고로 인하여 이용자에게 그 손해가 발생한 경우에는 그 손해를 배상할 책임이 있습니다. 다만, 다음과 같은 경우 회사는 이용자에 대하여 일부 또는 전부에 대하여 책임을 지지 아니합니다.<br>
						가. 회사가 접근매체에 따른 확인 외에 보안강화를 위하여 전자금융거래 시 요구하는 추가적인 보안조치를 이용자가 정당한 사유 없이 거부하여 전자금융거래법 제9조 제1항 제3호에 따른(이하 '사고'라 한다) 사고가 발생한 경우<br>
						나. 이용자가 동항 제 가목의 추가적인 보안조치에서 사용되는 매체, 수단 또는 정보에 대하여 다음과 같은 행위를 하여 '사고'가 발생하는 경우<br>
						   - 누설, 누출 또는 방치한 행위<br>
						   - 3자에게 대여하거나 그 사용을 위임한 행위 또는 양도나 담보의 목적으로 제공한 행위<br><br>
						제11조 (전자지급거래계약의 효력)<br>
						1. 회사는 이용자의 거래지시가 전자지급거래에 관한 경우 그 지급절차를 대행하며, 전자지급거래에 관한 거래지시의 내용을 전송하여 지급이 이루어지도록 합니다.<br>
						2. 회사는 이용자의 전자지급거래에 관한 거래지시에 따라 지급거래가 이루어지지 않은 경우 수령한 자금을 이용자에게 반환하여야 합니다.<br><br>
						제12조 (거래지시의 철회)<br>
						1. 이용자가 전자금융거래를 한 경우, 이용자는 지급의 효력이 발생하기 전까지 본 약관 제8조 제4항 기재 담당자에게 전자문서의 전송(전자우편을 이용한 전송을 포함합니다)에 의한 방법으로 거래지시를 철회할 수 있습니다.<br>
						2. 전항의 지급의 효력이 발생 시점이란 (i) 전자자금이체의 경우에는 거래 지시된 금액의 정보에 대하여 수취인의 계좌가 개설되어 있는 금융기관의 계좌 원장에 입금기록이 끝난 때 (ii) 그 밖의 전자지급수단으로 지급하는 경우에는 거래 지시된 금액의 정보가 수취인의 계좌가 개설되어 있는 금융기관의 전자적 장치에 입력이 끝난 때를 말합니다.<br>
						3. 이용자는 지급의 효력이 발생한 경우에는 전자상거래 등에서의 소비자보호에 관한 법률 등 관련 법령상 청약의 철회의 방법 또는 본 약관 제5조에서 정한 바에 따라 결제대금을 반환 받을 수 있습니다.<br><br>
						제13조 (전자금융거래 기록의 생성 및 보존)<br>
						1. 회사는 이용자가 전자금융거래의 내용을 추적, 검색하거나 그 내용에 오류가 발생한 경우에 이를 확인하거나 정정할 수 있는 기록을 생성하여 보존합니다.<br>
						2. 전항의 규정에 따라 회사가 보존하여야 하는 기록의 종류 및 보존방법은 제8조 제3항에서 정한 바에 따릅니다.<br><br>
						제14조 (전자금융거래정보의 제공금지)<br>
						회사는 전자금융거래 서비스를 제공함에 있어서 취득한 이용자의 인적 사항, 이용자의 계좌, 접근매체 및 전자금융거래의 내용과 실적에 관한 정보 또는 자료를 금융실명법 등 법령에 의하거나 이용자의 동의를 얻지 아니하고 제3자에게 제공, 누설하거나 업무상 목적 외에 사용하지 아니합니다.<br><br>
						제15조 (분쟁처리 및 분쟁조정)<br>
						1. 이용자는 다음의 분쟁처리 책임자 및 담당자에 대하여 전자금융거래 서비스 이용과 관련한 의견 및 불만의 제기, 손해배상의 청구 등의 분쟁처리를 요구할 수 있습니다.<br>
						- 담당자: (주)DongAh충전서비스 고객센터<br>
						- 연락처 : 16700-119<br><br>
						2. 이용자가 회사에 대하여 분쟁처리를 신청한 경우에는 회사는 15일 이내에 이에 대한 조사 또는 처리 결과를 이용자에게 안내합니다.<br>
						3. 이용자는 '금융위원회의 설치 등에 관한 법률' 제51조의 규정에 따른 금융감독원의 금융분쟁조정위원회나 '소비자기본법' 제33조 제1항의 규정에 따른 소비자보호원에 회사의 전자금융거래 서비스의 이용과 관련한 분쟁조정을 신청할 수 있습니다.<br><br>
						제16조 (회사의 안정성 확보 의무)<br>
						회사는 전자금융거래가 안전하게 처리될 수 있도록 선량한 관리자로서의 주의를 다하며, 전자금융거래의 안전성과 신뢰성을 확보할 수 있도록 전자금융거래의 종류별로 전자적 전송이나 처리를 위한 인력, 시설, 전자적 장치 등의 정보기술부문 및 전자금융업무에 관하여 금융위원회가 정하는 기준을 준수합니다.<br><br>
						제17조 (약관 외 준칙)<br>
						1. 회사와 이용자 사이에 개별적으로 합의한 사항이 이 약관에 정한 사항과 다를 때에는 그 합의사항을 이 약관에 우선하여 적용합니다.<br>
						2. 전자금융거래에 관하여 이 약관에 정하지 않은 사항은 개별약관이 정하는 바에 따릅니다.<br>
						3. 이 약관과 전자금융거래에 관한 개별약관에 정하지 않은 사항(용어의 정의 포함)에 대하여는 다른 합의사항이 없으면 전자금융거래법, 전자상거래 등에서의 소비자 보호에 관한 법률, 여신전문금융업법 등 관계 법령에서 정한 바에 따릅니다.<br><br>
						제18조 (관할)<br>
						회사와 이용자간에 발생한 분쟁에 관한 관할은 민사소송법에서 정한 바에 따릅니다.<br><br>
						    부칙 : <br>
						    최초 시행일자 : 2024년 01월 01일<br>
						</p>
                	</div>
                </div>
              </div>
	          <div class="summary-wrap">
	          <div class="tableTit">
                	<h2>꼭 확인하세요!</h2>
                </div>
	          	<ul class="guide-box2">
	          		<li>충전 완료 이후 '간편 결제 카드'에 등록된 카드로 자동 결제됩니다.</li>
	      		</ul>
	          </div>                
	</fieldset>
</div>

