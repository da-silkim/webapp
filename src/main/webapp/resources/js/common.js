/* Side Navigation */
$('.toggle-side-bar-btn').click(function(){
    //console.log("토글 사이드바 버튼클릭");

    var $clicked = $(this);
    var nowAnimating = $clicked.attr('data-ico-now-animating');

    if ( nowAnimating == "Y" ){
        return;
    }

    if ( $clicked.hasClass('active') ){
        hideLeftSideBar();
        $(".footNavi-wrap").css('display','block');
    }
    else {
        showLeftSideBar();
        $(".footNavi-wrap").css('display','none');
    }

    $clicked.attr('data-ico-now-animating', 'Y');

    $clicked.toggleClass('active');

    setTimeout(function(){
        $clicked.attr('data-ico-now-animating', 'N');
    }, 400);
});

function showLeftSideBar(){

    $('.sideNavi-sub-wrap > .menu-1 ul > li.active').removeClass('active');
    $('.sideNavi-wrap').addClass('active');
};
function hideLeftSideBar(){
    $('.sideNavi-wrap').removeClass('active');
};


$('.sideNavi-wrap, .btn-sidenavi-close').click(function(){
    //console.log('배경클릭');
    $('.toggle-side-bar-btn').click();
});

$('.sideNavi-sub-wrap').click(function(e){
    e.stopPropagation();
});

/* 즐겨찾기 클릭 시 이미지 on, off  */
$( document ).ready( function() {
    $( '.btn-bookmark' ).click( function() {
    $( '.btn-bookmark' ).toggleClass( 'on' );
  });
});

$(document).on("input", "[onlyNumber]", function(event){
    this.value=this.value.replace(/[^0-9]/g,"");
    var code = event.keyCode;
    if((code>47&&code<58)||event.ctrlKey||event.altKey||code==8||code==9||code==46){
        return;
    }
    event.preventDefault();
    return false;
});

$(document).on("input", "[autoNext]", function(event){
    let length = $(this).val().length;
    let maxlength = $(this).attr("maxlength");
    if(maxlength == length){
        const nextName = $(this).attr("autoNext");
        $("[name='"+nextName+"']").focus();
    }
});

$(document).on("input", "[onlyNumberEng]", function(event){
    this.value=this.value.replace(/[^0-9]/g,"");
    var code = event.keyCode;
    if((code>47&&code<58)||event.ctrlKey||event.altKey||code==8||code==9||code==46){
        return;
    }
    event.preventDefault();
    return false;
});


function payment(uri, chargerNo){
    const connectorValue = $(":input:radio[name=connector]:checked").val();

    if(connectorValue === undefined){
        pop_alert("fail", "커넥터를 선택해주세요.", "챠랑에 맞는 커넥터를 확인 후 선택해주세요.")
        return;
    }
    const connectorNumber = Number(connectorValue);
    const query = `?chargerNo=${chargerNo}&connector=${connectorNumber}`;
    const url = '/app/payment/' + uri + query;

    fnOpenPopup(url);
}


function formatPopupContents(text){
    let result = '';

    result += '<p class="popup-contents-signle-text">';
    result += text
    result += '</p>';

    return result;
}

function maxLengthCheck(object){
    if (object.value.length > object.maxLength){
        object.value = object.value.slice(0, object.maxLength);
    }
}

function fn_comma(x){
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

//app -> web 호출 : 충전중이면 충전화면으로 이동
function jsFunc_checkState(){
    $.ajax({
        url: "/app/member/check_charge/action",
        type: 'post',
        async: false,
        success : function(res) {
            if(res['result']['message'] == 'success'){
                location.href="/app/member/now_charge";
            }
        }
    });
}


function checkPassword ()  {


    let pw = $("#userpw").val();
    let pw2 = $("#userpw2").val();

    const check1 = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9])/.test(pw);   //영문,숫자,특수문자
    const check2 = /^.{8,20}$/.test(pw);   //8~20자

    if(pw != '' && (!check1 || !check2)){
        $("#is_comb").show();
    }
    else{
        $("#is_comb").hide();
    }



    if(pw != '' && !(check2)){
        $("#is_password").show();
    }
    else{
        $("#is_password").hide();
    }


    var check3 = (pw == pw2);

    if( pw != "" && pw2 != "" && (!check3) ){
        $("#is_same").show();
    }
    else{
        $("#is_same").hide();
    }

    return check1 && check2 && check3;
}

function phonetoArr(phone)  {
    let arr = [];
    if( phone != 'undifiend' && phone != null  ){
        if( phone.length == 11 ){
            arr[0] = phone.substr(0, 3);
            arr[1] = phone.substr(3, 4);
            arr[2] = phone.substr(7, 4);

        }
    }
    return arr;

}

function fn_pop_close(){
    $("#popupBackground").addClass("none");
    let popName = fn_getCookie("pupupFocusId");
    if( popName ){
        fn_deleteCookie("pupupFocusId");
        $("#"+popName).focus();
    }
    let popGubun = fn_getCookie("pupupGubunId");
    if( popGubun == "login" ){
        fn_deleteCookie("pupupGubunId");
        location.href="/app/main";
    }
}

function fn_pop_open(gubun, title, content){
	$(".modal-title").html(title);
	$(".modal-body").html('<p>' + content + '</p>');
	$("#customModal").modal("show");

}

function fn_setCookie(name, value, expiredays)  {
    var todayDate = new Date();
    todayDate.setDate(todayDate.getDate() + expiredays);
    document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";"

}

function fn_getCookie(cookieName)  {
    var cookieValue = null;
    if(document.cookie){
        var array = document.cookie.split((escape(cookieName) +'='));
        if(array.length >= 2) {
            var arraySub=array[1].split(';');
            cookieValue=unescape(arraySub[0]);
        }
    }
    return cookieValue;

}

function fn_deleteCookie(cookieName)  {
    document.cookie = cookieName + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function callAjax(callUrl, callData, successCallback, errorCallback, options){
	
	let isSubmitable = true;

    //init
    let callMethod = "post";
    let callAsync = false;
    let callContentType = "application/x-www-form-urlencoded;charset=UTF-8";

    if(options != null){
        if(options.method != null){
            callMethod = options.method;
        }
        if(options.async != null){
            callAsync = options.async;
        }
        if(options.contentType != null){
            callContentType=options.contentType;
        }
    }

    console.log("AJAX URL", callUrl);
    console.log("AJAX DATA", callData);

    $.ajax({
        url: callUrl,
        type: callMethod,
        data: callData,
        async: callAsync,
        contentType: callContentType,
        success : function(res) {
            console.log("AJAX RESULT success : ", res);
			successCallback(res);
        },
        error : function(xhr, textStatus, errorThrown){
			console.log(xhr);
			console.log(textStatus);
			console.log(errorThrown);
			errorCallback(xhr);
        }
    });
}

function $formatNumber(number) {
    if (number === 0 || number === '0') {
        return number
    } else {
        const result = number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
        return result
    }
}

function $fnNumComma(number){
    // 숫자만 입력
    var num = number+"";
    num.replace(/[^0-9]/g, "");
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
    return str;
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

function fn_makeMobileNumber( mobile ) {
		var m = mobile;
		var result='';
		if( m.length > 0 ) {
			if( m.length > 10 ) {
				a=m.substring(0,3);
				b=m.substring(3,7);
				c=m.substring(7);
			} else {
				a=m.substring(0,3);
				b=m.substring(3,6);
				c=m.substring(6);
			}
			result = a+"-"+b+"-"+c;
		}
		return result;
	}
