<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="ProjectManageComponents.do?fkprm=true" />

<form action="${requestURI}" method="post" >
		<spring:bind path="projectForm.pk">
				<input type="hidden" name="${status.expression}" value="${fn:escapeXml(status.value)}" />
		</spring:bind>
		
		<table class="formTable">
		<tr>
			<th><fmt:message key="entity.project.code" /></th>
			<td>${fn:escapeXml(projectForm.code)}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.project.name" /></th>
			<td>${fn:escapeXml(projectForm.name)}</td>
		</tr>
		
	</table>
	
	<display:table name="${projectForm.components}" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" id="component"
		sort="list">
		<display:column property="code" titleKey="entity.component.code" sortable="true" escapeXml="true" />
		<display:column property="name" titleKey="entity.component.name" sortable="true" escapeXml="true" />
		
		<display:column url="/ComponentEdit.do?fkprm=true" paramId="pk" paramProperty="pk" media="html" class="action"  headerClass="action" 
			titleKey="action.edit">
			<%-- 
			titleKey="projectManagement.editComponent.action">
			--%>
	    	<fmt:message key="action.edit"/>
    	</display:column>
    	
    	<display:column titleKey="action.delete" media="html" class="action" headerClass="action">
    		<a href="ComponentDelete.do?fkprm=true&pk=${component.pk}"><span onclick="JavaScript: {return deleteConfirm('<fmt:message key="delete.confirmation" />')}"><fmt:message key="action.delete"/></span></a>
		</display:column>
		
	</display:table>
	
	<table class="formTable">
		<tr>
			<td align="right">
				<a href="ComponentCreate.do?fkprm=true&projectPk=${projectForm.pk}"><fmt:message key="action.createComponent"/></a>
			</td>
		</tr>
	</table>
	
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="unknown" class="button" value="<fmt:message key="button.back"/>" />
			</td>
		</tr>
	</table>

</form>
