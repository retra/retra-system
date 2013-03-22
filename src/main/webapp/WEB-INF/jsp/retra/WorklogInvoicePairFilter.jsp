<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />

<spring:bind path="form.*">
	<c:if test="${!empty status.errorMessages}">
	<div class="errors">
		<c:forEach var="error" items="${status.errorMessages}">
			${error}<br />
		</c:forEach>
	</div>
	</c:if>
</spring:bind>

<form action="${requestURI}" method="post" class="invisibleInPrint">

	<h3><fmt:message key="worklog.invoice.pair.define.invoice" /></h3>

	<table class="formTable">
		<tr>
			<spring:bind path="form.invoice">
				<th><fmt:message key="worklog.invoice" /></th>
				<td>
					<c:if test="${empty invoices}">
					<vc:select name="${status.expression}" valueObjects="${invoices}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName">
							<vc:select-option value="">--- <fmt:message key="invoice.please.define" /> ---</vc:select-option>
					</vc:select>
					</c:if>

					<c:if test="${!empty invoices}">
					<vc:select name="${status.expression}" valueObjects="${invoices}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" />
					</c:if>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
	</table>

	<h3><fmt:message key="worklog.invoice.pair.define.items" /></h3>

	<table class="formTable">
		<tr>
			<spring:bind path="form.dateFrom">
				<th><fmt:message key="worklog.workFrom" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" id="worklogDateFromId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogDateFromImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('worklogDateFromId','worklogDateFromImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="form.dateTo">
				<th><fmt:message key="worklog.workTo" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" id="worklogDateToId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogDateToImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('worklogDateToId','worklogDateToImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="form.project">
				<th><fmt:message key="worklog.project" /></th>
				<td>
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- <fmt:message key="worklog.allProjects" /> ---</vc:select-option>
					</vc:select> 
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="form.activity">
				<th><fmt:message key="worklog.activity" /></th>
				<td>
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- <fmt:message key="worklog.allActivities" /> ---</vc:select-option>
					</vc:select>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
	</table>

	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="_cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="_target1" class="button" value="<fmt:message key="button.next"/>" />
			</td>
		</tr>
	</table>

</form>