<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<table class="dashboardComponents" cellspacing="0" cellpadding="0">
	<tr valign="top">
		<td>
			<div class="component dashboardComponentHalf">
				<h2><fmt:message key="menu.worklog" /></h2>
				<p><a href="WorklogDaily.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklogDaily" /></a></p>
<%--				<p><a href="WorklogList.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklogList" /></a></p> --%>
				<p><a href="WorklogOverview.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklogOverview" /></a></p>
<%-- 
				<p><a href="WorklogOverviewList.do?cleanFilter=true"><fmt:message key="menu.worklogOverviewList" /></a></p>
				<p><a href="WorklogDailyOverview.do?cleanFilter=true"><fmt:message key="menu.worklogDailyOverview" /></a>,
				<a href="WorklogWeeklyOverview.do?cleanFilter=true"><fmt:message key="menu.worklogWeeklyOverview" /></a>,
				<a href="WorklogMonthlyOverview.do?cleanFilter=true"><fmt:message key="menu.worklogMonthlyOverview" /></a></p>
				<p><a href="WorklogCreate.do"><fmt:message key="menu.worklogCreate" /></a></p>
--%>
				<p><a href="WorklogProjectOverview.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.worklogProjectOverview" /></a></p>
				<p><a href="WorklogImport.do?fkprm=true"><fmt:message key="menu.worklogImport" /></a></p>
			</div>
			<div class="component dashboardComponentHalf">
				<h2><fmt:message key="menu.invoice" /></h2>
				<p><a href="InvoiceList.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.invoiceList" /></a></p>
				<p><a href="InvoiceCreate.do?fkprm=true"><fmt:message key="menu.invoiceCreate" /></a></p>
				<p><a href="WorklogInvoicePair.do?fkprm=true"><fmt:message key="menu.invoiceWorklogPair" /></a></p>
				<p><a href="InvoiceBatchGenerate.do?fkprm=true"><fmt:message key="menu.invoiceBatchGenerate" /></a></p>
			</div>
		</td>
		<td>
			<div class="component dashboardComponentHalf">
				<h2><fmt:message key="menu.employee"/></h2>
				<p><a href="EmployeeManagement.do?fkprm=true"><fmt:message key="menu.employeeManagement" /></a></p>
				<p><a href="EmployeeCreate.do?fkprm=true"><fmt:message key="menu.employeeCreate" /></a></p>
			</div>
			<div class="component dashboardComponentHalf">
				<h2><fmt:message key="menu.project"/></h2>
				<p><a href="ProjectManagement.do?fkprm=true"><fmt:message key="menu.projectManagement" /></a></p>
				<p><a href="ProjectCreate.do?fkprm=true"><fmt:message key="menu.createProject" /></a></p>
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="2">
			<hr />
			<h2>Settings and help</h2>
		</td>
	<tr valign="top">
		<td>
			<div class="component dashboardComponentHalf">
				<h2><fmt:message key="menu.settings" /></h2>
				<p><a href="SettingsDashboard.do?fkprm=true"><fmt:message key="menu.settingsDashboard" /></a></p>
				<p><a href="EmployeeChangePassword.do?fkprm=true"><fmt:message key="menu.settingsChangePassword" /></a></p>
				<p><a href="EmployeeChangeContactInfo.do?fkprm=true"><fmt:message key="menu.settingsChangeContactInfo" /></a></p>
				<p><a href="ChangeVisualConfiguration.do?fkprm=true"><fmt:message key="menu.settingsVisualConfiguration" /></a></p>
			</div>
		</td>
		<td>
			<div class="component dashboardComponentHalf">
				<h2><fmt:message key="menu.help" /></h2>
				<p><a href="HelpPrint.do?fkprm=true"><fmt:message key="menu.helpPrint" /></a></p>
				<p><a href="HelpCopySchedule.do?fkprm=true"><fmt:message key="menu.helpCopySchedule" /></a></p>
				<p><a href="HelpWorklogImportFull.do?fkprm=true"><fmt:message key="worklog.help.import" /></a></p>
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td colspan="2">
			<hr />
			<h2>Oldies :-)</h2>
		</td>
	</tr>
	<tr valign="top">
		<td>
			<div class="component dashboardComponentHalf">
				<h2><fmt:message key="menu.schedule" /></h2>
				<p><a href="ScheduleList.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.scheduleList" /></a></p>
				<p><a href="ScheduleOverviewList.do?fkprm=true&cleanFilter=true"><fmt:message key="menu.scheduleOverviewList" /></a></p>
				<p><a href="ScheduleCreate.do?fkprm=true"><fmt:message key="menu.scheduleCreate" /></a></p>
				<p><a href="ScheduleCopy.do?fkprm=true"><fmt:message key="menu.scheduleCopy" /></a></p>
			</div>
		</td>
	</tr>
</table>