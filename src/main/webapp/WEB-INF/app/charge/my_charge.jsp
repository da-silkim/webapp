<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	$(document).ready(function () {

	});
	
	
</script>
	<div id="wrap" class="flex-center">
        <div id="head" class="flex-center relative">
            <a onclick="location.href='/app/main'" class="prev-location absolute"><img src="/resources/image/leftarrow-icon-all.png" alt=""></a>
            <h2 class="title">마이충전소</h2>
        </div>
        <div id="body" class="flex-body full-page">
            <div id="myChargerList" class="container-body">
                <div class="my-charger-item">
                    <div class="my-charger-status mb10">충전가능</div>
                    <div class="my-charger-title mb10">삼방주유소1</div>
                    <div class="my-charger-address">2.43km<span class="sub-plat"></span>서울 서초구 사평대로 364 (서초동, LG 삼방주유소)</div>
                    <div class="my-charger-bookmark" data-checked="true"><img src="/resources/image/ic_star2.png"></div>
                </div>
                <div class="my-charger-item">
                    <div class="my-charger-status mb10">충전가능</div>
                    <div class="my-charger-title mb10">삼방주유소2</div>
                    <div class="my-charger-address">3.43km<span class="sub-plat"></span>서울 서초구 사평대로 364 (서초동, LG 삼방주유소)</div>
                    <div class="my-charger-bookmark" data-checked="true"><img src="../src/image/ic_star2.png"></div>
                </div>
                <div class="my-charger-item">
                    <div class="my-charger-status mb10">충전가능</div>
                    <div class="my-charger-title mb10">삼방주유소3</div>
                    <div class="my-charger-address">4.43km<span class="sub-plat"></span>서울 서초구 사평대로 364 (서초동, LG 삼방주유소)</div>
                    <div class="my-charger-bookmark" data-checked="true"><img src="../src/image/ic_star2.png"></div>
                </div>
                <div class="my-charger-item cant-use">
                    <div class="my-charger-status mb10 disable">충전불가</div>
                    <div class="my-charger-title mb10">삼방주유소4</div>
                    <div class="my-charger-address">5.43km<span class="sub-plat"></span>서울 서초구 사평대로 364 (서초동, LG 삼방주유소)</div>
                    <div class="my-charger-bookmark" data-checked="true"><img src="../src/image/ic_star2.png"></div>
                </div>
                <div class="my-charger-item">
                    <div class="my-charger-status mb10">충전가능</div>
                    <div class="my-charger-title mb10">삼방주유소5</div>
                    <div class="my-charger-address">6.43km<span class="sub-plat"></span>서울 서초구 사평대로 364 (서초동, LG 삼방주유소)</div>
                    <div class="my-charger-bookmark" data-checked="true"><img src="../src/image/ic_star2.png"></div>
                </div>
                <div class="my-charger-item cant-use">
                    <div class="my-charger-status mb10 disable">충전불가</div>
                    <div class="my-charger-title mb10">삼방주유소6</div>
                    <div class="my-charger-address">7.43km<span class="sub-plat"></span>서울 서초구 사평대로 364 (서초동, LG 삼방주유소)</div>
                    <div class="my-charger-bookmark" data-checked="true"><img src="../src/image/ic_star2.png"></div>
                </div>
                <div class="my-charger-item">
                    <div class="my-charger-status mb10">충전가능</div>
                    <div class="my-charger-title mb10">삼방주유소7</div>
                    <div class="my-charger-address">8.43km<span class="sub-plat"></span>서울 서초구 사평대로 364 (서초동, LG 삼방주유소)</div>
                    <div class="my-charger-bookmark" data-checked="true"><img src="../src/image/ic_star2.png"></div>
                </div>
            </div>
        </div>
    </div>