// JavaScript Document

$(function () {

    // 스크롤시 header 고정
    $(window).scroll(function(){
		//console.log($(document).height()); 
		var height = $(document).height(); 
        if($(window).scrollTop() > 0 && height>1500 ) {
            $('#header').addClass('fixed');
        } else {
            $('#header').removeClass('fixed');
        }
    });
	


    // header hover
    $('#header').on('mouseenter',function () {
        if ($(window).width() > 1025) {
            headerEnter();
        } else {
            return true;
        }
    });
    function headerEnter() {
        $('#header').addClass('hover');
    }
    $('#header').on('mouseleave',function () {
        $('#header').removeClass('hover');
    });


    // header gnb 서브메뉴
    $(document).on('mouseenter', '#header__lnb .lnb-menu > li > a', function() {

		var is_mobile_menu = $('#header').hasClass('ismobile'); 
		if(is_mobile_menu) {
			// alert('모바일메뉴보는중'); 
			// return false; 
		}
       if ($(window).width() > 1025 || is_mobile_menu) {

			$('.lnb_sub_menu').addClass('over');
			if(!is_mobile_menu) { 
	            $('#header').addClass('open');
			} 
			var obj = $(this).parent('li');
			$('.lnb-menu > li').stop().removeClass('over'); 
			
			var has = obj.hasClass('over'); 
			
			// obj.addClass('over');		
			
				var timeout = setTimeout(function(){
					obj.addClass('over');		
				}, 10); 
				
			if(!has) { 

			} 
			// console.log('over'); 
		} else {
			return true;
		}
    });

    $(document).on('mouseleave', '#header__lnb .lnb-menu > li > a', function() {
        if ($(window).width() > 1025) {

			var obj = $(this).parent(); 
			obj.removeClass('over'); 
			// alert('a');
        } else {
            return true;
        }
    });

    $('#header__lnb > .nav').on('mouseleave', function(){
        $('.lnb_sub_menu').removeClass('over');
        $('#header').removeClass('open');
		$('.lnb-menu > li').removeClass('over'); 
		// console.log('nav'); 
		//alert('a');
    });

    $(document).on('click', '#header__lnb .lnb-menu > li', function() {
		var is_mobile_menu = $('#header').hasClass('ismobile'); 

        if ($(window).width() < 1200 || is_mobile_menu) {
            var gnbmenu = $(this).find('.lnb_sub_menu');
            if(gnbmenu.css('display') == 'none') {
                gnbmenu.fadeIn();
                $("#header__lnb .lnb-menu > li").removeClass('ov');
                $(this).addClass('ov');
                $('.lnb_sub_menu').not(gnbmenu).hide();
                return false;
            }
            else {
                gnbmenu.fadeOut();
            }
        } else {
            return true;
        }
    });

    $(document).on('mouseenter', '#header__lnb .lnb_sub_menu', function() {
		if ($(window).width() > 1025) {
			var inx = $(this).index(); 
			var p = $(this).parent(); 
			$('.lnb-menu > li').removeClass('over'); 
			p.addClass('over'); 
			//p.addClass('on');
			

        } else {
			return true;
        }
    });
    $(document).on('mouseleave', '#header__lnb .lnb_sub_menu', function() {
        if ($(window).width() > 1025) {
			var inx = $(this).index(); 
			var p = $(this).parent('li'); 
			var has = p.hasClass('over');	// 현재오버중 : 서브메뉴에서
		
			// p.removeClass('over'); 

			if(!has) { 
				//p.removeClass('on'); 
			} 
        } else {
            return true;
        }
    });

});
