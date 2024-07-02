<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	const userId = "${userid}";
	const userName = "${username}";
	var _param = {};

	$(document).ready(function() {
		$("#userId").val(userId);
		FAQ.init();

		$("#btnModify").hide();

	});

	$(document).on("click", "#btnQueReg, #btnInsert , #btnModify, .btnClosePopup, .btnCancel", function(e) {
		e.preventDefault();
		var id = $(this).attr("id");

		//문의하기 팝업 호출
		if( id == "btnQueReg") {
			var reserveYes_height = $("#qnaPop").height()/1.5;
			$(".popupWrap").css("margin-top",-reserveYes_height);
			$("#qnaPop").show();

			$("#receptUpperCd1").val("");
			$("#receptSubCd1").val("");
			$("#vocContents").val("");
			$("#vocId").val("");

			$("#btnInsert").show();
			$("#btnModify").hide();

			$("#receptSubCd1").empty();
			var html2 = "";
			html2 += '<option value="">선택</option>';
			$("#receptSubCd1").append(html2);
		}

		//문의하기 등록
		if( id == "btnInsert") {
			if($("#vocContents").val()=="") {
				alert("문의내용을 입력해주세요.");
				return;
			} else {
				if( confirm("1:1문의를 등록하시겠습니까?")) {
					var data = $('form[name="frm"]').serialize();
					$.ajax({
						url: "/app/customer/qna_insert",
						type: 'get',
						data: data,
						dataType:"json",
						success : function(res) {
							if(res.result.message == "success") {
								alert("등록되었습니다");
								$("#qnaPop, .mask").hide();
								Qna.init(1);
							} else {
								alert(res.result.content);
							}
						}
					});
				}
			}
		}

		//문의하기 수정
		if( id == "btnModify") {
			if($("#vocContents").val()=="") {
				alert("문의내용을 입력해주세요.");
				return;
			} else {
				if( confirm("1:1문의를 수정하시겠습니까?")) {
					var data = $('form[name="frm"]').serialize();
					$.ajax({
						url: "/app/customer/qna_modify",
						type: 'get',
						data: data,
						dataType:"json",
						success : function(res) {
							if(res.result.message == "success") {
								alert("등록되었습니다");
								$("#qnaPop, .mask").hide();
								Qna.init(1);
							} else {
								alert(res.result.content);
							}
						}
					});
				}
			}
		}

		//닫기버튼
		if( id == "btnClosePopup, btnCancel") {
			$("#qnaPop, .mask").hide();
		}
	});

	var FAQ = {
		init : function() {
			$.ajax({
				url: "/app/customer/faq_list",
				type: 'get',
				dataType:"json",
				success : function(res) {
					callBack(res);
				}
			});
		}
	}

	function callBack(data) {
		var result = data.result;
		var html = "";
		result.forEach(function(item) {
			html += "<li>";
			html += "	<div class='menu'>";
			html += "		<span class='cate-tit'>"+item.subject+"</span>";
			html +='		<div class="btn-dropdownWrap">';
			html +='		<button type="button" class="btn-dropdown" onclick=""><img src="/resources/images/btn_dropDown_off.png" alt="드롭다운버튼"></button>';
			html +='		</div>';
			html += "	</div>";
			html += "	<div class='menu-sub'>";
			html += "		<p class='question'>";
			html += "			<span class='qna-icon'>Q.</span>";
			html += "			<span>"+item.subject+"</span>";
			html += "		</p>";
			html += "		<p class='question'>";
			html += "			<span class='qna-icon'>A.</span>";
			html += "			<span>"+item.content+"</span>";
			html += "		</p>";
			html += "	</div>";
			html += "</li>";
		});

		$("#faqList").append(html);
	}

	//1:1문의 List
	var myGrid = null;

	$(function() {
		var opt = {
			gridId : "questionBody"
			,pageId : "dataGridPageNavi"
		};
		myGrid = gf_initMyGrid(opt);
		Qna.init(1);
	});

	var Qna = (function(){

		var obj = {};
		var page = 1;
		var url = "/app/customer/qna_list";
		var contentHtml;

		var getParam = function( pageNo ){
			var param = {};
			param.currPageNo = pageNo;
			param.userId = userId;
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
						$("#questionBody").html(el);
						return false;
					}

					data.rows.forEach(function(item){
						html +='<tr class="menu">';
						html +='	<td class="qu-state"><span class="menu2">'+item.VOC_STATUS_NM+'</span></td>';
						html +='	<td class="qu-tit"><span class="menu2">'+item.RECEPT_UPPER_NM+'</span></td>';
						html +='	<td class="qu-date"><span class="menu2">'+item.REG_DT+'</span></td>';
						html +='</tr>';

						html +='<tr class="menu-sub">';
						html +='	<td colspan="3">';
						html +='		<div class="menu-sub-subwrap">';
						html +='			<p class="question">';
						html +='				<span class="qna-icon">Q.</span>';
						html +='				<span class="qna-an">'+item.VOC_RECEPT_MEMO+'</span>';
						html +='			</p>';
						html +='			<p class="answer">';
						html +='				<span class="qna-icon">A.</span>';

						if ( item.VOC_FAULT_MEMO == undefined || item.VOC_FAULT_MEMO == null ){
							html +='			<span class="qna-an"></span>';
						} else {
							html +='			<span class="qna-an">'+item.VOC_FAULT_MEMO+'</span>';
						}
						html +='			</p>';
						if( item.vocStatusCd == "2" ){
							html +='		<div class="btn-wrap">';
							html +='			<button type="button" class="btn-two btn-h32 btn-color-sub1" onclick="qnaModifyPopup('+item.vocId+');">수정</button>';
							html +='			<button type="button" class="btn-two btn-h32 btn-color-sub3" onclick="qnaDelete('+item.vocId+');">삭제</button>';
							html +='		</div>';
						}
						html +='		</div>';
						html +='	</td>';
						html +='</tr>';
					});

					contentHtml(html);

					if(listInfo.length != 0){
						myGrid.makePageNvai(pageInfo, "Qna.init");
					}
				}
			})
		}

		var contentAdd = function(html){
			$("#questionBody").append(html);
		}
		var contentNew = function(html){
			$("#questionBody").html(html);
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

	//문의하기 삭제
	function qnaDelete(vocId) {
		var param = { vocId: vocId };
		if( confirm("1:1문의를 삭제하시겠습니까?")) {
			$.ajax({
				url : "/app/customer/qna_delete"
				,type : "get"
				,dataType : "json"
				,data : param
				, success : function(data) {
					if(data.result.message == "success") {
						alert("등록되었습니다");
						$("#qnaPop, .mask").hide();
						Qna.init(1);
					} else {
						alert(data.result.content);
					}
				}
			});
		}
	}

	//문의하기 수정 팝업 호출
	function qnaModifyPopup(vocId) {
		var reserveYes_height = $("#qnaPop").height()/1.5;
		$(".popupWrap").css("margin-top",-reserveYes_height);
		$("#qnaPop").show();
		$(".mask").css("display", "block");

		$("#receptUpperCd1").val("");
		$("#receptSubCd1").val("");
		$("#vocContents").val("");
		$("#vocId").val("");

		$("#btnInsert").hide();
		$("#btnModify").show();

		var param = { vocId };
		$.ajax({
			url : "/app/customer/qna_detail"
			,type : "get"
			,dataType : "json"
			,data : param
			, success : function(data) {
				$("#receptSubCd1").empty();

				var selectQnaType = data.selectQnaType;

				var html2 = "";

				var subCdList = data.subClCdList;
				var html2 = "";

				selectQnaType.forEach(function(data) {
					html2 += '<option value='+data.cd+'>'+data.cdNm+'</option>';
				});

				$("#receptSubCd1").append(html2);

				$("#receptUpperCd1").val(data.result.vocKindCd);
				$("#receptSubCd1").val(data.result.vocKindDetailCd);
				$("#vocContents").val(data.result.VOC_RECEPT_MEMO);
				$("#vocId").val(data.result.vocId);
			}
		});
	}

	//문의하기 CallBack 함수
	function selectQnaType(data) {
		$.ajax({
			url: "/app/customer/select_qna_type",
			type: 'get',
			data: {qnaGubun: data},
			dataType:"json",
			success : function(res) {
				$("#receptSubCd1").empty();
				var selectQnaType = res.selectQnaType;
				var html2 = "";

				html2 += '<option value="">선택</option>';
				selectQnaType.forEach(function(data) {
					html2 += '<option value='+data.cd+'>'+data.cdNm+'</option>';
				});

				$("#receptSubCd1").append(html2);
			}
		});
	}
</script>
<div class="contents-wrap cont-padding-typeA faq-wrap" id="content">
	<!-- FAQ :s -->
	<div class="list-wrap faqsub-wrap">
		<div class="list-Tit">
			<h2>FAQ</h2>
		</div>
		<div class="list-subwrap">
			<ul id="faqList">
			</ul>
		</div>
	</div>

	<!-- 1:1문의 :s-->
	<div class="list-wrap oneque-wrap">
		<div class="list-Tit" style="display: flex;">
			<h2 style="width: 40%; margin-top: 3%;">1:1 문의</h2>
			<div class="btn-wrap" style="width: 35%; margin-left: 25%;">
				<button type="button" class="btn-one btn-h40 btn-color-main1 btnOpenPopup" id="btnQueReg">문의하기</button>
			</div>
		</div>
		<div class="list-subwrap" style="margin-top: 3%;">
			<table>
				<colgroup>
					<col style="width:33%">
					<col style="width:33%">
					<col style="width:34%">
				</colgroup>
				<tbody id="questionBody">
				</tbody>
			</table>
		</div>
		<div class="pageNavi" id="dataGridPageNavi">
		</div>
	</div>

	<!-- //1:1문의 :e -->

	<!-- 1:1문의 > 문의하기 클릭 시 팝업: s -->
	<div class="popupWrap" id="qnaPop" style="top: 57%;">
		<h3>문의하기</h3>
		<button type="button" class="btnClosePopup">
			<img src="/resources/images/btn-popup-close.png" alt="닫기 버튼">
		</button>
		<form method="post" name="frm">
			<input type="hidden" name="userId" id="userId"/>
			<table class="basic_table">
				<caption>문의하기</caption>
				<colgroup>
					<col style="width:34%">
					<col style="width:66%">
				</colgroup>
				<tbody>
				<tr>
					<th>문의 구분</th>
					<td class="select-wrap">
						<select class="select-one select-h40" id="receptUpperCd1" name="receptUpperCd1" onchange="selectQnaType(value)">
							<option value="">선택</option>
							<option value="VOC002">사용문의</option>
							<option value="VOC003">구매문의</option>
							<option value="VOC004">결제관련</option>
							<option value="VOC005">불편사항</option>
							<option value="VOC006">안전사고</option>
							<option value="VOC007">기타</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>문의 분류</th>
					<td class="select-wrap">
						<select class="select-one select-h40" id="receptSubCd1" name="receptSubCd1">
							<option value="">선택</option>
						</select>
					</td>
				</tr>
				<tr>
					<th class="tettop">문의 내용</th>
					<td>
						<textarea class="txtarea-one" id="vocContents" name="vocContents"></textarea>
						<input type="hidden" name="vocId" id="vocId"/>
					</td>
				</tr>
				</tbody>
			</table>
		</form>
		<div class="btn-wrap">
			<button type="button" class="btn-two btn-h40 btn-color-sub1" id="btnInsert">등록</button>
			<button type="button" class="btn-two btn-h40 btn-color-sub3 btnCancel">취소</button>
			<button type="button" class="btn-two btn-h40 btn-color-sub1" id="btnModify">수정</button>
		</div>
	</div>
</div>
<script src="/resources/js/dropdown.js"></script>