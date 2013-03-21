<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="ScheduleList.do?fkprm=true&scheduleFilterEmployee=${schedule.employee.pk}" />

<form action="${requestURI}" method="post" >
	<table class="formTable">
		<tr>
			<th><fmt:message key="schedule.date" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.DATE_FORMAT}" value="${schedule.workFrom}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.type" /></th>
			<td class="${schedule.cssClass}">${schedule.type.code} - ${schedule.type.name}</td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.state" /></th>
			<td><fmt:message key="schedule.state.${schedule.state}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.workFrom" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.HOUR_FORMAT}" value="${schedule.workFrom}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.workTo" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.HOUR_FORMAT}" value="${schedule.workTo}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.comment" /></th>
			<td>${schedule.comment}</td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.createdOn" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.DATE_FORMAT_WITH_HOURS_MINUTES_SECONDS}" value="${schedule.createdOn}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.changedOn" /></th>
			<td><fmt:formatDate pattern="${TypeFormats.DATE_FORMAT_WITH_HOURS_MINUTES_SECONDS}" value="${schedule.changedOn}" /></td>
		</tr>
	</table>
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" />
				<c:if test="${securityContext.loggedEmployee.pk == schedule.employee.pk}">
					<a class="button" href="ScheduleCopy.do?fkprm=true&schedulePk=${schedule.pk}" title="<fmt:message key="schedule.createCopy"/>"><fmt:message key="schedule.createCopy"/></a>
				</c:if>
			</td>
		</tr>
	</table>
</form>
