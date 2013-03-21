<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<%-- TODO: RequestURI is not nice practice --%>
<c:set var="requestURI" value="ProjectManagement.do?fkprm=true" />

<form action="${requestURI}" method="post" >
	<table class="formTable">
		<tr>
			<th><fmt:message key="entity.project.parent.code" /></th>
			<td>${project.parent.code}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.project.code" /></th>
			<td>${project.code}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.project.name" /></th>
			<td>${project.name}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.project.category" /></th>
			<td>
				<c:if test="${project.category != null}" >${project.category.code} - ${project.category.name}</c:if>
				<c:if test="${project.category == null}" >-</c:if>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.project.estimation" /></th>
			<td>${project.estimation}</td>
		</tr>		
		<tr>
			<th><fmt:message key="entity.project.workEnabled" /></th>
			<td>
				<c:choose>
					<c:when test="${project.workEnabled != null && project.workEnabled == true}"><input type="checkbox" checked="checked" disabled="disabled" /></c:when>
					<c:otherwise><input type="checkbox" disabled="disabled" /></c:otherwise>
				</c:choose>
			</td>
		</tr>


		<tr>
			<th><fmt:message key="entity.project.state" /></th>
			<td><img src="${imgRoot}/<fmt:message key='state.img.${project.state}' />" alt="<fmt:message key='state.${project.state}' />" title="<fmt:message key='state.${project.state}' />" align="absmiddle" /></td>
		</tr>
	
	</table>
	
	<br><center><b>Employees</b></center>
	
	<display:table name="project.employees" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" id="employee"
		sort="list">
		<display:column property="user.contactInfo.displayName" titleKey="entity.contactInfo.displayName" sortable="true"/>
	</display:table>	
	
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" />
			</td>
		</tr>
	</table>
</form>
