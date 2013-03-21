<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<a href="WorklogDaily.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklogDaily" /></a> |<%--
<a href="WorklogList.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklogList" /></a> |--%>
<a href="WorklogOverview.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklogOverview" /></a> |
<%-- 

TODO radek: Clean JSPs, Controllers, refactore it 

<a href="WorklogOverviewList.do?cleanFilter=true"><fmt:message key="menu.worklogOverviewList" /></a> |
<a href="WorklogDailyOverview.do?cleanFilter=true"><fmt:message key="menu.worklogDailyOverview" /></a> |
<a href="WorklogWeeklyOverview.do?cleanFilter=true"><fmt:message key="menu.worklogWeeklyOverview" /></a> |
<a href="WorklogMonthlyOverview.do?cleanFilter=true"><fmt:message key="menu.worklogMonthlyOverview" /></a> |
<a href="WorklogCreate.do"><fmt:message key="menu.worklogCreate" /></a>

--%>
<a href="WorklogProjectOverview.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklogProjectOverview" /></a> |
<a href="WorklogImport.do?fkprm=true"><fmt:message key="menu.worklogImport" /></a>
