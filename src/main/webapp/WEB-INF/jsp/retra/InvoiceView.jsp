<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />

<form action="${requestURI}" method="post" >
	<table class="formTable">
		<tr>
			<th><fmt:message key="entity.invoice.code" /></th>
			<td>${invoice.code}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.invoice.orderDate" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.DATE_FORMAT}" value="${invoice.orderDate}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="entity.invoice.finishDate" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.DATE_FORMAT}" value="${invoice.finishDate}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="entity.invoice.employee" /></th>
			<td>${invoice.employee.user.contactInfo.displayName}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.invoice.name" /></th>
			<td>${invoice.name}</td>
		</tr>
		<tr>
			<th><fmt:message key="entity.invoice.state" /></th>
			<td><img src="${imgRoot}/<fmt:message key='invoice.state.img.${invoice.state}' />" alt="<fmt:message key='invoice.state.${invoice.state}' />" title="<fmt:message key='invoice.state.${invoice.state}' />" align="absmiddle" /></td>
		</tr>
	</table>

	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" />
    			
    			<c:if test="${securityContext.loggedEmployee.pk == invoice.employee.pk}">
				<c:choose>
					<c:when test="${invoice.state != ENTITY_STATE_DELETED && invoice.state == ENTITY_STATE_CLOSED}">
						<input type="submit" name="open" class="button" value="<fmt:message key="action.open"/>" onclick="JavaScript: {location.href='InvoiceOpen.do?fkprm=true&pk=${invoice.pk}'; return false;}"/>
					</c:when>
					<c:when test="${invoice.state != ENTITY_STATE_DELETED && invoice.state == ENTITY_STATE_ACTIVE}">
						<input type="submit" name="close" class="button" value="<fmt:message key="action.close"/>" onclick="JavaScript: {location.href='InvoiceClose.do?fkprm=true&pk=${invoice.pk}'; return false;}"/>
					</c:when>
				</c:choose>
				</c:if>
				
			</td>
		</tr>
	</table>

	<br />
	
	<h2><fmt:message key="invoice.paired.worklog.items" /></h2>

	<c:set var="requestURIDT" value="InvoiceView.do?fkprm=true&pk=${invoice.pk}" />
			
	<display:table name="worklogs" requestURI="${requestURIDT}" export="true" id="worklog" varTotals="worklogTotals">
		<!-- set names of files (do not want default names) -->
		<display:setProperty name="export.csv.filename" value="${invoice.code}-worklog.csv" />
		<display:setProperty name="export.excel.filename" value="${invoice.code}-worklog.xls" />
		<display:setProperty name="export.xml.filename" value="${invoice.code}-worklog.xml" />
		<display:setProperty name="export.pdf.filename" value="${invoice.code}-worklog.pdf" />
		<display:setProperty name="export.rtf.filename" value="${invoice.code}-worklog.rtf" />
		
		<!-- data columns -->
		<display:column property="workFrom" titleKey="worklog.date" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />
	
		<display:column titleKey="worklog.project" media="html" >
			<!-- this is hack for sorting -->
			<span class="invisible">${worklog.project.code}</span>
			<acronym title="${worklog.project.name}">${worklog.project.code}</acronym>
		</display:column>
		<display:column property="project.code" titleKey="worklog.project" media="csv excel xml pdf rtf"/>
	
		<display:column titleKey="worklog.component" media="html">
			<!-- this is hack for sorting -->
			<span class="invisible">${worklog.component.code}</span>
			<acronym title="${worklog.component.name}">${worklog.component.code}</acronym>
		</display:column>
		<display:column property="component.code" titleKey="worklog.component" media="csv excel xml pdf rtf"/>
		
		<display:column titleKey="worklog.activity" media="html">
			<!-- this is hack for sorting -->
			<span class="invisible">${worklog.activity.code}</span>
			<acronym title="${worklog.activity.name}">${worklog.activity.code}</acronym>
		</display:column>
		<display:column property="activity.code" titleKey="worklog.activity" media="csv excel xml pdf rtf"/>

		<display:column property="descriptionGui" titleKey="worklog.description" media="html"/>	
		<display:column property="description" titleKey="worklog.description" media="csv excel xml pdf rtf"/>

		<display:column property="workFrom" titleKey="worklog.workFrom" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
		<display:column property="workTo" titleKey="worklog.workTo" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
		<display:column property="hours" titleKey="worklog.hours" format="{0,number,#,##0.00}" total="true"/>

		<display:column title="" url="/InvoiceWorklogUnpair.do?fkprm=true&ipk=${invoice.pk}" paramId="wpk" paramProperty="pk" media="html" class="action" headerClass="action" >
		    <c:if test="${securityContext.loggedEmployee.pk == worklog.employee.pk && invoice.state == ENTITY_STATE_ACTIVE}">
		    	<span onclick="JavaScript: {location.replace('InvoiceWorklogUnpair.do?fkprm=true&ipk=${invoice.pk}&amp;wpk=${worklog.pk}'); return false;}"><fmt:message key="action.unpair"/></span>
		    </c:if>
	    </display:column>
		
		<display:footer media="html">
			<tr>
				<th><fmt:message key="worklog.total"/></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th><strong><fmt:formatNumber value="${worklogTotals.column8}" type="number" maxFractionDigits="2" minFractionDigits="2"/></strong></th>
				<th></th>
			</tr>
		</display:footer>
		<display:footer media="pdf rtf">
			<fmt:message key="worklog.total" />:<fmt:formatNumber value="${worklogTotals.column7}" type="number" maxFractionDigits="2" minFractionDigits="2"/>
		</display:footer>

	</display:table>
	
	<br />
	
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" />

    			<c:if test="${securityContext.loggedEmployee.pk == invoice.employee.pk}">
				<c:choose>
					<c:when test="${invoice.state != ENTITY_STATE_DELETED && invoice.state == ENTITY_STATE_CLOSED}">
						<input type="submit" name="open" class="button" value="<fmt:message key="action.open"/>" onclick="JavaScript: {location.href='InvoiceOpen.do?fkprm=true&pk=${invoice.pk}'; return false;}"/>
					</c:when>
					<c:when test="${invoice.state != ENTITY_STATE_DELETED && invoice.state == ENTITY_STATE_ACTIVE}">
						<input type="submit" name="close" class="button" value="<fmt:message key="action.close"/>" onclick="JavaScript: {location.href='InvoiceClose.do?fkprm=true&pk=${invoice.pk}'; return false;}"/>
					</c:when>
				</c:choose>
				</c:if>
			</td>
		</tr>
	</table>
</form>
