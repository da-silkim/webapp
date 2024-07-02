$(function() {
   //set your date picker
	$(".date-pick").datepicker({
		dateFormat: 'yy-mm-dd'
		, changeMonth: true
		, changeYear: true
		, showOn: "both"
		, buttonImage: "/resources/images/icon_calender.png"
		, buttonImageOnly: true
		, buttonImageText: "Calendar"
		, monthNames : ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월',]
		, monthNamesShort : [ '01','02','03','04','05','06','07','08','09','10','11','12']
		, dayNamesMin : ['일','월','화','수','목','금','토']
	});
});
