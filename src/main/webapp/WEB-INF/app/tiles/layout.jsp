<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<!DOCTYPE html>
<html lang="ko">
<tiles:insertAttribute name="header"/>
<div id="m-wrap">
    <tiles:insertAttribute name="top"/>
    <span id="mainCheck" id="mainCheck" style="display:none;"></span>
    <input id="serviceCompany" id="serviceCompany" type="hidden"  />
    <div class="m-wrap">
        <tiles:insertAttribute name="content"/>
        <div class="mask"></div>
        <!-- popup :e -->
    </div>
    <footer id="mobileTemplateFooter">
        <tiles:insertAttribute name="navi"/>
        <tiles:insertAttribute name="footer"/>
    </footer>
</div>
</body>
</html>
