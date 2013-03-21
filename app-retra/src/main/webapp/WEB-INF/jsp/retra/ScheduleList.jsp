<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="ScheduleList.do?fkprm=true" />

<%-- filter --%>
<form action="${requestURI}" method="post" >
	<div class="filter">
	<table class="filterTable">
		<tr>
			<th><fmt:message key="schedule.workFrom" /></th>
			<td>
				<input type="text" name="scheduleFilterFrom" value="${scheduleFilterFrom}" tabindex="1" id="scheduleFilterWorkFromId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="scheduleFilterWorkFromImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('scheduleFilterWorkFromId','scheduleFilterWorkFromImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="schedule.employee" /></th>
			<td>
				<vc:select name="scheduleFilterEmployee" valueObjects="${employees}" selected="${scheduleFilterEmployee}" 
				valueProperty="pk" labelProperty="user.contactInfo.displayName" orderBy="user.contactInfo.displayName"
				tabindex="3" /> 
			</td>
			<td rowspan="3" class="buttons"><input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="6"/></td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.workTo" /></th>
			<td>
				<input type="text" name="scheduleFilterTo" value="${scheduleFilterTo}" tabindex="2" id="scheduleFilterWorkToId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="scheduleFilterWorkToImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('scheduleFilterWorkToId','scheduleFilterWorkToImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="schedule.type" /></th>
			<td>
				<vc:select name="scheduleFilterType" valueObjects="${types}" selected="${scheduleFilterType}"
				valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName"
				tabindex="4">
					<vc:select-option value="">--- <fmt:message key="schedule.allTypes" /> ---</vc:select-option>
				</vc:select>
			</td>
		</tr>
		<tr>
			<td colspan="2"></td>
			<th><fmt:message key="schedule.state" /></th>
			<td>
				<vc:select name="scheduleFilterState" valueObjects="${scheduleStates}" selected="${scheduleFilterState}"
				valueProperty="value" labelProperty="label" orderBy="label"
				tabindex="5">
					<vc:select-option value="">--- <fmt:message key="schedule.allStates" /> ---</vc:select-option>
				</vc:select>
			</td>
		</tr>
	</table>
	</div>
</form>

<%-- data --%>
<display:table name="schedules" requestURI="${requestURI}" defaultsort="1" pagesize="${defaultPageSize}" export="true" 
	id="schedule" sort="list">
	<!-- set names of files (do not want default names) -->
	<display:setProperty name="export.csv.filename" value="${employee.user.contactInfo.displayName}-schedule.csv" />
	<display:setProperty name="export.excel.filename" value="${employee.user.contactInfo.displayName}-schedule.xls" />
	<display:setProperty name="export.xml.filename" value="${employee.user.contactInfo.displayName}-schedule.xml" />
	<display:setProperty name="export.pdf.filename" value="${employee.user.contactInfo.displayName}-schedule.pdf" />
	<display:setProperty name="export.rtf.filename" value="${employee.user.contactInfo.displayName}-schedule.rtf" />
	
	<display:caption>${employee.user.contactInfo.displayName}</display:caption>
	
	<!-- data columns -->
	<display:column property="workFrom" titleKey="schedule.date" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />
	<display:column property="workFrom" titleKey="schedule.workFrom" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
	<display:column property="workTo" titleKey="schedule.workTo" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>

	<display:column titleKey="schedule.type" sortable="true" media="html">
		<!-- this is hack for sorting -->
		<span class="invisible">${schedule.type.code}</span>
		<span title="${schedule.type.name}">${schedule.type.code}</span>
	</display:column>
	<display:column property="type.code" titleKey="schedule.type" sortable="true" media="csv excel xml pdf rtf"/>

	<display:column titleKey="schedule.state" sortable="true">
		<fmt:message key="schedule.state.${schedule.state}"/>
	</display:column>

	<display:column property="createdOn" titleKey="schedule.createdOn" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />
	<display:column property="changedOn" titleKey="schedule.changedOn" sortable="true" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />

	<!-- action columns -only for html (parameter media) -->
	<display:column title="" url="/ScheduleView.do?fkprm=true" paramId="schedulePk" paramProperty="pk" media="html" class="action" headerClass="action">
	    <fmt:message key="action.view"/>
    </display:column>

    <c:if test="${securityContext.loggedEmployee.pk == employee.pk}">

		<display:column titleKey="schedule.create.action" url="/ScheduleEdit.do?fkprm=true" paramId="schedulePk" paramProperty="pk" media="html" class="action"  headerClass="action">
    		<fmt:message key="action.edit"/>
	    </display:column>
		<display:column title="" url="/ScheduleDelete.do?fkprm=true" paramId="schedulePk" paramProperty="pk" media="html" class="action" headerClass="action" >
	    	<span onclick="JavaScript: {return deleteConfirm('<fmt:message key="delete.confirmation" />')}"><fmt:message key="action.delete"/></span>
	    </display:column>
		<display:column title="" url="/ScheduleCopy.do?fkprm=true" paramId="schedulePk" paramProperty="pk" media="html" class="action" headerClass="action">
		    <fmt:message key="action.copy"/>
	    </display:column>
    </c:if>

</display:table>