<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<form action="EmployeeManagement.do?fkprm=true" method="post" >
	<table class="formTable">
		<tr>
			<th><fmt:message key="entity.contactInfo.displayName" /></th>
			<td>${fn:escapeXml(employee.user.contactInfo.displayName)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.contactInfo.firstName" /></th>
			<td>${fn:escapeXml(employee.user.contactInfo.firstName)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.contactInfo.lastName" /></th>
			<td>${fn:escapeXml(employee.user.contactInfo.lastName)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.contactInfo.email" /></th>
			<td>${fn:escapeXml(employee.user.contactInfo.email)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.contactInfo.web" /></th>
			<td>${fn:escapeXml(employee.user.contactInfo.web)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.contactInfo.phone1" /></th>
			<td>${fn:escapeXml(employee.user.contactInfo.phone1)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.contactInfo.phone2" /></th>
			<td>${fn:escapeXml(employee.user.contactInfo.phone2)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.contactInfo.fax" /></th>
			<td>${fn:escapeXml(employee.user.contactInfo.fax)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.login.name" /></th>
			<td>${fn:escapeXml(employee.user.login.name)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.login.ldapLogin" /></th>
			<td>${fn:escapeXml(employee.user.login.ldapLogin)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.employee.icompany" /></th>
			<td>
				<c:if test="${employee.icompany != null}" >${fn:escapeXml(employee.icompany.codeAndName)}</c:if>
				<c:if test="${employee.icompany == null}" >-</c:if>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.employee.igenerate" /></th>
			<td>
				<c:choose>
					<c:when test="${employee.igenerate != null && employee.igenerate == true}"><input type="checkbox" checked="checked" disabled="disabled" /></c:when>
					<c:otherwise><input type="checkbox" disabled="disabled" /></c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
	
	<br><center><b>Projects</b></center>
	
	<display:table name="employee.projects" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" id="project" sort="list">
		<display:column property="name" titleKey="entity.project.name" sortable="true" escapeXml="true"/>
	</display:table>
	
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" />
			</td>
		</tr>
	</table>
</form>
