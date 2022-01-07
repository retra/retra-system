<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="ComponentCreate.do?fkprm=true" />

<form action="${requestURI}" method="post" >

	<spring:bind path="componentForm.pk">
		<input type="hidden" name="${status.expression}" value="${fn:escapeXml(status.value)}" />
	</spring:bind>

	<spring:bind path="componentForm.projectPk">
				<input type="hidden" name="${status.expression}" value="${fn:escapeXml(status.value)}" />
	</spring:bind>
 
	<table class="formTable">
		<tr>
			<spring:bind path="componentForm.code">
				<th><fmt:message key="entity.component.code" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" maxlength="30"/>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="componentForm.name">
				<th><fmt:message key="entity.component.name" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" maxlength="250"/>
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
				<!-- <input type="submit" name="cancel" class="button" value="<fmt:message key="button.cancel"/>" /> --> 
				<input type="submit" name="undefined" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="save" class="button" value="<fmt:message key="button.save"/>" />
			</td>
		</tr>
	</table>
	
</form>
