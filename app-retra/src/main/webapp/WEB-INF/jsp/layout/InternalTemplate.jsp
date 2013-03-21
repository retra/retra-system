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
	<meta name="generator" content="Retra version <fmt:message key="about.version.major" />.<fmt:message key="about.version.minor" />.<fmt:message key="about.version.build" />"/>

	<title><fmt:message key="header.title"/> - <fmt:message key="${title}"/></title>
	<link rel="stylesheet" type="text/css" media="screen" href="${cssRoot}/screen.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="${commonCssRoot}/calendar.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="${commonCssRoot}/timeselector.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="${commonCssRoot}/tabber.css"  />
	<link rel="stylesheet" type="text/css" media="screen" href="${cssRoot}/tabber.css"  />
	<link rel="stylesheet" type="text/css" media="screen" href="${cssRoot}/timeselector.css"  />
	
	<link rel="stylesheet" type="text/css" media="print" href="${commonCssRoot}/tabber-print.css"  />	
	<link rel="stylesheet" type="text/css" media="print" href="${commonCssRoot}/print.css" />
	<!--[if lte IE 6]>
		<link rel="stylesheet" type="text/css" media="screen" href="${cssRoot}/screenMsie.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="${commonCssRoot}/calendarMsie.css"/>
		<link rel="stylesheet" type="text/css" media="print" href="${commonCssRoot}/printMsie.css"/>
	<![endif]-->
	<!--[if gte IE 7]>
		<link rel="stylesheet" type="text/css" media="screen" href="${cssRoot}/screenMsie7.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="${commonCssRoot}/calendarMsie7.css"/>
		<link rel="stylesheet" type="text/css" media="print" href="${commonCssRoot}/printMsie7.css"/>
	<![endif]-->
	<link rel="shortcut icon" type="image/ico" href="${skinRoot}/favicon.ico" />
	<link rel="icon" type="image/ico" href="${skinRoot}/favicon.ico" />

	<script src="${commonJsRoot}/timeselector.js" type="text/javascript"></script>
	<script src="${commonJsRoot}/calendar.js" type="text/javascript"></script>
	<script src="${commonJsRoot}/calendar-setup.js" type="text/javascript"></script>
	<script src="${commonJsRoot}/calendar-en.js" type="text/javascript"></script>
	<script src="${commonJsRoot}/mira.js" type="text/javascript"></script>
	<script src="${commonJsRoot}/tabber.js" type="text/javascript"></script>
	
</head>

<!-- body -->
<body>
<!-- 
<div id="logoDiv"></div>
 -->
<c:if test="${primaryLinks != null}">
<div id="primaryLinks" class="invisibleInPrint">
	${primaryLinks}
</div>
</c:if>
<div id="mainDiv">
<h1><fmt:message key="header.title"/> - <fmt:message key="${title}"/></h1>

<div id="headerDiv">
	<div id="logoutDiv">
		<a href="Logout.do?fkprm=true"><fmt:message key="logout.label" /></a>
	</div>
	<div id="currentUserDiv">
		<fmt:message key="currentUser.label" />: ${securityContext.loggedUser.contactInfo.displayName}
	</div>
	
	<img id="miraLogoInHeader" src="${imgRoot}/retra-logo-small.png" alt="<fmt:message key="header.title"/>" title="<fmt:message key="header.title"/>" />

</div>

<div id="menuDiv">
	<jsp:include page="${mainMenu}" />
</div>
<c:if test="${not empty subMenu}">
	<div id="submenuDiv">
		<jsp:include page="${subMenu}"/>
	</div>
</c:if>
  
<div id="bodyDiv">
	<h2 class="invisibleInPrint"><fmt:message key="${title}"/></h2>
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

<div id="footerDiv">
	<div id="copyDiv"><fmt:message key="footer.copyright" /></div>
</div>

</div>
	
</body>
</html>