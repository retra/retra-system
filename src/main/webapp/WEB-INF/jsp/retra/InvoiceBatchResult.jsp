<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />

<form action="${requestURI}" method="post" >

	<%-- data --%>
	<display:table name="invoices" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" export="true" id="invoice" 
		sort="list">
		<!-- set names of files (do not want default names) -->
		<display:setProperty name="export.csv.filename" value="invoices.csv" />
		<display:setProperty name="export.excel.filename" value="invoices.xls" />
		<display:setProperty name="export.xml.filename" value="invoices.xml" />
		<display:setProperty name="export.pdf.filename" value="invoices.pdf" />
		<display:setProperty name="export.rtf.filename" value="invoices.rtf" />

		<display:column property="employee.icompany.codeAndName" titleKey="entity.employee.icompany" sortable="true" escapeXml="true" />		
		<display:column property="code" titleKey="entity.invoice.code" sortable="true" maxLength="10" escapeXml="true" /> 
		<display:column property="employee.user.contactInfo.displayName" titleKey="entity.invoice.employee" escapeXml="true" />
		<display:column property="name" titleKey="entity.invoice.name" sortable="true" escapeXml="true" />
		
	</display:table>

	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" onclick="JavaScript: {location.href='InvoiceList.do?fkprm=true'; return false;}"/>
			</td>
		</tr>
	</table>
	
</form>
