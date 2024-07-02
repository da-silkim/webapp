<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	let customFavId = "";
	$(document).ready(function() {
		fn_list();
	});

	function fn_list(pageIndex) {
		$.ajax({
			url : "/app/charge/my_charge/action",
			type : 'post',
			data : {},
			async : false,
			success : function(res) {
				if (res['result']['message'] != 'success') {
					console.log("fail");
				} else {
					fn_gridList(res['data']['list']);

				}

			}
		});

	}

	function fn_gridList(info) {
		$("#csList").empty();
		let html = "";
		for (let i = 0; i < info.length; i++) {
			html += '<div class="station-info-wrap list">';
			html += '<div class="info-subwrap">';
			html += '<div class="tit">';
			html += '<p class="station-name">';
			html += '<span>' + info[i].name + '</span>';
			html += '<button type="button" class="btn-bookmark favDel" data-id="'+ info[i].favId +'"><img src="/resources/images/bookmark_on.png" alt="즐겨찾기'+ info[i].favId +'"></button>';
			html += '</p></div>';
			html += '<ul class="detail">';
			html += '<li class="address">' + info[i].fullAddr + '</li>';

			if (info[i].parkingFeeYn == "Y") {
				html += '<li class="parkPrice">주차요금 : <span>'
						+ info[i].parkingFee + ' 원</span></li>';
			} else {
				html += '<li class="parkPrice">주차요금 : <span>없음</span></li>';
			}
			var _openStartTime = "00:00"
			var _openEndTime = "00:00"
			if (info[i].openStartTime != null
					&& info[i].openStartTime.length == 4) {
				_openStartTime = info[i].openStartTime[0]
						+ info[i].openStartTime[1] + ':'
						+ info[i].openStartTime[2] + info[i].openStartTime[3];
			}
			if (info[i].openEndTime != null && info[i].openEndTime.length == 4) {
				_openEndTime = info[i].openEndTime[0] + info[i].openEndTime[1]
						+ ':' + info[i].openEndTime[2] + info[i].openEndTime[3];
			}
			html += '<li class="time">운영시간 : ' + _openStartTime + ' ~ '
					+ _openEndTime + '</li>';
			html += '<li class="charType">';
			if (info[i].cpCount1 > 0) {
				html += '<span>' + info[i].cpType1+' '+'<span class="num">' + info[i].cpCount1
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount2 > 0) {
				html += '<span>' + info[i].cpType2+' '+'<span class="num">' + info[i].cpCount2
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount3 > 0) {
				html += '<span>' + info[i].cpType3+' '+'<span class="num">' + info[i].cpCount3
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount4 > 0) {
				html += '<span>' + info[i].cpType4+' '+'<span class="num">' + info[i].cpCount4
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount5 > 0) {
				html += '<span>' + info[i].cpType5+' '+'<span class="num">' + info[i].cpCount5
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount6 > 0) {
				html += '<span>' + info[i].cpType6+' '+'<span class="num">' + info[i].cpCount6
						+ '</span><span>기 </span></span>';
			}
/* 			if (info[i].cpCount1 > 0) {
				html += '<span>' + info[i].cpType1 + ' (' + info[i].cpPrice4
						+ '원/kWh)<span class="num">' + info[i].cpCount1
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount2 > 0) {
				html += '<span>' + info[i].cpType2 + ' (' + info[i].cpPrice4
						+ '원/kWh)<span class="num">' + info[i].cpCount2
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount3 > 0) {
				html += '<span>' + info[i].cpType3 + ' (' + info[i].cpPrice4
						+ '원/kWh)<span class="num">' + info[i].cpCount3
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount4 > 0) {
				html += '<span>' + info[i].cpType4 + ' (' + info[i].cpPrice4
						+ '원/kWh)<span class="num">' + info[i].cpCount4
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount5 > 0) {
				html += '<span>' + info[i].cpType5 + ' (' + info[i].cpPrice5
						+ '원/kWh)<span class="num">' + info[i].cpCount5
						+ '</span><span>기 </span></span>';
			}
			if (info[i].cpCount6 > 0) {
				html += '<span>' + info[i].cpType6 + ' (' + info[i].cpPrice6
						+ '원/kWh)<span class="num">' + info[i].cpCount6
						+ '</span><span>기 </span></span>';
			} */
			html += '</li></ul>';

			html += '<div class="btn-wrap flex">';
			//html += '<a href="fn_map('+info[i].id+');" class="btn-three btn-h40 btn-color-main1">지도 보기</a>';
			html += '<div onclick="fn_map('
					+ info[i].id
					+ ');" class="btn-three btn-h40 btn-color-main1">지도 보기</div>';
			html += '<a onclick="fn_mapDetail(' + info[i].id
					+ ');" class="btn-three btn-h40 btn-color-main1">상세 보기</a>';
			html += '<a onclick="fn_voc(&quot;'
					+ info[i].id
					+ '&quot;);" class="btn-three btn-h40 btn-color-main1">고장 신고</a>';
			html += '</div>';
			html += '</div></div>';
		}

		if (info.length == 0) {
			html += '<div class="station-info-wrap list">';
			html += '<div class="info-subwrap">';
			html += '<div class="tit">';
			html += '<p class="station-name">';
			html += '<span>마이 충전소 내역이 없습니다.</span></p>';
			html += '</div>';
			html += '</div></div>';
		}

		$("#csList").append(html);
	}

	$(document).on("click", ".favDel", function() {
		let favId = $(this).attr("data-id");

		if( confirm("즐겨찾기를 삭제하시겠습니까?")) {
			$.ajax({
				url : "/app/charge/my_charge/delete",
				type : 'post',
				data : {favId: favId},
				async : false,
				success : function(res) {
					if (res['result']['message'] != 'success') {
						console.log("fail");
					} else {
						fn_gridList(res['data']['list']);
					}
				}
			});
		}
	});

	function fn_map(idx) {
		fn_setCookie("csId", idx, 1);
		location.href = "/app/find/find_map?id=" + idx;
	}

	function fn_mapDetail(idx) {
		location.href = "/app/find/cs_detail?id=" + idx;

	}
	function fn_voc(idx) {
		location.href = "/app/customer/my_voc?csId="+idx;

	}
</script>
<div id="wrap" class="flex-center">
	<div id="body" class="flex-body full-page">
		<div class="contents-wrap cont-padding-typeA bookmark-wrap" id="content">
			<div class="detailList evWrap" id="csList"></div>
		</div>
	</div>
</div>

