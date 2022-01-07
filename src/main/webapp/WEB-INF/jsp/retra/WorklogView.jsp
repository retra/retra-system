<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}&worklogFilterEmployee=${worklog.employee.pk}" />

<form action="${requestURI}" method="post" >
	<table class="formTable">
		<tr>
			<th><fmt:message key="worklog.date" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.DATE_FORMAT}" value="${worklog.workFrom}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.project" /></th>
			<td>${fn:escapeXml(worklog.project.code)} - ${fn:escapeXml(worklog.project.name)}</td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.component" /></th>
			<td>
				<c:if test="${worklog.component != null}" >${fn:escapeXml(worklog.component.code)} - ${fn:escapeXml(worklog.component.name)}</c:if>
				<c:if test="${worklog.component == null}" >-</c:if>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.activity" /></th>
			<td>${fn:escapeXml(worklog.activity.code)} - ${fn:escapeXml(worklog.activity.name)}</td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.invoice" /></th>
			<td>
				<c:if test="${worklog.onInvoice}" >${fn:escapeXml(worklog.invoice.code)} (${fn:escapeXml(worklog.invoice.name)})&nbsp;-&nbsp;<img src="${imgRoot}/<fmt:message key='invoice.state.img.${worklog.invoice.state}' />" alt="<fmt:message key='invoice.state.${worklog.invoice.state}' />" title="<fmt:message key='invoice.state.${worklog.invoice.state}' />" align="absmiddle" /></c:if>
				<c:if test="${!worklog.onInvoice}" >-</c:if>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.workFrom" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.HOUR_FORMAT}" value="${worklog.workFrom}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.workTo" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.HOUR_FORMAT}" value="${worklog.workTo}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.description" /></th>
			<td>${worklog.descriptionGui}</td>
		</tr>
	</table>
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" />
			</td>
		</tr>
	</table>
</form>
