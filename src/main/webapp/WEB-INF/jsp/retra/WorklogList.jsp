<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="WorklogList.do?fkprm=true" />

<!-- TODO sigl: this is only temporary solution -->
<jsp:include page="WorkWithHistoryData.jsp?requestURI=${requestURI}" />

<%-- filter --%>
<form action="${requestURI}" method="post" >
	<div class="filter">
	<table class="filterTable">
		<tr>
			<th><fmt:message key="worklog.workFrom" /></th>
			<td>
				<input type="text" name="worklogFilterFrom" value="${worklogFilterFrom}" tabindex="1" id="worklogFilterWorkFromId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogFilterWorkFromImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('worklogFilterWorkFromId','worklogFilterWorkFromImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="worklog.employee" /></th>
			<td>
				<vc:select name="worklogFilterEmployee" valueObjects="${employees}" selected="${worklogFilterEmployee}" 
				valueProperty="pk" labelProperty="user.contactInfo.displayName" orderBy="user.contactInfo.displayName"
				tabindex="3" /> 
			</td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.workTo" /></th>
			<td>
				<input type="text" name="worklogFilterTo" value="${worklogFilterTo}" tabindex="2" id="worklogFilterWorkToId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogFilterWorkToImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('worklogFilterWorkToId','worklogFilterWorkToImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="worklog.project" /></th>
			<td>
				<vc:select name="worklogFilterProject" valueObjects="${projects}" selected="${worklogFilterProject}" 
				valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"
				tabindex="4">
					<vc:select-option value="">--- <fmt:message key="worklog.allProjects" /> ---</vc:select-option>
				</vc:select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td colspan="2"></td>
			<th><fmt:message key="worklog.activity" /></th>
			<td>
				<vc:select name="worklogFilterActivity" valueObjects="${activities}" selected="${worklogFilterActivity}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
					<vc:select-option value="">--- <fmt:message key="worklog.allActivities" /> ---</vc:select-option>
				</vc:select> <!-- tabindex="5" -->
			</td>
			<td></td>
		</tr>
		<tr>
		<td colspan="4"></td>
		<td class="buttons right"><input class="button" type="submit" name="filter" value="<fmt:message key="button.filter"/>" tabindex="6"/><td>
		</tr>
	</table>
	</div>
</form>

<%-- data --%>
<display:table name="worklogs" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" export="true" id="worklog" 
	sort="list">
	<!-- set names of files (do not want default names) -->
	<display:setProperty name="export.csv.filename" value="${employee.user.contactInfo.displayName}-worklog.csv" />
	<display:setProperty name="export.excel.filename" value="${employee.user.contactInfo.displayName}-worklog.xls" />
	<display:setProperty name="export.xml.filename" value="${employee.user.contactInfo.displayName}-worklog.xml" />
	<display:setProperty name="export.pdf.filename" value="${employee.user.contactInfo.displayName}-worklog.pdf" />
	<display:setProperty name="export.rtf.filename" value="${employee.user.contactInfo.displayName}-worklog.rtf" />
	
	<display:caption>${employee.user.contactInfo.displayName}</display:caption>
	
	<!-- data columns -->
	<display:column property="workFrom" titleKey="worklog.date" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />

	<display:column titleKey="worklog.project" sortable="true" media="html" >
		<!-- this is hack for sorting -->
		<span class="invisible">${worklog.project.code}</span>
		<span title="${worklog.project.name}">${worklog.project.code}</span>
	</display:column>
	<display:column property="project.code" titleKey="worklog.project" sortable="true" media="csv excel xml pdf rtf"/>

	<display:column titleKey="worklog.activity" sortable="true" media="html">
		<!-- this is hack for sorting -->
		<span class="invisible">${worklog.activity.code}</span>
		<span title="${worklog.activity.name}">${worklog.activity.code}</span>
	</display:column>
	<display:column property="activity.code" titleKey="worklog.activity" sortable="true" media="csv excel xml pdf rtf"/>

	<display:column property="description" titleKey="worklog.description" sortable="true"/>
	<display:column property="workFrom" titleKey="worklog.workFrom" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
	<display:column property="workTo" titleKey="worklog.workTo" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
	<display:column property="hours" titleKey="worklog.hours" sortable="true" format="{0,number,#,##0.00}"/>

	<!-- action columns -only for html (parameter media) -->
	<display:column title="" url="/WorklogView.do?fkprm=true" paramId="worklogPk" paramProperty="pk" media="html" class="action" headerClass="action">
	    <fmt:message key="action.view"/>
    </display:column>

    <c:if test="${securityContext.loggedEmployee.pk == employee.pk}">

		<display:column titleKey="worklog.create.action" url="/WorklogEdit.do?fkprm=true" paramId="worklogPk" paramProperty="pk" media="html" class="action"  headerClass="action">
    		<fmt:message key="action.edit"/>
	    </display:column>
		<display:column title="" url="/WorklogDelete.do?fkprm=true" paramId="worklogPk" paramProperty="pk" media="html" class="action" headerClass="action" >
	    	<span onclick="JavaScript: {return deleteConfirm('<fmt:message key="delete.confirmation" />')}"><fmt:message key="action.delete"/></span>
	    </display:column>

    </c:if>

</display:table>
