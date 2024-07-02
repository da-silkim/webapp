<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	const userId = "${userid}";
	const userName = "${username}";
	var _param = {};


	$(document).ready(function() {
		setData();
		$("#popUserId").val(userId);
	});

	function setData() {
		$.ajax({
			url: "/app/customer/my_info_set_data",
			type: 'get',
			data: {userId:userId},
			dataType:"json",
			success : function(res) {
				detailCallback(res);
			}
		});
	}

	$(document).on("click", "#_btnZipcode, #btnUpdate , #btnPassUpdate , #btnWithdrawal", function(e) {
		e.preventDefault();
		var id = $(this).attr("id");

		if( id == "_btnZipcode") {
			openZipCode( 'zipcode','address','addressDetail' );

		} else 	if( id == "btnUpdate") {
			if( !validator() ) return;
			if( confirm("저장 하시겠습니까?") ) {
				var data = $('form[name="frm"]').serialize();
				$.ajax({
					url: "/app/customer/modify_my_info",
					type: 'post',
					data: data,
					success : function(res) {
						if (res.result.message !== "success") {
							alert(res.result.content);
						} else {
							alert("저장 되었습니다.");
							return false;
						}
					}
				});
			}

		} else if( id == "btnPassUpdate" ) {
			if( !validatorPop() ) return;
			if( confirm("비밀번호를 변경 하시겠습니까?") ) {
				var data = $('form[name="popFrm"]').serialize();
				$.ajax({
					url: "/app/customer/modify_password",
					type: 'post',
					data: data,
					success : function(res) {
						if (res.result.message !== "success") {
							alert(res.result.content);
						} else {
							bridgeUtil.setAutologinModifyPassword($('#confirmPassword').val())
							alert("저장 되었습니다.");

							$(".popupWrap, .mask").hide();
							return false;
						}
					}
				});
			}

		} else if( id == "btnWithdrawal" ) {
			//회원탈퇴 버튼 추가
			if( _param.totDispayFee > 0 ){
				alert("미수금 " + gf_comma(_param.totDispayFee) + "원이 있습니다.\n정산 후 탈퇴 가능합니다.");

			} else if( confirm("정말 탈퇴하시겠습니까?\n(재가입 시, 동일 ID사용 불가)") ) {
				var data = $('form[name="frm"]').serialize();
				$.ajax({
					url: "/app/customer/withdrawal",
					type: 'post',
					data: data,
					success : function(res) {
						if (res.result.message !== "success") {
							alert(res.result.content);
						} else {
							alert("탈퇴 되었습니다.");
							location.href = '/app/member/logout';
						}
					}
				});
			}
		}
	});

	function withdrawalCallback( data ) {
		if( data.result == "fail" ) {
			return false;
		} else {
			footer("");
			alert("탈퇴처리 되었습니다");
			location.href="/mobile/login";
		}
	}

	function detailCallback(data) {
		$("table tbody td").find("input").each( function( idx ) {
			var id = $(this).attr("id");
			var tagName = $(this).prop("tagName").toUpperCase();
			if( tagName == "INPUT"  ) {
				var type = $(this).attr("type");
				if(type != "radio"){
					if( id == "mobile" ) {
						$("#"+id).val( fn_makeMobileNumber( data.result["mobile"] ) );
					} else {
						$("#"+id).val( data.result[id]) ;
					}
				}
			}
		});

		//회원탈퇴 버튼 개인회원만 가능함.
		if( data.result?.customerType == "P" ){
			$("#btnWithdrawalId").show();
		} else {
			$("#btnWithdrawalId").hide();
		}

		_param.totDispayFee = data.result?.totDispayFee;
	}

	function validatorPop() {
		if( $.trim( $("#prePassword").val() ).length == 0 ) {
			alert("현재 비밀번호를 입력해 주세요.");
			$("#prePassword").focus();
			return false;
		}
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
		if( $("#prePassword").val() == $("#changePassword").val() ) {
			alert("현재 비밀번호와 변경 비밀번호가 일치합니다.");
			return false;
		}
		return true;
	}

	function validator() {
		//성명
		if( $.trim( $("#customerName").val() ).length == 0 ) {
			alert("성명을 입력해주세요");
			$("#customerName").focus();
			return false;
		}
		if( $.trim( $("#brithDay").val() ).length == 0 ) {
			alert("생년월일을 입력해주세요");
			$("#brithDay").focus();
			return false;
		}
		if( $.trim( $("#mobile").val() ).length == 0 ) {
			alert("휴대폰번호를 입력해주세요");
			$("#mobile").focus();
			return false;
		}
		if( $.trim( $("#email").val() ).length == 0 ) {
			alert("이메일을 입력해주세요");
			$("#mobile").focus();
			return false;
		}
		//주소 우편번호
		if( $.trim( $("#zipcode").val() ).length == 0 ) {
			alert("우편번호를 입력해주세요");
			$("#zipcode").focus();
			return false;
		}

		if( $.trim( $("#address").val() ).length == 0 ) {
			alert("주소를 입력해주세요");
			$("#address").focus();
			return false;
		}

		if( $.trim( $("#addressDetail").val() ).length == 0 ) {
			alert("상세주소를 입력해주세요");
			$("#addressDetail").focus();
			return false;
		}
		if( $.trim( $("#carNo").val() ).length == 0 ) {
			alert("차량번호를 입력해주세요");
			$("#carNo").focus();
			return false;
		}
		return true;
	}

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
<div class="contents-wrap bgGray cont-padding-typeA modifyMyinfo-wrap">
	<div id="__layer__" style="display:none;position:fixed;overflow:hidden;z-index:1;-webkit-overflow-scrolling:touch;">
		<img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnCloseLayer" style="cursor:pointer;position:absolute;right:-3px;top:-3px;z-index:1" onclick="closeDaumPostcode()" alt="닫기 버튼">
	</div>

	<form method="post" name="frm">
		<fieldset>
			<legend>내 정보 관리</legend>
			<div class="table-wrap">
				<table class="basic_table">
					<caption>기본정보</caption>
					<colgroup>
						<col style="width:35%">
						<col style="width:65%">
					</colgroup>
					<tbody>
					<tr>
						<th>성명<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input class="input-one input-h40" type="text" name="customerName" id="customerName" >
						</td>
					</tr>
					<tr id="idTr">
						<th>아이디</th>
						<td>
							<input class="input-one input-h40 input-disable" type="text" name="userId" id="userId" readonly>
						</td>
					</tr>
					<tr id="passwordTr">
						<th>비밀번호<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<p class="btn-inputInclude-wrap">
								<input class="input-one input-h40" type="password" name="password" id="password" readonly/>
								<button type="button" class="btn-h32 btn-color-sub1 btnOpenPopup">비밀번호	변경</button>
							</p>
						</td>
					</tr>
					<tr class="birthTr">
						<th>생년월일<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input class="input-one input-h40" type="text" name="brithDay" id="brithDay" numberOnly maxlength="8">
						</td>
					</tr>
					<tr>
						<th>휴대폰 번호<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input class="input-one input-h40" type="text" name="mobile" id="mobile" numberOnly maxlength="11">
						</td>
					</tr>
					<tr id="emailTr">
						<th>이메일<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input class="input-one input-h40" type="text" name="email" id="email" maxlength="100">
						</td>
					</tr>
					<tr>
						<th>주소<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<p class="btn-inputInclude-wrap mb10">
								<input class="input-one input-h40" type="text" name="zipcode" id="zipcode" numberOnly>
								<button type="button" class="btn-h32 btn-color-sub1" id="_btnZipcode">우편번호</button>
							</p>
							<p class="mb10">
								<input class="input-one input-h40" type="text" name="address" id="address" numberOnly>
							</p>
							<p>
								<input class="input-one input-h40" type="text" name="addressDetail" id="addressDetail">
							</p>
						</td>
					</tr>
					<tr>
						<th>차량 번호<span class="essential-icon"><img src="/resources/images/ico_essential.png" alt="필수 입력사항"></span></th>
						<td>
							<input class="input-one input-h40" type="text" name="carNo" id="carNo">
						</td>
					</tr>
					<tr>
						<th>적용 프로모션</th>
						<td>
							<input class="input-one input-h40" type="text" name="promotionNm" id="promotionNm" readonly="readonly" placeholder="미 적용 시 공란">
							<input class="input-one input-h40" type="text" name="remark" id="remark" readonly="readonly" placeholder="미 적용 시 공란">
						</td>
					</tr>
					</tbody>
				</table>
				<div>
					<a class="btn-wrap" href="" id="btnWithdrawalId">
						<button type="button" class="btn-one_full btn-h32 btn-color-sub1" id="btnWithdrawal">회원 탈퇴</button>
					</a>
				</div>
			</div>

			<!-- 저장하기 버튼 :s -->
			<div class="btn-wrap bottomFixed">
				<button type="submit" class="btn-one btn-h60 btn-gradient btn-rectangle" id="btnUpdate">저장하기</button>
			</div>
			<!-- 저장하기 버튼 :e -->
		</fieldset>
	</form>
	<!-- modify :e -->

	<!-- popup :s -->
	<form name="popFrm" id="popFrm">
		<input type="hidden" name="popUserId" id="popUserId"/>
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
						<th>현재 비밀번호</th>
						<td><input class="input-one input-h40" type="password" name="prePassword" id="prePassword"></td>
					</tr>
					<tr>
						<th>변경 비밀번호</th>
						<td>
							<input class="input-one input-h40" type="password" name="changePassword" id="changePassword" placeholder="영문, 숫자 및 특수문자 포함 8자리 이상"/>
						</td>
					</tr>
					<tr>
						<th>비밀번호 확인</th>
						<td><input class="input-one input-h40" type="password" name="confirmPassword" id="confirmPassword"/></td>
					</tr>
				</tbody>
			</table>
			<div class="btn-wrap mt12">
				<button type="button" class="btn-one btn-h40 btn-color-sub1" id="btnPassUpdate">비밀번호 변경</button>
			</div>
		</div>
		<div class="mask"></div>
	</form>
	<!-- popup :e -->
</div>