<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="WorklogOverviewList.do?fkprm=true" />

<%-- filter --%>
<form action="${requestURI}" method="post" class="invisibleInPrint">
	<div class="filter">
	<table class="filterTable">
		<tr>
			<th><fmt:message key="worklog.month" /></th>
			<td>
				<vc:select name="worklogFilterMonth" valueObjects="${months}" selected="${fn:escapeXml(worklogFilterMonth)}"
				valueProperty="value" labelProperty="label" orderBy="label" tabindex="1" />
			</td>
			<th><fmt:message key="worklog.employee" /></th>
			<td>
				<vc:select name="worklogFilterEmployee" valueObjects="${employees}" selected="${fn:escapeXml(worklogFilterEmployee)}" 
				valueProperty="pk" labelProperty="user.contactInfo.displayName" orderBy="user.contactInfo.displayName"
				tabindex="2" /> 
			</td>
			<td rowspan="2" class="buttons"><input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="4"/></td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.year" /></th>
			<td>
				<vc:select name="worklogFilterYear" valueObjects="${years}" selected="${fn:escapeXml(worklogFilterYear)}"
				valueProperty="value" labelProperty="label" orderBy="label" tabindex="3" />
			</td>
			<td colspan="2"></td>
		</tr>
	</table>
	</div>
</form>

<div class="nextPrevious invisibleInPrint">[<a href="${requestURI}&${parametersPrevious}"><fmt:message key="action.previous"/></a>]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<a href="${requestURI}&${parametersNext}"><fmt:message key="action.next"/></a>]</div>
<display:table name="worklogs" requestURI="${requestURI}" id="worklog" export="true" varTotals="totals" >
	<!-- set names of files (do not want default names) -->
	<display:setProperty name="export.csv.filename" value="${employee.user.contactInfo.displayName}-worklog.csv" />
	<display:setProperty name="export.excel.filename" value="${employee.user.contactInfo.displayName}-worklog.xls" />
	<display:setProperty name="export.xml.filename" value="${employee.user.contactInfo.displayName}-worklog.xml" />
	<display:setProperty name="export.pdf.filename" value="${employee.user.contactInfo.displayName}-worklog.pdf" />
	<display:setProperty name="export.rtf.filename" value="${employee.user.contactInfo.displayName}-worklog.rtf" />

	<!-- header -->
	<display:caption>${fn:escapeXml(employee.user.contactInfo.displayName)} - <fmt:message key="month.${worklogFilterMonth}"/> ${fn:escapeXml(worklogFilterYear)}</display:caption>
	
	<!-- data columns -->
	<display:column property="date" titleKey="worklog.date" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" group="1" class="printBold" />

	<display:column titleKey="worklog.project">
		${fn:escapeXml(worklog.project.codefn:escapeXml)} - ${fn:escapeXml(worklog.project.name)}
	</display:column>

	<display:column titleKey="worklog.activity">
		${fn:escapeXml(worklog.activity.code)} - ${fn:escapeXml(worklog.activity.name)}
	</display:column>

	<display:column property="hours" titleKey="worklog.hours" total="true" format="{0,number,0.00}"/>
	
	<display:footer media="html"><tr><th colspan="3"><fmt:message key="worklog.total" /></th><th><fmt:formatNumber pattern="0.00" >${totals.column4}</fmt:formatNumber></th></tr></display:footer>
	<display:footer media="pdf rtf"><fmt:message key="worklog.total" />:<fmt:formatNumber pattern="0.00" >${totals.column4}</fmt:formatNumber></display:footer>
</display:table>

<form action="" method="post" class="invisibleInPrint">
	<div class="printer">
	<table>
		<tr>
			<td>
				<input class="button" type="submit" name="print" value="<fmt:message key="button.print"/>" onclick="JavaScript: {return pagePrint();}"/>
				<a class="button" href="HelpPrint.do?fkprm=true" title="<fmt:message key="workog.help.print"/>"><fmt:message key="workog.help.print"/></a>
			</td>
		</tr>
	</table>
	</div>
</form>

<div id="signatureDiv" class="visibleOnlyInPrint">
	<table>
		<tr>
			<td>.......................</td>
			<td>.......................</td>
		</tr>
		<tr>
			<td><fmt:message key="workog.signature" /></td>
			<td><fmt:message key="workog.signature" /></td>
		</tr>
	</table>
</div>