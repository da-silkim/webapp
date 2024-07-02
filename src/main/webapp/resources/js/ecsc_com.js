var GV_CURR_PAGE_NO = "1";      // Default : 1  [현재 페이지 번호]
var GV_PAGE_COUNT   = "10";     // Default : 10 [한 페이지 개수]
var GV_BLOCK_COUNT  = "10";     // Default : 10 [한 페이지 Block 개수]

/*******************************************************
 * Ajax Submit !!
********************************************************/
/*******************************************************
 * Callback Function Return !!
 * 
 * @param {string} actionUrl
 * @param {string} callbackFunction
 * @returns {string} callbackFunction
 * @description CallBack Function Make
 *******************************************************/
function gf_getAfterFunction(){
    var args = gf_getAfterFunction.arguments;
    
    var actionUrl        = args[0];
    var callbackFunction = args.length > 1 ? args[1] : null;
    
    if(gf_isNull(callbackFunction)){
        var sepSpace = actionUrl.lastIndexOf("/");
        var sepPoint = actionUrl.indexOf(".");
        
        callbackFunction = eval(actionUrl.substring(sepSpace+1, sepPoint) + "After");
    }
    
    return callbackFunction;
}

/*******************************************************
 * General Submit !!
 * 
 * @param {string} actionUrl
 * @param {string} param
 * @param {string} callbackFunction
 * @returns {Boolean}
 * @description 일반적인 Ajax 호출
 *******************************************************/
function gf_send(){
    var args = gf_send.arguments;
    
    var actionUrl        = args[0];
    var param            = args.length > 1 ? args[1] : "";
    var callbackFunction = args.length > 2 ? args[2] : null;
    var syncBool         = args.length > 3 ? args[3] : true;
    
    var myajax = new MyAjax();
    
    myajax.send(actionUrl, param, gf_getAfterFunction(actionUrl, callbackFunction), syncBool);
    
    return false;
}

/*******************************************************
 * Form Submit !!
 * 
 * @param {string} formId
 * @param {string} actionUrl
 * @param {string} callbackFunction
 * @returns {Boolean}
 * @description Form Ajax 호출
 *******************************************************/
function gf_sendForm(){
    var args = gf_sendForm.arguments;
    
    var formId           = args[0];
    var actionUrl        = args.length > 1 ? args[1] : "";
    var callbackFunction = args.length > 2 ? args[2] : null;
    var syncBool         = args.length > 3 ? args[3] : true;
    
    var formObj = $("#" + formId);
    
    if(gf_isNull(formId) || gf_isNull(formObj)){
        return false;
    }
    
    var url = "";
    
    if(actionUrl != ""){
        url = actionUrl;
        
        formObj.attr("action", url);
    } else {
        url = formObj.attr("action");
    }
    
    var myajax = new MyAjax();
    
    myajax.sendForm(formId, gf_getAfterFunction(url, callbackFunction), syncBool);
    
    return false;
}

/*******************************************************
 * File Form Submit !!
 * 
 * @param {string} formId
 * @param {string} actionUrl
 * @param {string} callbackFunction
 * @returns {Boolean}
 * @description File Upload sendForm
 *******************************************************/
function gf_sendFileFormAjax(){
    var args = gf_sendFileFormAjax.arguments;
    
    var formId           = args[0];
    var actionUrl        = args.length > 1 ? args[1] : "";
    var callbackFunction = args.length > 2 ? args[2] : null;
    
    var formObj = $("#" + formId);
    
    if(gf_isNull(formId) || gf_isNull(formObj)){
        return false;
    }
    
    var url = "";
    
    if(actionUrl != ""){
        url = actionUrl;
        
        formObj.attr("action", url);
    } else {
        url = formObj.attr("action");
    }
    
    var myajax = new MyAjax();
    
    myajax.sendFileForm(formId, gf_getAfterFunction(url, callbackFunction));
    
    return false;
}

/*******************************************************
 * File Form Submit !!
 * 
 * @param {string} formId
 * @param {string} actionUrl
 * @param {string} callbackFunction
 * @returns {Boolean}
 * @description File Upload sendForm
 *              ==> Mozilla [Chrome, Firefox, Safari 등등]에서 기본적으로 작동
 *              ==> IE 는 10 부터 작동 (IE 9 이하는 작동 안함)
 *******************************************************/
function gf_sendFileForm(){
	var args = gf_sendFileForm.arguments;
    
    var formId           = args[0];
    var actionUrl        = args.length > 1 ? args[1] : "";
    var callbackFunction = args.length > 2 ? args[2] : null;
    
    var formObj = $("#" + formId);
    if(gf_isNull(formId) || gf_isNull(formObj)){
        return false;
    }
    
    var url = "";
    
    if(actionUrl != ""){
        url = actionUrl;
        
        formObj.attr("action", url);
    } else {
        url = formObj.attr("action");
    }
    
    
    
    // Upload Count Setting !!
    var uploadCnt = 0;
    
    $("input[type=file]", formObj).each(function() {
        if($(this).val() != ""){
            uploadCnt++;
        }
    });
    if($("#uploadCnt", formObj).length == 0){
        formObj.append('<input id="uploadCnt" name="uploadCnt" type="hidden" value="' + uploadCnt + '" />');
    } else {
        $("#uploadCnt", formObj).val(uploadCnt);
    }
    
    // Browser Check 후 분기 Submit !!
    var browserType = gf_getBrowser();
    
    if(browserType == "IE7" || browserType == "IE8" || browserType == "IE9"){
        // 필요하다면 구현 해야함 !!
    } else {
        gf_sendFileFormAjax(formId, url, callbackFunction);
    }
}

/*******************************************************
 * Form Submit !!
********************************************************/
/*******************************************************
 * Form Submit !!
 * 
 * @param {string} formId
 * @param {string} actionUrl
 * @returns N/A
 * @description : Form Submit !!
 *******************************************************/
function gf_submitForm(){
    var args = gf_submitForm.arguments;
    
    var formId    = args[0];
    var actionUrl = args.length > 1 ? args[1] : "";
    
    if(gf_isNull(formId)){
        return false;   // form ID 없음
    }
    
    var formObj = $("#" + formId);
    
    if(!gf_isNull(actionUrl)){
        formObj.attr("action", actionUrl);
    }
    
    formObj.submit();
}


/*******************************************************
 * 페이지 전환 !!
 * 
 * @param {string} actionUrl
 * @param {string} param
 * @returns N/A
 * @description : 페이지 전환시 사용 !!
 *******************************************************/
function gf_baseMovePage(){
    var args = gf_baseMovePage.arguments;
    
    var actionUrl = args[0];
    var param     = args.length > 1 ? args[1] : "";
    
    if(gf_isNull(actionUrl)){
        return false;   // actionUrl 없음
    }
    
    if(!gf_isNull(param)){
        param = "?" + param;
    }
    
    param += "&pBranchCd="  + $.cookie("__branchCd");
    param += "&pBranchId="  + $.cookie("__branchId");
    param += "&pBranchNm="  + $.cookie("__branchNm");
    param += "&pBranchUrl=" + $.cookie("__branchUrl");
    param += "&pMenuId="    + $.cookie("__menuId");
    param += "&pMenuType="  + $.cookie("__menuType");
    
    location.href = actionUrl + param;
}

function gf_searchMovePage(){
    var args = gf_searchMovePage.arguments;
    
    var actionUrl = args[0];
    var param     = args.length > 1 ? args[1] : "";
    
    if(gf_isNull(actionUrl)){
        return false;   // actionUrl 없음
    }
    
    if(!gf_isNull(param)){
        param = "?" + param + "&" + gf_searchParam(GV_CURR_PAGE_NO);
    }
    
    param += "&pBranchCd="  + $.cookie("__branchCd");
    param += "&pBranchId="  + $.cookie("__branchId");
    param += "&pBranchNm="  + $.cookie("__branchNm");
    param += "&pBranchUrl=" + $.cookie("__branchUrl");
    param += "&pMenuId="    + $.cookie("__menuId");
    param += "&pMenuType="  + $.cookie("__menuType");
    
    location.href = actionUrl + param;
}

/*******************************************************
 * 페이지 전환 Submit : chest에서는 반드시 아래 함수를 사용할 것 !!
 * 
 * @param {string} actionUrl
 * @param {string} param
 * @param {string} addArea
 * @returns N/A
 * @description 페이지 전환시 사용
 *******************************************************/
function gf_submitMovePage(){
    var args = gf_submitMovePage.arguments;
    
    var actionUrl = args[0];
    var param     = args[1];
    var target    = args.length > 2 ? args[2] : "_self";
    var addAreaId = args.length > 3 ? args[3] : "";
    
    if(gf_isNull(actionUrl)){
        return false;   // actionUrl 없음
    }
    
    var formObj = $("#movePageForm");
    
    if(formObj.length > 0){
        formObj.remove();
    }
    
    // 추가 영역에 대한 추가 파라메터 !!
    if(!gf_isNull(addAreaId)){
        param += "&" + gf_searchParam("1", addAreaId);
    }
        
    var formHtml = "";
    
    var arrParam = param.split("&");
    
    formHtml += '<form id="movePageForm" name="movePageForm" method="post" target="' + target + '" action="' + actionUrl + '">';
    
    for(var i=0; i<arrParam.length; i++){
        var keyVal = arrParam[i].split("=");
        
        formHtml += '    <input id="' + keyVal[0] + '" name="' + keyVal[0] + '" type="hidden" value="' + keyVal[1] + '" />';
    }
    
    formHtml += '    <input id="pBranchCd"  name="pBranchCd"  type="hidden" value="' + $.cookie("__branchCd")  + '" />';
    formHtml += '    <input id="pBranchId"  name="pBranchId"  type="hidden" value="' + $.cookie("__branchId")  + '" />';
    formHtml += '    <input id="pBranchNm"  name="pBranchNm"  type="hidden" value="' + $.cookie("__branchNm")  + '" />';
    formHtml += '    <input id="pBranchUrl" name="pBranchUrl" type="hidden" value="' + $.cookie("__branchUrl") + '" />';
    formHtml += '    <input id="pMenuId"    name="pMenuId"    type="hidden" value="' + $.cookie("__menuId")    + '" />';
    formHtml += '    <input id="pMenuType"  name="pMenuType"  type="hidden" value="' + $.cookie("__menuType")  + '" />';
    
    formHtml += '</form>';
    
    $("body").append(formHtml);
    
    $("#movePageForm").submit();
}


/*******************************************************
 * 페이지 전환 Submit : chest에서는 반드시 아래 함수를 사용할 것 !!
 * 
 * @param {string} actionUrl
 * @param {string} param
 * @param {string} addArea
 * @returns N/A
 * @description 페이지 전환시 사용(메뉴ID검색 필요시)
 *******************************************************/
function gf_submitOtherMovePage(){
    var args = gf_submitOtherMovePage.arguments;
    
    var actionUrl = args[0];
    var param     = args[1];
    var target    = args.length > 2 ? args[2] : "_self";
    var addAreaId = args.length > 3 ? args[3] : "";
    
    if(gf_isNull(actionUrl)){
        return false;   // actionUrl 없음
    }
    
    var formObj = $("#movePageForm");
    
    if(formObj.length > 0){
        formObj.remove();
    }
    
    // 추가 영역에 대한 추가 파라메터 !!
    if(!gf_isNull(addAreaId)){
        param += "&" + gf_searchParam("1", addAreaId);
    }
    
    // 다른페이지로 이동시 menu_id find start
    var arrMenuInfo = $plugins.common.findMenuId(actionUrl, "");    
    var menuId   = arrMenuInfo[0];
    var menuType = arrMenuInfo[1];

    $.cookie("__menuId",  menuId);
    $.cookie("__menuType",  menuType);
    // 다른페이지로 이동시 menu_id find end
    
    var formHtml = "";
    
    var arrParam = param.split("&");
    
    formHtml += '<form id="movePageForm" name="movePageForm" method="post" target="' + target + '" action="' + actionUrl + '">';
    
    for(var i=0; i<arrParam.length; i++){
        var keyVal = arrParam[i].split("=");
        
        formHtml += '    <input id="' + keyVal[0] + '" name="' + keyVal[0] + '" type="hidden" value="' + keyVal[1] + '" />';
    }
    
    formHtml += '    <input id="pBranchCd"  name="pBranchCd"  type="hidden" value="' + $.cookie("__branchCd")  + '" />';
    formHtml += '    <input id="pBranchId"  name="pBranchId"  type="hidden" value="' + $.cookie("__branchId")  + '" />';
    formHtml += '    <input id="pBranchNm"  name="pBranchNm"  type="hidden" value="' + $.cookie("__branchNm")  + '" />';
    formHtml += '    <input id="pBranchUrl" name="pBranchUrl" type="hidden" value="' + $.cookie("__branchUrl") + '" />';
    formHtml += '    <input id="pMenuId"    name="pMenuId"    type="hidden" value="' + $.cookie("__menuId")    + '" />';
    formHtml += '    <input id="pMenuType"  name="pMenuType"  type="hidden" value="' + $.cookie("__menuType")  + '" />';
    
    formHtml += '</form>';
    
    $("body").append(formHtml);
    
    $("#movePageForm").submit();
}

/*******************************************************
 * 검색 조건 parameter !!
 * @param {string} pageNo
 * @returns {string} parameter
 * @description 검색 조건 Parameter Setting
 *******************************************************/
function gf_searchParam(pageNo, areaId, pageCount, blockCount){
    pageNo     = !gf_isNull(pageNo)     ? pageNo     : "1";
    areaId     = !gf_isNull(areaId)     ? areaId     : "searchArea";
    pageCount  = !gf_isNull(pageCount)  ? pageCount  : GV_PAGE_COUNT;
    blockCount = !gf_isNull(blockCount) ? blockCount : (gf_getDevice() == "mobile" ? "5" : GV_BLOCK_COUNT);
    
    var param = "";
    
    var rdoId = "";
    var chkId = "";
    
    var areaObj = $("#" + areaId);
    
    $("*", areaObj).each(function() {
        var tagName = $(this).prop("tagName").toUpperCase();
        
        if(tagName == "INPUT" || tagName == "SELECT" || tagName == "TEXTAREA"){
            var id   = $(this).attr("id");
            var name = $(this).attr("name");
            var type = $(this).attr("type");
            
            if(type == "radio"){
                if(rdoId != id){
                    var rdoVal = "";
                    
                    $("#" + areaId + " input[id=" + id + "]").each(function(){
                        if(!$(this).attr("disabled") && $(this).is(":checked")){
                            rdoVal = $(this).val();
                            
                            return false;
                        }
                    });
                    
                    param += "&" + id + "=" + rdoVal;
                    
                    rdoId = id;
                }
            } else if(type == "checkbox"){
                if(chkId != id){
                    var chkVal = "";
                    
                    $("#" + areaId + " input[id=" + id + "]").each(function(){
                        if(!$(this).attr("disabled") && $(this).is(":checked")){
                            chkVal += "|" + $(this).val();
                        }
                    });
                    
                    param += "&" + id + "=" + (chkVal != "" ? chkVal.substring(1) : "");
                    
                    chkId = id;
                }
            } else {
                param += "&" + id + "=" + $(this).val();
            }
        }
    });
    
    // Page Value !!
    param += "&currPageNo=" + pageNo;
    param += "&pageCount="  + pageCount;
    param += "&blockCount=" + blockCount;
    
    // Cookies Value !!
    param += "&pBranchCd="  + $.cookie("__branchCd");
    param += "&pBranchId="  + $.cookie("__branchId");
    param += "&pBranchNm="  + $.cookie("__branchNm");
    param += "&pMenuId="    + $.cookie("__menuId");
    param += "&pMenuType="  + $.cookie("__menuType");
    
    return param.substring(1);
}


/*******************************************************
 * 값 초기화 !!
 * @param {string} targetId
 * @returns N/A
 * @description 값 초기화
 *******************************************************/
function gf_initializeArea(targetId){
    var targetObj = $("#" + targetId);
    
    if(gf_isNull(targetId) || gf_isNull(targetObj)){
        return false;
    }
    
    $("*", targetObj).each(function() {
        var id      = $(this).attr("id");
        var tagName = $(this).prop("tagName").toUpperCase();
        
        var cssNoInit = $(this).hasClass("_noInit");    // 초기화 하지 말아야 할 것
        
        if(tagName == "INPUT"){
            var type = $(this).attr("type");
            
            if(type == "checkbox" || type == "radio"){
                $(this).prop("checked", false);
            } else {
                if(!cssNoInit){
                    $(this).val("");
                }
            }
        } else if(tagName == "TEXTAREA"){
            if(!cssNoInit){
                $(this).val("");
            }
        } else if(tagName == "SELECT"){
            if(!cssNoInit){
                $(this).val("").prop("selected", "selected");
            }
        } else if(tagName == "SPAN"){
            if(!cssNoInit){
                $(this).text("");
            }
        } else if(tagName == "DIV"){
            if(!cssNoInit){
                $(this).html("");
            }
        }
        
        $(this).attr("title", "");
    });
}

/*******************************************************
 * 폼 값 초기화 !!
 * @param {string} formId
 * @returns N/A
 * @description 값 초기화
 *******************************************************/
function gf_initializeForm(formId){
    var formObj = $("#" + formId);
    
    if(gf_isNull(formId) || gf_isNull(formObj)){
        return false;
    }
    
    formObj.each(function() {
        this.reset();
    });
    
    // form 초기화 후 다음 작업 : title 및 hidden 값 제거
    // input hidden 값 or span tag의 경우 지워지게 않할려면 class="_noInit" 를 주면 됨
    $("*", formObj).each(function() {
        var tagName = $(this).prop("tagName").toUpperCase();
        
        var chkBool = false;
        
        if(tagName == "INPUT" || tagName == "SELECT" || tagName == "TEXTAREA" || tagName == "SPAN"){
            var type      = $(this).attr("type");
            var cssNoInit = $(this).hasClass("_noInit");
            
            if(tagName == "INPUT"){
                if(type == "hidden"){
                    if(!cssNoInit){
                        $(this).val("");
                    }
                }
                
                chkBool = true;
            } else if(tagName == "SPAN"){
                if(!cssNoInit){
                    $(this).text("");
                    
                    chkBool = true;
                }
            } else if(tagName == "SELECT"){
                if(!cssNoInit){
                    $(this).val("").prop("selected", "selected");
                }
            } else {
                chkBool = true;
            }
            
            if(chkBool){
                $(this).attr("title", "");
            }
        }
    });
}


/*******************************************************
 * 한 건 Data Mapping !!
 * @param {object} data : Data Information
 * @param {string} targetId : Mapping 시킬 영역 ID [ form ID or div ID or 등등 ]
 * @returns N/A
 * @description 한 건 Data Mapping
 *******************************************************/
function gf_setDataMapping(dataInfo, targetId){
    var targetObj = $("#" + targetId);
    
    if(gf_isNull(dataInfo) || gf_isNull(targetId) || gf_isNull(targetObj)){
        return false;
    }
    
    var rdoId = "";
    var chkId = "";
    
    
    // 초기화 분기
    var tagName = targetObj.prop("tagName").toUpperCase();
    
    if(tagName == "FORM"){
        gf_initializeForm(targetId);
    } else {
        gf_initializeArea(targetId);
    }
    
    
    $("*", targetObj).each(function() {
        var tagName = $(this).prop("tagName").toUpperCase();
        
        if(tagName == "INPUT" || tagName == "SELECT" || tagName == "TEXTAREA" || tagName == "SPAN" || tagName == "DIV"){
            var id   = $(this).attr("id");
            var name = $(this).attr("name");
            var type = $(this).attr("type");
            
            if(!gf_isNull(id)){
                var val = gf_getNullToString(dataInfo[id]);
                
                if(!gf_isNull(val)){
                    if(tagName == "TEXTAREA" || tagName == "SPAN" || tagName == "DIV"){
                        $(this).html(val);
                    } else if(tagName == "SELECT"){
                        $("#" + targetId + " select[id=" + id + "]").val(val).prop("selected", "selected");
                    } else {
                        if(type == "checkbox"){
                            if(chkId != id){
                                var arrVal = val.split("|");
                                
                                $("#" + targetId + " input:checkbox[name=" + id + "]").each(function(idx){
                                    if($(this).val() == arrVal[idx]){
                                        $(this).prop("checked", true);
                                    }
                                });
                                
                                chkId = id;
                            }
                        } else if(type == "radio"){
                            if(rdoId != id){
                                $("#" + targetId + " input:radio[name=" + id + "]").each(function(){
                                    if($(this).val() == val){
                                        $(this).prop("checked", true);
                                        return false;
                                    }
                                });
                                
                                rdoId = id;
                            }
                        } else {
                            $(this).val(val);
                        } 
                    }
                    
                    // tag Name 이 INPUT or SPAN 건만 title 추가 !!
                    if(tagName == "INPUT" || tagName == "SPAN"){
                        $(this).attr("title", val);
                    }
                }
            }
        }
    });
}


/*******************************************************
 * 저장 Data Check !!
 * @param {string} targetId
 * @description 저장 Data Check !!
 *******************************************************/
function gf_storeDataCheck(targetId){
    var targetObj = $("#" + targetId);
    
    if(gf_isNull(targetId) || gf_isNull(targetObj)){
        return false;
    }
    
    var bool = true;
    
    $("*", targetObj).each(function(idx) {
        var tagName = $(this).prop("tagName").toUpperCase();
        
        if(tagName == "INPUT" || tagName == "SELECT" || tagName == "TEXTAREA"){
            var id   = $(this).attr("id");    // ID
            var val  = $(this).val();         // Value
            var type = $(this).attr("type");  // Type
            
            var thTxt = $(this).closest("td").prev().text();    // <th> Tag Text
                thTxt = $.trim(thTxt.replace("*", "")); 
            
            // 필수 체크
            if($(this).hasClass("_required")){
                if(gf_isNull(val)){
                    alert(thTxt + "(을)를" + (tagName == "SELECT" ? "선택" : "입력") + "하세요.");  // [field Name] (을)를 입력/선택하십시오.
                    $("#" + id, targetObj).focus();
                    bool = false;
                    return false;
                }
            }
            
            // 입력 Byte 수 체크
            var maxlength = Number($(this).attr("maxlength"));
            var curLength = gf_getStrByte(val);
            
            if(!gf_isNull(maxlength) && maxlength > 0){
                if(!gf_isNull(val)){
                    if(curLength > maxlength){
                        alert(thTxt + " 의 입력 길이 초과입니다.\n[ 입력:" + curLength + " byte, 입력제한:" + maxlength + " byte ]");
                        $("#" + id, targetObj).focus();
                        bool = false;
                        return false;
                    }
                }
            }
        }
    });
    
    return bool;
}











/***********************************************************************
 * 공통 팝업 관련 함수
 **********************************************************************/
/*******************************************************
 * Window Open !!
 * 
 * @param {string} loc     : Popup URL
 * @param {string} popname : Popup Window Name
 * @param {int} width
 * @param {int} height
 * @param {int} left
 * @param {int} top
 * @returns {string} openw : popup window name
 * @description Window Open
 *******************************************************/
function gf_openWin() {
    var args = gf_openWin.arguments;
    
    var loc     = args[0];
    var popname = args.length > 1 ? args[1] : "";
    var width   = args.length > 2 ? args[2] : (document.body.clientWidth  / 2);
    var height  = args.length > 3 ? args[3] : (document.body.clientHeight / 2);
    var left    = 0;
    var top     = 0;
    
    if(args.length < 5) {
        left = (screen.width  - width)  / 2;
        top  = (screen.height - height) / 2;
    }
    
    var status = "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0,width=" + width + ",height=" + height + ",top=" + top + ",left=" + left;
    
    var openw = window.open(loc, popname, status);
    
    return openw;
}

/*******************************************************
 * ShowModalDialog OPEN !!
 * 
 * @param {string} url : PopUp URL
 * @param {object} args
 * @param {int} width
 * @param {int} height
 * @returns {object} args
 * @description Modal Window Open
 *******************************************************/
function gf_openWinDialog(url, args, width, height){
    if(!args)
        args = new Object();
    
    var status = "scroll:auto;status:no;help:no;dialogWidth:" + width + "px;dialogHeight:" + height + "px;border-color:#ADADAD";
    
    var ret = window.showModalDialog(url, args, status);
    
    args.returnValue = ret;
    
    return args;
}


/*******************************************************
 * 일반 첨부파일 Delete Submit !!
 * @param {string} filePath
 * @param {string} pFileNm
 * @param {string} lFileNm
 * @returns N/A
 * @description 일반 첨부파일 Delete Submit!!
 *******************************************************/
function gf_genDeleteFile(fileLinkObj){
    var parentObj = $(fileLinkObj).parent();
    
    if(fileLinkObj == null || parentObj == null){
        return false;
    }
    
    var filePath   = $("#filePath",   parentObj).val();
    var phycFileNm = $("#phycFileNm", parentObj).val();
    var logcFileNm = $("#logcFileNm", parentObj).val();
    
    if(filePath == null || phycFileNm == null || logcFileNm == null){
        return false;
    }
    
    var param  = "filePath="    + filePath;
        param += "&phycFileNm=" + phycFileNm;
        param += "&logcFileNm=" + logcFileNm;
    
    if(!confirm("삭제하시겠습니까?")){
        return false;
    }
    
    gf_send("/common/genDeleteFile.do", param, gf_genDeleteFileAfter);
}

function gf_genDeleteFileAfter(data){
    if(Number(data.result) == 1){
        alert("파일이 정상적으로 삭제되었습니다");
    } else {
        alert("파일 삭제에 실패하였습니다.");
    }
}


/*******************************************************
 * 일반 첨부파일 다운로드 Submit !!
 * @param {string} filePath
 * @param {string} phycFileNm
 * @param {string} logcFileNm
 * @returns N/A
 * @description 일반 첨부파일 다운로드 Submit !!
 *******************************************************/
function gf_genFileExport(fileLinkObj){
    var parentObj = $(fileLinkObj).parent();
    
    if(fileLinkObj == null || parentObj == null){
        return false;
    }
    
    var filePath   = $("#filePath",   parentObj).val();
    var phycFileNm = $("#phycFileNm", parentObj).val();
    var logcFileNm = $("#logcFileNm", parentObj).val();
    
    if(filePath == null || phycFileNm == null || logcFileNm == null){
        return false;
    }
    
    var param  = "filePath="    + filePath;
        param += "&phycFileNm=" + phycFileNm;
        param += "&logcFileNm=" + logcFileNm;
    
    gf_genDownLoadFile(param);
}

/*******************************************************
 * 일반 파일 다운로드 Submit !!
 * @param {string} param
 * @param {string} actionUrl
 * @returns N/A
 * @description 일반 파일 다운로드 Submit !!
 *******************************************************/
function gf_genDownLoadFile(){
    var args = gf_genDownLoadFile.arguments;
    
    var param        = args[0];
    var actionChkUrl = args.length > 1 ? args[1] : "/common/checkGenDownloadFile.do";
    var actionUrl    = args.length > 2 ? args[2] : "/common/genDownloadFile.do";
    
    if($("#genDownloadForm").length > 0){
        $("#genDownloadForm").remove();
    }
    
    var formHtml = "";
    
    var arrParam = param.split("&");
    
    formHtml += '<form id="genDownloadForm" name="genDownloadForm" method="post" action="' + actionChkUrl + '">';
    
    for(var i=0; i<arrParam.length; i++){
        var keyVal = arrParam[i].split("=");
        
      //formHtml += '<input id="' + keyVal[0] + '" name="' + keyVal[0] + '" type="hidden" value="' + encodeURI(keyVal[1]) + '" />';
        formHtml += '<input id="' + keyVal[0] + '" name="' + keyVal[0] + '" type="hidden" value="' + keyVal[1] + '" />';
    }
    
    formHtml += '<input id="actionUrl" name="actionUrl" type="hidden" value="' + actionUrl + '" />';
    formHtml += '</form>';
    
    $("body").append(formHtml);
    
    gf_sendForm("genDownloadForm", "", gf_genDownLoadFileAfter);
}

/*******************************************************
 * 일반 파일 다운로드 Check After Submit !!
 * @param {object} data
 * @returns N/A
 * @description 일반 파일 다운로드 Check After Submit !!
 *******************************************************/
function gf_genDownLoadFileAfter(data){
    if(data.existYn == "Y"){
        $("#genDownloadForm").attr("action", data.actionUrl).submit();
    } else {
        alert("지정된 경로에 파일이 없습니다.");
        return false;
    }
}

/*******************************************************
 * List Excel Download !!
 * @returns N/A
 * @description List Excel Download
 *******************************************************/
function gf_genExcelDownList(){
    if(confirm("엑셀 다운로드 하시겠습니까?")){
        $("#genExcelDownForm").submit();
    }
}

/*******************************************************
 * List Excel Download !!
 * @param {string} actionUrl
 * @param {string} dataGridId
 * @param {string} param
 * @returns N/A
 * @description List Excel Download
 *******************************************************/
function gf_genExcelDownListForm(opt){
    opt = !gf_isNull(opt) ? opt : {};
    
    var actionUrl  = !gf_isNull(opt.actionUrl)  ? opt.actionUrl  : "";                 // action Url
    var dataGridId = !gf_isNull(opt.dataGridId) ? opt.dataGridId : GV_GRID_ID;         // target Id
    var param      = !gf_isNull(opt.param)      ? opt.param      : gf_searchParam();   // Param
    var excepCol   = !gf_isNull(opt.excepCol)   ? opt.excepCol   : "";                 // Exception Column
    
    if(actionUrl == "" || dataGridId == ""){
        return false;
    }
    
    if($("#genExcelDownForm").length > 0){
        $("#genExcelDownForm").remove();
    }
    
    var formHtml = "";
    
    var arrParam = param.split("&");
    
    formHtml += '<form id="genExcelDownForm" name="genExcelDownForm" method="post" action="' + actionUrl + '">';
    
    for(var i=0; i<arrParam.length; i++){
        var keyVal = arrParam[i].split("=");
        
        formHtml += '<input id="' + keyVal[0] + '" name="' + keyVal[0] + '" type="hidden" value="' + keyVal[1] + '" />';
    }
    
    // Exception Column
    var arrExcepCol = excepCol != "" ? excepCol.split(",") : "";
    
    // Excel Title Name !!
    var tempTitle = "";
    
    $("#" + dataGridId + " thead:eq(0) tr th").each(function(idx) {
        var bool = true;
        
        var titleText = $(this).text();
        
        for(var i=0; i<arrExcepCol.length; i++){
            if(i == idx){
                bool = false;
                break;
            }
        }
        
        if(bool){
            tempTitle += "," + titleText;
        }
    });
    
    formHtml += '<input id="titleInfo" name="titleInfo" type="hidden" value="' + tempTitle.substring(1) + '" />';
    
    
    // Excel Title Width !!
    var tempWidth = "";
    
    $("#" + dataGridId + " colgroup col").each(function(idx) {
        var bool = true;
        
        var titleWidth = $(this).css("width");
        
        for(var i=0; i<arrExcepCol.length; i++){
            if(i == idx){
                bool = false;
                break;
            }
        }
        
        if(bool){
            var wid = Math.ceil(titleWidth.replace("%", ""));
            
            tempWidth += "," + wid;
        }
    });
    
    formHtml += '<input id="titleWidth" name="titleWidth" type="hidden" value="' + tempWidth.substring(1) + '" />';
    
    
    // Excel Cell Align !!
    var tempAlign = "";
    
    $("#" + dataGridId + " tbody tr:eq(0) td").each(function(idx) {
        var bool = true;
        
        for(var i=0; i<arrExcepCol.length; i++){
            if(i == idx){
                bool = false;
                break;
            }
        }
        
        if(bool){
            if($(this).hasClass("tl")){
                tempAlign += ",L";
            } else if($(this).hasClass("tr")){
                tempAlign += ",R";
            } else {
                tempAlign += ",C";
            }
        }
    });
    
    formHtml += '<input id="colAlign" name="colAlign" type="hidden" value="' + tempAlign.substring(1) + '" />';
    
    formHtml += '</form>';
    
    $("body").append(formHtml);
}

function prograssbarOpen(){
	$('#asxLoading').show();
}
function prograssbarClose(){
	$("#asxLoading").hide();
}


/*******************************************************
 * file Submit !!
 * 
 * @param {string} actionUrl
 * @param {string} dataParam
 * @param {string} callbackFunction
 * @returns {Boolean}
 * @description 파일을 포함한 ajax
 *******************************************************/
function gf_fileSend(){
    var args = gf_fileSend.arguments;
    
    var actionUrl        = args[0];
    var param            = args.length > 1 ? args[1] : "";
    var callbackFunction = args.length > 2 ? args[2] : null;
    var syncBool         = args.length > 3 ? args[3] : true;
    
    var myajax = new MyAjax();
    
    myajax.sendFile(actionUrl, param, gf_getAfterFunction(actionUrl, callbackFunction), syncBool);
    
    return false;
}