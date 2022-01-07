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

	<table class="formTable">
		<tr>
			<c:if test="${isCodeGenerated}">
			<spring:bind path="form.sequence">
				<th><fmt:message key="entity.invoice.sequence" /></th>
				<td>
					<vc:select name="${status.expression}" valueObjects="${sequences}" selected="${fn:escapeXml(status.value)}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName">
						<vc:select-option value="">--- <fmt:message key="invoice.sequence.please.define" /> ---</vc:select-option>
					</vc:select> 
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
			</c:if>
			<c:if test="${!isCodeGenerated}">
			<spring:bind path="form.code">
				<th><fmt:message key="entity.invoice.sequence" /></th>
				<td>
					<span class="error"><fmt:message key="error.invoiceBatchGenerate.codeNotGenerated" /></span>
				</td>
			</spring:bind>
			</c:if>
		</tr>
		<tr>
			<spring:bind path="form.orderDate">
				<th><fmt:message key="entity.invoice.orderDate" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" id="invoiceOrderDateId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="invoiceOrderDateImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('invoiceOrderDateId','invoiceOrderDateImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="form.finishDate">
				<th><fmt:message key="entity.invoice.finishDate" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" id="invoiceFinishDateId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="invoiceFinishDateImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('invoiceFinishDateId','invoiceFinishDateImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="form.name">
				<th><fmt:message key="entity.invoice.name" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" maxlength="250" size="75"/>
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