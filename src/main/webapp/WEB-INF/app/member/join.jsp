<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
    var _param = {};
    var duplChk = false;

    $(document).ready(function() {
        init();

        keyEvent();	//숫자만 입력가능
    });

    function init() {
        //alert 초기화
        $("#alertIdForm").css("display","none");
        $("#duplicatedId").css("display","none");
        $("#ableUsedId").css("display","none");
        $("#alertPwCfm").css("display","none");

        $("#chkSameAddr").change(function(){
            if($("#chkSameAddr").is(":checked")){
                if($("#zipcode").val()=="" || $("#address").val()=="" || $("#addressDetail").val()==""){
                    alert("기본주소를 먼저 입력해주세요.");
                    $("#chkSameAddr").attr("checked", false);
                }
                $("#delvZipcode").val($("#zipcode").val());
                $("#delvAddress").val($("#address").val());
                $("#delvAddressDetail").val($("#addressDetail").val());
            }else{
                $("#delvZipcode").val("");
                $("#delvAddress").val("");
                $("#delvAddressDetail").val("");
            }
        });
    }

    function blankReplace( obj ) {
        if( obj.indexOf(" ") > -1  ) {
            obj = obj.replace(/(\s*)/g, "") ;
        }
        $("#carNo").val(obj);
    }


    $(document).on("click", "#_btnZipcode ,#_btnDelvZipcode", function(e) {
        e.preventDefault();
        var id = $(this).attr("id");

        if( id == "_btnZipcode") {
            openZipCode( 'zipcode','address','addressDetail' );
        }
        if( id == "_btnDelvZipcode") {
            openZipCode( 'delvZipcode','delvAddress','delvAddressDetail' );
        }
    });

    $(document).on("keydown", "#userId" , function () {
        $("#alertIdForm").hide();
        $("#duplicatedId").hide();
        $("#ableUsedId").hide();
        duplChk = false;
    });

    $(document).on("blur", "#passwordConfirm" , function () {
        if( $("#password").val() != $("#passwordConfirm").val() ) {
            //alert("비밀번호와 비밀번호확인이 일치하지 않습니다");
            $("#alertPwCfm").css("display","");
            $("#passwordConfirm").val("");
            $("#passwordConfirm").focus();
        }else{
            $("#alertPwCfm").css("display","none");
        }
    });

    function fn_submit(){
        var customerType = $('input[name="customerType"]:checked').val();
        $("#customerType").val(customerType);

        if( !validator() ) return;
        if(duplChk){
            /* makeFormData(); */
            if( confirm("저장 하시겠습니까?") ) {
                var data = $('form[name="frm"]').serialize();
                $.ajax({
                    url: $("#frm").attr("action"),
                    type: 'post',
                    data: $("#frm").serializeArray(),
                    async: false,
                    success : function(res) {
                        if(res.result.message != 'success'){
                        	alert(res.result.content);
                        } else {
                        	alert("고객님의 회원가입이 완료되었습니다. \n로그인페이지로 이동합니다.");
                            location.href = "/app/member/login";
                        }
                    },
                    beforeSend : function(e){
                    },
                    error : function(xhr){
                    	alert("고객님의 회원가입이 완료되었습니다. \n로그인페이지로 이동합니다.");
                        location.href = "/app/member/login";
                    }
                });
            }
        } else{
            alert("아이디 중복확인을 해주세요.");
            return;
        }
    }

    //아이디 중복 조회
    function fn_check_id(){
        if($("#userId").val()==""){
            alert("아이디를 입력하세요.");
            return;
        }
        if($("#userId").val().length<6 || $("#userId").val().length>12){
            alert("아이디 길이는 6~12 자리로 입력하세요");
            return;
        } else{
            $.ajax({
                url: "/app/member/check_id/action",
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify({'userid': $("#userId").val()}),
                async: false,
                success : function(res) {
                    if(res.result.message != 'success'){
                        if (res.result.message == 'idForm') {
                            $("#alertIdForm").css("display","");
                        } else {
                            $("#duplicatedId").css("display","");
                        }

                        $("#check_id").val("N");
                        $("#ableUsedId").css("display","none");
                        $("#userId").val("");
                        $("#userId").focus();
                        duplChk = false;
                    } else {
                        $("#check_id").val("Y");
                        $("#alertIdForm").css("display","none");
                        $("#duplicatedId").css("display","none");
                        $("#ableUsedId").css("display","");
                        duplChk = true;
                    }
                }
            });
        }
    }

    function makeFormData() {
        //TODO
        $("#companyId").val( '2' ) ;
    }

    /*
     * 숫자(2~3)문자(한글자)숫자(4자리)
     */
    function validCarNo(carNo) {
        var carNoLen = carNo.length;

        if(carNoLen==7){
            if(isNumber(carNo.substr(0, 2))==false || isAlphabet(carNo.substr(2, 1))==true || isNumber(carNo.substr(3, 4))==false){
                alert("유효하지 않은 차량번호입니다.");
                $("#carNo").focus();
                return false;
            }else{
                return true;
            }
        }else if(carNoLen==8){
            if(isNumber(carNo.substr(0, 3))==false || isAlphabet(carNo.substr(3, 1))==true || isNumber(carNo.substr(4, 4))==false){
                alert("유효하지 않은 차량번호입니다.");
                $("#carNo").focus();
                return false;
            }else{
                return true;
            }
        }else{
            alert("유효하지 않은 차량번호입니다.");
            $("#carNo").focus();
            return false;
        }
    }

    function validator() {

        //성명
        if( $.trim( $("#customerName").val() ).length == 0 ) {
            alert("성명을 입력해주세요");
            $("#customerName").focus();
            return false;
        }
        //아이디
        if( $.trim( $("#userId").val() ).length == 0 ) {
            alert("아이디를 입력해주세요");
            $("#userId").focus();
            return false;
        }
        //비밀번호
        if( $.trim( $("#password").val() ).length == 0 ) {
            alert("비밀번호를 입력해주세요");
            $("#password").focus();
            return false;
        }

        if(chkPwd($('#password').val())==false){
            $("#password").focus();
            return false;
        }

        //비밀번호확인
        if( $.trim( $("#passwordConfirm").val() ).length == 0 ) {
            alert("비밀번호확인을 입력해주세요");
            $("#passwordConfirm").focus();
            return false;
        }
        //생년월일
        if( $.trim( $("#brithDay").val() ).length == 0 ) {
            alert("생년월일을 입력해주세요");
            $("#brithDay").focus();
            return false;
        }
        //휴대폰 번호
        if( $.trim( $("#mobile").val() ).length == 0 ) {
            alert("휴대폰번호를 입력해주세요");
            $("#mobile").focus();
            return false;
        }
        if( $.trim( $("#email").val() ).length == 0 ) {
            alert("이메일을 입력해주세요");
            $("#mobile").focus();
            return false;
        }

        //생년월일
        if( $.trim( $("#brithDay").val() ).length != 8 ) {
            alert("생년월일을 확인해주세요");
            $("#brithDay").focus();
            return false;
        }

        //생년월일 만20세 체크
        if( $.trim( $("#brithDay").val() ).length > 4 ) {
            var today = new Date();
            var nowY = today.getFullYear();
            var myY = $("#brithDay").val().substring(0,4);
            var age=nowY-myY;
            if( age < 18 ) {
                alert("생년월일을 확인해주세요");
                $("#brithDay").focus();
                return false;
            }
        }

        //주소 우편번호
        if( $.trim( $("#zipcode").val() ).length == 0 ) {
            alert("우편번호를 입력해주세요");
            $("#zipcode").focus();
            return false;
        }

        if( $.trim( $("#address").val() ).length == 0 ) {
            alert("주소를 입력해주세요");
            $("#address").focus();
            return false;
        }

        if( $.trim( $("#addressDetail").val() ).length == 0 ) {
            alert("상세주소를 입력해주세요");
            $("#addressDetail").focus();
            return false;
        }

        //비밀번호 확인
        if( $("#password").val() != $("#passwordConfirm").val() ) {
            //alert("비밀번호와 비밀번호확인이 일치하지 않습니다");
            $("#alertPwCfm").css("display","");
            return false;
        }else{
            $("#alertPwCfm").css("display","none");
        }
        //차량번호
        if( $.trim( $("#carNo").val() ).length == 0 ) {
            alert("차량번호를 입력해주세요");
            $("#carNo").focus();
            return false;
        }

        return true;
    }

    function chkPwd(str){
        var pw = str;
        var num = pw.search(/[0-9]/g);
        var eng = pw.search(/[a-z]/ig);
        var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

        if(pw.length < 8 || pw.length > 15){
            alert("비밀번호는 8자리 ~ 15자리 이내로 입력해주세요.");
            return false;
        }

        if(pw.search(/₩s/) != -1){
            alert("비밀번호는 공백없이 입력해주세요.");
            return false;
        }

        if(num < 0 || eng < 0 || spe < 0 ){
            alert("영문,숫자, 특수문자를 혼합하여 입력해주세요.");
            return false;
        }
        return true;
    }

    // function setData() {
    //     $("#customerName").val("테스트트트");
    //     $("#userId").val("testtttt");
    //     $("#password").val("1q2w3e4r1!");
    //     $("#passwordConfirm").val("1q2w3e4r1!");
    //     $("#brithDay").val("19940203");
    //     $("#mobile").val("01056658454");
    //     $("#email").val("testttttt@naver.com");
    //     $("#zipcode").val("05411");
    //     $("#address").val("서울");
    //     $("#addressDetail").val("123");
    //     $("#carNo").val("11너1234");
    // }
</script>
<div class="contents-wrap bgGray cont-padding-typeA join-wrap">
    <div id="__layer__" style="display: none; position: fixed; overflow: hidden; z-index: 1; -webkit-overflow-scrolling: touch;">
        <img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnCloseLayer"
             style="cursor: pointer; position: absolute; right: -3px; top: -3px; z-index: 1"
             onclick="closeDaumPostcode()" alt="닫기 버튼" />
    </div>
    <form action="/app/member/join/action" method="post" id="frm" name="frm">
        <fieldset>
            <legend>기본정보</legend>
            <div class="table-wrap">
                <table class="basic_table">
                    <caption>기본정보</caption>
                    <colgroup>
                        <col style="width: 35%">
                        <col style="width: 65%">
                    </colgroup>
                    <tbody>
                    <tr>
                        <th>성명
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <input class="input-one input-h40" type="text" name="customerName" id="customerName">
                            <%--                            <button type="button" class="btn-h32 btn-color-sub1" onclick="setData()">데이터</button>--%>
                        </td>
                    </tr>
                    <tr>
                        <th>아이디
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <p class="btn-inputInclude-wrap">
                                <input class="input-one input-h40" type="text" name="userId" id="userId" maxlength="20">
                                <button type="button" class="btn-h32 btn-color-sub1" onclick="fn_check_id()">중복확인</button>
                            </p>
                            <p id="alertIdForm" class="txt-alert">아이디는 영문+숫자 5~12자리로 입력하시기 바랍니다.</p>
                            <p id="duplicatedId" class="txt-alert">이미 사용중인 아이디 입니다.</p>
                            <p id="ableUsedId" class="txt-alert">사용가능한 아이디 입니다.</p>
                        </td>
                    </tr>
                    <tr>
                        <th>비밀번호
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <input class="input-one input-h40" type="password" name="password" id="password" placeholder="영문, 숫자 및 특수문자 포함 8자리 이상">
                        </td>
                    </tr>
                    <tr>
                        <th>비밀번호 확인
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <input class="input-one input-h40" type="password" name="passwordConfirm" id="passwordConfirm">
                            <p id="alertPwCfm" class="txt-alert">비밀번호가 일치하지 않습니다.</p>
                        </td>
                    </tr>
                    <tr class="birthTr">
                        <th>생년월일
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <input class="input-one input-h40" type="text" name="brithDay"
                                   id="brithDay" placeholder="YYYYMMDD" numberOnly maxlength="8">
                        </td>
                    </tr>
                    <tr>
                        <th>휴대폰 번호
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <input class="input-one input-h40" type="text" name="mobile"
                                   id="mobile" maxlength="11" placeholder="숫자만 입력">
                        </td>
                    </tr>
                    <tr>
                        <th>이메일
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <input class="input-one input-h40" type="text" name="email"
                                   id="email" maxlength="100" placeholder="@이후까지 정확하게 입력">
                            <!-- p class="txt-alert">이미 사용중인 이메일 입니다.</p-->
                        </td>
                    </tr>
                    <tr class="addressTr">
                        <th>주소
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <p class="btn-inputInclude-wrap mb10">
                                <input class="input-one input-h40 input-disable" type="text" name="zipcode" id="zipcode" readonly>
                                <button type="button" class="btn-h32 btn-color-sub1" id="_btnZipcode">우편번호</button>
                            </p>
                            <p class="mb10">
                                <input class="input-one input-h40 input-disable" type="text" name="address" id="address" readonly>
                            </p>
                            <p>
                                <input class="input-one input-h40" type="text" name="addressDetail" id="addressDetail">
                            </p>
                        </td>
                    </tr>
                    <tr>
                        <th>차량 번호
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <input class="input-one input-h40" type="text" name="carNo" id="carNo" maxlength="8" onKeyup="blankReplace(this.value);" placeholder="예: 12가 1234">
                        </td>
                    </tr>
                    <tr>
                        <th>개인 / 법인
                            <span class="essential-icon">
                                <img src="/resources/images/ico_essential.png" alt="필수 입력사항">
                            </span>
                        </th>
                        <td>
                            <input type="radio" id="personal" name="customerType" value="P" required="required" checked="on">
                            <label class="mr18" for="personal">개인</label>
                            <input type="radio" id="corporation" name="customerType" value="C" required="required">
                            <label for="corporation">법인</label>
                            <input type="hidden" id="customerType">
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <!-- 회원가입완료 버튼 :s -->
            <div class="btn-wrap bottomFixed">
                <button type="submit" class="btn-one btn-h60 btn-gradient btn-rectangle" onclick="fn_submit(); return false;">회원 가입</button>
            </div>
            <!-- 회원가입완료 버튼 :e -->
        </fieldset>
    </form>
</div>