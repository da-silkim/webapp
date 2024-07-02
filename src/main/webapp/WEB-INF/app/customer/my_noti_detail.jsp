<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>

</script>
<div class="contents-wrap cont-padding-typeA notice-detail-wrap">
	<!-- 공지사항 상세 :s-->
	<div class="table-wrap">
		<table class="basic_table2">
			<colgroup>
				<col style="width:20%">
				<col style="width:50%">
				<col style="width:20%">
				<col style="width:auto">
			</colgroup>
			<tbody>
			<tr>
				<th>제목</th>
				<td colspan="3" id="subject">${subject}</td>
			</tr>
			<tr>
				<th>등록일</th>
				<td id="fmtRegDate">${fmtRegDate}</td>
				<th>조회수</th>
				<td id="views" class="tetcenter">${views}</td>
			</tr>
			</tbody>
		</table>
		<textarea style="height:500px;width:88%;border:0;overflow-y: scroll;" id="content" disabled>${content}</textarea>
	</div>
</div>