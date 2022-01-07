<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="${requestURI}" />
<br />

<!-- TODO sigl: this is only temporary solution -->
<jsp:include page="WorkWithHistoryData.jsp?requestURI=${requestURI}" />

<form action="${requestURI}" method="post">
<c:if test="${ empty requestScope.badImportFile }">
	<table class="formTable">
		<caption><fmt:message key="worklog.projectMapping" /></caption>
		<tbody>

<c:choose>	<%-- if parser is able to find some external projects present them for mapping otherwise present internal ones for selection only --%>
	<c:when test="${ not empty form.externalProjects}">
		<c:forEach var="externalProject" items="${form.externalProjects}" varStatus="status">
			<tr>
				<th>${fn:escapeXml(externalProject.name)}</th>
				<td>
					<vc:select name="externalProjects[${status.index}].projectId" valueObjects="${projects}" selected="${fn:escapeXml(externalProject.projectId)}" 
							valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="-1"><fmt:message key="worklog.pleaseSpecify" /></vc:select-option>
					</vc:select>
				<c:if test="${showInvoices}">
					<vc:select name="externalProjects[${status.index}].invoiceId" valueObjects="${invoices}" selected="${fn:escapeXml(externalProject.invoiceId)}" 
							valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="-1">&nbsp;</vc:select-option>
					</vc:select>
				</c:if>	
				</td>
			</tr>			
		</c:forEach>
	</c:when>
	<c:otherwise>
				<td>
					<vc:select name="selectedProject.pk" valueObjects="${projects}"
							valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
					</vc:select>
				</td>
	</c:otherwise>
</c:choose>
	
	
		</tbody>
	</table>
	<br />
	<table class="formTable">
		<caption><fmt:message key="worklog.activityMapping" /></caption>
		<tbody>
		
<c:choose> <%-- if parser is able to find some external activities present them for mapping otherwise present internal ones for selection only --%>
	<c:when test="${ not empty form.externalActivities}">
		<c:forEach var="externalActivity" items="${form.externalActivities}" varStatus="status">
			<tr>
				<th>${fn:escapeXml(externalActivity.name)}</th>
				<td>
					<vc:select name="externalActivities[${status.index}].activityId" valueObjects="${activities}" selected="${fn:escapeXml(externalActivity.activityId)}"
							valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="-1"><fmt:message key="worklog.pleaseSpecify" /></vc:select-option>
					</vc:select>
				</td>
			</tr>
		</c:forEach>
	</c:when>
	<c:otherwise>
				<td>
					<vc:select name="selectedActivity.pk" valueObjects="${activities}"
							valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
					</vc:select>
				</td>
	</c:otherwise>
</c:choose>

		</tbody>
	</table>
</c:if>
	

	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="_cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="_target0" class="button" value="<fmt:message key="button.back"/>" />
			<c:if test="${ empty requestScope.badImportFile }">
				<input type="submit" name="_target2" class="button" value="<fmt:message key="button.next"/>" />
			</c:if>
			</td>
		</tr>
	</table>
	
	
</form>
