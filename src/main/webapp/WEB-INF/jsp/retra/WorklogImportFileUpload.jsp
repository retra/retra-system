<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />

<spring:bind path="form.*">
	<c:forEach var="error" items="${status.errorMessages}">
		<div class="errors">${error}</div>
	</c:forEach>
</spring:bind>

<br />

<!-- TODO sigl: this is only temporary solution -->
<jsp:include page="WorkWithHistoryData.jsp?requestURI=${requestURI}" />

<script type="text/javascript">

	function openWindow(url, windowName, height, width){
  		var handler = window.open(url, windowName, 'resizable=no,scrollbars=no, personalbar=no, menubar=no, status=yes, toolbar=no, height=' + height + ', width=' + width + ',left=0, top=0, screenX=0, screenY=0');
  		return handler;
	}
	
</script>

<form action="${requestURI}" method="post" enctype="multipart/form-data">
	<table class="formTable">
		<tr>
			<th><fmt:message key="worklog.importType" /></th>
			<td>
				<select name="importType">
					<option value="TYPE_CSV" title='<fmt:message key="worklog.csv.title" />'><fmt:message key="worklog.csv" /></option>
					<option value="TYPE_TRACKIT" title='<fmt:message key="worklog.trackit.title" />'><fmt:message key="worklog.trackit" /></option>
					<option value="TYPE_OUTLOOK_EXPRESS" title='<fmt:message key="worklog.outlookExpress.title" />'><fmt:message key="worklog.outlookExpress" /></option>
				</select>
			</td>
			<td align="right"><a href="#" onclick="openWindow('HelpWorklogImport.do?fkprm=true','WorklogImportHelp', 430, 600);"><fmt:message key="worklog.help.import" /></a></td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.importFile" /></th>
			<td>
				<input type="file" name="importData" />
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.importFileEncoding" /></th>
			<td>
				<select name="importDataEncoding">
					<option value="UTF-8" title='<fmt:message key="worklog.importDataEncoding.UTF-8" />'><fmt:message key="worklog.importDataEncoding.UTF-8" /></option>
					<option value="ISO-8859-1" title='<fmt:message key="worklog.importDataEncoding.ISO-8859-1" />'><fmt:message key="worklog.importDataEncoding.ISO-8859-1" /></option>
					<option value="windows-1250" title='<fmt:message key="worklog.importDataEncoding.windows-1250" />'><fmt:message key="worklog.importDataEncoding.windows-1250" /></option>
				</select>
			</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="_cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="_target1" class="button" value="<fmt:message key="button.next"/>" />
			</td>
		</tr>
	</table>
</form>
