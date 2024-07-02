var ajax = {
    stackLoading : 0,
    post : function(url, param, fnCallback) {
        return $.post(url, param, fnCallback);
    },
    get : function(url, param, fnCallback) {
        return $.ajax({
            url : url,
            data : param,
            type : 'get',
            contentType : "application/html; charset=UTF-8",
            success : function(data) {
                fnCallback(data);
            }
        });
    },
    json : function(url, param, fnCallback, doAsync) {
    	if (!sessionCheck()) {
    		return false;
    	}

        if( $.type(param) == 'object' ){
            param = JSON.stringify(param);
        }

        if (doAsync) {
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
                if( data && data.exceptionMsg ){
                    alert( data.exceptionMsg );
                } else {
                    try {
                        fnCallback(data);
                    } catch(e) {
                        alert('Script error has occurred.'); //스크립트 오류가 발생하였습니다
                        //console.error(e);
                    }
                }
            }
        });
    },
    updateDiv : function(url, param, divId, fnCallback, doAsync) {

    	if (!sessionCheck()) {
    		return false;
    	}

        if (doAsync) {
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
                try{
                    if( fnCallback ){
                        fnCallback();
                    }
                }catch(e){
                    alert('Script error has occurred.');
                    //console.error(e);
                }
            }
        });
    },
    api : {
        url : _CONTEXT_PATH + 'api/sendApi.do',
        send : function(apiInfo, apiData, fnCallback, fnErrorCallback, doAsync) {

        	if (!sessionCheck()) {
        		return false;
        	}

            if (doAsync) {
            	setAsync = doAsync;
            } else {
            	setAsync = false;
            }

            return $.ajax({
                url : this.url,
                data : JSON.stringify([ apiInfo, apiData ]),
                method : 'post',
                dataType : 'json',
                async : setAsync,
                contentType : "application/json; charset=UTF-8",
                success : function(data) {
                    if( data.responseCode == 'sessionInvalid'){
                        alert( data.errorMsg );
                        windowClose();
                    }else if (data.responseCode != "200") {
                        // Java 에러
                        alert('An error has occurred. '); // 에러가 발생하였습니다.\n관리자에게 문의하여 주십시오.
                        //console.error(apiInfo.apiId + ' 전송 실패\n(실패사유 : ' + data.responseDesc + ')');
                        if (fnErrorCallback) {
                            try {
                                fnErrorCallback(data);
                            } catch (e) {
                                console.error('api.send - callback function error');
                            }
                        }
                    } else {
                        // API 에러
                        if (data.response.Fault) {
                            // 에러메세지
                            var strFaultMessage = data.response.Fault.faultstring;
                            if (data.response.Fault.detail.errors) {
                                strFaultMessage += "\n";
                                $.each(data.response.Fault.detail.errors.error,
                                        function(n, error) {
                                            strFaultMessage += ' - ' + error.message + '\n';
                                });
                            }
                            alert(strFaultMessage);

                            if(fnErrorCallback) {
                                try {
                                    $('#loading').hide();
                                    fnErrorCallback(data);
                                } catch (e) {
                                    console.error('api.send - callback function error');
                                }
                            }
                        } else {
                            // 정상 실행
                            try {
                                fnCallback(data);
                            } catch(e) {
                                console.error('api.send - callback function error');
                                console.error(e);
                            }
                        }
                    }

                }
            });
        },
        // API 에러 메세지를 무시함.
        // ( 데이터가 없습니다 등등 )
        inquiry : function(apiInfo, apiData, fnCallback, fnErrorCallback, doAsync) {

        	if (!sessionCheck()) {
        		return false;
        	}

            if (doAsync) {
            	setAsync = doAsync;
            } else {
            	setAsync = false;
            }

            return $.ajax({
                url : this.url,
                data : JSON.stringify([ apiInfo, apiData ]),
                method : 'post',
                dataType : 'json',
                async : setAsync,
                contentType : "application/json; charset=UTF-8",
                success : function(data) {
                    if( data.responseCode == 'sessionInvalid') {
                        alert( data.errorMsg );
                        windowClose();
                    } else if (data.responseCode != "200") {
                        // Java 에러
                        alert('에러가 발생하였습니다.\n관리자에게 문의하여 주십시오.');
                        console.error(apiInfo.apiId
                                + ' 전송 실패\n(실패사유 : '
                                + data.responseDesc + ')');
                        if(fnErrorCallback) {
                            try {
                                fnErrorCallback(data);
                            } catch (e) {
                                console.error('api.send - callback function error');
                            }
                        }
                    } else {
                        // 정상 실행
                        try {
                            fnCallback(data);
                        } catch (e) {
                            alert('스크립트 오류가 발생하였습니다.');
                            console.error('api.send - callback function error');
                            console.error(e);
                        }
                    }

                }
            });
        }
    }
}

function sessionCheck() {

    var sessionOk = true;

    $.ajax({
        url : _CONTEXT_PATH + 'sessionCheck.json',
        data : JSON.stringify({}),
        type : 'post',
        dataType : 'json',
        async : false, // false면 순차적 실행
        contentType : "application/json; charset=UTF-8",
        success : function(res) {
        	if (res.successYn != "Y") {
        		top.location.href = _CONTEXT_PATH + 'logout.do';
        		sessionOk = false;
        	}
        }
    });

    return sessionOk;
}

function getType(p) {
    if (Array.isArray(p)) return 'array';
    else if (typeof p == 'string') return 'string';
    else if (p != null && typeof p == 'object') return 'object';
    else return 'other';
}

function jsonToParameter(jsonObj){
    var rtn = '';
    for( var key in jsonObj ){
        rtn += key + '=' + jsonObj[key] + '&';
    }
    if( rtn.length > 0 ){
        rtn = rtn.substring(0, rtn.length-1);
    }
    return rtn;
}
function showLoading(){
    $('#loading').height($(document).height());
    $('#loading').show();
    ajax.stackLoading++;
}

function hideLoading(isForce){
    ajax.stackLoading--;
    if (ajax.stackLoading <= 0 || isForce == true) {
        ajax.stackLoading = 0;
        $('#loading').hide();
    }
}
$.ajaxSetup({
    beforeSend : function(jqXHR, settings) {
        var addtionalParam = 'topMenuId='+_TOP_MENU_ID;
        addtionalParam += '&currentMenuId='+_CURRENT_MENU_ID;
        addtionalParam += '&dt=' + new Date().getTime()
        if(settings.url.indexOf("?") > -1) {
            settings.url = settings.url + '&'+addtionalParam;
        }else{
            settings.url = settings.url + '?' + addtionalParam;
        }
        showLoading();
    },
    complete : function() {
        hideLoading();
    },
    error :function(jqXHR, textStatus, errorThrown) {
        hideLoading(true);
    }

});