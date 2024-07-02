function popzone(param,btn,obj,auto,f,s,p,h){
	var param = $(param);
	var btn = param.find(btn);
	var obj = param.find(obj);

	var stop = btn.find("a[class=stop]");
	var play = btn.find("a[class=play]");

	var returnNodes; // 버튼 이벤트를 위해 반복 명령 받아두기
	var elem = 0;
	var fade = f;
	var speed = s;
	var data = "";

	// setup
	obj.hide().eq(0).show();

	//페이징
	if(p.use==true){
		var target = param.find(p.path);
		target.html("");

		if(p.type == null){
			$.each(obj,function(e){
				target.append('<a href="#">'+(e+1)+'</a>');
			});
			var pbtn = target.find("a");

			pbtn.not(elem).removeClass("ov").eq(elem).addClass("ov");
			pbtn.bind("click",function(event){
				clearInterval(returnNodes);
				var t = $(this);
				elem = t.index();
				pbtn.not(elem).removeClass("ov").eq(elem).addClass("ov");
				obj.not(elem).stop(false,true).fadeOut(f/2).eq(elem).stop(false,true).fadeIn(f/2);
				stop.hide();
				play.show();
				event.preventDefault();
			});
		}

		if(p.type == 1){
			target.html("<em>"+(elem+1)+"</em>/"+obj.size());
		}
	}

	function init(n){
		if(data == "prev"){
			if(elem != 0) elem--; else elem = obj.length-1;	 
		}else{
			if(elem<obj.length-1) elem++; else elem = 0;
		}		

		if(p.use==true){
			if(p.type == null) pbtn.not(elem).removeClass("ov").eq(elem).addClass("ov");
			if(p.type == 1) target.children().text(elem+1);
		}

		obj.not(elem).stop(true,true).fadeOut(n).eq(elem).stop(true,true).fadeIn(n);
	}

	function rotate(){ returnNodes = setInterval(function(){ init(f); },speed); } // 초단위 반복

	if(h==true) play.hide();
	if(obj.size() <= 1 ) return false; // 팝업 갯수가 하나면 실행하지않습니다.
	if(auto != false) rotate();
	
	// 포커스 들어오면 멈춤
	obj.children().bind("focusin",function(){
		clearInterval(returnNodes);
	});

	// 멈춤
	btn.find("a[class=stop]").bind("click",function(event){
		clearInterval(returnNodes);
		if(h==true){
			stop.hide();
			play.show();
		}
		event.preventDefault();
	});
	
	// 시작
	btn.find("a[class=play]").bind("click",function(event){
		clearInterval(returnNodes);
		if(h==true){
			play.hide();
			stop.show();
		}
		rotate();
		event.preventDefault();
	});
	
	// 이전
	btn.find("a[rel=prev]").bind("click",function(event){
		clearInterval(returnNodes);
		data = "prev";
		init(f/2);
		if(h==true){
			stop.hide();
			play.show();
		}
		event.preventDefault();
	});
	
	// 다음
	btn.find("a[rel=next]").bind("click",function(event){
		clearInterval(returnNodes);
		data = "next";
		init(f/2);
		if(h==true){
			stop.hide();
			play.show();
		}
		event.preventDefault();
	});
}