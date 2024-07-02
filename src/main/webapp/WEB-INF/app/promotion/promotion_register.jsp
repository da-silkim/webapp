<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
    const userId = "${userid}";
    const customerName = "${username}";
    var _param = {};

$(document).ready(function () {
    setData();
});

function setData() {
    $.ajax({
        url:"/app/promotion/promotion_list",
        type:"get",
        dataType:"json",
        data: {userId: userId},
        success:function(data){
            dataInsert(data);
        }
    });
}

function dataInsert(data) {
    var result = data.result;
    var html = "";
    var i = 0;
    result.forEach(function(item) {
        i++;
        html += '<li class="promo-list">';
        html += '	<div class="btn-dropdownWrap">';
        html += '		<button type="button" class="btn-dropdown" id="btnDropdown"><img src="/resources/images/btn_common_dropOpen.png" alt="드롭다운버튼"></button>';
        html += '	</div>';
        html += '	<div class="list-Tit">'+item.promotionNm+'</div>';
        html += '	<div class="list-contWrap">';
        html += '		<div class="list-contSub">';
        html += '			<p class="proGuide">'+item.remark+'</p>';
        if (item.useYn >= 1) {
            html += '			<input class="input-one input-h40" type="text" id="authCode" name="authCode" placeholder="이미 등록한 프로모션입니다." readonly>';
        } else {
            html += '			<div class="input-wrap">';
            html += '				<input class="input-one input-h40" type="text" id="authCode" name="authCode" placeholder="프로모션 코드를 입력하세요">';
            html += '				<input type="text" id="promotionIdHid" name="promotionIdHid" style="display:none;" value="'+item.promotionId+'">';
            html += '			</div>';
            html += '			<div class="btn-wrap">';
            html += '				<button class="btn-one btn-h40 btn-color-main1" type="button" id="btnAuth" name="btnAuth">등록</button>';
            html += '			</div>';
        }
        html += '		</div>';
        html += '	</div>';
        html += '</li>';
    });

    $("#promotionReg").append(html);
}

//드롭다운버튼
$(document).on("click", "#btnDropdown", function () {
    var submenu = $(this).parent().next("div").next("div");

    if( submenu.is(":visible") ){
        submenu.slideUp();
        $(this).children("img").attr("src", "/resources/images/btn_common_dropOpen.png");
    }else{
        submenu.slideDown();
        $(this).children("img").attr("src", "/resources/images/btn_common_dropClose.png");
    }
});

//프로모션 인증코드 조회
$(document).on("click", "#btnAuth", function () {
    if( $(this).parent().parent().find("#authCode").val() == "" ) {
        alert("코드를 입력해주세요.");
        return;
    } else {
        if( confirm("프로모션을 등록하시겠습니까?")) {
            var authCode = $(this).parent().parent().find("#authCode").val();
            var promotionId = $(this).parent().parent().find("#promotionIdHid").val();
            _param = { authCode : authCode, promotionId : promotionId, userId: userId };

            $.ajax({
                url:"/app/promotion/promotion_register",
                type:"get",
                dataType:"json",
                data: _param,
                success:function(data){
                    if(data.result.message == "success") {
                        alert("프로모션이 등록되었습니다.");
                        $('[id^=authCode]').val("");
                    } else {
                        alert(data.result.content);
                    }
                }
            });
        }
    }
});
</script>
<!-- //Common Version -->

<!-- contents -->
<div class="contents-wrap bgGray cont-padding-typeA promo-wrap">
    <div class="cont-subwrap">
        <form action="" method="post" name="promotion">
            <ul id="promotionReg">
            </ul>
        </form>
    </div>
</div>