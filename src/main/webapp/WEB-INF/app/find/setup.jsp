<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
    const userId = "${userid}";
    const customerName = "${username}";

$(document).ready(function () {
	$("#customerName").val(customerName);
	$("#userId").val(userId);

    setData();
});

function setData() {
    $.ajax({
        url: "/app/find/setup_data",
        type: 'get',
        data: {userId: userId},
        dataType: "json",
        success: function (res) {
            $("#carNo").val(res.carNo);

            $("table tbody td").find("input").each( function( idx ) {
                var id = $(this).attr("id");
                var tagName = $(this).prop("tagName").toUpperCase();
                if( tagName == "INPUT"  ) {
                    var flag = false;
                    if( res[id] == "Y" ) {
                        flag = true;
                    }
                    $("input:checkbox[id='"+id+"']").prop("checked", flag );
                }
            });
        }
    });
}

    $(document).on("click", "#btnUpdate", function(e) {
        e.preventDefault();
        var id = $(this).attr("id");

        if( id == "btnUpdate") {
            if( confirm("저장 하시겠습니까?") ) {
                if($("input:checkbox[id='notiYn2']").is(":checked") == true){
                    $("#alramYn2").val("Y");
                }else{
                    $("#alramYn2").val("N");
                }
                if($("input:checkbox[id='notiYn3']").is(":checked") == true){
                    $("#alramYn3").val("Y");
                }else{
                    $("#alramYn3").val("N");
                }
                if($("input:checkbox[id='notiYn4']").is(":checked") == true){
                    $("#alramYn4").val("Y");
                }else{
                    $("#alramYn4").val("N");
                }

                var data = $('form[name="frm"]').serialize();

                $.ajax({
                    url: "/app/find/update_noti_yn",
                    type: 'post',
                    data: data,
                    dataType: "json",
                    success: function (res) {
                        if (res.result.message == "success") {
                            alert("저장되었습니다");
                        } else {
                            alert(res.result.content);
                        }
                    }
                });
            }
        }

    });
</script>
<div class="contents-wrap bgGray cont-padding-typeA setup-wrap">
    <form action="" method="post" name="frm">
        <input type="hidden" name="alramYn2" id="alramYn2">
        <input type="hidden" name="alramYn3" id="alramYn3">
        <input type="hidden" name="alramYn4" id="alramYn4">
      <!-- 회원정보 :s -->
        <fieldset>
            <legend>회원정보</legend>
            <div class="table-wrap">
              <table class="basic_table">
                <caption>회원정보</caption>
                <colgroup>
                    <col style="width: 35%">
                    <col style="width: 65%">
                </colgroup>
                <tbody>
                  <tr>
                    <th>회원 이름</th>
                    <td >
                      <input value="" class="input-one input-h40 input-disable" type="text" name="customerName" id="customerName" readonly>
                    </td>
                  </tr>
                  <tr>
                    <th>아이디</th>
                    <td >
                      <input value="" class="input-one input-h40 input-disable" type="text" name="userId" id="userId" readonly>
                    </td>
                  </tr>
                  <tr>
                    <th>차량 번호</th>
                    <td >
                      <input value="" class="input-one input-h40 input-disable" type="text" name="carNo" id="carNo" readonly>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
        </fieldset>
            <!-- 회원정보 :e -->

            <!-- 알림설정 :s -->
        <fieldset>
            <legend>알림설정</legend>
            <div class="table-wrap tablespan">
              <table class="basic_table">
              <caption>알림설정</caption>
              <colgroup>
                <col style="width: 35%">
                <col style="width: 65%">
              </colgroup>
              <tbody>
                <tr>
                  <th rowspan="4">알림 설정</th>
                  <td>
                    <input class="btn-toggle" type="checkbox" name="notiYn2" id="notiYn2" />
                    <label for="notiYn2"><span>충전 종료</span></label>
                  </td>
                </tr>
                <tr>
                  <td>
                    <input class="btn-toggle" type="checkbox" name="notiYn3" id="notiYn3" />
                    <label for="notiYn3"><span>충전 월정산</span></label>
                  </td>
                </tr>
                <tr>
                  <td>
                    <input class="btn-toggle" type="checkbox" name="notiYn4" id="notiYn4" />
                    <label for="notiYn4"><span>1:1 문의</span></label>
                  </td>
                </tr>
                </tbody>
            </table>
            </div>
        </fieldset>
        <!-- 알림설정 :e -->

        <!-- 설정 저장 버튼 :s -->
        <div class="btn-wrap">
            <button type="submit" class="btn-one btn-h60 btn-gradient btn-rectangle" id="btnUpdate">설정 저장</button>
        </div>
        <!-- 설정 저장 버튼 :e -->
    </form>
</div>
