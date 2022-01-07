<%-- comments are here to eliminate white-spaces

--%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%--
--%><%@ page errorPage="/WEB-INF/jsp/Error.jsp" %><%--
--%><%@ page import="cz.softinel.uaf.state.StateEntity" %><%--

--%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--
--%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%--
--%><%@ taglib prefix="display" uri="http://displaytag.sf.net" %><%--
--%><%--
--%><%@ taglib prefix="vc" uri="/WEB-INF/tld/visual-component.tld" %><%--
--%><%--
--%><c:set var="guiRoot" value="gui"/><%--
--%><c:set var="commonSkinRoot" value="${guiRoot}/skin00"/><%--
--%><c:set var="commonCssRoot" value="${commonSkinRoot}/css"/><%--
--%><c:set var="commonImgRoot" value="${commonSkinRoot}/img"/><%--
--%><c:set var="commonJsRoot" value="${commonSkinRoot}/js"/><%--

--%><c:set var="skinRoot" value="${guiRoot}/${skinName}"/><%--
--%><c:set var="cssRoot" value="${skinRoot}/css"/><%--
--%><c:set var="imgRoot" value="${skinRoot}/img"/><%--
--%><c:set var="jsRoot" value="${skinRoot}/js"/><%--

--%><c:set var="defaultPageSize" value="20"/><%--

--%><c:set var="ENTITY_STATE_DELETED"><%=StateEntity.STATE_DELETED%></c:set><%--
--%><c:set var="ENTITY_STATE_ACTIVE"><%=StateEntity.STATE_ACTIVE%></c:set><%--
--%><c:set var="ENTITY_STATE_CLOSED"><%=StateEntity.STATE_CLOSED%></c:set><%--
--%>