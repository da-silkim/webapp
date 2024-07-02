function MyAjax() {
    this.sendForm = function(formId, callback) {
        $.ajax({
            async    : false        // true, false
           ,type     : "POST"       // POST, GET
           ,url      : $("#" + formId).attr("action")
           ,dataType : "json"       // 전송받을 데이터의 타입[xml, html, script, json 등 지정 가능, 미지정시 자동 판단]
         //,timeout  : 30000        // 제한시간 지정
           ,cache    : false        // true, false
           ,data     : $("#" + formId).serialize() // 서버에 보낼 파라메터 [form 에 serialize() 실행시 a=b&c=d 형태로 생성되며 한글은 UTF-8 방식으로 인코딩, {a:b, c:d} json 형식 입력 가능]
         //,contentType : "application/x-www-form-urlencoded; charset=UTF-8"
           ,success : function(data) {
               if($.type(callback) == "function") {
                   callback(data);
               }
           }
           ,error: function(request, status, error){      // 통신 에러 발생시 처리
             //alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
               alert("시스템 오류가 발생하였습니다.\n시스템 관리자에게 문의하세요.");
           }
           ,beforeSend: function() {        // 통신을 시작할때 처리
               /* loading Image 를 이곳에 설정해야함.
               if($("#loadingImg").length <= 0){
                   var loadingImg = $("<div id=\"loadingImg\"><img src=\"/common/images/loading.gif\" /></div>");
                   
                   loadingImg.appendTo("body");
               }
               
               $("#loadingImg").show().css({
                   position: "absolute"
                  ,top: $(document).scrollTop() + ($(window).height() ) / 2.6 + 'px'
                  ,left: ($(window).width()) / 2 + 'px'
               });
               */
           }
           ,complete  : function() {        // 통신이 완료된 후 처리
               // $("#loadingImg").hide();
           }
        });
    };
    
    this.send = function(url, param, callback) {
		$.ajax({
            async    : false        // true, false
           ,type     : "GET"       // POST, GET
           ,url      : url
           ,dataType : "json"       // 전송받을 데이터의 타입[xml, html, script, json 등 지정 가능, 미지정시 자동 판단]
         //,timeout  : 30000        // 제한시간 지정
           ,cache    : false        // true, false
           ,data     : param        // 서버에 보낼 파라메터 [form 에 serialize() 실행시 a=b&c=d 형태로 생성되며 한글은 UTF-8 방식으로 인코딩, {a:b, c:d} json 형식 입력 가능]
         //,contentType : "application/x-www-form-urlencoded; charset=UTF-8"
           ,success : function(data) {
				if($.type(callback) == "function") {
                   callback(data);
               }
           }
           ,error: function(request, status, error){      // 통신 에러 발생시 처리
             //alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
               alert("시스템 오류가 발생하였습니다.\n시스템 관리자에게 문의하세요.");
           }
           ,beforeSend: function() {        // 통신을 시작할때 처리
        	   showLoadingBar();
           }
           ,complete  : function() {        // 통신이 완료된 후 처리
        	   $('#mask, #loadingImg').hide(); 
        	   $('#mask, #loadingImg').remove();
           }
        });
    };
    
    this.sendFileForm = function(formId, callback) {
        $.ajax({
            async    : false        // true, false
           ,type     : "POST"       // POST, GET
           ,url      : $("#" + formId).attr("action")
           ,dataType : "json"       // 전송받을 데이터의 타입[xml, html, script, json 등 지정 가능, 미지정시 자동 판단]
         //,timeout  : 30000        // 제한시간 지정
         //,cache    : false        // true, false
           ,data     : new FormData($("#" + formId)[0])
           ,processData : false
           ,contentType : false
           ,success : function(data) {
               if($.type(callback) == "function") {
                   callback(data);
               }
           }
           ,error: function(request, status, error){      // 통신 에러 발생시 처리
             //alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
               alert("시스템 오류가 발생하였습니다.\n시스템 관리자에게 문의하세요.");
           }
           ,beforeSend: function() {        // 통신을 시작할때 처리
               /* loading Image 를 이곳에 설정해야함.
               if($("#loadingImg").length <= 0){
                   var loadingImg = $("<div id=\"loadingImg\"><img src=\"/common/images/loading.gif\" /></div>");
                   
                   loadingImg.appendTo("body");
               }
               
               $("#loadingImg").show().css({
                   position: "absolute"
                  ,top: $(document).scrollTop() + ($(window).height() ) / 2.6 + 'px'
                  ,left: ($(window).width()) / 2 + 'px'
               });
               */
           }
           ,complete  : function() {        // 통신이 완료된 후 처리
               // $("#loadingImg").hide();
           }
        });
    };
    
    this.sendFile1 = function(url, formId, callback) {
        $.ajax({
            async    : false        // true, false
           ,type     : "POST"       // POST, GET
           ,url      : url
           ,dataType : "json"       // 전송받을 데이터의 타입[xml, html, script, json 등 지정 가능, 미지정시 자동 판단]
         //,timeout  : 30000        // 제한시간 지정
         //,cache    : false        // true, false
           ,data     : new FormData($("#" + formId)[0])
           ,processData : false
           ,contentType : false
           ,success : function(data) {
               if($.type(callback) == "function") {
                   callback(data);
               }
           }
           ,error: function(request, status, error){      // 통신 에러 발생시 처리
             //alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
               alert("시스템 오류가 발생하였습니다.\n시스템 관리자에게 문의하세요.");
           }
           ,beforeSend: function() {        // 통신을 시작할때 처리
               /* loading Image 를 이곳에 설정해야함.
               if($("#loadingImg").length <= 0){
                   var loadingImg = $("<div id=\"loadingImg\"><img src=\"/common/images/loading.gif\" /></div>");
                   
                   loadingImg.appendTo("body");
               }
               
               $("#loadingImg").show().css({
                   position: "absolute"
                  ,top: $(document).scrollTop() + ($(window).height() ) / 2.6 + 'px'
                  ,left: ($(window).width()) / 2 + 'px'
               });
               */
           }
           ,complete  : function() {        // 통신이 완료된 후 처리
               // $("#loadingImg").hide();
           }
        });
    };
    
    this.sendFile = function(url, param, callback) {
		$.ajax({
			type : 'POST',
			url : url, // 바꾸기전 -> '/file/upload.json'
			data : param, 	//필수 //JSON.stringify(param)
			enctype: 'multipart/form-data', // 필수 //'multipart/form-data'
			dataType : "json",
			processData : false, //processData : false => post방식 (필수), processData : ture => get방식 
			contentType : false, //contentType : false => multipart/form-data (필수),
								 //contentType : ture => application/x-www-form-urlencoded
			async    : false,    //true -> 동기, false -> 비동기
			success : function(data) {
               if($.type(callback) == "function") {
                   callback(data);
               }
			},
			error : function(error) {
				
			}
		});
    };
}