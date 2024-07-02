function selectAll(selectAll)  {
const checkboxes
 = document.getElementsByName('agreeYn');

checkboxes.forEach((checkbox) => {
checkbox.checked = selectAll.checked;
})
}

//
$(document).ready(function(){

$(".btn-dropdown").click(function(){

   var subcontent = $(this).parent().siblings(".agreeContent, .list-contWrap");

   if( subcontent.is(":visible") ){
       subcontent.slideUp();
       $(this).parent(".btn-dropdownWrap").children(".btn-dropdown").children("img").attr("src", "/resources/images/btn_dropDown_off.png");
   }else{
       subcontent.slideDown();
       $(this).parent(".btn-dropdownWrap").children(".btn-dropdown").children("img").attr("src", "/resources/images/btn_dropDown_on.png");
   }
});

});
