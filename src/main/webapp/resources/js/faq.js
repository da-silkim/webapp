// 1:1문의 메뉴 클릭 시 서브메뉴 온, 오프
$(document).on("click", ".oneque-wrap .menu", function () {
	  var submenu2 = $(this).next(".menu-sub").find(".menu-sub-subwrap");
		var submenu3 = $(this).next(".menu-sub");
    if( submenu2.is(":visible") ){
        submenu2.slideUp();
				submenu3.css("border-top","none");
    }else{
        submenu2.slideDown();
				submenu3.css("border-top","1px solid #e3e3e3");
    }
});

$(document).on("click", ".faqsub-wrap .menu", function () {
	  var submenu2 = $(this).next(".menu-sub");
		var submenu3 = $(this).next(".menu");
  if( submenu2.is(":visible") ){
      submenu2.slideUp();
				submenu3.css("border-top","none");
  }else{
      submenu2.slideDown();
				submenu3.css("border-top","1px solid #e3e3e3");
  }
});

//문의하기 Popup
function qnaPopup(obj) {
	var reserveYes_height = $("#qnaPop").height()/2;
    $(".popupWrap").css("margin-top",-reserveYes_height);
    $("#qnaPop, .mask").show();
}

$(document).on("click", ".btnClosePopup, .btnCancel", function(e) {
	$("#qnaPop, .mask").hide();
});


//수정하기 Popup
function qnaModifyPop(obj) {
	var reserveYes_height = $("#qnaModifyPop").height()/2;
    $(".popupWrap").css("margin-top",-reserveYes_height);
    $("#qnaModifyPop, .mask").show();
}

$(document).on("click", ".btnCloseModifyPopup, .btnModifyCancel", function(e) {
	$("#qnaModifyPop, .mask").hide();
});
