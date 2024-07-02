// 파라미터 받아오기
function getParameter(strParamName) {
    var arrResult = null;

    if (strParamName) {
        arrResult = location.search.match(new RegExp("[&?]" + strParamName + "=(.*?)(&|$)"));
    }

    return arrResult && arrResult[1] ? arrResult[1] : null;
}



// 모바일 테이블
$(function() {
    if (chaked_OS() == true) {
        if ($(window).width() < 768) {
            $.each($(".replace_calendar"), function() {
                var table = $(this);
                var caption = table.find("caption");
                var create_tr = "";
                var create_th = "";
                var create_td = "";

                $.each(table.find("tr"), function() {
                    var tr = $(this);

                    $.each(tr.find("[data-title]"), function() {
                        var td = $(this);

                        create_th = "<th scope='row'>" + td.attr("data-title") + "</th>";
                        create_td = "<td>" + td.html() + "</td>";
                        create_tr += "<tr>" + create_th + create_td + "</tr>";
                    });
                });

                table.html(create_tr);
                table.prepend(caption + "<colgroup><col style='width:20%'><col></colgroup>");
            });
        }
    }
});


/* flow label */
$(document).ready(function() {
    if (!$("label").is(".flow")) return false;

    var o1 = $("label.flow");
    var o2 = $("label.flow").next();

    o1.css({ "position": "absolute" });

    o1.bind("click focusin", function() {
        $(this).css({ "visibility": "hidden" });
    });
    o2.bind("click focusin", function() {
        $(this).prev().css({ "visibility": "hidden" });
    });

    o2.bind("focusout", function() {
        if ($(this).val() == "") {
            $(this).prev().css({ "visibility": "visible" });
        }
    });

    if (o2.val() != "") { o1.css({ "visibility": "hidden" }); }



});


/* OS 체크 불린값 전달 window 폰, window 기반 태블릿pc 에서 테스트가 필요함 */
function chaked_OS() {
    var device = navigator.userAgent;

    var str = device.split(";");
    str = str[0].split("(");
    str = str[1].split(" ");

    var chkOS = false;

    if (str[0] != "Windows" && str[0] != "Macintosh" && str[0] != "compatible") {
        chkOS = true; // 데스크탑이 아닐 때 true
    }

    return chkOS;
}

function return_width() {
    var w = $(window).width();
    return w;
}

/* MSIE 9이하 버전체크 */
function ms_ver() {
    if (navigator.userAgent.match('MSIE')) {
        var msie = navigator.userAgent;
        var ms_ver = msie.substr(msie.lastIndexOf('MSIE')).split('MSIE')[1];
        ms_ver = Number(ms_ver.split('.')[0]);

        return ms_ver;
    } else {
        return null;
    }
}


//쿠키저장
function setCookie(name, value, expiredays) {
    var todayDate = new Date();
    todayDate.setDate(todayDate.getDate() + expiredays);
    document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}

function getCookie(name) {
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    while (i < clen) {
        var j = i + alen;
        if (document.cookie.substring(i, j) == arg) {
            var end = document.cookie.indexOf(";", j);
            if (end == -1) end = document.cookie.length;
            return unescape(document.cookie.substring(j, end));
        }
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) break;
    }
    return null;
}


/*********팝업관련 자바스크립트 소스****************/
function close_layer(num) {
    document.getElementById(num).style.display = 'none';

    //쿠키굽기
    setCookie(num, 'done', 1);
}

function close_layer2(num) {
    document.getElementById(num).style.display = 'none';

}

function link_target(url, target) {
    if (target == '_blank') {
        window.open(url);
    } else if (target == '_self') {
        location.href = url;
    } else {
        opener.location.href = url;
    }
}

function admissionFormCheck() {
    var f = document.admissionForm;
    if (f.n_name.value.length < 1) {
        alert("이름을 입력해 주세요");
        f.n_name.focus();
        return false;
    }

    if (f.jumin_num.value.length != 13) {
        alert("주민번호는 숫자만 입력해 주세요");
        f.jumin_num.focus();
        return false;
    }
    return true;
}

function message() {
    alert("서비스준비중입니다.");
    return false;


}



// 탭메뉴
function document_tab(param, btn, obj) {
    var param = $(param);
    var btn = param.find(btn);
    var obj = param.find(obj);
    var elem = 0;

    var lcv = location.href;
    lcv = lcv.split("?ttab=");

    if (lcv[1]) {
        elem = lcv[1];
    }

    obj.css("opacity", 0);
    obj.eq(elem).css("opacity", 1);

    obj.hide().eq(elem).show();
    btn.removeClass("on");
    btn.eq(elem).addClass("on");


    btn.bind('click', function() {
        var t = $(this);
        btn.removeClass("on");
        t.addClass("on");
        elem = t.index();
        obj.delay(50).hide().animate({ "opacity": 0 }, 150, 'swing')
        obj.eq(elem).delay(50).show().animate({ "opacity": 1 }, 150, 'swing')

        return false;
    });
}
//
// function targetOpener(btn, option) {
//     var btn = $(btn);
//
//     function _in(event) {
//         var t = $(this);
//         var href = t.attr("href");
//
//         if (t.children().is('img')) {
//             var btnIMG = btn.find('img');
//             var thisIMG = t.children();
//             var thisSRC = thisIMG.attr('src');
//             thisSRC = thisSRC.substr(thisSRC.lastIndexOf('_')).split(".");
//
//             if (thisSRC[0] != "_ov") {
//                 $.each(btnIMG, function() {
//                     $(this).attr("src", $(this).attr('src').replace('_ov.' + thisSRC[1], '.' + thisSRC[1]));
//                 });
//
//                 thisIMG.attr("src", thisIMG.attr('src').replace('.' + thisSRC[1], '_ov.' + thisSRC[1]));
//
//             }
//         }
//
//         if (option.lv == 0) {
//             if ($(href).css("display") == "none") {
//                 $(href).show();
//                 t.addClass("ov");
//             } else {
//                 $(href).hide();
//                 t.removeClass("ov");
//             }
//         }
//
//         if (option.lv == 1) {
//             $(option.obj).hide();
//             $(href).show();
//             btn.removeClass("ov");
//             t.addClass("ov");
//         }
//         event.preventDefault();
//     }
//
//     btn.on("click", _in);
// }



function targetOpener(btn,option){
    var btn = $(btn);

    $(option.obj).hide().first().show();

    btn.bind("click",function(event){
        var t = $(this);
        href = t.attr("href").substr(1);


        if(t.children().is('img')){
            var btnIMG = btn.find('img');
            var thisIMG = t.children();
            var thisSRC = thisIMG.attr('src');
            thisSRC = thisSRC.substr(thisSRC.lastIndexOf('_')).split(".");

            if(thisSRC[0] != "_ov"){
                $.each(btnIMG,function(){
                    $(this).attr("src",$(this).attr('src').replace('_ov.'+thisSRC[1],'.'+thisSRC[1]));
                });
                thisIMG.attr("src",thisIMG.attr('src').replace('.'+thisSRC[1],'_ov.'+thisSRC[1]));
            }
        }

        if(option.lv == 0){
            if($("[id="+href+"]").css("display") == "none"){
                $("[id="+href+"]").show().css({'visibility':'visible'});
                t.addClass("ov");
            }else{
                $("[id="+href+"]").hide().css({'visibility':'hidden'});
                t.removeClass("ov");
            }
        }

        if(option.lv == 1){
            $(option.obj).hide().css({'visibility':'hidden'});
            $("[id="+href+"]").show().css({'visibility':'visible'});
            btn.removeClass("ov");
            t.addClass("ov");
        }
        event.preventDefault();
    });
}




$(function() {
    $("#gnb").find("a[target*=blank]").append("<img class='window' alt='새창열림' src='/images/common/ico_window.gif' />");
    $("#lnb").find("a[target*=blank]").append("<img class='window' alt='새창열림' src='/images/common/ico_window.gif' />");
});

$(function() {
    $('.join_cer .btn_l').hide();

    $("#childNm14 input:checkbox").prop("checked", false);
    $('#childNm14 input:checkbox').on('click', function() {
        if ($('#childNm14 input:checkbox').is(':checked')) {
            $('#childNm14').hide();
            $('.join_cer .btn_l').show();
            $("#childNm14 input:checkbox").prop("checked", true);
            /*
            $('#ipin').attr('onclick','inqMemberNo("Y");'); 
            $('#gpin').prop('onclick','inqMemberNo("N");'); 
            */
           // document.getElementById("#ipin").attachEvent("onclick", new Function('inqMemberNo("Y")'));

           $('#ipin').attr('onclick','inqMemberNo("Y");');
            $('#gpin').attr('onclick','inqMemberNo("N");'); 

        } else {
            $('#childNm14').show();
            $('.join_cer .btn_l').hide();
            $("#childNm14 input:checkbox").prop("checked", false);
            $('#ipin').attr('onclick','openPCCWindow();'); 
            $('#gpin').attr('onclick','GpinAuth();'); 
            
        }

        //14세 이상시 ipin => openPCCWindow() gpin =>GpinAuth()
        //14세 미만시 ipin => inqMemberNo('Y') gpin =>inqMemberNo('N')
    });



    $('#lang_site').hide();
    $('.lang h2').unbind().bind("click touchstart", function(event) {
        $('#lang_site').toggle();
        $(this).toggleClass('fold');
        $('#lnb_hm').css({ 'overflow': 'visible' });
        event.preventDefault();
    });
    $('#lang_site .close').unbind().bind("click touchstart", function(event) {
        $('#lang_site').toggle();
        $('.lang h2').removeClass('fold');
        $('#lnb_hm').css({ 'overflow': 'hidden' });
        event.preventDefault();
    });
});




$(function() {
    var obj = $("#topkeyword");
    var btn = $("#open_sc");
    var close = obj.find(".close>a");


    function blurApple() {
        var focusNow = document.activeElement;
        if (focusNow.getAttribute('id') != lastFocus) {
            clearInterval(checkFocusInterval);
            obj.removeClass("hiddenSearch");

        }
    }

    btn.unbind().bind("click touchend", function(event) {
        if (obj.css("display") == "none") {
            obj.slideToggle('fast');
            obj.addClass("hiddenSearch");
            btn.addClass("ov");
            $("#quickmenu").removeClass("hiddenSearch");
            //$("#shadow_device").stop(true,true).fadeIn(dur/2);
        } else {
            obj.slideUp('fast');
            obj.removeClass("hiddenSearch");
            btn.removeClass("ov");
            //$("#shadow_device").stop(true,true).fadeOut(dur/2);
        }

        event.preventDefault();
        event.stopPropagation();
    });

    close.unbind().bind("click touchend", function(event) {
        obj.slideUp('fast');
        obj.removeClass("hiddenSearch");
        btn.removeClass("ov");
        event.preventDefault();
        event.stopPropagation();
    });


});


$(function() {
    var obj = $("#menu-area");
    var btn = $("#open_area");



    btn.unbind().bind("click", function(event) {
        if (obj.css("display") == "none") {
            obj.slideToggle('fast');
            obj.addClass("hiddenSearch");
            btn.addClass("ov");
        } else {
            obj.slideUp('fast');
            obj.removeClass("hiddenSearch");
            btn.removeClass("ov");
        }

        event.preventDefault();
        event.stopPropagation();
    });

});


$(function() {

    linklst();
});



/* link list */
function linklst() {



    var dep1 = $("#link_list > ul > li > button");
    var linkList = $("#link_list > ul > li article");
    var dep2 = $("#link_list .dep2");
    var close = $("#link_list .close button");
    var close2 = $("#link_list article .close button");

    linkList.hide();
    dep2.show();

    dep1.click(function() {
        if ($(this).parent().index() == 4) {
            $(this).toggleClass("on");
            dep1.not(this).removeClass("on");
            dep2.stop().slideToggle();
            linkList.slideUp();
        } else {
            $(this).addClass("on").next().slideDown();
            dep1.not(this).removeClass("on").next().slideUp();
            dep2.stop().slideUp();
        }
    });

    close.click(function() {
        dep1.removeClass("on");
        dep2.stop().slideUp();
    });

    close2.click(function() {
        var val = $(this).val();
        if (val == 1) {
            $("#sitemap2").focus();
        }
        if (val == 2) {
            $("#sitemap3").focus();
        }
        if (val == 3) {
            $("#sitemap4").focus();
            dep2.stop().slideUp();
        }
        if (val == 4) {
            $("#sitemap5").focus();
            dep2.stop().slideUp();
        }
        dep1.removeClass("on");
        linkList.stop().slideUp();
    });
}



/* 모바일에서 정상적인 테이블 뷰가 가능하도록 */
function responseTable() {
    if ($(".responseTable").size() === 0) return false;

    if (location.href.match("BBSMSTR")) return false;
    if (location.href.match("prog")) return false;

    if (return_width() < 768) {
        var param = $(".tbl_basic, .basic_table");
        //alert('aa');

        $.each(param, function() {
            if ($(this).parent().parent().attr("class") != "responseTable") {
                $(this).wrap("<div style='position:relative' class='responseTable'></div>");
                $(this).wrap("<div style='width:100%;min-width:730px' class='table_scroll_x'></div>");
            }

            $(this).parent().parent().height($(this).outerHeight() + 20);
            $(window).resize(function(event) {
                $(this).parent().parent().height($(this).outerHeight() + 20);
                event.stopPropagation();
            });

        });

        fleXenv.initByClass("responseTable");


        var startX, startY;

        function getCoord(e, c) {
            return /touch/.test(e.type) ? (e.originalEvent || e).changedTouches[0]['page' + c] : e['page' + c];
        }

        $(".responseTable").on('touchstart', function(event) {
            startX = getCoord(event, 'X');
            startY = getCoord(event, 'Y');
        }).on('touchend', function(event) {
            if (Math.abs(getCoord(event, 'X') - startX) < 20 && Math.abs(getCoord(event, 'Y') - startY) < 20) {
                $(window).stop(true, false).animate({ top: startY }, 0);
                //event.preventDefault();
            }
        });
    }
}


$(document).ready(function() {
    //responseTable();
});


function footersitelink(param,btn,obj){
    var param = $(param);
    var btn = param.find(btn);
    var obj = param.find(obj);

    btn.bind("click",function(event){
        var t = $(this);

        if(t.next().css('display') == 'none'){
            obj.hide();
            t.next().show();
            btn.removeClass("ov");
            $(this).addClass("ov");
        }else{
            obj.hide();
            btn.removeClass("ov");
        }

        event.preventDefault();
    });
/*
    param.bind("mouseleave",function(){
        obj.hide();
        btn.removeClass("ov");
    });
*/
    param.find("a").last().bind("focusout",function(){
        obj.hide();
        btn.removeClass("ov");
    });
}