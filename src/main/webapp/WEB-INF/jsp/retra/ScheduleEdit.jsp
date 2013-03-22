<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="ScheduleEdit.do?fkprm=true" />

<form action="${requestURI}" method="post" >
	<spring:bind path="scheduleForm.pk">
		<td><input type="hidden" name="${status.expression}" value="${status.value}" /></td>
	</spring:bind>
	
	<table class="formTable">
		<tr>
			<spring:bind path="scheduleForm.date">
				<th><fmt:message key="schedule.date" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" id="scheduleDateId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="scheduleDateImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('scheduleDateId','scheduleDateImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="scheduleForm.type">
				<th><fmt:message key="schedule.type" /></th>
				<td>
					<vc:select name="${status.expression}" valueObjects="${types}" selected="${status.value}"
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="scheduleForm.workFrom">
				<th><fmt:message key="schedule.workFrom" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" id="scheduleWorkFromId"/><%--
				--%><img src="${imgRoot}/hoursIco.gif" alt="<fmt:message key='hours.label' />" title="<fmt:message key='hours.label' />" align="top" id="scheduleWorkFromImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForTime('scheduleWorkFromId','scheduleWorkFromImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="scheduleForm.workTo">
				<th><fmt:message key="schedule.workTo" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" id="scheduleWorkToId"/><%--
				--%><img src="${imgRoot}/hoursIco.gif" alt="<fmt:message key='hours.label' />" title="<fmt:message key='hours.label' />" align="top" id="scheduleWorkToImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForTime('scheduleWorkToId','scheduleWorkToImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="scheduleForm.comment">
				<th><fmt:message key="schedule.comment" /></th>
				<td>
					<textarea name="${status.expression}" cols="40" rows="5" >${status.value}</textarea>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
	</table>
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input class="invisible" type="submit" name="save" value="firstSaveNotVissible" />
				<input type="submit" name="cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="save" class="button" value="<fmt:message key="button.save"/>" />
			</td>
		</tr>
	</table>
</form>
