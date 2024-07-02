$(document).ready(function(){

  //'충전소검색 버튼' 클릭 시, '충전소검색 레이어팝업' 노출
  $(".btn_open-charSearch").click(function(){

     var searchCont = $(this).siblings("#charSearch-popup");

     if( searchCont.is(":visible") ){
         searchCont.slideUp();
     }else{
         searchCont.slideDown();
     }
  });

  //'충전소검색 레이어팝업' 내 '닫기 버튼' 클릭 시, 레이어 닫힘
  $("#charSearch-popup .btn_close-slidePopup").click(function(){

     var searchCont2 = $(this).parent("#charSearch-popup");

     if( searchCont2.is(":visible") ){
         searchCont2.slideUp();
     }else{
         searchCont2.slideDown();
     }
  });

  //범례
  $(".btn_view-stateList").click(function(){

     var stateList = $(this).siblings(".stateList");

     if( stateList.is(":visible") ){
         stateList.slideUp();
         $(this).children("img").attr("src", "/resources/images/map_off.png");
     }else{
         stateList.slideDown();
         $(this).children("img").attr("src", "/resources/images/map_on.png");
     }
  });

  //'마커' 클릭 시, '충전소 상세보기 팝업' 노출
  $(".mark > button").click(function(){

    $("#charSearch-popup").css("display", "none");

     var charInfo = $(".charInfo-wrap");
     var thisMark = $(this);

     if( charInfo.is(":visible") ){
         charInfo.slideUp();
         $(".mark > button").removeClass("off");
         $(thisMark).addClass("off");
     }else{
         charInfo.slideDown();
         $(".mark > button").addClass("off");
         $(thisMark).removeClass("off");
     }
  });

  //'충전소 상세보기 팝업' 내 '닫기 버튼' 클릭 시 닫힘
  $("#charInfo-popup .btn_close-slidePopup").click(function(){

     var charInfo2 = $(".charInfo-wrap");
     var thisMark2 = $(this);

     if( charInfo2.is(":visible") ){
         charInfo2.slideUp();
         $(".mark > button").removeClass("off");
     }else{
         charInfo2.slideDown();
     }
  });


});
