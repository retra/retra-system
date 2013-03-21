<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="EmployeeChangePassword.do?fkprm=true" />

<form action="${requestURI}" method="post" >
	<table class="formTable">
		<tr>
			<spring:bind path="employeeForm.user.login.name">
				<th><fmt:message key="entity.login.name" /></th>
				<td>
					${status.value}
					<input type="hidden" name="${status.expression}" value="${status.value}" />
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.login.ldapLogin">
				<th><fmt:message key="entity.login.ldapLogin" /></th>
				<td>
					${status.value}
					<input type="hidden" name="${status.expression}" value="${status.value}" />
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.login.passwordOriginal">
				<th><fmt:message key="entity.login.password.original" /></th>
				<td>
					<input type="password" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.login.password">
				<th><fmt:message key="entity.login.password.new" /></th>
				<td>
					<input type="password" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.login.passwordConfirmation">
				<th><fmt:message key="entity.login.password.confirmation" /></th>
				<td>
					<input type="password" name="${status.expression}" value="${status.value}" />
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
