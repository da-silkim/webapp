<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
</style>
<script>
	const userId = "${userid}";
	const userName = "${username}";
	var myGrid = null;

	$(document).ready(function () {
		setData();
	})

	function setData() {
		var param = {};
		var searchWord = $("#searchWord").val()||"";
		var sysSelect = $("#sysSelect").val()||"";
		param.searchWord = searchWord;
		param.sysSelect = sysSelect;
		param.userid = userId;

		$.ajax({
			url: "/app/customer/board_list",
			type: 'get',
			data: param,
			dataType:"json",
			success : function(data) {
				$("#boardBody").empty();

				var listInfo = data.rows;

				var html = "";

				if( listInfo.length == 0 ) {
					html = '<tr><td colspan=4>데이터가 없습니다</td></tr>';
				}

				data.rows.forEach(function(item){
					var date = item.fmtRegDate||"";
					var views = $fnNumComma(item.views);
					html +='<tr>';
					html +='<td class="left notiTit"><a href="/app/customer/my_noti_detail?boardId='+item.id+'">'+item.subject+'</a></td>';
					html +='<td>'+date+'</td>';
					html +='<td>'+ views +'</td>';
					html +='</tr>';
				});

				$("#boardBody").append(html);
			}
		});
	}

	$(document).on("click", "#searchButton", function(e) {
		e.preventDefault();
		var id = $(this).attr("id");

		if( id == "searchButton") {
			setData();
		}
	});
</script>
<div class="contents-wrap cont-padding-typeA notiList-wrap">
	<!-- 20220214: as is 활용코드 :s -->
	<form action="" method="post">
		<!-- 검색영역 :s -->
		<div class="search-wrap mb20">
			<div class="cateSearch selectSearch-wrap mb10">
				<div class="select-wrap">
					<select class="select-one select-h40" name="searchType" id="sysSelect">
						<option value="all">전체</option>
						<option value="subject">제목</option>
						<option value="content">내용</option>
					</select>
				</div>
				<input type="text" class="input-h40" id="searchWord" value="" placeholder="검색어를 입력하세요.">
			</div>

			<div class="input-wrap">
				<input type="button" class="input-one input-h40 btn-color-sub1" id="searchButton" value="조회" title="조회">
			</div>
		</div>
		<!-- //검색영역 :e -->
		<div class="contents-wrap cont-padding-typeA notiList-wrap">

			<!-- 공지사항 리스트 :s -->
			<table class="basic_table3">
				<colgroup>
					<col style="width:51%">
					<col style="width:29%">
					<col style="width:20%">
				</colgroup>
				<thead>
				<tr>
					<th>제목</th>
					<th>작성일</th>
					<th>조회수</th>
				</tr>
				</thead>
				<tbody id="boardBody"></tbody>
			</table>
			<!-- //공지사항 리스트 :e -->
		</div>
	</form>
</div>