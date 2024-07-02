// 앱과 브릿지 연결을 통한 함수 정의
var bridgeUtil = {};

// 앱이 실행중인지 판단하는 함수
bridgeUtil.getAppRunning = function(){
    let agt = navigator.userAgent.toLowerCase();
    if( agt.indexOf("app_running_ios") > 0 || agt.indexOf("app_running_aos") > 0 ){
        return true;

    } else {
        return false;
    }
}

//  front -> app 으로 qr코드 화면을 요청하는 브릿지 요청
bridgeUtil.fn_qrScanProc = function (){
    let params = {
        pluginId : "getQRCode"
        , params : ""
        , callBack : "window.setQRCode"
    };
    bridgeUtil.postMessage(params);
}


bridgeUtil.doAutoLogin = function(params){
    console.log("hello~~~!~!~!");
    console.log(JSON.stringify(params));
    bridgeUtil.postMessage(params);
}

// 브릿지에 자동로그인을 요청
// 앱에서 해당요청을 받은 후에 기기메모리에 저장되어 있는 정보를 토대로
// callback 함수인 window.setAutoLogin을 실행한다.
bridgeUtil.setAutoLogin = function(){
    //자동로그인 네이티브 브릿지 요청

    console.log("!!setAutoLogin!")
        let params = {
            pluginId : "setAutoLogin"
            , params : ""
            , callBack : "window.setAutoLogin"
        };
    bridgeUtil.postMessage(params);

}

// 브릿지에 자동로그인 초기화를 요청하는 함수
// 해당 함수를 실행하면 기기 내의 자동로그인 정보가 지워진다.
bridgeUtil.setAutologinClear = function(){
    if(bridgeUtil.getAppRunning()){
        let param = {
            "user_id" : "",
            "user_pw" : "",
            "remember-me" : "off"
        };

        let params = {
            pluginId : "autoLogin"
            , params : param
            , callBack : "window.setAutoLogin"
        };
        bridgeUtil.postMessage(params);
    }
}
// 브릿지에 자동로그인 패스워드 정보를 변경하는 함수
// 해당 함수를 실행하게 되면, remember-me가 on일 시에 기기내의 패스워드 정보가 변경된다.
bridgeUtil.setAutologinModifyPassword = function (password){
    if(bridgeUtil.getAppRunning()){
        let param = {
            "user_pw": password
        };

        let params = {
            pluginId : "autoLoginModifyPassword"
            ,params : param
            ,callBack : ""
        };
        bridgeUtil.postMessage(params);
    }
}



// callback 함수이자 전역함수
// 해당 함수를 실행하면, 로그인data를 request하여 로그인 세션을 얻게 된다.
window.setAutoLogin = (flag, pluginId, param) => {
    if(flag){
        const userInfo = JSON.parse(decodeURI(param));
        let id = userInfo.user_id;
        let pw = userInfo.user_pw;
        let rememberMe = userInfo.remember_me;
        let loginInfo = {};
        //console.log(id);
        //console.log(pw);
        if(rememberMe === 'on' && id && pw){
            loginInfo.userId = id;
            loginInfo.userPassword = pw;
        }

        $.ajax({
            type: 'POST',
            url: '/app/j_spring_security_check',
            data: loginInfo,
            dataType: 'json',
            async: false,
            success: function (res) {
                //console.log(res);
                switch (res.result){
                    case 'success':{
                        console.log("loginSuccess")
                        location.href = res.return_url;
                        break;
                    }
                    default:
                    {
                        console.log("loginFailed")
                    }
                        break;
                }
            },
            error: function (res){
            }
        });
    }
}

// callBack 함수이자 전역함수.
// qr코드가 app에서 정상적으로 decoding이 되면 해당 함수를 호출한다.
// href에 입력된 경로로 전달..?
window.setQRCode = (flag, pluginId, param) => {

    if( flag ){
        let reqParam = JSON.parse(decodeURI(param));
        let qrCode = reqParam.qrCode;
        let qrCodeArr = qrCode.substr(1).split("/");
        if( !isNull(qrCodeArr[0]) && !isNull(qrCodeArr[1]) ){
	        //location.href = "/app/find/cs_detail?csId=" + qrCodeArr[0];
	        location.href = "/app/charge/charge_remote?cpId=" + qrCodeArr[0] +"&connectorId="+qrCodeArr[1];
			
		} else {
			location.href = "/app/charge/charge_remote?cpId=" + qrCodeArr[0];
		}


    }
};


bridgeUtil.getCurrentLocation = function(){
    let params = {
        pluginId : "getLocation"
        , params : ""
        , callBack : "window.setLocation"
    };
    bridgeUtil.postMessage(params);
}

window.setLocation = (flag, pluginId, param) => {

    let drivenInitLat;
    let drivenInitlng;
    let drivenIsLatLngInit;
    if( flag ){
        let locationJson = JSON.parse(decodeURI(param));
        const init_lat = locationJson.lat;
        const init_lng = locationJson.lng;
        fn_setCookie("init_lat", init_lat,1);
        fn_setCookie("init_lng", init_lng,1);
        fn_setCookie("isLatLngInit",true,1);

        drivenInitLat = fn_getCookie("init_lat");
        drivenInitlng = fn_getCookie("init_lng");
        drivenIsLatLngInit = fn_getCookie("isLatLngInit");

		
	}

    console.log( ` baked cookie = ${drivenInitLat}, ${drivenInitlng}, ${drivenIsLatLngInit}`)
};

// app bridge function
// flutterInAppWebViewPlatformReady 상태를 확인한 후에, 내부 app에 접근하여 함수 호출한다.
// return을 받게 하여, 추후에 필요한 경우 사용
bridgeUtil.postMessage = (params) => {

    console.log("postMessage");

    // var isFlutterInAppWebViewReady = false;
    // console.log(`isFlutterInAppWebViewReady? : ${isFlutterInAppWebViewReady}`)
    // window.addEventListener("flutterInAppWebViewPlatformReady", function(event) {
    //     isFlutterInAppWebViewReady = true;
    // });
    //
    // if(!isFlutterInAppWebViewReady) {
    //
    //     console.log('here 1');
    //     window.addEventListener("flutterInAppWebViewPlatformReady", function (event) {
    if (window.flutter_inappwebview.callHandler) {
        console.log('here 2');
        window.flutter_inappwebview.callHandler('blueNetworks', JSON.stringify(params))
            .then(function (result) {
                return JSON.stringify(result);
            });
    } else {
        console.log('here 3');
        window.flutter_inappwebview._callHandler('blueNetworks', JSON.stringify(params))
            .then(function (result) {
                return JSON.stringify(result);
            });
    }
    // isFlutterInAppWebViewReady=true;
    // });
    // }else{
    //     if (window.flutter_inappwebview.callHandler) {
    //         console.log('here 4');
    //         window.flutter_inappwebview.callHandler('greenPower', JSON.stringify(params))
    //             .then(function (result) {
    //                 return JSON.stringify(result);
    //             });
    //     } else {
    //         console.log('here 5');
    //         window.flutter_inappwebview._callHandler('greenPower', JSON.stringify(params))
    //             .then(function (result) {
    //                 return JSON.stringify(result);
    //             });
    //     }
    // }
}

