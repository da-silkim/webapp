
/* waitMe Documentation
 *
 * Basic
 * $('#elem').waitMe({}) - #elem is html object, click on which causes to show waitme.
 *
 * Options
 * effect - animation effect. Use: 'bounce' - default, 'none', 'rotateplane', 'stretch', 'orbit', 'roundBounce', 'win8', 'win8_linear', 'ios', 'facebook', 'rotation', 'timer', 'pulse', 'progressBar', 'bouncePulse', 'img'.
 * text - place text under the effect. Use: 'text'.
 * bg - background for container. Use: 'rgba(255,255,255,0.7)' - default, false.
 * color - color for background animation and text. Use: '#000' - default, ['','',...] - you can use multicolor for effect.
 * maxSize - set max size for elem animation. Use: '' - default, 40.
 * waitTime - wait time im ms to close. Use: -1 - default, 3000.
 * textPos - change text position. Use: 'vertical' - default, 'horizontal'.
 * fontSize - change font size. Use: '' - default, '18px'.
 * source - url to image. Use: '' - default, 'url' - for effect: 'img'.
 * onClose - code execution after popup closed. Use: function(event, el){}.
 *
 * Methods
 * hide - for close waitMe. Use: $(container).waitMe("hide");.
 *
 * Triggers
 * close - execution after closed. Use: $(el).on('close', function() {});.
 *
 */
function run_waitMe(el, loadingEffect, num){

    text = 'Please wait...';

    effect = 'progressBar'; 
    maxSize = '';
    textPos = 'vertical';
    fontSize = '12px';

    if (loadingEffect != null && loadingEffect != '') {
    	effect = loadingEffect;
    }
    
    if (num != null) {
    	switch (num) {
    	case 1:
    		maxSize = '';
    		textPos = 'vertical';
    		break;
    	case 2:
    		text = '';
    		maxSize = 30;
    		textPos = 'vertical';
    		break;
    	case 3:
    		maxSize = 30;
    		textPos = 'horizontal';
    		fontSize = '18px';
    		break;
    	};
    }

    $(el).waitMe({
        effect: effect,
        text: text,
        bg: 'rgba(255,255,255,0.7)',
        color: '#000',
        maxSize: maxSize,
        waitTime: -1,
        source: 'img.svg',
        textPos: textPos,
        fontSize: fontSize,
        onClose: function(el) {}
    });
}

function prograssbarOpen(){
	$('#asxLoading').show();
}
function prograssbarClose(){
	$("#asxLoading").hide();
}

var dep1;
var dep2;

function simpleAlert(strMsg, strType, strAlign, intWidth){

    var setIcon = 'fa fa-info-circle';
    var setColor = 'orange';

    if(convertUndefinedToString(strMsg)==""){
        $.alert({
            title : ' ',
            content : '<center>Please enter a message!</center>',
            type : 'red',
            icon: 'fa fa-exclamation-triangle',
            useBootstrap : false,
            boxWidth : '400px'
        });

        return false;
    }

    if(convertUndefinedToString(strAlign)!="") {
        if(strAlign=="left") {
            strMsg = '<left>' + strMsg + '</left>'
        } else {
            strMsg = '<center>' + strMsg + '</center>'
        }
    } else {
        strMsg = '<center>' + strMsg + '</center>'
    }

    if(convertUndefinedToString(intWidth)=="") {
        intWidth = 400;
    }

    if(convertUndefinedToString(strType).toUpperCase()=="ERROR") {
        setColor = 'red';
        setIcon = 'fa fa-exclamation-triangle';
    } else if(convertUndefinedToString(strType).toUpperCase()=="SUCCESS") {
        setIcon = 'fa fa-smile';
        setColor = 'green';
    }

    $.alert({
        title : ' ',
        content : strMsg,
        type : setColor,
        icon: setIcon,
        useBootstrap : false,
        boxWidth : intWidth + 'px'
    });
}

function getTimeStamp(date) {
	var d = new Date();
    if( date ){
    	d = new Date(d.setDate(d.getDate() + date));
    }

    var s = leadingZeros(d.getFullYear(), 4) + '-' +
        leadingZeros(d.getMonth() + 1, 2) + '-' +
        leadingZeros(d.getDate(), 2);
    return s;
}

function getDayTimeStamp() {
    var d = new Date();

    var s = leadingZeros(d.getFullYear(), 4) + '년 ' +
        leadingZeros(d.getMonth() + 1, 2) + '월 ' +
        leadingZeros(d.getDate(), 2) + '일 ' +
        leadingZeros(d.getHours(), 2) + ':' +
        leadingZeros(d.getMinutes(), 2);
    return s;
}

function getFirstDayTimeStamp() {
   var d = new Date();

   var s = leadingZeros(d.getFullYear(), 4) + '-' +
        leadingZeros(d.getMonth() +1,1) + '-' + '01' ;
    return  s;
}

function setYearStamp(date, year) {
	var d = new Date(date);
    if( date ){
    	d = new Date(d.setYear(d.getFullYear() + year));
    }

    var s = leadingZeros(d.getFullYear(), 4) + '-' +
        leadingZeros(d.getMonth() + 1, 2) + '-' +
        leadingZeros(d.getDate(), 2);
    return s;
}

function getYearStamp() {
	var d = new Date();
    return d.getFullYear();
}

function getMonthStamp() {
	var d = new Date();
    return d.getMonth()+1;
}

function leadingZeros(n, digits) {
    var zero = '';
    n = n.toString();

    if (n.length < digits) {
        for (i = 0; i < digits - n.length; i++)
            zero += '0';
    }
    return zero + n;
}

function getDateDiff(d1, d2) {
    const date1 = new Date(d1);
    const date2 = new Date(d2);
    const diffDate = date1.getTime() - date2.getTime();
    console.log("diffDate : "+diffDate);
    return diffDate / (1000 * 60 * 60 * 24);
}

/**
 * @설명 : 알림 레이어팝업 호출
 * @파라미터 : 
 */
function notice_action(){
	if( $(".notice_pop").css("display") == "none" ){
		ajax.json(
			'/common/pop/selectErrorInfoViewPop.json'
			, {"pageGubun" : "main"}
			, function (data) {
				if( data.length > 0 ){
					$(".notice_pop").find("[name=chargeBoxSerialNumber]").val(data[0].chargeBoxSerialNumber);
					$(".notice_pop").find("#notisCpInfoPop").text("충전소 : "+data[0].csNm+"("+data[0].cpNm+")");
					$(".notice_pop").find("#notisModelTypePop").text("모델명 : "+data[0].modelNm);
					$(".notice_pop").find("#notisTimestampPop").text("발생시간 : "+data[0].timestamp);
					$(".notice_pop").find("#notisErrorCodePop").text("에러코드 : "+data[0].stopReason);
					$(".notice_pop").find("#notisErrorInfoPop").html('<span class="red">'+data[0].errorNm+'</span>');
					if( !isNull(data[0].coRefVal2) ){
						$(".notice_pop").find("#notisErrorActionPop").html('<img src="/resources/images/common/noti_ico1.png"><span>'+data[0].coRefVal2+'</span>');
					}
					
					$(".notice_pop").css("display","");
					$("#noticeIconId").attr("src", "/resources/images/common/top_ico4.png");
				} else {
					$("#noticeIconId").attr("src", "/resources/images/common/top_ico5.png");
				}
			}
		);
	}
}
function notice_close(){
	$(".notice_pop").css("display","none");
	$("#noticeIconId").attr("src", "/resources/images/common/top_ico5.png");
}

/**
 * @설명 : 회원정보 레이어팝업 호출
 * @파라미터 : 
 */
function userReg_action(){
	if( document.querySelector('.user_pop').style.display == "none" ){
		ajax.json(
			'/system/user/selectUserDetail.json'
			, {"pageGubun" : "main"}
			, function (data) {
				var userInfoMap = data.userMap;
				$(".user_pop").find("#companyNmMainPop").text(userInfoMap.companyNm);
				$(".user_pop").find("#userNmMainPop").text(userInfoMap.userNm);
				$(".user_pop").find("[name=emailMainPop]").val(userInfoMap.email);
				$(".user_pop").find("[name=telNoMainPop]").val(phoneFomatter(userInfoMap.telNo));
				$(".user_pop").find("[name=mobileNoMainPop]").val(phoneFomatter(userInfoMap.mobileNo));
			}
		);
		$(".user_pop").css("display","");	
	} else {
		$(".user_pop").css("display","none");
	}
	
}
function userReg_closle(){
	$(".user_pop").css("display","none");
}


function pop_open(num,cls,top,height){
	
	//레이어팝업 class명 num=dim+num,cls = 팝업 사이즈클래스(width_10~width_70) id=$("#layer")+num,top=팝업 top 위치퍼센트
	$(".dim"+num).css("display","");
	$("#layer"+num).addClass(cls);
	$("#layer"+num).css("top",top+"%");
    $("#pgrid"+num).jqGrid( 'setGridWidth', $("#layer"+num).width()-60 );
    if(height){
		$("#layer"+num).css("height",height+"%");
		$("#layer"+num+" > .pop-container").css("max-height","calc(100% - 50px)");
	}
}

function pclosle(num){
	$(".dim"+num).css("display","none");
}

function sig_menu(num){
	//$(".menu2 li").removeClass("up");
	//$(".sig_menu"+num).addClass("up");
	var numItems = $('.sigup').length;
	if(numItems>0){
		if ($(".sig_menu"+num).hasClass("sigup")) {
			$(".sig_menu"+num).toggleClass("sigup");
			$(".sigtitle").toggleClass("son1");
		}else{
			$(".menu2 li").removeClass("sigup");
			$(".sigtitle").removeClass("son1");
			$(".sigtitle").toggleClass("son1");
			$(".sig_menu"+num).toggleClass("sigup");
		}
	}else{
		$(".sig_menu"+num).toggleClass("sigup");
		$(".sigtitle").toggleClass("son1");
	}
}

function sig_submenu(num,subnum){
	//$(".menu2 li").removeClass("up");
	//$(".sig_menu"+num).addClass("up");
	//$(".sig_menu"+num+" li").removeClass("up");
	//$(".sig_menu"+num+"_"+subnum).addClass("up");
	$(".sigtitle").addClass("son1");
	$(".sig_menu"+num).addClass("sigup");
	var gnbdep2 = $(".menu2 > ul > li >ul");
	var numItems = gnbdep2.children('.sigup').length;
	//console.log(numItems);
	if(numItems>0){
		if ($(".sig_menu"+num+"_"+subnum).hasClass("sigup")) {
			$(".sig_menu"+num+"_"+subnum).toggleClass("sigup");
		}else{				
			$(".sig_menu"+num+" li").removeClass("sigup");
			$(".sig_menu"+num+"_"+subnum).toggleClass("sigup");
		}
	}else{
		$(".sig_menu"+num+"_"+subnum).toggleClass("sigup");
	}
}

$(window).resize(function() {
	var popnum = '1';
	$(".gridWrap").find("table").jqGrid( 'setGridWidth', $(".gridWrap").width()-5 );
	$("#pgrid"+popnum).jqGrid( 'setGridWidth', $("#layer"+popnum).width()-60 );
});

/**
 * @설명 : request 파라미터 처리
 * @파라미터 : form - form obj
 */
function makeFormData(form) {
	var obj = {
		"param": form.serializeObject()
	};
	return obj;
}

/**
 * @설명 : 필터옵션 onChange 변경 처리
 * @파라미터 : filterOption obj
 * @파라미터 : allYn 셀렉트박스에 전체 항목 추가 여부 'Y' 전체추가 '' 전체없음
 */
function getFilterOption(selCd, allYn){
	if( selCd.value ){
		ajax.json(
			'/standard/commCode/selectFilterOptionList.json',
			JSON.stringify({filterCd : selCd.value}),
			function (data) {
	            $("select[name=optionGroup]").css("display", "");
	            $("select[name=optionGroup]").empty();
	        	if( data.list.length > 0 ){
	        		if( allYn == 'Y' ){
						$("select[name=optionGroup]").append($("<option value=''>전체</option>"));
					} else if( allYn == 'C' ){
						$("select[name=optionGroup]").append($("<option value=''>선택</option>"));
					}
					$.each(data.list, function(i, item){
	        			$("select[name=optionGroup]").append($("<option value=" +item.cd+">"+item.cdNm+"</option>"));
					});
					//조회 함수 실행
					fnSearch();
	        	} else {
					$("select[name=optionGroup]").css("display", "none");
				}
				console.log(selCd.value);
				if( selCd.value == "AREA_A" || selCd.value == "VOC_KIND_CD" || selCd.value == "VOC_STATUS_NM" ){
					$("[name=filterOptionSearch]").css("display", "none");
				} else {
					$("[name=filterOptionSearch]").css("display", "");
				}
	        }


		);
	} else {
		$("select[name=optionGroup]").css("display", "none");
	}
}

/**
 * @설명 : 옵션별 그룹 onChange 변경 처리
 * @파라미터 : optionGroup obj
 */
function getOptionGroup(selCd){
	if( selCd.value ){
		//조회 함수 실행
		fnSearch();
	}
}

/**
 * @설명 : 옵션별 그룹 onChange 변경 처리
 * @파라미터 : optionGroup obj
 */
function goMovePage(url, param){
	ajax.updateDiv(
		url
		, {searchParam : param}
		, 'contentWrap', function() {}
	)
}

function validNum(val, nm, valref){
	if($.isNumeric(val)){
		return [true, ""];
	}else{
        return [false, "숫자만 입력 가능 합니다."];
	}
}

/**
 * @설명 : 공통코드 조회
 * @파라미터 : preParamData obj
		, cdId string
		, gubun(select, input, radio ...)
		, allYn : 전체 생성여부 'Y' 전체 / 'N' 전체없음
		, async : 동기, 비동기 여부 기본true
 */
function setCommonCode( preParamData, cdId, gubun, allYn, async, arrId ){
	var doAsync = true;
	if( !isNull(async) ){
		doAsync = async;
	}
	$.ajax({
		url:'/system/selectCodeList.json',
        datatype: "json",
        mtype: "post",
        data:{param : JSON.stringify(preParamData)},
        contentType : "application/json; charset=UTF-8",
		async: doAsync,
        success : function(datas) {

			if( gubun == 'select' ){
				if( allYn == 'Y' ){
					$('#'+cdId).append('<option value="">전체</option>');
				} else if( allYn == 'sel' ){
					$('#'+cdId).append('<option value="">== 선택하세요 ==</option>');
				}
				$.each(datas.codeList, function(n, data) {
					$('#'+cdId).append('<option value='+data.cd+'>'+data.cdNm+'</option>');
					
				});
			} else if( gubun == 'radio' ){
				var htmlVal = '';
				if( allYn == 'Y' ){
					htmlVal = '<label class="mr10" for="전체"><input type="radio" id="'+ cdId+'" name="'+ cdId +'" value="" /> 전체 </label>';
				}
				$.each(datas.codeList, function(n, data) {
					htmlVal += '<label class="mr10" for="'+ cdId+'_'+ n +'">'
						+'<input type="radio" id="'+ cdId+'_'+ n +'" name="'+ cdId +'" value="'+ data.cd +'" /> '+ data.cdNm 
						+' </label>';
				});
				
				$('#'+cdId).parent("td").html(htmlVal);
				$('#'+cdId).attr( "checked", "checked");
			
			} else if( gubun == 'checkbox' ){
				var htmlVal = '';
				if( allYn == 'Y' ){
					htmlVal = '<label class="mr10" for="전체"><input type="checkbox" id="'+ cdId+'" name="'+ cdId +'" value="" /> 전체 </label>';
				}
				$.each(datas.codeList, function(n, data) {
					htmlVal += '<label class="mr10" for="'+ cdId+'_'+ n +'">'
						+'<input type="checkbox" id="'+ cdId+'_'+ n +'" name="'+ cdId +'" value="'+ data.cd +'" /> '+ data.cdNm 
						+' </label>';
				});
				$('#'+cdId).parent("td").html(htmlVal);

			} else if( isNull(gubun)){
				if( cdId == 'readGradeCd' ){
					$.each(datas.codeList, function(idx, item){
				    	readGradeList[item.cd] = item.cdNm;
					});	
				
				} else if( cdId == 'opCdList' || cdId == 'vocKindCdList' || cdId == 'vocStatusList' || cdId == 'projStatusList' || cdId == 'incomeStatusList' || cdId == 'collectStatusList' || cdId == 'errorCdList' || cdId == 'protocolCdList' || cdId == 'itemUnitList' || cdId == 'errLevelList'){	//운영상태
					$.each(datas.codeList, function(idx, item){
						arrId[item.cd] = item.cdNm;
					});
					
				} else if( cdId == 'languageList' ){	//국가
					$.each(datas.codeList, function(idx, item){
						arrId[item.cd] = item.coRefVal2;
					});
		
				} else {
					$.each(datas.codeList, function(idx, item){
				    	arrId[item.cd] = item.cdNm;
					});	
				}
			}
        }
    });	
}

/**
 * @설명 : 검색 필터 리스트 조회(SELECT박스)
 * @파라미터 : selCd (string) 구분자 service.cpinfra.ControlService
		, cdId (string) : select 엘리멘트 ID
		, allYn : 전체 생성여부 'Y' 전체 / 'N' 전체없음
		, param1 : 추가 파라미터
		
 */
function getFilterList( selCd, cdId, allYn, param1 ){
	
	if( selCd){
		ajax.json(
			'/cpinfra/controlhist/selectFilterList.json',
	        JSON.stringify({filterCd : selCd, param : param1}),
	
			function(datas) {
				$("#"+cdId).empty();
				if( datas.list.length > 0 ){
		        		if( allYn == 'Y' ){
							$("#"+cdId).append($("<option value=''>전체</option>"));
						} else if( allYn == 'C' ){
							$("#"+cdId).append($("<option value=''>선택</option>"));
						}
						$.each(datas.list, function(i, item){
							if(item.cdNm != null && item.cdNm !=''){
		        				$("#"+cdId).append($("<option value=" +item.cd+">"+item.cdNm+"</option>"));
							}
						});
						
		        	}
			}
		);
	}
}

/**
 * @설명 : 숫자만 입력 & 콤마 (onkeyup)
 * @파라미터 : 
 */
function fnOnlyNum(e){
	// 숫자만 입력
	$(e).val($(e).val().replace(/[^0-9]/g, ""));
}

/**
 * @설명 : 숫자와 소수점만 입력 & 콤마 (onkeyup)
 * @파라미터 : 
 */
function fnNumDecimalPoint(e){
	var value = $(e).val();
	var length = $(e).val().length;
	var split = [];
	var splitValue = "";
	var count = 0;
	
	if (value[0] == "."){
		$(e).val(value.substr(1, length));
	} else {
		split = value.split(".")
		count = split.length;
		if (count < 3) {
			$(e).val(value.replace(/[^0-9.]/g, ""));
		} else {
			for (var i = 0; i < 3; i++){
				splitValue += split[i];
				if (i == 0){
					splitValue += ".";
				}
			}
			$(e).val(splitValue);
		}
	}
}

/**
 * @설명 : fw version 입력 방지 처리
 */
function fnFwVersion(e){
	var value = $(e).val();
	var length = $(e).val().length;
	
	if (value[0] == "."){
		$(e).val(value.substr(1, length));
	} else {
		$(e).val(value.replace(/[^0-9.]/g, ""));
	}
}

/**
 * @설명 : 파라미터 콤마 
 * @파라미터 : 
 */
function fnNumComma(number){
	// 숫자만 입력
	var num = $(number).val();
	num = num.replace(/[^0-9]/g, "");
	if (num == 0) {
		return $(number).val("");
	} else {
		var len, point, str;
	
		point = num.length % 3;
		len = num.length;
		str = num.substring(0, point);
		while (point < len) {
			if (str != "")
				str += ",";
			str += num.substring(point, point + 3);
			point += 3;
		}
		return $(number).val(str);
	}
}

/**
 * @설명 : 파라미터 콤마 
 * @파라미터 : 
 */
function fnNumCommaInit(str){
	str = String(str);
	return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

	
/**
 * @설명 : 이미지 체킹
 * @파라미터 : fileTargetId - 파일 input ID
 */
function fnImgChk(fileName){
	var fileNameExt = fileName.substring(fileName.lastIndexOf(".")+1);
	fileNameExt = fileNameExt.toLowerCase();
	
	if("jpg" != fileNameExt && "jpeg" != fileNameExt && "gif" != fileNameExt && "png" != fileNameExt && "bmp" != fileNameExt){
		simpleAlert("이미지(jpg, jpeg, gif, png, bmp) 파일이 아닙니다.");
		return false;
	} else {
		return true;
	}
	
}

function fnjpgpdfChk(fileName){
	var fileNameExt = fileName.substring(fileName.lastIndexOf(".")+1);
	fileNameExt = fileNameExt.toLowerCase();
	if("jpg" != fileNameExt && "pdf" != fileNameExt && "png" != fileNameExt){
		alert("이미지(JPG), PDF, PNG 파일이 아닙니다.");
		return false;
	} else {
		return true;
	}
}

function fnExcelfChk(fileName){
	var fileNameExt = fileName.substring(fileName.lastIndexOf(".")+1);
	fileNameExt = fileNameExt.toLowerCase();
	if("xlsx" != fileNameExt && "xls" != fileNameExt){
		alert("xlsx, xls파일이 아닙니다.");
		return false;
	} else {
		return true;
	}
}


/**
 * @설명 : 비디오 체킹
 * @파라미터 : fileTargetId - 파일 input ID
 */
function fnVideoChk(fileName){
	var fileNameExt = fileName.substring(fileName.lastIndexOf(".")+1);
	fileNameExt = fileNameExt.toLowerCase();
	
	if("mp4" != fileNameExt && "avi" != fileNameExt && "mov" != fileNameExt && "mpg" != fileNameExt && "mpeg" != fileNameExt){
		simpleAlert("이미지(mp4, avi, mov, mpg, mpeg) 파일이 아닙니다.");
		return false;
	} else {
		return true;
	}
	
}
/**
 * @설명 : 파일용량 체크
 * @파라미터 : fileTargetId - 파일 input ID
 */
function fnSizeChk(file, size){
	var fileSize = file.size / 1000000; //MB로 변환
	if( fileSize > size ){
		simpleAlert("파일 용량("+ size +"M)이 초과되었습니다.");
		return false;
	} else {
		return true;
	}
}

/**
 * @설명 : gird내 필수항목 체크 함수
 * @파라미터 : val(조회할 컬럼 ID), id(GridId)
 */
function fnValidJqGrid(val, id){
	var jqgridId = $("#"+id);
	var rowIds  = jqgridId.jqGrid('getDataIDs');
	var colNames = jqgridId.jqGrid('getGridParam', 'colNames');
	var colModel = jqgridId.jqGrid('getGridParam', 'colModel');
	for (var z = 0; z < rowIds.length; z++) {
		for (var y = 0; y < colModel.length; y++) {	
			for (var x = 0; x < val.length; x++) {
				if( val[x] == colModel[y].name ){
					var rowData = jqgridId.jqGrid('getRowData', rowIds[z]);
					var colNm = colNames[y];
					var cellVal = jqgridId.getCell(rowIds[z], y);
					if( (rowData.status == "C" || rowData.status == "U") && isNull(cellVal) ){
						colNm = colNm.replace('<strong class="orange_font">&nbsp;*</strong>', '');
						alert(z+1 + ' 번행의 '+inputText.replace("{0}", colNm));
						jqgridId.find('tr#'+ rowIds[z] +' > td[aria-describedby$='+colModel[y].name+']').click();
						return false;
					}
				}
			}	
		}
	}
	
	return true;
}

/**
 * @설명 : gird내 필수항목 체크 함수 위 함수에서 약간 수정함..kjk
 * @파라미터 : val(조회할 컬럼 ID), id(GridId)
 */
function fnValidJqGridNew(val, id){
	var jqgridId = $("#"+id);
	var rowIds  = jqgridId.jqGrid('getDataIDs');
	var colNames = jqgridId.jqGrid('getGridParam', 'colNames');
	var colModel = jqgridId.jqGrid('getGridParam', 'colModel');
	
	for (var z = 0; z < rowIds.length; z++) {
		var rowData = jqgridId.jqGrid('getRowData', rowIds[z]);
		if( rowData.status == "C" || rowData.status == "U" ){
			for (var y = 0; y < colModel.length; y++) {	
				for (var x = 0; x < val.length; x++) {
					if( val[x] == colModel[y].name ){
						var colNm = colNames[y];
						if( isNull(jqgridId.getCell(rowIds[z], y)) ){
							colNm = colNm.replace('<strong class="orange_font">&nbsp;*</strong>', '');
							alert(z+1 + ' 번행의 '+inputText.replace("{0}", colNm));
							jqgridId.find('tr#'+ rowIds[z] +' > td[aria-describedby$='+colModel[y].name+']').click();
							return false;
						}
					}
				}
			}
		}
	}
	
	return true;
}


/**
 * @설명 : table내 필수항목 체크 함수
 * @파라미터 : val(조회할 컬럼 ID)
 */
function fnValidTable(val){
	//console.log("val >>> "+val);
	for (var i = 0; i < val.length; i++) {
		if( isNull($("#"+val[i]).val()) ){
			var valMsg = $("#"+val[i]).closest('td').attr("data-label");
			console.log(i + "번째 " + inputText.replace('{0}',valMsg));
			alert(inputText.replace('{0}',valMsg));
			$("#"+val[i]).focus();
			return false;
		}
	}
	return true;
}
/**
 * @설명 : 달력 표시 생성
 * @파라미터 : gubun(달력구분, 'A' : 기본달력, 'B' : 년,월만), dateId(달력 ID)
 */
function makeDatapiker(gubun, dateId){
	if( gubun == "A" ){
		var cal = {
				dateFormat: 'yy-mm-dd' //Input Display Format 변경
				,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
				,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
				,changeYear: true //콤보박스에서 년 선택 가능
				,changeMonth: true //콤보박스에서 월 선택 가능                
				,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
				,buttonImage: "./resources/images/common/calendar.png" //버튼 이미지 경로
				,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
				,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
				,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
				,yearRange:"c-100:c+10"	//년도의 년도를 보여줄때 이전 100년 이후 10년을 보여준다.
				,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 텍스트
				,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
				,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
				,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
		};
		
	} else if( gubun == "B" ){
		var cal = {
				currentText : "이달",
				changeMonth: true, 
				changeYear: true, 
				monthNames : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
				monthNamesShort : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월",	"9월", "10월", "11월", "12월" ],
				showMonthAfterYear : true, 
				buttonImageOnly: true,
				showButtonPanel: true,
				showOn : "both",
				buttonImage : "/resources/images/common/calendar.png"
		};
		
        cal.closeText = "선택";
    	cal.dateFormat = "yy-mm";
    	cal.onClose = function (dateText, inst) {
        	var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
        	var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
        	$(this).datepicker( "option", "defaultDate", new Date(year, month, 1) );
        	$(this).datepicker('setDate', new Date(year, month, 1));
			$("#ui-datepicker-div").removeClass("hideCalendar");
    	}
 
    	cal.beforeShow = function () {
        	var selectDate = $("#"+dateId).val().split("-");
        	var year = Number(selectDate[0]);
        	var month = Number(selectDate[1]) - 1;
        	$(this).datepicker( "option", "defaultDate", new Date(year, month, 1) );
			$("#ui-datepicker-div").addClass("hideCalendar");
    	}
	}
	
	$("#"+dateId).datepicker(cal);
}

/**
 * @설명 : Null인지 아닌지 확인
 * @파라미터 : obj
 */
function isNull(obj){
	if(typeof obj == "undefined"){
		return true;
	}
	if($.trim(""+obj) == "null"){
		return true;
	}
	if($.trim(""+obj) == ""){
		return true;
	}
	if(Object.keys(obj).length === 0){
		return true;
	}

	return false;
};

/**
 * @설명 : Null인지 아닌지 확인
 * @파라미터 : obj
 */
function isNotNull(obj){
	if(typeof obj == "undefined"){
		return false;
	
	} else if($.trim(""+obj) == "null"){
		return false;
	
	} else if($.trim(""+obj) == ""){
		return false;
	
	} else if(Object.keys(obj).length === 0){
		return false;
	}

	return true;
};

/**
 * @설명 : Null인지 아닌지 확인 Obj 추가함
 * @파라미터 : obj
 */
function isObjNull(obj){
	if(typeof obj == "undefined"){
		return true;
	}
	if($.trim(""+obj) == "null"){
		return true;
	}
	if($.trim(""+obj) == ""){
		return true;
	}
	if(Object.keys(obj).length === 0){
		return true;
	}
	
	return false;
};

/**
 * @설명 : Null이면 "" 아니면 obj 리턴
 * @파라미터 : obj
 */
function isNvl(obj)
{
   if(typeof obj == "undefined"){
      return "";
   }
   if($.trim(""+obj) == "null"){
      return "";
   }
   if($.trim(""+obj) == ""){
      return "";
   }

   return obj;
};

function getSearchParam(popId){
	if( isNull(popId) ){
		return $('#searchForm').serializeForm();
	} else {
		return $('#'+popId).serializeForm();
	}    
};

function getSearchParam2(popId){
	if( isNull(popId) ){
		return $('#searchForm2').serializeForm();
	} else {
		return $('#'+popId).serializeForm();
	}    
}
/**
 * @설명 : 엑셀다운로드 공통함수
 * @파라미터 : id(그리드 ID)
 */
function fnExcelDownload(id, popId, pageId){
	var jqgridId = $("#"+id);
	var colModel = jqgridId.jqGrid('getGridParam', 'colModel');
	var colNames = jqgridId.jqGrid('getGridParam', 'colNames');
	var cnt = 0;
	
	/*excelColNames = [];
	excelColModels = [];
	excelWidth = [];
	excelDataType = [];
	
	//statusNm  행상태정보, rn 순번은 제거
	$.each(colModel, function(n, data) {
		if( !data.hidden && 'cb' != data.name && 'statusNm' != data.name && 'rn' != data.name && 'iconPop' != data.name ){
			excelColNames[cnt]	= colNames[n].replace('<strong class="orange_font">&nbsp;*</strong>', '');
			excelColModels[cnt] = data.name;
			excelWidth[cnt] 	= data.width;
			if ('number' == data.formatter){
				excelDataType[cnt] = 'int';
			} else {
				excelDataType[cnt] = 'string';
			}
			cnt++;
		}
		//충전소 및 충전기 운영상태 명 변경처리
		//if( ( pageId == "csMain" || pageId == "cpMain" ) && data.name == "opCd" ){
			//excelColModels[cnt-1] = "opCdNm";
		//}
		
	});*/
	//prograssbarOpen();
	//fileDownloadPrograss();
	//setInterval(fileDownloadPrograss, 1000);
	
	//console.log(excelColModels);
	var param = getSearchParam(popId);
    	param.excelId 		= excelId;
    	param.excelNm 		= excelNm;
    	param.excelHeader 	= excelColNames;
    	param.excelKey 		= excelColModels;
    	param.excelWidth 	= excelWidth;
    	param.excelDataType = excelDataType;
    	param.pageId 		= pageId;
    	param.excelDownGubun = excelDownGubun;

	//console.log(param);
    excel.download(param);
    //excel.downloadJqGrid(param, );

};

/**
 * @설명 : 공통 파일다운로드
 * @파라미터 : id(파일 ID)
 */
function fileDownload(id){
	location.href = "/common/fileDownload/"+id;
};

$(document).on("click", "#fileDownloadClick", function(){
	let fileId = $(this).attr("value");
	if( isNotNull(fileId) ){
		location.href = "/common/downLoadAtchFile?fid="+fileId;
	}
});

/**
 * @설명 : 공통 템플릿 파일다운로드
 * @파라미터 : id(파일 ID)
 * id = 
 * "customer" => 개인회원 엑셀 업로드 양식
 * 
 */
function tempFileDownload(id){
	location.href = "/common/tempFileDownload/"+id;
	
};

/**
 * @설명 : 공통 이미지 뷰어 팝업 호출
 * @파라미터 : fullUrl(이미지 풀 경로(경로+파일명))
 */
function imageFileView(fullUrl){
	var imageInfo = new Image();
	imageInfo.src = fullUrl;
	var image_width = imageInfo.width+30;
	var image_height = imageInfo.height+30;
	image_width = 800;
	image_height = 600;
	var imageStatus = 'width='+ image_width +'px,height='+ image_height +'px, menubars=no, scrollbars=auto';
	
	var OpenWindow=window.open('','_blank', imageStatus);
	OpenWindow.document.write("<style>body{margin:0px;}</style><img src='"+fullUrl+"' width='"+image_width+"'>");
	
};

//jqgrid내에 전화번호 하이픈 자동 삽입 함수
function jqgridPhoneFmatter(cellvalue, options, rowObject){
	return phoneFomatter(cellvalue);
}

//jqgrid내에 전화번호 하이픈 자동 삽입 함수
function jqgridBizFmatter(cellvalue, options, rowObject){
	return bizFomatter(cellvalue, 1);
}

//jqgrid내에 날짜 하이픈 자동 삽입 함수
function jqgridDateFmatter(cellvalue, options, rowObject){
	return dateFomatter(cellvalue);
}

/**
* 전화번호 Fomatter 
* @param 	value : '-' 포함안된 숫자로만 이루어진 문자열
* @param 	type  : 0 (중간 전화번호 **** 처리)
* @returns  value
* @author 	전화번호 Format
* @example	phoneFomatter('01000000000');
 */
function phoneFomatter(num){
	var formatNum = '';
	if( !isNull(num) ){
		num = num.replaceAll('-', '');
		formatNum = num;
	    if(num.length==11){
	    	formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
	    }else if(num.length == 8){
	        formatNum = num.replace(/(\d{4})(\d{4})/, '$1-$2');
	    }else{
	        if(num.indexOf('02')==0){
	            if(num.length==9){
	                formatNum = num.replace(/(\d{2})(\d{3})(\d{4})/, '$1-$2-$3');
	            }else{
	                formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3');
	            }
	        }else{
				if(num.length==9){
	                formatNum = num.replace(/(\d{3})(\d{3})(\d{3})/, '$1-$2-$3');
	            }else if(num.length==10){
	                formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
	            }else{
	                formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
	            }
	        }
	    }
	}
	return formatNum;
}

/**
* 사업자등록번호 하이픈 입력 type 0 끝 5자리 별표
 */
function bizFomatter(num, type){
	var formatNum = '';
	if( !isNull(num) ){
	    num = num.replaceAll('-', '');
		formatNum = num;
		if(num.length==10){
			if( type == 0 ){
				formatNum = num.replace(/(\d{3})(\d{2})(\d{5})/, '$1-$2-*****');
			} else {
				formatNum = num.replace(/(\d{3})(\d{2})(\d{5})/, '$1-$2-$3');
			}
	    }
	}
	return formatNum;
}
/**
* 날짜 하이픈 입력
 */
function dateFomatter(num){
	var formatNum = '';
	if( !isNull(num) ){
	    num = num.replaceAll('-', '');
		formatNum = num;
		if(num.length==8){
			formatNum = num.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3');
			
	    } else if(num.length==6){
			formatNum = num.replace(/(\d{2})(\d{2})(\d{2})/, '$1-$2-$3');
		}
	}
	return formatNum;
}

/**
* 그리드의 셀 키업시 숫자만 입력함
* @param 	el
 */
function cmGridKeyUpOnlyNum(el){
	var pattern = /^[0-9]*$/;
	$(el).keyup(function(){
		if(pattern.test(el.value) == false){
			alert("숫자만 입력하시기 바랍니다.");
			//keyVal = keyVal.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
			//keyVal = keyVal.replace(/,/g,'');          // ,값 공백처리
			el.value = el.value.replace(/[^0-9]/g,''); 
		}
 	});
}
/**
* 그리드의 셀 키업시 숫자만 입력함
* @param 	num
 */
function cmGridCellComma(num){
	num = num.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    num = num.replace(/,/g,'');          // ,값 공백처리
	return num.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function checkInputNum(){
	if ((event.keyCode < 48) || (event.keyCode > 57)){
        event.returnValue = false;
    }
}


//input에 numberOnly text가 있으면 keyup시 숫자만 입력받음
$(document).on("keyup", "input:text[numberOnly]", function () {
    $(this).val($(this).val().replace(/[^0-9]/g,""));
});
$(document).on("keyup", "input:text[numberEngOnly]", function () {
    $(this).val($(this).val().replace(/[^a-z0-9]/gi,""));
});
$(document).on("keyup", "input:text[englishOnly]", function () {
    $(this).val($(this).val().replace(/[^A-Z]/gi,""));
});
//input에 한글 및 특수문자 입력 방지 !@#&_.- 값은 허용함.
$(document).on("keyup", "input:text[koreaNotKey]", function () {
    $(this).val($(this).val().replace(/[^a-z0-9!@#&_.-]/gi,""));
});
//input에 numberPhone text가 있으면 focusout시 폰번호(하이픈) 포맷변경
$(document).on("focusout", "input:text[numberPhone]", function () {
	var keyupVal = $(this).val();
	$(this).val(phoneFomatter(keyupVal));
});
//input에 numberBiz text가 있으면 focusout시 사업자번호 포맷변경
$(document).on("focusout", "input:text[numberBiz]", function () {
	var keyupVal = $(this).val();
	$(this).val(bizFomatter(keyupVal, 1));
});
//input에 numberIdtoken text가 있으면 focusout시 카드번호 포맷변경
$(document).on("focusout", "input:text[numberIdtoken]", function () {
	var keyupVal = $(this).val();
	$(this).val(cardNumFomatter(keyupVal, 1));
});

//금액 한글변환
function fn_getAmountKorean(num) {	
    var numArr = new Array("","일","이","삼","사","오","육","칠","팔","구","십");
    var danArr = new Array("","십","백","천","","십","백","천","","십","백","천","","십","백","천");
    var result = "";
    num = num.replace(/,/gi,'');
	for(i=0; i<num.length; i++) {		
		str = "";
		han = numArr[num.charAt(num.length-(i+1))];
		if(han != "")
			str += han+danArr[i];
		if(i == 4) str += "만";
		if(i == 8) str += "억";
		if(i == 12) str += "조";
		result = str + result;
	}
	if(num != 0)
		result ="일금 "+ result + "원 정";
    return result ;
}

// 영문+숫자만 입력 체크 
function checkEngNum(obj) { 
	var inText = obj;
	var deny_char = /^[A-Za-z0-9]+$/;
	if (deny_char.test(inText)) {
		return false;
	}
	return true;
}

// 영문+숫자+(-)만 입력 체크 (충전기 관리코드)
function checkMgmtCode(obj) { 
	var inText = obj;
	var deny_char = /^[A-Za-z\d-]+$/;
	if (deny_char.test(inText)) {
		return true;
	}
	return false;
}

// 배열값이 같은지 비교함
function cmChkArrCompare(arr1, arr2) { 
	if( JSON.stringify(arr1) == JSON.stringify(arr2) ){
		return true;
	}
	
	return false;
}

/**
 * @설명 : 문자 최대 입력크기 체크(단건)
 * @파라미터 : id - 객체 ID, title - 객체 타이틀,  maxLength - 제한 글자수
 */
function fnMaxLengthChk(id, title, maxLength){
	var obj = $("#"+id).val();
	if( isNull(obj) ){
		return true;
	
	}else if(Number(fnByteChk(obj)) > Number(maxLength)){
		alert(title + "의 입력길이["+ Number(maxLength)/3 +"]가 초과되었습니다.")
		//layerPop("alert", "47", maxLength +"|"+ title );
		//$('#alert_confirm').off("click").on("click", function(){ // 확인 버튼 클릭시
			obj.focus();
		//});
		return false;
	} else {
		return true;
	}
}

/**
 * @설명 : 바이트수 반환
 * @파라미터 : el - 해당 엘리먼트 객체
 */
function fnByteChk(el){
	var codeByte = 0;
	for(var i=0; i < el.val().length; i++){
		var oneChar = escape(el.val().charAt(i));
		if(oneChar.length == 1){
			codeByte ++;
		} else if(oneChar.indexOf("%u") != -1){ // 한글?
			codeByte += 3;
		} else if(oneChar.indexOf("%") != -1){
			codeByte ++;
		}
	}
	console.log(codeByte);
	return codeByte;
}

/**
 * @설명 : 셀렉트박스에 전화번호 자동 입력
 * @파라미터 : telId 해당 ID값 / gubun : A(전화번호), B(지역번호)
 */
function makeTelFront(telId, gubun){
	var telList = [];
	if(gubun == 'A'){
		telList = ["010", "011", "016", "017", "019"];
	} else if(gubun == 'B'){
		telList = ["02", "031", "032", "041", "042", "043", "044", "051", "052", "053", "054", "055", "061", "062", "063", "064", "010", "011", "016", "017", "019"];
	} else if(gubun == 'C'){
		telList = ["010", "011", "016", "017", "019", "02", "031", "032", "041", "042", "043", "044", "051", "052", "053", "054", "055", "061", "062", "063", "064"];
	}
	for (var i = 0; i < telList.length; i++) {
		$('#'+telId).append('<option value='+telList[i]+'>'+telList[i]+'</option>');
	}
}

/**
 * @설명 : 우측 Detail 화면 모든 input, select checkbox 상태 disabled 적용
 * @파라미터 : flag 
 */
function rightDivInit(flag){
    $(".rg_con").find('input').prop("disabled", flag);
    $(".rg_con").find('select').prop("disabled", flag);
    $(".rg_con").find('checkbox').prop("disabled", flag);
    $(".rg_con").find('textarea').prop("disabled", flag);
    $(".rg_con").find('.datepicker').datepicker('option', 'disabled', flag);

}

/**
 * @설명 : 원하는 포맷으로 값 변경
 * @파라미터 : gubun : A(전화번호), B(사업자번호) / num : 변경할 값 / sort (가져올 순서)
 */
function getDataNumber(gubun, num, sort){
	var s = "";
	var arr = [];
	if( isNull(num) ){
		return s;
	}
	 
	if( gubun == "A" ){
		arr = phoneFomatter(num).split("-");
	} else if( gubun == "B" ){
		arr = bizFomatter(num, 1).split("-");
	} else if( gubun == "C" ){
		arr = num.split("@");
	}
	if( arr.length > 0 ){
		s = arr[sort];
	}
	return s; 
}

/**
 * @설명 : 전화번호 벨리데이션 체크
 * @파라미터 : obj(상위 class) / gubun A전화번호, B핸드폰번호, C팩스번호
  *@return : 전화번호
 * Ex> checkPhoneVal('pop-conts', 'A');
 */
function checkPhoneVal(obj, gubun){
	var ch = $("."+obj);
	var tel = tel1 = tel2 = tel3 = "";
	if( "A" == gubun ){
		tel2 = ch.find(".telePhone2").val();
		tel3 = ch.find(".telePhone3").val();
		
		if( (isNull(tel2) && !isNull(tel3)) || (!isNull(tel2) && isNull(tel3)) ){
			alert("전화번호를 입력하시기 바랍니다.");
			if(isNull(tel2)){
				ch.find(".telePhone2").focus();
			} else {
				ch.find(".telePhone3").focus();
			}
			return "fail";
			
		} else if( (isNull(tel2) && isNull(tel3))){
			tel = "";
		} else if( (!isNull(tel2) && !isNull(tel3))){
			tel = ch.find(".telePhone1").val() + tel2 + tel3;
		}
		
	} else if( "B" == gubun ){
		tel2 = ch.find(".handPhone2").val();
		tel3 = ch.find(".handPhone3").val();
		
		if( (isNull(tel2) && !isNull(tel3)) || (!isNull(tel2) && isNull(tel3)) ){
			alert("핸드폰번호를 입력하시기 바랍니다.");
			if(isNull(tel2)){
				ch.find(".handPhone2").focus();
			} else {
				ch.find(".handPhone3").focus();
			}
			return "fail";
			
		} else if( (isNull(tel2) && isNull(tel3))){
			tel = "";
		} else if( (!isNull(tel2) && !isNull(tel3))){
			tel = ch.find(".handPhone1").val() + tel2 + tel3;
		}
	} else if( "C" == gubun ){
		tel2 = ch.find(".faxPhone2").val();
		tel3 = ch.find(".faxPhone3").val();
		
		if( (isNull(tel2) && !isNull(tel3)) || (!isNull(tel2) && isNull(tel3)) ){
			alert("팩스번호를 입력하시기 바랍니다.");
			if(isNull(tel2)){
				ch.find(".faxPhone2").focus();
			} else {
				ch.find(".faxPhone3").focus();
			}
			return "fail";
			
		} else if( (isNull(tel2) && isNull(tel3))){
			tel = "";
		} else if( (!isNull(tel2) && !isNull(tel3))){
			tel = ch.find(".faxPhone1").val() + tel2 + tel3;
		}
	}
	return tel;
}


/**
 * @설명 : 이메일 벨리데이션 체크
 * @파라미터 : obj(상위 class)
 * Ex> checkEmailVal('pop-conts');
 */
function checkEmailVal(obj){
	var ch = $("."+obj);
	var email = email1 = email2 = "";
	
	email1 = ch.find(".emailAdress1").val();
	email2 = ch.find(".emailAdress2").val();
	
	if( (isNull(email1) && !isNull(email2)) || (!isNull(email1) && isNull(email2)) ){
		alert("이메일주소를 입력하시기 바랍니다.");
		if(isNull(email1)){
			ch.find(".emailAdress1").focus();
		} else {
			ch.find(".emailAdress2").focus();
		}
		return "fail";
		
	} else if( (isNull(email1) && isNull(email2))){
		email = "";
	} else if( (!isNull(email1) && !isNull(email2))){
		email = email1 + '@' + email2;
	}
		
	return email;
}

/**
 * @설명 : 비밀번호 벨리데이션 체크
 * @파라미터 : obj(상위 class)
 * Ex> checkPwVal(obj);
 */
function checkPwVal(obj){
	var pw = obj;
	var vaild = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{10,}$/;
	if (vaild.test(pw)) {
		return pw;
	}
	alert("숫자, 문자, 특수문자 조합 10자리이상 입력하셔야 합니다.");
	return "fail";
}

/**
 * @설명 : 비밀번호 벨리데이션 체크 자리수 8자리 이상
 * @파라미터 : obj(상위 class)
 * Ex> checkPwValSimple(obj);
 */
function checkPwValSimple(obj){
	var pw = obj;
	if( pw.length >= 8 ){
		return pw;
	} else {
		alert("비밀번호는 8자리이상 입력하셔야 합니다.");
		return "fail";	
	}
}

/**
 * @설명 : 그리드 엑셀업로드 실패(소수점 제거)
 * @파라미터 : 
 * Ex> 그리드 옵션 formatter : doubleToIntFormatter
 */
function doubleToIntFormatter(cellvalue, options, rowObject) {
		var numRegex =/^\d*[.]\d$/;
	    if( !isNull(cellvalue) && numRegex.test(cellvalue)) {
		    return Math.floor(cellvalue);
	    } 

	    return cellvalue;
	}
	
	
/**
 * @설명 : 차량번호 정규 표현
 * @파라미터 : obj(상위 class)
 * Ex> checkPwVal(obj);
 */
function checkCarNumber(obj){
	var pw = obj
	var success = false;
	var valid_1 = /^(([가-힣]{2}|[가-힣]{4})?\s?(\d{2}|\d{3})\s?[가-힣]\s?\d{4}|임\s?[가-힣]{4,6}\s?(\d{6}|\d{4}))/;
	if( valid_1.test(pw) ) {
		success = true;
	}
	if( success ){
		return pw;
	} else {
		alert("차량번호를 확인하여주시기 바랍니다.\nex) 서울99하9999 / 99하9999");
		return "fail";
	}
}	

/**
* 카드번호 하이픈 Fomatter 
* @param 	value : '-' 포함안된 숫자로만 이루어진 문자열
* @author 	김재균
* @example	cardNumFomatter('1234567812345678');
 */
function cardNumFomatter(num){
	var formatNum = '';
	if( !isNull(num) ){
		num = num.replaceAll('-', '');
		formatNum = num;
		if( num.length==16 ){
			formatNum = num.replace(/(\d{4})(\d{4})(\d{4})(\d{4})/, '$1-$2-$3-$4');
		}
	}
	return formatNum;
}