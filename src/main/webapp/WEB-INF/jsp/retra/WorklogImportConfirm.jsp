<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />
<br />

<form action="${requestURI}" method="post" >
	<display:table name="form.worklogItems" requestURI="${requestURI}" export="false" id="worklog" >
		<display:column title="<input type='checkbox' name='selectAll' onChange='checkAll(this)' checked='true' />">
			<input type="checkbox" name="confirmedItems" value="${worklog_rowNum}" checked="true" onChange="" />
		</display:column>		
		
		<!-- data columns -->
		<display:column property="workFrom" titleKey="worklog.date" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />
		<display:column property="project.code" titleKey="worklog.project" escapeXml="true" />
		<display:column property="activity.code" titleKey="worklog.activity" escapeXml="true" />
		<display:column property="invoice.name" titleKey="workLog.invoice" escapeXml="true" />
		<display:column property="description" titleKey="worklog.description" escapeXml="true" />
		<display:column property="workFrom" titleKey="worklog.workFrom" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
		<display:column property="workTo" titleKey="worklog.workTo" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
		<display:column property="hours" titleKey="worklog.hours" format="{0,number,#,##0.00}"/>
	</display:table>
	
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="_cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="_target1" class="button" value="<fmt:message key="button.back"/>" />
				<input type="submit" name="_finish" class="button" value="<fmt:message key="button.finish"/>" />
			</td>
		</tr>
	</table>
	
</form>

