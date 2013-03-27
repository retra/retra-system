<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cz.softinel.retra.project.ProjectHelper" %>

<%@ include file="../Includes.jsp"%> 

<!-- TODO: It is not nice practice (define requestURI here) -->
<c:set var="requestURI" value="ProjectManagement.do?fkprm=true" />

<%-- filter --%>
<form action="${requestURI}" method="post" >
	<div class="filter">
	<table class="filterTable">
		<tr>
			<th><fmt:message key="entity.project.code" /></th>
			<td>
				<input type="text" name="projectFilterCode" value="${projectFilterCode}" maxlength="30"/>
			</td>
			<th><fmt:message key="entity.project.employee" /></th>
			<td>
				<vc:select name="projectFilterEmployee" valueObjects="${employees}" selected="${projectFilterEmployee}" 
				valueProperty="pk" labelProperty="user.contactInfo.displayName" orderBy="user.contactInfo.displayName"
				tabindex="2">
					<vc:select-option value="-1">--- <fmt:message key="project.allEmployees" /> ---</vc:select-option>
				</vc:select> 
			</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.project.name" /></th>
			<td>
				<input type="text" name="projectFilterName" value="${projectFilterName}" maxlength="255"/>
			</td>
			<th><fmt:message key="entity.project.state" /></th>
			<td>
				<select name="projectFilterState">
					<option value="-1">--- <fmt:message key="state.all" /> ---</option>
					<c:forEach items="${states}" var="state">
						<c:choose>
							<c:when test="${projectFilterState != null && projectFilterState != '' && state.code == projectFilterState}">
								<option value="${state.code}" selected="selected"><fmt:message key="state.${state.code}" /></option>
							</c:when>
							<c:otherwise>
								<option value="${state.code}"><fmt:message key="state.${state.code}" /></option>
							</c:otherwise>
						</c:choose>
					</c:forEach>				
				</select>
			</td>
			<td rowspan="2" class="buttons">
				<input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="4"/>
			</td>
		</tr>
	</table>
	</div>
</form>

<display:table name="projects" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" id="project" sort="list">

	<display:column property="parent.code" titleKey="entity.project.parent.code" sortable="true" maxLength="10"/>
	<display:column property="code" titleKey="entity.project.code" sortable="true" maxLength="10"/> 
	<display:column property="name" titleKey="entity.project.name" sortable="true" />
	<display:column property="estimation" titleKey="entity.project.estimation" sortable="true" />
	<display:column property="category.code" titleKey="entity.project.category" sortable="true" />
	
	<display:column property="state" titleKey="entity.project.state" media="html" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.StateDecorator" maxLength="5" class="center"/>
	<display:column titleKey="entity.project.state" media="csv excel xml pdf rtf">
		<fmt:message key="state.${project.state}" />
	</display:column>
	
	<!-- action columns -only for html (parameter media) -->
	<display:column url="/ProjectView.do?fkprm=true" paramId="pk" paramProperty="pk" media="html" class="action" headerClass="action" title="">
	<%-- 
		titleKey="projectManagement.view.action">
	--%>
	    <fmt:message key="action.view"/>
    </display:column>

	<display:column media="html" class="action"  headerClass="action">
		<c:choose>
			<c:when test="${project.state == ENTITY_STATE_ACTIVE}">
				<a href="ProjectEdit.do?fkprm=true&pk=${project.pk}"><fmt:message key="action.edit"/></a>
			</c:when>
			<c:otherwise>
				&nbsp;			
			</c:otherwise>
		</c:choose>
    </display:column>

    <display:column media="html" class="action"  headerClass="action" title="">
		<c:choose>
			<c:when test="${project.state == ENTITY_STATE_ACTIVE}">
				<a href="ProjectClose.do?fkprm=true&pk=${project.pk}"><fmt:message key="action.close"/></a>
			</c:when>
			<c:when test="${project.state == ENTITY_STATE_CLOSED}">
				<a href="ProjectOpen.do?fkprm=true&pk=${project.pk}"><fmt:message key="action.open"/></a>
			</c:when>
			<c:otherwise>
				&nbsp;			
			</c:otherwise>
		</c:choose>
	</display:column>   

	
    <display:column media="html" class="action"  headerClass="action" title="">
		<c:choose>
			<c:when test="${project.state == ENTITY_STATE_CLOSED}">
				<a href="ProjectDelete.do?fkprm=true&pk=${project.pk}"><span onclick="JavaScript: {return deleteConfirm('<fmt:message key="delete.confirmation" />')}"><fmt:message key="action.delete"/></span></a>
			</c:when>
			<c:otherwise>
				&nbsp;			
			</c:otherwise>
		</c:choose>
	</display:column>   

	<display:column media="html" class="action"  headerClass="action" title="">
		<c:choose>
			<c:when test="${project.state == ENTITY_STATE_ACTIVE}">
				<a href="ProjectManageComponents.do?fkprm=true&pk=${project.pk}"><fmt:message key="action.managecomponents"/></a>
			</c:when>
			<c:otherwise>
				&nbsp;			
			</c:otherwise>
		</c:choose>
    </display:column>
    
</display:table>
