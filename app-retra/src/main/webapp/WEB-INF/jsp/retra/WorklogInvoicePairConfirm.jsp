<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />

<table class="formTable">
	<tr>
		<th><fmt:message key="entity.invoice.code" /></th>
		<td>${invoice.code}</td>
	</tr>
	<tr>
		<th><fmt:message key="entity.invoice.employee" /></th>
		<td>${invoice.employee.user.contactInfo.displayName}</td>
	</tr>
	<tr>
		<th><fmt:message key="entity.invoice.name" /></th>
		<td>${invoice.name}</td>
	</tr>
	<tr>
		<th><fmt:message key="entity.invoice.state" /></th>
		<td><img src="${imgRoot}/<fmt:message key='invoice.state.img.${invoice.state}' />" alt="<fmt:message key='invoice.state.${invoice.state}' />" title="<fmt:message key='invoice.state.${invoice.state}' />" align="absmiddle" /></td>
	</tr>
</table>

<h3><fmt:message key="worklog.invoice.pair.select.items" /></h3>

<form action="${requestURI}" method="post" >
	<display:table name="worklogs" requestURI="${requestURI}" export="false" id="worklog" varTotals="worklogTotals">
		<display:column title="<input type='checkbox' name='selectAll' onChange='checkAll(this)' checked='true' />">
			<input type="checkbox" name="confirmedItems" value="${worklog.pk}" checked="true" onChange="" />
		</display:column>		
		
		<!-- data columns -->
		<display:column property="workFrom" titleKey="worklog.date" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />

		<display:column titleKey="worklog.project" media="html" >
			<!-- this is hack for sorting -->
			<span class="invisible">${worklog.project.code}</span>
			<span title="${worklog.project.name}">${worklog.project.code}</span>
		</display:column>
		<display:column property="project.code" titleKey="worklog.project" media="csv excel xml pdf rtf"/>

		<display:column titleKey="worklog.component" media="html">
			<!-- this is hack for sorting -->
			<span class="invisible">${worklog.component.code}</span>
			<acronym title="${worklog.component.name}">${worklog.component.code}</acronym>
		</display:column>
		<display:column property="component.code" titleKey="worklog.component" media="csv excel xml pdf rtf"/>
			
		<display:column titleKey="worklog.activity" media="html">
			<!-- this is hack for sorting -->
			<span class="invisible">${worklog.activity.code}</span>
			<span title="${worklog.activity.name}">${worklog.activity.code}</span>
		</display:column>
		<display:column property="activity.code" titleKey="worklog.activity" media="csv excel xml pdf rtf"/>

		<display:column property="description" titleKey="worklog.description"/>
		<display:column property="workFrom" titleKey="worklog.workFrom" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
		<display:column property="workTo" titleKey="worklog.workTo" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
		<display:column property="hours" titleKey="worklog.hours" decorator="cz.softinel.retra.core.utils.decorator.HoursDecorator" total="true"/>

		<display:footer media="html">
			<tr>
				<th></th>
				<th><fmt:message key="worklog.total"/></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th><strong><fmt:formatNumber value="${worklogTotals.column9}" type="number" maxFractionDigits="2" minFractionDigits="2"/></strong></th>
		    </tr>
		</display:footer>

	</display:table>
	
	<br />
	
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="_cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="_target0" class="button" value="<fmt:message key="button.back"/>" />
				<input type="submit" name="_finish" class="button" value="<fmt:message key="button.finish"/>" />
			</td>
		</tr>
	</table>
	
</form>
