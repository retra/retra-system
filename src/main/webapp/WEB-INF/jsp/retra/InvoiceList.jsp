<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />

<!-- TODO sigl: this is only temporary solution -->
<jsp:include page="WorkWithHistoryData.jsp?requestURI=${requestURI}" />

<%-- filter --%>
<form action="${requestURI}" method="post" >
	<div class="filter">
	<table class="filterTable">
		<tr>
			<th><fmt:message key="entity.invoice.orderDateFrom" /></th>
			<td>
				<input type="text" name="invoiceFilterOrderDateFrom" value="${fn:escapeXml(invoiceFilterOrderDateFrom)}" tabindex="1" id="invoiceFilterOrderDateFromId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="invoiceFilterOrderDateFromImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('invoiceFilterOrderDateFromId','invoiceFilterOrderDateFromImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="entity.invoice.employee" /></th>
			<td>
				<vc:select name="invoiceFilterEmployee" valueObjects="${employees}" selected="${fn:escapeXml(invoiceFilterEmployee)}" 
				valueProperty="pk" labelProperty="user.contactInfo.displayName" orderBy="user.contactInfo.displayName"
				tabindex="2">
					<vc:select-option value="-1">--- <fmt:message key="invoice.allEmployees" /> ---</vc:select-option>
				</vc:select> 
			</td>
			<td rowspan="5" class="buttons">
				<input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="6"/>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.invoice.orderDateTo" /></th>
			<td>
				<input type="text" name="invoiceFilterOrderDateTo" value="${fn:escapeXml(invoiceFilterOrderDateTo)}" tabindex="1" id="invoiceFilterOrderDateToId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="invoiceFilterOrderDateToImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('invoiceFilterOrderDateToId','invoiceFilterOrderDateToImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="entity.invoice.state" /></th>
			<td>
				<select name="invoiceFilterState">
					<option value="-1">--- <fmt:message key="state.all" /> ---</option>
					<c:forEach items="${states}" var="state">
						<c:choose>
							<c:when test="${invoiceFilterState != null && invoiceFilterState != '' && state.code == invoiceFilterState}">
								<option value="${state.code}" selected="selected"><fmt:message key="invoice.state.${state.code}" /></option>
							</c:when>
							<c:otherwise>
								<option value="${state.code}"><fmt:message key="invoice.state.${state.code}" /></option>
							</c:otherwise>
						</c:choose>
					</c:forEach>				
				</select>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.invoice.finishDateFrom" /></th>
			<td>
				<input type="text" name="invoiceFilterFinishDateFrom" value="${fn:escapeXml(invoiceFilterFinishDateFrom)}" tabindex="1" id="invoiceFilterFinishDateFromId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="invoiceFilterFinishDateFromImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('invoiceFilterFinishDateFromId','invoiceFilterFinishDateFromImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="entity.invoice.code" /></th>
			<td>
				<input type="text" name="invoiceFilterCode" value="${fn:escapeXml(invoiceFilterCode)}" maxlength="30"/>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.invoice.finishDateTo" /></th>
			<td>
				<input type="text" name="invoiceFilterFinishDateTo" value="${fn:escapeXml(invoiceFilterFinishDateTo)}" tabindex="1" id="invoiceFilterFinishDateToId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="invoiceFilterFinishDateToImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('invoiceFilterFinishDateToId','invoiceFilterFinishDateToImgId');
				// -->
				</script>
			</td>
			<th></th>
			<td></td>
		</tr>
	</table>
	</div>
</form>

<%-- data --%>
<display:table name="invoices" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" export="true" id="invoice" 
	sort="list">
	<!-- set names of files (do not want default names) -->
	<display:setProperty name="export.csv.filename" value="${employee.user.contactInfo.displayName}-invoice.csv" />
	<display:setProperty name="export.excel.filename" value="${employee.user.contactInfo.displayName}-invoice.xls" />
	<display:setProperty name="export.xml.filename" value="${employee.user.contactInfo.displayName}-invoice.xml" />
	<display:setProperty name="export.pdf.filename" value="${employee.user.contactInfo.displayName}-invoice.pdf" />
	<display:setProperty name="export.rtf.filename" value="${employee.user.contactInfo.displayName}-invoice.rtf" />
	
	<!-- data columns -->
	<display:column property="orderDate" titleKey="entity.invoice.orderDate" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator"/>
	<display:column property="finishDate" titleKey="entity.invoice.finishDate" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />

	<display:column property="code" titleKey="entity.invoice.code" sortable="true" maxLength="10" escapeXml="true" /> 
	<display:column property="name" titleKey="entity.invoice.name" sortable="true" escapeXml="true" />
	<display:column property="employee.user.contactInfo.displayName" titleKey="entity.invoice.employee" escapeXml="true" />
	<display:column property="employee.user.login.ldapLogin" titleKey="entity.invoice.employee.ldaplogin" media="csv excel xml pdf rtf" escapeXml="true"/>
	
	<display:column property="state" titleKey="entity.invoice.state" media="html" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.StateDecorator" maxLength="5" class="center"/>
	<display:column titleKey="entity.invoice.state" media="csv excel xml pdf rtf">
		<fmt:message key="invoice.state.${invoice.state}" />
	</display:column>

	<!-- action columns -only for html (parameter media) -->
	<display:column url="/InvoiceView.do?fkprm=true" paramId="pk" paramProperty="pk" media="html" class="action" headerClass="action"
		title="">
	    <fmt:message key="action.view"/>
    </display:column>
    
    <c:if test="${securityContext.loggedEmployee.pk == invoice.employee.pk}">

	<display:column media="html" class="action"  headerClass="action" titleKey="invoice.create.action">
		<c:choose>
			<c:when test="${invoice.state != ENTITY_STATE_DELETED}">
				<a href="InvoiceEdit.do?fkprm=true&pk=${invoice.pk}"><fmt:message key="action.edit"/></a>
			</c:when>
			<c:otherwise>
				&nbsp;			
			</c:otherwise>
		</c:choose>
    </display:column>

    <display:column media="html" class="action"  headerClass="action" title="">
		<c:choose>
			<c:when test="${invoice.state != ENTITY_STATE_DELETED && invoice.state == ENTITY_STATE_CLOSED}">
				<a href="InvoiceOpen.do?fkprm=true&pk=${invoice.pk}"><fmt:message key="action.open"/></a>
			</c:when>
			<c:when test="${invoice.state != ENTITY_STATE_DELETED && invoice.state == ENTITY_STATE_ACTIVE}">
				<a href="InvoiceClose.do?fkprm=true&pk=${invoice.pk}"><fmt:message key="action.close"/></a>
			</c:when>
			<c:otherwise>
				&nbsp;			
			</c:otherwise>
		</c:choose>
	</display:column>
    
    <display:column media="html" class="action"  headerClass="action" title="">
		<c:choose>
			<c:when test="${invoice.state != ENTITY_STATE_DELETED}">
				<a href="InvoiceDelete.do?fkprm=true&pk=${invoice.pk}"><span onclick="JavaScript: {return deleteConfirm('<fmt:message key="delete.confirmation" />')}"><fmt:message key="action.delete"/></span></a>
			</c:when>
			<c:otherwise>
				&nbsp;			
			</c:otherwise>
		</c:choose>
	</display:column>

	</c:if>

</display:table>
