
$(document).ready(function(){

  // 버튼 클릭 시 팝업 열림
  $(".btnOpenPopup").click(function(){
    $("#popFrm").find("input").val('');
    var popup_height = $(".popupWrap").height()/2;
    $(".popupWrap").css("margin-top",-popup_height);
    $(".popupWrap, .mask").show();
  });

  // 팝업 내 'X' 버튼 클릭 시 팝업 닫힘
  $(".btnClosePopup, .btnCancel").click(function(){
    $(".popupWrap, .mask").hide();
  });

  // 브라우저 사이즈의 변화가 있을 시, 팝업창 css의 margin-top값 변경
  $(window).resize(function(){
    var resize_popup_height = $(".popupWrap").height()/2;
    $(".popupWrap").css("margin-top",-resize_popup_height);
  });

});

// 20200806추가 : 우편번호 버튼 클릭 시, 팝업 노출 위치
$(document).ready(function(){
  $("div[id^='__daum__layer_']").attr('style', 'position: fixed !important');
});
