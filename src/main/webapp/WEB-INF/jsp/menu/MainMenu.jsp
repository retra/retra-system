<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<div id="menuAboutDiv">
	<a href="About.do?fkprm=true"><fmt:message key="about.label" /></a> |
	<a href="Newsboard.do?fkprm=true"><fmt:message key="newsboard.label" /></a>
</div>

<div id="menuMainDiv">
	<a href="Dashboard.do?fkprm=true"><fmt:message key="menu.dashboard" /></a> |
	<a href="WorklogDaily.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklog" /></a> |
	<a href="InvoiceList.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.invoice" /></a> |<%--
	<a href="ScheduleList.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.schedule" /></a> |--%>
	<a href="ProjectManagement.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.project" /></a> |
	<a href="EmployeeManagement.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.employee" /></a>
</div>

<div id="menuHelpDiv">
	<a href="EmployeeChangePassword.do?fkprm=true"><fmt:message key="menu.settings" /></a> |
	<a href="HelpPrint.do?fkprm=true"><fmt:message key="menu.help" /></a>
</div>