<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="WorklogDailyOverview.do?fkprm=true" />

<%-- filter --%>
<form action="${requestURI}" method="post" >
	<div class="filter">
	<table class="filterTable">
		<tr>
			<th></th>
			<td></td>
			<th><fmt:message key="worklog.employee" /></th>
			<td>
				<vc:select name="worklogFilterEmployee" valueObjects="${employees}" selected="${worklogFilterEmployee}" 
				valueProperty="pk" labelProperty="user.contactInfo.displayName" orderBy="user.contactInfo.displayName"
				tabindex="3" /> 
			</td>
			<td rowspan="3" class="buttons"><input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="6"/></td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.date" /></th>
			<td>
				<input type="text" name="worklogFilterDate" value="${worklogFilterDate}" tabindex="1" id="worklogFilterDateId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogFilterDateImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('worklogFilterDateId','worklogFilterDateImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="worklog.project" /></th>
			<td>
				<vc:select name="worklogFilterProject" valueObjects="${projects}" selected="${worklogFilterProject}" 
				valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"
				tabindex="4">
					<vc:select-option value="">--- <fmt:message key="worklog.allProjects" /> ---</vc:select-option>
				</vc:select>
			</td>
		</tr>
		<tr>
			<td colspan="2"></td>
			<th><fmt:message key="worklog.activity" /></th>
			<td>
				<vc:select name="worklogFilterActivity" valueObjects="${activities}" selected="${worklogFilterActivity}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
					<vc:select-option value="">--- <fmt:message key="worklog.allActivities" /> ---</vc:select-option>
				</vc:select> <!-- tabindex="5" -->
			</td>
		</tr>
	</table>
	</div>
</form>


<div class="tabber">

	<div class="tabbertab">
		<h2>Worklog Items</h2>
<display:table name="worklogs" requestURI="${requestURI}" export="true" id="worklog" >
	<!-- set names of files (do not want default names) -->
	<display:setProperty name="export.csv.filename" value="${employee.user.contactInfo.displayName}-worklog.csv" />
	<display:setProperty name="export.excel.filename" value="${employee.user.contactInfo.displayName}-worklog.xls" />
	<display:setProperty name="export.xml.filename" value="${employee.user.contactInfo.displayName}-worklog.xml" />
	<display:setProperty name="export.pdf.filename" value="${employee.user.contactInfo.displayName}-worklog.pdf" />
	<display:setProperty name="export.rtf.filename" value="${employee.user.contactInfo.displayName}-worklog.rtf" />
	
	<%-- -->display:caption>${employee.user.contactInfo.displayName}</display:caption--%>
	
	<!-- data columns -->
	<display:column property="workFrom" titleKey="worklog.date" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />

	<display:column titleKey="worklog.project" media="html" >
		<!-- this is hack for sorting -->
		<span class="invisible">${worklog.project.code}</span>
		<span title="${worklog.project.name}">${worklog.project.code}</span>
	</display:column>
	<display:column property="project.code" titleKey="worklog.project" media="csv excel xml pdf rtf"/>

	<display:column titleKey="worklog.activity" media="html">
		<!-- this is hack for sorting -->
		<span class="invisible">${worklog.activity.code}</span>
		<span title="${worklog.activity.name}">${worklog.activity.code}</span>
	</display:column>
	<display:column property="activity.code" titleKey="worklog.activity" media="csv excel xml pdf rtf"/>

	<display:column property="description" titleKey="worklog.description"/>
	<display:column property="workFrom" titleKey="worklog.workFrom" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
	<display:column property="workTo" titleKey="worklog.workTo" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
	<display:column property="hours" titleKey="worklog.hours" sortable="true" format="{0,number,#,##0.00}"/>

</display:table>

	</div>

	<div class="tabbertab">
		<h2>Project overview</h2>
		<display:table id="worklogByProjectId" name="worklogByProject.list" requestURI="${requestURI}" export="true">
			<display:column property="key" titleKey="worklog.project" />
			<display:column property="value.sum" titleKey="worklog.hours" format="{0,number,#,##0.00}" />
			<display:footer media="html">
				<tr>
					<th><fmt:message key="worklog.total" /></th>
					<th><fmt:formatNumber value="${worklogByProject.summary.sum}" type="number" maxFractionDigits="2" /></th>
				</tr>
			</display:footer>
		</display:table>
	</div>

	<div class="tabbertab">
		<h2>Activity overview</h2>
		<display:table id="worklogByActivityId" name="worklogByActivity.list" requestURI="${requestURI}" export="true">
			<display:column property="key" titleKey="worklog.activity" />
			<display:column property="value.sum" titleKey="worklog.hours" format="{0,number,#,##0.00}" />
			<display:footer media="html">
				<tr>
					<th><fmt:message key="worklog.total" /></th>
					<th><fmt:formatNumber value="${worklogByActivity.summary.sum}" maxFractionDigits="2" type="number" /></th>
				</tr>
			</display:footer>
		</display:table>
	</div>

</div>
