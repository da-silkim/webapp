// 충전 예약 현황 - 팝업 노출 js

$(document).ready(function(){

  //'충전 가능' 예약상태 클릭 시 팝업
  $(".btnOpenPopup.Yes").click(function(){
    var reserveYes_height = $("#reserveYes").height()/2;
    $(".popupWrap").css("margin-top",-reserveYes_height);
    $("#reserveYes, .mask").show();
  });

  $(".btnClosePopup, .btnCancel").click(function(){
    $("#reserveYes, .mask").hide();
  });

  $(window).resize(function(){
    var resize_reserveYes_height = $("#reserveYes").height()/2;
    $(".popupWrap").css("margin-top",-resize_reserveYes_height);
  });

  //'충전 불가능' 예약상태 클릭 시 팝업
  $(".btnOpenPopup.No").click(function(){
    var reserveNo_height = $("#reserveNo").height()/2;
    $(".popupWrap").css("margin-top",-reserveNo_height);
    $("#reserveNo, .mask").show();
  });

  $(".btnClosePopup, .btnCancel").click(function(){
    $("#reserveNo, .mask").hide();
  });

  $(window).resize(function(){
    var resize_reserveNo_height = $("#reserveNo").height()/2;
    $(".popupWrap").css("margin-top",-resize_reserveNo_height);
  });

});
