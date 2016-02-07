<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<script src="${commonJsRoot}/componentselector.js" type="text/javascript"></script>

<c:set var="requestURI" value="${requestURI}" />

<!-- TODO sigl: this is only temporary solution -->
<jsp:include page="WorkWithHistoryData.jsp?requestURI=${requestURI}&worklogPk=${param.worklogPk}" />

<form action="${requestURI}" method="post" >
	<spring:bind path="worklogForm.pk">
		<td><input type="hidden" name="${status.expression}" value="${status.value}" /></td>
	</spring:bind>
	
	<table class="formTable">
		<tr>
			<spring:bind path="worklogForm.date">
				<th><fmt:message key="worklog.date" /></th>
				<td colspan="2">
					<input type="text" name="${status.expression}" value="${status.value}"  id="worklogDateId"/><%--
				--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogDateImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForDate('worklogDateId','worklogDateImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="worklogForm.project">
				<th><fmt:message key="worklog.project" /></th>
				<td colspan="2">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="worklogForm.component">
				<th><fmt:message key="worklog.component" /></th>
				<td colspan="2">
					<vc:select name="${status.expression}" valueObjects="${components}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName"
					>
						<vc:select-option value=""><fmt:message key="worklog.component.unassigned" /></vc:select-option>
					</vc:select>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="worklogForm.activity">
				<th><fmt:message key="worklog.activity" /></th>
				<td colspan="2">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="worklogForm.invoice">
				<th><fmt:message key="worklog.invoice" /></th>
				<td colspan="2">
					<vc:select name="${status.expression}" valueObjects="${invoices}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName"
					>
						<vc:select-option value=""><fmt:message key="worklog.invoice.unassigned" /></vc:select-option>
					</vc:select>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="worklogForm.workFrom">
				<th><fmt:message key="worklog.workFrom" /></th>
				<td colspan="2">
					<input type="text" name="${status.expression}" value="${status.value}" id="worklogWorkFromId"/><%--
				--%><img src="${imgRoot}/hoursIco.gif" alt="<fmt:message key='hours.label' />" title="<fmt:message key='hours.label' />" align="top" id="worklogWorkFromImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForTime('worklogWorkFromId','worklogWorkFromImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="worklogForm.workTo">
				<th><fmt:message key="worklog.workTo" /></th>
				<td colspan="2">
					<input type="text" name="${status.expression}" value="${status.value}" id="worklogWorkToId" /><%--
				--%><img src="${imgRoot}/hoursIco.gif" alt="<fmt:message key='hours.label' />" title="<fmt:message key='hours.label' />" align="top" id="worklogWorkToImgId"/>
					<script type="text/javascript">
					<!--
						showMiraCalendarForTime('worklogWorkToId','worklogWorkToImgId');
					// -->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="worklogForm.description">
				<th><fmt:message key="worklog.description" /></th>
				<td width="300">
					<textarea id="descriptionId" name="${status.expression}" cols="40" rows="7" onkeypress="return imposeMaxLength(event, this, 249);">${status.value}</textarea>
					<script language="javascript" type="text/javascript">
					<!--
					function imposeMaxLength(Event, Object, MaxLen)
					{
					        return (Object.value.length <= MaxLen)||(Event.keyCode == 8||Event.keyCode==46||(Event.keyCode>=35&&Event.keyCode<=40))
					}
					//-->
					</script>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
			<td>
				<c:if test="${!empty issues}" >
				<div style="max-height:100px; overflow:auto;">
					<c:forEach items="${issues}" var="issue" varStatus="count">
						<a href="" title="Copy to description" onclick="return copyTextIntoTextArea('${issue.key}', 'descriptionId')">+&nbsp;${issue.key}</a> - ${issue.summary} ${issue.guiLink}<br />
					</c:forEach>
				</div>
				</c:if>
			</td>
		</tr>
		<%-- deprecated so not used --%>
		<c:if test="${jiraIntegrationEnabled}" >
		<tr>
			<spring:bind path="worklogForm.issueTrackingReference">
				<td><fmt:message key="worklog.jiraIssue" /></td>
				<td colspan="2">
					<input type="text" name="${status.expression}" value="${status.value}" id="jiraissue" maxlength="50"/>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		</c:if>
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
