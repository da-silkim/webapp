<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	const userId = "${userid}";
	const userName = "${username}";
	const csId = "${csId}";
	var latitude = 37.566535;
	var longitude = 126.9779692;

	$(document).ready(function() {
		$("#userId").val(userId);

		// 위치정보에 따른 충전소 검색
		getLocation();
		// as 접수 종류
		selectAsType();

		// 첨부파일
		$('input[type=file]').on('change',function(){
			if(window.FileReader){
				var filename = $(this)[0].files[0].name;
			} else {
				var filename = $(this).val().split('/').pop().split('\\').pop();

			}
			$(this).siblings('label').text(filename);

		});

		//이미지파일 체크
		function fnImgChk(fileName){
			var fileNameExt = fileName.substring(fileName.lastIndexOf(".")+1);
			fileNameExt = fileNameExt.toLowerCase();

			if("jpg" != fileNameExt && "jpeg" != fileNameExt && "gif" != fileNameExt && "png" != fileNameExt && "bmp" != fileNameExt && "jfif" != fileNameExt){
				alert("이미지(jpg, jpeg, gif, png, bmp, jfif) 파일이 아닙니다.");
				$("#atchFileId").siblings('label').text("");
				return false;
			} else {
				return true;
			}
		}
	});

	function getLocation() {
		$.ajax({
			url: "/app/customer/station_list",
			type: 'get',
			dataType:"json",
			data: {latitude: latitude, longitude: longitude},
			success : function(res) {
				$("#cs").empty();
				var stationList = res;
				var html = "";

				html += '<option value="">선택</option>';
				stationList.forEach(function(item) {
					html += '<option value='+item.id+'>'+item.name+'</option>';
				});

				$("#cs").append(html);

				if (csId) {
					$("#cs").val(csId);
					selectCp(csId);
				}
			}
		});
		/* if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function(position) {
				latitude = position.coords.latitude;
				longitude = position.coords.longitude;

				
			});
		} else {
			alert("고객님의 위치정보가 없습니다.");
		} */
	}

	function showPosition(position) {
		latitude = position.coords.latitude;
		longitude = position.coords.longitude;
	}

	function formCheck() {
		var opinion = $("#opinionType").val() || 0;
		var cs = $("#cs").val() || 0;
		var cp = $("#cp").val() || 0;
		var subject = $("#subject").val() || 0;
		var content = $("#content").val() || 0;

		if (cs == 0) {
			alert("충전소를 선택해주세요.");
			return false;
		}

		if (cp == 0) {
			alert("충전기를 선택해주세요.");
			return false;
		}

		if (opinion == 0) {
			alert("AS접수 종류를 선택해주세요.");
			return false;
		}

		if (subject == 0) {
			alert("제목을 입력해주세요.");
			return false;
		}

		if (content == 0) {
			alert("상세 내용을 입력해주세요.");
			return false;
		}

		return true;
	}

	function selectCp(csId) {
		$.ajax({
			url: "/app/customer/charger_list",
			type: 'get',
			dataType:"json",
			data: {csId: csId},
			success : function(res) {
				if (res.length == 0) {
					alert("해당 충전소에 충전기가 없습니다.");
				}

				$("#cp").empty();
				var chargerList = res;
				var html = "";

				html += '<option value="">선택</option>';
				chargerList.forEach(function(item) {
					html += '<option value='+item.id+'>'+item.name+'</option>';
				});

				$("#cp").append(html);
			}
		});
	}

	function selectAsType() {
		$.ajax({
			url: "/app/customer/as_type_list",
			type: 'get',
			dataType:"json",
			data: {groupCd: "VOC001"},
			success : function(res) {
				$("#opinionType").empty();
				var opinionTypeList = res;
				var html = "";

				html += '<option value="">선택</option>';
				opinionTypeList.forEach(function(item) {
					html += '<option value='+item.cd+'>'+item.cdNm+'</option>';
				});

				$("#opinionType").append(html);
			}
		});
	}

	$(document).on("click", "#registBtn", function(e) {
		e.preventDefault();
		var id = $(this).attr("id");

		//문의하기 팝업 호출
		if( id == "registBtn") {
			if( !formCheck() ) return;

			if( confirm("고장 신고를 등록하시겠습니까?") ) {
				var formData = new FormData($('#brokenForm')[0]);
				var param = $('form[name="brokenForm"]').serialize();;

				formData.append("file", $('#atchFileId')[0].files[0]);
				formData.append("param", JSON.stringify(param));
				$.ajax({
					url: "/app/customer/broken_report",
					type: 'post',
					data : formData, 	//필수 //JSON.stringify(param)
					enctype: 'multipart/form-data', // 필수 //'multipart/form-data'
					dataType : "json",
					processData: false,
					contentType: false,
					success : function(res) {
						if(res.result.message == "success") {
							alert("등록되었습니다");
							location.href = "/app/main";
						} else {
							alert(res.result.content);
						}
					}
				});
			}
		}
	});
</script>
<div id="wrap" class="flex-center">
	<div class="contents-wrap bgGray cont-padding-typeA broken-wrap">
	  	<form id="brokenForm" name="brokenForm" method="post" action="">
	    	<span id="csId" style="display:none;"></span>
			<input type="hidden" name="userId" id="userId"/>
	    	<div style="width:0px;height:0px;position:absolute;left:-100px;top:-100px;overflow:hidden">
	      		<input type="hidden" name="brokenForm4_hf_0" id="brokenForm4_hf_0" />
	    	</div>
	   		<div class="table-wrap">
	      		<table class="basic_table">
	        		<caption>A/S 접수</caption>
	        		<colgroup>
			          	<col style="width:34%">
			          	<col style="width:66%">
	        		</colgroup>
	        		<tbody>
	          			<tr>
	            			<th>충전소
								<span class="essential-icon">
                                	<img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            	</span>
							</th>
	            			<td class="select-wrap">
	              				<select class="select-one select-h40"id="cs" name="cs" onchange="selectCp(value);">
									<option value="">선택</option>
								</select>
	            			</td>
	          			</tr>
	          			<tr>
	            			<th>충전기
								<span class="essential-icon">
                                	<img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            	</span>
							</th>
	            			<td class="select-wrap">
	              				<select class="select-one select-h40"id="cp" name="cp">
									<option value="">선택</option>
								</select>
	            			</td>
	          			</tr>
	          			<tr>
	            			<th>AS접수 종류
								<span class="essential-icon">
                                	<img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            	</span>
							</th>
	            			<td class="select-wrap">
	              			<select class="select-one select-h40" id="opinionType" name="opinionType">
								<option value="">선택</option>
							</select>
	            			</td>
	          			</tr>
	          			<tr>
	            			<th>제목
								<span class="essential-icon">
                                	<img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            	</span>
							</th>
	            			<td>
	              				<input class="input-one input-h40" type="text" placeholder="제목을 입력하세요." id="subject" value="" name="subject">
	            			</td>
	          			</tr>
	          			<tr>
	            			<th class="tettop">상세 내용
								<span class="essential-icon">
                                	<img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            	</span>
							</th>
	            			<td>
	              				<textarea placeholder="고장 내용을 입력하세요." id="content" name="content"></textarea>
	            			</td>
	          			</tr>
	          			<tr>
	            			<th class="tetmiddle">사진</th>
	            			<td class="btn-inputInclude-wrap">
	              				<input class="input-one input-h40" id="atchFileId" type="file" value="찾아보기">
	              				<label for="atchFileId"></label>
	            			</td>
	          			</tr>
	        		</tbody>
	      		</table>
	      		<div class="btn-wrap">
	        		<button type="button" id="registBtn" class="btn-two btn-h40 btn-color-main1">제출</button>
	        		<a href="javascript: history.back();" class="btn-two btn-h40 btn-color-main1 reverse">취소</a>
	      		</div>
	    	</div>
	  	</form>
	</div>
</div>