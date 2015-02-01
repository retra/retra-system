<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />

<table class="formTable">
	<tr>
		<th><fmt:message key="entity.invoice.sequence" /></th>
		<td>${invoiceSeq.codeAndName}</td>
	</tr>
	<tr>
		<th><fmt:message key="entity.invoice.orderDate" /></th>
		<td>${form.orderDate}</td>
	</tr>
	<tr>
		<th><fmt:message key="entity.invoice.finishDate" /></th>
		<td>${form.finishDate}	</td>
	</tr>
	<tr>
		<th><fmt:message key="entity.invoice.name" /></th>
		<td>${form.name}</td>
	</tr>
</table>

<h3><fmt:message key="invoice.batch.generate.select.employees" /></h3>

<form action="${requestURI}" method="post" >
	<display:table name="employees" requestURI="${requestURI}" export="false" id="employee">
		<display:column title="<input type='checkbox' name='selectAll' onChange='checkAll(this)' checked='true' />">
			<input type="checkbox" name="confirmedItems" value="${employee.pk}" checked="true" onChange="" />
		</display:column>		
		
		<!-- data columns -->
		<display:column titleKey="entity.employee.icompany" media="html" >
			<!-- this is hack for sorting -->
			<span class="invisible">${employee.icompany.code}</span>
			<span title="${employee.icompany.codeAndName}">${employee.icompany.codeAndName}</span>
		</display:column>
		<display:column property="employee.icompany" titleKey="entity.employee.icompany" media="csv excel xml pdf rtf"/>

		<display:column property="user.contactInfo.displayName" titleKey="entity.contactInfo.displayName" sortable="true" 	/>

	</display:table>
	
	<br />
	
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="_cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="_target0" class="button" value="<fmt:message key="button.back"/>" />
				<input type="submit" name="_finish" class="button" value="<fmt:message key="button.finish"/>" />
			</td>
		</tr>
	</table>
	
</form>
