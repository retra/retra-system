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
			<th><fmt:message key="worklog.date" /></th>
			<td>
				<input type="text" name="worklogFilterDate" value="${worklogFilterDate}" tabindex="1" id="worklogFilterDateId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogFilterDateImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('worklogFilterDateId','worklogFilterDateImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="worklog.employee" /></th>
			<td>
				<vc:select name="worklogFilterEmployee" valueObjects="${employees}" selected="${worklogFilterEmployee}" 
				valueProperty="pk" labelProperty="user.contactInfo.displayName" orderBy="user.contactInfo.displayName"
				tabindex="3" /> 
			</td>
			<td class="buttons"><input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="6"/></td>
		</tr>
	</table>
	</div>
</form>

<div class="nextPrevious invisibleInPrint">
	[ 
	<a href="${requestURI}&${parametersPrevious}">
		Previous day (${previousDate})
	</a>
	]
	&nbsp; &nbsp; &nbsp; &nbsp;
	[
	<a href="${requestURI}&${parametersNext}">
		Next day (${nextDate})
	</a>
	]
</div>

<table id="worklog" class="listTable">
	<caption>Daily worklog for ${employee.user.contactInfo.displayName}, ${worklogFilterDate} (<fmt:message key="day.${dayOfWeek}"/>&nbsp;-&nbsp;<span class="${dayOfWeekCss}" title="<fmt:message key="${dayOfWeekTitle}"/>">&nbsp;&nbsp;&nbsp;</span>)</caption>
	<thead>
		<tr>
			<th><fmt:message key="worklog.date"/></th>
			<th><fmt:message key="worklog.workFrom"/></th>
			<th><fmt:message key="worklog.workTo"/></th>
			<th><fmt:message key="worklog.description"/></th>
			<th><fmt:message key="worklog.project"/></th>
			<th><fmt:message key="worklog.component"/></th>
			<th><fmt:message key="worklog.activity"/></th>
			<th><fmt:message key="worklog.invoice"/></th>
			<th><fmt:message key="worklog.hours"/></th>
			<th><c:if test="${securityContext.loggedEmployee.pk == employee.pk}"><fmt:message key="worklog.create..daily.action"/></c:if>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${worklogs}" var="worklog">
			<c:choose>
				<c:when test="${empty lastWorklog}">
					<c:if test="${securityContext.loggedEmployee.pk == employee.pk}">
						<tr class="even"><td colspan="10">
							<a href="WorklogDailyCreate.do?fkprm=true&setDate=<fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.DATE_FORMAT}"/>&setWorkTo=<fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.HOUR_FORMAT}"/>">
							Insert new worklog item - before <fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.HOUR_FORMAT}"/></a>
						</td></tr>
					</c:if>
				</c:when>
				<c:otherwise>
					<c:if test="${lastWorklog.workTo.time != worklog.workFrom.time}">
						<tr class="even"><td colspan="10">
							<c:choose>
								<c:when test="${securityContext.loggedEmployee.pk == employee.pk}">
									<a href="WorklogDailyCreate.do?fkprm=true&setDate=<fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.DATE_FORMAT}"/>&setWorkFrom=<fmt:formatDate value="${lastWorklog.workTo}" pattern="${TypeFormats.HOUR_FORMAT}"/>&setWorkTo=<fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.HOUR_FORMAT}"/>">
									Insert new worklog item - between
									<fmt:formatDate value="${lastWorklog.workTo}" pattern="${TypeFormats.HOUR_FORMAT}"/> and
									<fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.HOUR_FORMAT}"/></a>
								</c:when>
								<c:otherwise>
									Some break between
									<fmt:formatDate value="${lastWorklog.workTo}" pattern="${TypeFormats.HOUR_FORMAT}"/> and
									<fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.HOUR_FORMAT}"/>
								</c:otherwise>
							</c:choose>
						</td></tr>
					</c:if>
				</c:otherwise>
			</c:choose>
			<tr class="odd">
				<td><fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.DATE_FORMAT}"/></td>
				<td><fmt:formatDate value="${worklog.workFrom}" pattern="${TypeFormats.HOUR_FORMAT}"/></td>
				<td><fmt:formatDate value="${worklog.workTo}" pattern="${TypeFormats.HOUR_FORMAT}"/></td>
				<td>${worklog.description}</td>
				<td><acronym title="${worklog.project.name}">${worklog.project.code}</acronym></td>
				<td><acronym title="${worklog.component.name}">${worklog.component.code}</acronym></td>
				<td><acronym title="${worklog.activity.name}">${worklog.activity.code}</acronym></td>
				<td class="nowrap"><acronym title="${worklog.invoice.name}">${worklog.invoice.code}</acronym><c:if test="${worklog.invoice != null}">&nbsp;-&nbsp;<img src="${imgRoot}/<fmt:message key='invoice.state.img.${worklog.invoice.state}' />" alt="<fmt:message key='invoice.state.${worklog.invoice.state}' />" title="<fmt:message key='invoice.state.${worklog.invoice.state}' />" align="absmiddle"/></c:if></td>
				<td><fmt:formatNumber value="${worklog.hours}" maxFractionDigits="2" minFractionDigits="2"/></td>
				<td class="actions">
					[ 
						<a href="WorklogDailyView.do?fkprm=true&worklogPk=${worklog.pk}"><fmt:message key="action.view"/></a>
<c:if test="${securityContext.loggedEmployee.pk == employee.pk && worklog.editableAccordingToInvoice}">
					|
						<a href="WorklogDailyEdit.do?fkprm=true&worklogPk=${worklog.pk}"><fmt:message key="action.edit"/></a>
					|
						<a href="WorklogDailyDelete.do?fkprm=true&worklogPk=${worklog.pk}"><span onclick="JavaScript: {return deleteConfirm('<fmt:message key="delete.confirmation" />')}"><fmt:message key="action.delete"/></span></a>
</c:if>
					]
				</td>
			</tr>
			<c:set var="lastWorklog" value="${worklog}" />
		</c:forEach>
		<c:choose>
			<c:when test="${empty lastWorklog}">
				<tr class="even"><td colspan="10">
					<c:choose>
						<c:when test="${securityContext.loggedEmployee.pk == employee.pk}">
							<a href="WorklogDailyCreate.do?fkprm=true&setDate=${worklogFilterDate}">Insert new worklog item</a>
						</c:when>
						<c:otherwise>
							Nothing found to display.
						</c:otherwise>
					</c:choose>
				</td></tr>
			</c:when>
			<c:otherwise>
				<c:if test="${securityContext.loggedEmployee.pk == employee.pk}">
					<tr class="even"><td colspan="10">
						<a href="WorklogDailyCreate.do?fkprm=true&setDate=<fmt:formatDate value="${lastWorklog.workFrom}" pattern="${TypeFormats.DATE_FORMAT}"/>&setWorkFrom=<fmt:formatDate value="${lastWorklog.workTo}" pattern="${TypeFormats.HOUR_FORMAT}"/>">
						Insert new worklog item - after <fmt:formatDate value="${lastWorklog.workTo}" pattern="${TypeFormats.HOUR_FORMAT}"/></a>
					</td></tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<tr>
			<th><fmt:message key="worklog.total"/></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th><fmt:formatNumber value="${grouppedWorklog.map[false][false].sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
			<th></th>
		</tr>
	</tbody>
</table>

<c:set var="PROJECT" value="${0}" />
<c:set var="ACTIVITY" value="${1}" />

<c:set var="projectProjection" value="${grouppedWorklog.projectionFor[PROJECT]}" />
<c:set var="projectProjectionList" value="${projectProjection.list}" />

<c:set var="activityProjection" value="${grouppedWorklog.projectionFor[ACTIVITY]}" />
<c:set var="activityProjectionList" value="${activityProjection.list}" />

<h2>Project and Activity Summary</h2>
<table class="groupTable">
	<tr>
		<th>Project / Activity</th>
		<c:forEach items="${activityProjectionList}" var="activity">
			<th>${activity.key.code}</th>
		</c:forEach>
		<th><fmt:message key="worklog.total"/></th>
	</tr>
	<c:forEach items="${projectProjectionList}" var="project">
		<tr class="odd">
			<th>${project.key.code}</th>
			<c:forEach items="${activityProjectionList}" var="activity">
				<td style='text-align:right;'>
					<fmt:formatNumber value="${grouppedWorklog.map[project.key][activity.key].sum}" maxFractionDigits="2" minFractionDigits="2" />
				</td>
			</c:forEach>
			<th style='text-align:right;'><fmt:formatNumber value="${project.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
		</tr>
	</c:forEach>
	<tr>
		<th><fmt:message key="worklog.total"/></th>
		<c:forEach items="${activityProjectionList}" var="activity">
			<th style='text-align:right;'><fmt:formatNumber value="${activity.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
		</c:forEach>
		<th style='text-align:right;'><fmt:formatNumber value="${grouppedWorklog.map[false][false].sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
	</tr>
</table>
