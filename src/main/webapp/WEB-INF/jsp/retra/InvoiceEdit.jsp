<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />

<form action="${requestURI}" method="post" >
	<spring:bind path="invoiceForm.pk">
		<input type="hidden" name="${status.expression}" value="${fn:escapeXml(status.value)}" />
	</spring:bind>
	
	<table class="formTable">
		<tr>
			<c:if test="${isCodeGenerated}">
				<th><fmt:message key="entity.invoice.code" /></th>
				<td>
					${invoiceForm.code}
					<spring:bind path="invoiceForm.code">
						<input type="hidden" name="${status.expression}" value="${fn:escapeXml(status.value)}" />
					</spring:bind>
				</td>
			</c:if>

			<c:if test="${!isCodeGenerated}">
			<spring:bind path="invoiceForm.code">
				<th><fmt:message key="entity.invoice.code" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" maxlength="30"/>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
			</c:if>
		</tr>
		<tr>
			<spring:bind path="invoiceForm.orderDate">
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
			<spring:bind path="invoiceForm.finishDate">
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
			<spring:bind path="invoiceForm.name">
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
				<input type="submit" name="save" class="invisible" value="firstSaveNotVissible" />
				<input type="submit" name="cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="save" class="button" value="<fmt:message key="button.save"/>" />
			</td>
		</tr>
	</table>

</form>
