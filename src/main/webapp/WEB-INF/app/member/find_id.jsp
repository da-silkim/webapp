<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	var _param = {};
	var data = {"btnSearchId":"id","btnSearchPw":"pw"};
	$(document).ready(function() {
		$("#alertFindId").css("display","none");
		$("#alertFindPwd").css("display","none");
		keyEvent();	//숫자만 입력가능
		
	});
	


	$(document).on("click", "#btnSearchId, #btnSearchPw, #btnPassUpdate", function(e) {
		e.preventDefault();
		var id = $(this).attr("id");
		_param={};
		
		if( id == "btnSearchId") {
			if( !validatorId( id ) ) return false;
			var mobile = $("#phoneid").val();
			_param.customerName = $("#nameid").val();
			_param.brithDay = $("#ymdid").val();
			_param.mobile = mobile;
			_param.flag = "id";

			$.ajax({
				url: "/app/member/find_id/action",
				type: 'post',
				data: _param,
				async: true,
				success : function(res) {
					if(res.result.message != 'success'){
						$("#alertFindId").css("display","");
						$("#findid_Id").val("");
						return false;
					} else {
						$("#alertFindId").css("display","none");
						$("#findid_Id").val( res.result.content );
					}
				}
			});
		}

		if( id == "btnSearchPw") {
			if( !validatorPass( id ) ) return false;
			var mobile = $("#phonepw").val();
			_param.customerName = $("#namepw").val();
			_param.brithDay = $("#ymdpw").val();
			_param.mobile = mobile;
			_param.userId = $("#idpw").val();
			_param.flag = "pw";

			$.ajax({
				url: "/app/member/find_id/action",
				type: 'post',
				data: _param,
				async: false,
				success : function(res) {
					if(res.result.message != 'success'){
						alert("입력하신 정보가 존재하지 않습니다.");
						$("#alertFindPwd").css("display","none");
					} else {
						openPopup();
					}
				}
			});
		}

		if( id == "btnPassUpdate") {
			if( !validatorPop() ) return false;
			var mobile = $("#phonepw").val();
			_param.userId = $("#idpw").val();
			_param.changePassword = $("#changePassword").val();

			$.ajax({
				url: "/app/member/password_change/action",
				type: 'post',
				data: _param,
				async: false,
				success : function(res) {
					if(res.result.message != 'success'){
						alert(res.result.content);
					} else {
						alert("변경되었습니다. 로그인 페이지로 이동합니다.");
						location.href = "/app/member/login";
					}
				}
			});
		}
	});

	function validatorId( id ) {
		if ($.trim($("#name" + data[id]).val()).length == 0) {
			$("#checkName").css("display","");
			$("#name" + data[id]).focus();
			return false;
		} else {
			$("#checkName").css("display","none");
		}
		
		if ($.trim($("#ymd" + data[id]).val()).length == 0) {
			$("#checkBirth").css("display","");
			$("#ymd" + data[id]).focus();
			return false;
		} else {
			$("#checkBirth").css("display","none");
		}
		
		if ($.trim($("#ymd" + data[id]).val()).length != 8) {
			$("#checkBirth2").css("display","");
			$("#ymd" + data[id]).focus();
			return false;
		} else {
			$("#checkBirth2").css("display","none");
		}
		
		if ($.trim($("#phone" + data[id]).val()).length == 0) {
			$("#checkPhone").css("display","");
			$("#phone1" + data[id]).focus();
			return false;
		} else {
			$("#checkPhone").css("display","none");
		} 
		
		return true;
	}
	
	function validatorPass( id ) {
		if ($.trim($("#name" + data[id]).val()).length == 0) {
			$("#checkPassName").css("display","");
			$("#name" + data[id]).focus();
			return false;
		} else {
			$("#checkPassName").css("display","none");
		}
		
		if ($.trim($("#ymd" + data[id]).val()).length == 0) {
			$("#checkPassBirth").css("display","");
			$("#ymd" + data[id]).focus();
			return false;
		} else {
			$("#checkPassBirth").css("display","none");
		}
		
		if ($.trim($("#ymd" + data[id]).val()).length != 8) {
			$("#checkPassBirth2").css("display","");
			$("#ymd" + data[id]).focus();
			return false;
		} else {
			$("#checkPassBirth2").css("display","none");
		}
		
		if ($.trim($("#phone" + data[id]).val()).length == 0) {
			$("#checkPassPhone").css("display","");
			$("#phone" + data[id]).focus();
			return false;
		} else {
			$("#checkPassPhone").css("display","none");
		} 
		
		if ($.trim($("#idpw").val()).length == 0) {
			$("#checkPassId").css("display","");
			$("#idpw").focus();
			return false;
		} else {
			$("#checkPassId").css("display","none");
		}
		return true;
	}

	function validatorPop() {
		if( $.trim( $("#changePassword").val() ).length == 0 ) {
			alert("변경 비밀번호를 입력해 주세요.");
			$("#changePassword").focus();
			return false;
		}
		if(chkPwd($('#changePassword').val())==false){
			$("#changePassword").focus();
			return false;
		}
		if( $.trim( $("#confirmPassword").val() ).length == 0 ) {
			alert("비밀번호 확인을 입력해 주세요.");
			$("#confirmPassword").focus();
			return false;
		}
		if( $("#changePassword").val() != $("#confirmPassword").val() ) {
			alert("변경 비밀번호와 비밀번호 확인이 서로 맞지 않습니다.");
			return false;
		}
		return true;
	}

	function openPopup() {
		var popup_height = $(".popupWrap").height() / 2;
		$(".popupWrap").css("margin-top", -popup_height);
		$(".popupWrap, .mask").show();
	}

	$(document).on("click", ".btnClosePopup", function(e) {
		$(".popupWrap, .mask").hide();
		$("#prePassword, #changePassword, #confirmPassword").val('');
	});

	function chkPwd(str){
		var pw = str;
		var num = pw.search(/[0-9]/g);
		var eng = pw.search(/[a-z]/ig);
		var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

		if(pw.length < 8 || pw.length > 15 || num < 0 || eng < 0 || spe < 0 ){
			alert("변경비밀번호는 8자리 ~ 15자리 이내로 영문,숫자, 특수문자를 혼합하여 입력해주세요.");
			return false;
		}
		return true;
	}
</script>
<div class="contents-wrap bgGray cont-padding-typeA">

	<!-- 20220203: as is 활용코드 :s -->
	<form action="" method="post">

		<!-- 아이디 찾기 :s -->
		<fieldset>
			<legend>아이디 찾기</legend>
			<div class="table-wrap">
				<div class="tableTit">
					<h2>아이디 찾기</h2>
					<span class="tableSubTit">'아이디 찾기'를 누르시면, 아이디 란에 아이디가 나타납니다</span>
				</div>
				<!-- 20220203: 클래스명 수정 -->
				<table class="basic_table">
					<!-- //20220203: 클래스명 수정 -->
					<caption>아이디 찾기</caption>
					<colgroup>
						<col style="width:34%">
						<col style="width:66%">
					</colgroup>
					<tbody>
					<tr>
						<th>성명<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td><input class="input-one input-h40" type="text" id="nameid" value="">
							<p id="checkName" style="display:none;font-size: 1.2rem;">* 성명을 입력하시기 바랍니다</p>
						</td>
					</tr>
					<tr class="birthTr">
						<th>생년월일<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input class="input-one input-h40" type="text" id="ymdid" placeholder="YYYYMMDD" maxlength="8" numberOnly value="">
							<p id="checkBirth" style="display:none;font-size: 1.2rem;">* 생년월일을 입력하시기 바랍니다</p>
							<p id="checkBirth2" style="display:none;font-size: 1.2rem;">* 생년월일은 8자리 입니다</p>
						</td>
					</tr>
					<tr>
						<th>휴대폰 번호<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input class="input-one input-h40" type="text" id="phoneid" placeholder="숫자만 입력" maxlength="11" numberOnly value="">
							<p id="checkPhone" style="display:none;font-size: 1.2rem;">* 휴대폰 번호를 입력하시기 바랍니다</p>
						</td>
					</tr>
					<!-- 20220203: 클래스명 수정-->
					<tr class="btn-wrap">
						<td colspan="2" style="text-align: center">
							<button type="submit" class="btn-one btn-h40 btn-color-main1" id="btnSearchId">아이디 찾기</button>
						</td>
					</tr>
					<!-- // 20220203: 클래스명 수정-->
					<tr>
						<th>아이디</th>
						<td>
							<input type="text" class="input-one input-h40 input-disable" id="findid_Id" readonly>
							<p id="alertFindId" class="txt-alert" style="font-size: 1.3rem;">해당 정보로 가입된 사용자가 없습니다.</p>
						</td>
					</tr>
					</tbody>
				</table>
			</div>
		</fieldset>
		<!-- 아이디 찾기 :e -->

		<!-- 비밀번호 찾기 :s -->
		<fieldset>
			<legend>비밀번호 찾기</legend>
			<div class="table-wrap">
				<div class="tableTit">
					<h2>비밀번호 찾기</h2>
				</div>
				<!-- 20220203: 클래스명 수정 -->
				<table class="basic_table">
					<!-- //20220203: 클래스명 수정 -->
					<caption>비밀번호 초기화</caption>
					<colgroup>
						<col style="width:34.375%">
						<col style="width:65.625%">
					</colgroup>
					<tbody>
						<tr>
							<th>성명<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
							<td><input type="text" id="namepw" class="input-one input-h40" value="">
								<p id="checkPassName" style="display:none;font-size: 1.2rem;">* 성명을 입력하시기 바랍니다</p>
							</td>
						</tr>
					<tr class="birthTr">
						<th>생년월일<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input type="text" id="ymdpw" class="input-one input-h40" placeholder="YYYYMMDD" maxlength="8" numberOnly value="">
							<p id="checkPassBirth" style="display:none;font-size: 1.2rem;">* 생년월일을 입력하시기 바랍니다</p>
							<p id="checkPassBirth2" style="display:none;font-size: 1.2rem;">* 생년월일은 8자리 입니다</p>
						</td>
					</tr>
					<tr class="phoneNumber">
						<th>휴대폰 번호<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input type="text" id="phonepw" class="input-one input-h40" placeholder="숫자만 입력" maxlength="11" numberOnly value="">
							<p id="checkPassPhone" style="display:none;font-size: 1.2rem;">* 휴대폰 번호를 입력하시기 바랍니다</p>
						</td>
					</tr>
					<tr>
						<th>아이디<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td><input type="text" id="idpw" class="input-one input-h40" value=""><p id="checkPassId" style="display:none;font-size: 1.2rem;">* 아이디를 입력하시기 바랍니다</p></td>
					</tr>
					<!-- 20220203: 클래스명 수정-->
					<tr class="btn-wrap">
						<td colspan="2">
							<button type="submit" class="btn-one btn-h40 btn-color-main1" id="btnSearchPw">비밀번호 변경</button>
							<p id="alertFindPwd" class="txt-alert center">비밀번호 변경을 진행하시면 됩니다.</p>
						</td>
						<!-- //20220203: 클래스명 수정-->
					</tr>
					</tbody>
				</table>
			</div>
		</fieldset>
		<!-- 비밀번호 찾기 :e -->
	</form>
	<!-- //20220203: as is 활용코드 :e -->
	<form name="popFrm" id="popFrm">
		<div class="popupWrap">
			<h3>비밀번호 변경</h3>
			<button type="button" class="btnClosePopup">
				<img src="/resources/images/btn-popup-close.png" alt="닫기 버튼">
			</button>
			<table class="basic_table">
				<caption>비밀번호 변경</caption>
				<colgroup>
					<col style="width: 34.375%">
					<col style="width: 65.625%">
				</colgroup>
				<tbody>
				<tr>
					<th>변경 비밀번호</th>
					<td>
						<input class="input-one input-h40" type="password" name="changePassword" id="changePassword" placeholder="영문, 숫자 및 특수문자 포함 8자 이상 20자 이하" style="font-size:1rem;"/>
						<!-- <p id="is_comb" class="txt-alert center" style="display:none;font-size: 1.2rem;">8자 이상 20자 이하, 영문 대소문자/숫자/특수문자 포함</p> -->
					</td>
				</tr>
				<tr>
					<th>비밀번호 확인</th>
					<td>
						<input class="input-one input-h40" type="password" name="confirmPassword" id="confirmPassword" style="font-size:1rem;"/>
						<!-- <p id="is_password" class="txt-alert center" style="display:none;font-size: 1.2rem;">8자 이상 20자 이하, 영문 대소문자/숫자/특수문자 포함</p>
						<p id="is_same" class="txt-alert center" style="display:none;font-size: 1.2rem;">비밀번호가 같지 않습니다</p> -->
					</td>
				</tr>
				</tbody>
			</table>
			<div class="btn-wrap mt12">
				<button type="button" class="btn-one btn-h40 btn-color-sub1" id="btnPassUpdate">비밀번호 변경</button>
			</div>
		</div>
		<div class="mask"></div>
	</form>
</div>