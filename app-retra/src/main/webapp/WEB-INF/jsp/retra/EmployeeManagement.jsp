<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<!-- TODO: It is not nice practice (define requestURI here) -->
<c:set var="requestURI" value="EmployeeManagement.do?fkprm=true" />

<display:table name="list" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" id="employee" 
	sort="list">

	<display:column property="user.contactInfo.displayName" titleKey="entity.contactInfo.displayName" sortable="true" 
		comparator="${nameComparator}"/>
	<display:column property="user.contactInfo.email" titleKey="entity.contactInfo.email" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.EmailDecorator"/>

	<!-- action columns -only for html (parameter media) -->
	<display:column url="/EmployeeView.do?fkprm=true" paramId="pk" paramProperty="pk" media="html" class="action" headerClass="action" title="">
	    <fmt:message key="action.view"/>
    </display:column>
	<display:column url="/EmployeeEdit.do?fkprm=true" paramId="pk" paramProperty="pk" media="html" class="action"  headerClass="action" titleKey="employeeManagement.create.action">
	    <fmt:message key="action.edit"/>
    </display:column>
	<%-- display:column url="/EmployeeDelete.do" paramId="pk" paramProperty="pk" media="html" class="action" headerClass="action">
	    <fmt:message key="action.delete"/>
    </display:column--%>

</display:table>

