<%@ page import="org.apache.commons.logging.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%--
--%><%@ page isErrorPage="true" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%>

<%! private Log logger = LogFactory.getLog("jsp.error"); %>

<%
	if (logger.isErrorEnabled()) {
		logger.error("Error in JSP page " + pageContext.getErrorData().getRequestURI(), exception);
	}
	
	java.io.StringWriter temp = new java.io.StringWriter();
	exception.printStackTrace(new java.io.PrintWriter(temp));
%>

<c:set var="exception" value="${pageContext.exception}" />
<c:set var="exceptionStack"><%= temp.getBuffer().toString() %></c:set>

<html>

	<head>
		<title>JSP Error</title>
	</head>

	<body>

		<h1>JSP Error</h1>
		
		<hr/>
		
		<%@ include file="StackTrace.jsp"%> 
		
	</body>
</html>


