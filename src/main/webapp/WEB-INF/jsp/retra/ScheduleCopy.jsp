<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="ScheduleCopy.do?fkprm=true" />

<form action="${requestURI}" method="post" >
	<spring:bind path="scheduleForm.pk">
		<td><input type="hidden" name="${status.expression}" value="${fn:escapeXml(status.value)}" /></td>
	</spring:bind>
	
	<table class="formTable">
		<tr>
			<spring:bind path="scheduleForm.date">
				<th><fmt:message key="schedule.copyFrom" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" id="scheduleCopyFromId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="scheduleCopyFromImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('scheduleCopyFromId','scheduleCopyFromImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="scheduleForm.copyDestinationFrom">
				<th><fmt:message key="scheduleForm.copyDestinationFrom" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" id="scheduleCopyDestinationFromId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="scheduleCopyDestinationFromImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('scheduleCopyDestinationFromId','scheduleCopyDestinationFromImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="scheduleForm.copyDestinationTo">
				<th><fmt:message key="scheduleForm.copyDestinationTo" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" id="scheduleCopyDestinationToId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="scheduleCopyDestinationToImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('scheduleCopyDestinationToId','scheduleCopyDestinationToImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="scheduleForm.copyType">
				<th><fmt:message key="schedule.copyType" /></th>
				<td>
					<c:forEach items="${scheduleCopyTypes}" var="copyType">
						<c:choose>
							<c:when test="${status.value == copyType.value}">
								<input type="radio" name="${status.expression}" value="${copyType.value}" checked="checked"/>
							</c:when>
							<c:otherwise>
								<input type="radio" name="${status.expression}" value="${copyType.value}"/>
							</c:otherwise>
						</c:choose>
						<fmt:message key="${copyType.key}" />
						<c:forEach items="${status.errorMessages}">
							<span class="error"><fmt:message key="error.sign" /></span>
						</c:forEach>
						<br />
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
	</table>
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input class="invisible" type="submit" name="save" class="button" value="firstSaveNotVissible" />
				<input type="submit" name="cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="save" class="button" value="<fmt:message key="button.copy"/>" />
				<a class="button" href="HelpCopySchedule.do?fkprm=true" title="<fmt:message key="schedule.help.copy"/>"><fmt:message key="schedule.help.copy"/></a>
			</td>
		</tr>
	</table>
</form>
