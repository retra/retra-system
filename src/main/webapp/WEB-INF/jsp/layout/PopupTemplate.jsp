<?xml version="1.0" encoding="utf-8"?><%--
--%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="../Includes.jsp"%>

<!-- html code -->
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<!-- head -->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Content-language" content="cs" />
	<meta http-equiv="Cache-control" content="max-age=0" />
	<meta http-equiv="Cache-control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache"/>
	<meta http-equiv="Expires" content="0"/>
	<meta http-equiv="Expires" content="Tue, 01 Jan 1980 1:00:00 GMT"/>

	<meta name="author" content="<fmt:message key="header.author"/>" />
	<meta name="descriptions" content="<fmt:message key="header.description"/>" />
	<meta name="keywords" content="<fmt:message key="header.keywords"/>" />
	<meta name="generator" content="Retra version <fmt:message key="about.version.major" />.<fmt:message key="about.version.minor" />.<fmt:message key="about.version.build" />" />
	
	<title><fmt:message key="header.title"/> - <fmt:message key="${title}"/></title>
	<link rel="stylesheet" type="text/css" media="screen" href="${cssRoot}/screen.css" />
	<link rel="stylesheet" type="text/css" media="print" href="${commonCssRoot}/print.css" />
	<!--[if lte IE 6]>
		<link rel="stylesheet" type="text/css" media="screen" href="${cssRoot}/screenMsie.css"/>
		<link rel="stylesheet" type="text/css" media="print" href="${commonCssRoot}/printMsie.css"/>
	<![endif]-->
	<!--[if gte IE 7]>
		<link rel="stylesheet" type="text/css" media="screen" href="${cssRoot}/screenMsie7.css"/>
		<link rel="stylesheet" type="text/css" media="print" href="${commonCssRoot}/printMsie7.css"/>
	<![endif]-->
	<link rel="shortcut icon" type="image/ico" href="${skinRoot}/favicon.ico" />
	<link rel="icon" type="image/ico" href="${skinRoot}/favicon.ico" />

	<script src="${jsRoot}/mira.js" type="text/javascript"></script>
</head>

<!-- body -->
<body>
<!-- 
<div id="logoDiv"></div>	
-->
<div id="mainDiv">
<h1><fmt:message key="header.title"/> - <fmt:message key="${title}"/></h1>

<%--
<div id="headerDiv">
<img id="miraLogoInHeader" src="${imgRoot}/retra-logo-small.gif" alt="<fmt:message key="header.title"/>" title="<fmt:message key="header.title"/>" />
</div>
--%>

<div id="bodyDiv">
	<div>&nbsp;</div>
	
	<%-- errors --%>
	<c:if test="${!empty requestMessages.errors}">
		<div class="errors">
		<c:forEach items="${requestMessages.errors}" var="requestError">${requestError.value}<br /></c:forEach>
		</div>
	</c:if>
	<%-- warnings --%>
	<c:if test="${!empty requestMessages.warnings}">
		<div class="warnings">
		<c:forEach items="${requestMessages.warnings}" var="requestWarning">${requestWarning.value}<br /></c:forEach>
		</div>
	</c:if>
	<%-- infos --%>
	<c:if test="${!empty requestMessages.infos}">
		<div class="infos">
		<c:forEach items="${requestMessages.infos}" var="requestInfo">${requestInfo.value}<br /></c:forEach>
		</div>
	</c:if>

	<jsp:include page="${body}" />
</div>

<div id="footerPublicDiv">
	<div id="copyDiv"><fmt:message key="footer.copyright.noLink" /></div>
</div>

</div>
	
</body>
</html>