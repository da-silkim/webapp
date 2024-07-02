<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>
var ajax = {
    stackLoading : 0,
    post : function(url, param, fnCallback) {
         return $.post(url, param, fnCallback);
    }
    , get : function(url, param, fnCallback) {
            return $.ajax({
                url: url,
                data: param,
                type: "get",
                contentType: "application/html; charset=UTF-8",
                success: function(data) {
                    fnCallback(data);
                }
            });
    }
    , json : function(url, param, fnCallback, doAsync) {
            if( $.type(param) == 'object' ) {
                param = JSON.stringify(param);
            }

            if(doAsync) {
                setAsync = doAsync;
            } else {
                setAsync = false;
            }
            
            return $.ajax({
                url : url,
                data : param,
                type : 'post',
                dataType : 'json',
                async : setAsync, // false면 순차적 실행
                contentType : "application/json; charset=UTF-8",
                success : function(data) {
                    checkSession(data);
                    if( data && data.exceptionMsg ){
                    	alert(data.exceptionMsg , 'error');
                    } else {
                        try {
                            fnCallback(data);
                        } catch(e) {
                        	alert('Script error has occurred.', 'error'); //스크립트 오류가 발생하였습니다
                            //console.error(e);
                        }
                    }
                }
            });
        }
    , updateDiv : function(url, param, divId, fnCallback, doAsync) {

        if(doAsync) {
            setAsync = doAsync;
        } else {
            setAsync = false;
        }
        
        return $.ajax({
            url : url,
            data : param,
            type : 'post',
            async : setAsync,
            //contentType : "application/html; charset=UTF-8",
            success : function(strHtml) {
            	$('#' + divId).html(strHtml);
                try {
                    if( fnCallback ) {
                        fnCallback();
                    }
                } catch(e) {
                	alert('Script error has occurred.', 'error');
                    //console.error(e);
                }
            }
        });
    }
}





</script>