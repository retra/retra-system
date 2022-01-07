<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="WorklogProjectOverview.do?fkprm=true" />

<!-- TODO sigl: this is only temporary solution -->
<jsp:include page="WorkWithHistoryData.jsp?requestURI=${requestURI}" />

<%-- filter --%>
<form action="${requestURI}" method="post" >
	<div class="filter">
	<table class="filterTable">
		<tr>
			<th><fmt:message key="worklog.workFrom" /></th>
			<td>
				<input type="text" name="worklogFilterFrom" value="${fn:escapeXml(worklogFilterFrom)}" tabindex="1" id="worklogFilterWorkFromId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogFilterWorkFromImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('worklogFilterWorkFromId','worklogFilterWorkFromImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="worklog.project" /></th>
			<td>
				<vc:select name="worklogFilterProject" valueObjects="${projects}" selected="${fn:escapeXml(worklogFilterProject)}" 
				valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName"
				tabindex="4">
					<vc:select-option value="">--- <fmt:message key="worklog.allProjects" /> ---</vc:select-option>
				</vc:select>
			</td>
			<td rowspan="3" class="buttons"><input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="6"/></td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.workTo" /></th>
			<td>
				<input type="text" name="worklogFilterTo" value="${fn:escapeXml(worklogFilterTo)}" tabindex="2" id="worklogFilterWorkToId"/><%--
			--%><img src="${imgRoot}/calendarIco.gif" alt="<fmt:message key='calendar.label' />" title="<fmt:message key='calendar.label' />" align="top" id="worklogFilterWorkToImgId"/>
				<script type="text/javascript">
				<!--
					showMiraCalendarForDate('worklogFilterWorkToId','worklogFilterWorkToImgId');
				// -->
				</script>
			</td>
			<th><fmt:message key="worklog.activity" /></th>
			<td>
				<vc:select name="worklogFilterActivity" valueObjects="${activities}" selected="${fn:escapeXml(worklogFilterActivity)}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName">
					<vc:select-option value="">--- <fmt:message key="worklog.allActivities" /> ---</vc:select-option>
				</vc:select> <!-- tabindex="5" -->
			</td>
		</tr>
		<tr>			
			<th><fmt:message key="worklog.invoice.code" /></th>
			<td>
				<input type="text" name="worklogFilterInvoiceCode" value="${fn:escapeXml(worklogFilterInvoiceCode)}" maxlength="30"/>
			</td>
			<th></th>
			<td></td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.invoice.relation" /></th>
			<td>
				<select name="worklogFilterInvoiceRelation">
					<c:forEach items="${invoiceRelations}" var="relation">
						<c:choose>
							<c:when test="${relation.code == worklogFilterInvoiceRelation}">
								<option value="${fn:escapeXml(relation.code)}" selected="selected"><fmt:message key="invoice.relation.${relation.code}" /></option>
							</c:when>
							<c:otherwise>
								<option value="${fn:escapeXml(relation.code)}"><fmt:message key="invoice.relation.${relation.code}" /></option>
							</c:otherwise>
						</c:choose>
					</c:forEach>				
				</select>
			</td>
			<th></th>
			<td></td>
		</tr>
	</table>
	</div>
</form>



<div class="tabber">

	<div class="tabbertab">
		<h2>Employee</h2>
		<display:table id="worklogByEmployeeId" name="worklogByEmployee.list" requestURI="${requestURI}" export="true">
			<display:column property="key" titleKey="worklog.employee" escapeXml="true"/>
			<display:column property="keyInfo" titleKey="worklog.employee.ldaplogin" media="csv excel xml pdf rtf" escapeXml="true" />			
			<display:column property="value.sum" titleKey="worklog.hours" format="{0,number,#,##0.00}"/>
			<display:footer media="html">
				<tr>
					<th><fmt:message key="worklog.total" /></th>
					<th><fmt:formatNumber value="${worklogByEmployee.summary.sum}" type="number" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</display:footer>
		</display:table>
	</div>

	<div class="tabbertab">
		<h2>Activity</h2>
		<display:table id="worklogByAcrivityId" name="worklogByAcrivity.list" requestURI="${requestURI}" export="true">
			<display:column property="key" titleKey="worklog.activity" escapeXml="true" />
			<display:column property="value.sum" titleKey="worklog.hours" format="{0,number,#,##0.00}" />
			<display:footer media="html">
				<tr>
					<th><fmt:message key="worklog.total" /></th>
					<th><fmt:formatNumber value="${worklogByAcrivity.summary.sum}" type="number" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</display:footer>
		</display:table>
	</div>

	<div class="tabbertab">
		<h2>Project</h2>
		<display:table id="worklogByProjectId" name="worklogByProject.list" requestURI="${requestURI}" export="true">
			<display:column property="key" titleKey="worklog.project" escapeXml="true" />
			<display:column property="value.sum" titleKey="worklog.hours" format="{0,number,#,##0.00}" />
			<display:footer media="html">
				<tr>
					<th><fmt:message key="worklog.total" /></th>
					<th><fmt:formatNumber value="${worklogByProject.summary.sum}" type="number" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</display:footer>
		</display:table>
	</div>

	<div class="tabbertab">
		<h2>Employee / Activity</h2>
		<table class="groupTable">
		<thead>
			<tr>
				<th>&nbsp;</th>
				<c:forEach items="${worklogGrouppedAcrivity.list}" var="activity">
					<th>${fn:escapeXml(activity.key)}</th>
				</c:forEach>
				<th>Total</th>
			</tr>
		</thead>
			<c:forEach items="${worklogByEmployee.list}" var="employee">
				<tr class="odd">
					<th>${fn:escapeXml(employee.key)}</th>
					<c:forEach items="${worklogGrouppedAcrivity.list}" var="activity">
						<td style='text-align:right;'>
							<fmt:formatNumber value="${worklogGroupped.map[employee.key][activity.key][false].sum}" maxFractionDigits="2" minFractionDigits="2" />
						</td>
					</c:forEach>
					<th style='text-align:right;'><fmt:formatNumber value="${employee.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</c:forEach>
			<tr class="tableBottom">
				<th><fmt:message key="worklog.total"/></th>
				<c:forEach items="${worklogGrouppedAcrivity.list}" var="activity">
					<th style='text-align:right;'><fmt:formatNumber value="${activity.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</c:forEach>
				<th style='text-align:right;'><fmt:formatNumber value="${worklogByEmployee.summary.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
			</tr>
		</table>
	</div>


<c:set var="EMPLOYEE" value="${0}" />
<c:set var="ACTIVITY" value="${1}" />
<c:set var="PROJECT" value="${2}" />

<c:set var="employeeProjection" value="${worklogGroupped.projectionFor[EMPLOYEE]}" />
<c:set var="employeeProjectionList" value="${employeeProjection.list}" />

<c:set var="activityProjection" value="${worklogGroupped.projectionFor[ACTIVITY]}" />
<c:set var="activityProjectionList" value="${activityProjection.list}" />

<c:set var="projectProjection" value="${worklogGroupped.projectionFor[PROJECT]}" />
<c:set var="projectProjectionList" value="${projectProjection.list}" />

	<%--  TODO radek: Use special visual component tag GroupedTable --%>
	
	<div class="tabbertab">
		<h2>Employee / Project</h2>
		<table class="groupTable">
		<thead>
			<tr>
				<th>&nbsp;</th>
				<c:forEach items="${projectProjectionList}" var="project">
					<th>${fn:escapeXml(project.key)}</th>
				</c:forEach>
				<th><fmt:message key="worklog.total"/></th>
			</tr>
		</thead>
			<c:forEach items="${employeeProjectionList}" var="employee">
				<tr class="odd">
					<th>${fn:escapeXml(employee.key)}</th>
					<c:forEach items="${projectProjectionList}" var="project">
						<td style='text-align:right;'>
							<fmt:formatNumber value="${worklogGroupped.map[employee.key][false][project.key].sum}" pattern="#,##0.0#" />
						</td>
					</c:forEach>
					<th style='text-align:right;'><fmt:formatNumber value="${employee.value.sum}" pattern="#,##0.0#" /></th>
				</tr>
			</c:forEach>
			<tr class="tableBottom">
				<th><fmt:message key="worklog.total"/></th>
				<c:forEach items="${projectProjectionList}" var="project">
					<th style='text-align:right;'><fmt:formatNumber value="${project.value.sum}" pattern="#,##0.0#" /></th>
				</c:forEach>
				<th style='text-align:right;'><fmt:formatNumber value="${worklogGroupped.map[false][false][false].sum}" pattern="#,##0.0#" /></th>
			</tr>
		</table>
	</div>

	<div class="tabbertab">
		<h2>Project / Activity</h2>
		<table class="groupTable">
		<thead>
			<tr>
				<th>&nbsp;</th>
				<c:forEach items="${activityProjectionList}" var="activity">
					<th>${fn:escapeXml(activity.key)}</th>
				</c:forEach>
				<th><fmt:message key="worklog.total"/></th>
			</tr>
		</thead>
			<c:forEach items="${projectProjectionList}" var="project">
				<tr class="odd">
					<th>${fn:escapeXml(project.key)}</th>
					<c:forEach items="${activityProjectionList}" var="activity">
						<td style='text-align:right;'>
							<fmt:formatNumber value="${worklogGroupped.map[false][activity.key][project.key].sum}" maxFractionDigits="2" minFractionDigits="2" />
						</td>
					</c:forEach>
					<th style='text-align:right;'><fmt:formatNumber value="${project.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</c:forEach>
			<tr class="tableBottom">
				<th><fmt:message key="worklog.total"/></th>
				<c:forEach items="${activityProjectionList}" var="activity">
					<th style='text-align:right;'><fmt:formatNumber value="${activity.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</c:forEach>
				<th style='text-align:right;'><fmt:formatNumber value="${worklogGroupped.map[false][false][false].sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
			</tr>
		</table>
	</div>


	<div class="tabbertab">
		<h2>Worklog Items</h2>
		<display:table name="worklogs" requestURI="${requestURI}" export="true" id="worklog" varTotals="worklogTotals">
			<!-- set names of files (do not want default names) -->
			<display:setProperty name="export.csv.filename" value="${employee.user.contactInfo.displayName}-worklog.csv" />
			<display:setProperty name="export.excel.filename" value="${employee.user.contactInfo.displayName}-worklog.xls" />
			<display:setProperty name="export.xml.filename" value="${employee.user.contactInfo.displayName}-worklog.xml" />
			<display:setProperty name="export.pdf.filename" value="${employee.user.contactInfo.displayName}-worklog.pdf" />
			<display:setProperty name="export.rtf.filename" value="${employee.user.contactInfo.displayName}-worklog.rtf" />
			
			<%-- -->display:caption>${employee.user.contactInfo.displayName}</display:caption--%>
			
			<!-- data columns -->
			<display:column property="workFrom" titleKey="worklog.date" decorator="cz.softinel.retra.core.utils.decorator.DateDecorator" />
		
			<display:column titleKey="worklog.project" media="html" >
				<!-- this is hack for sorting -->
				<span class="invisible">${fn:escapeXml(worklog.project.code)}</span>
				<acronym title="${fn:escapeXml(worklog.project.name)}">${fn:escapeXml(worklog.project.code)}</acronym>
			</display:column>
			<display:column property="project.code" titleKey="worklog.project" media="csv excel xml pdf rtf" escapeXml="true" />

			<display:column titleKey="worklog.component" media="html">
				<!-- this is hack for sorting -->
				<span class="invisible">${fn:escapeXml(worklog.component.code)}</span>
				<acronym title="${fn:escapeXml(worklog.component.name)}">${fn:escapeXml(worklog.component.code)}</acronym>
			</display:column>
			<display:column property="component.code" titleKey="worklog.component" media="csv excel xml rtf" escapeXml="true" />
		
			<display:column titleKey="worklog.activity" media="html">
				<!-- this is hack for sorting -->
				<span class="invisible">${fn:escapeXml(worklog.activity.code)}</span>
				<acronym title="${fn:escapeXml(worklog.activity.name)}">${fn:escapeXml(worklog.activity.code)}</acronym>
			</display:column>
			<display:column property="activity.code" titleKey="worklog.activity" media="csv excel xml pdf rtf" escapeXml="true" />
		
			<display:column titleKey="worklog.invoice" media="html" class="nowrap">
				<c:if test="${worklog.invoice != null}">
					<span class="invisible">${fn:escapeXml(worklog.invoice.code)}</span>
					<acronym title="${fn:escapeXml(worklog.invoice.name)}">${fn:escapeXml(worklog.invoice.code)}</acronym>&nbsp;-&nbsp;<img src="${imgRoot}/<fmt:message key='invoice.state.img.${worklog.invoice.state}' />" alt="<fmt:message key='invoice.state.${worklog.invoice.state}' />" title="<fmt:message key='invoice.state.${worklog.invoice.state}' />" align="middle"/>
				</c:if>
			</display:column>
			<display:column property="invoice.code" titleKey="worklog.invoice" media="csv excel xml rtf" escapeXml="true" />
		
			<display:column property="employee.user.contactInfo.displayName" titleKey="worklog.employee" escapeXml="true" />
		
			<display:column property="descriptionGui" titleKey="worklog.description" media="html"/>	
			<display:column property="description" titleKey="worklog.description" media="csv excel xml pdf rtf" escapeXml="true" />

			<display:column property="workFrom" titleKey="worklog.workFrom" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
			<display:column property="workTo" titleKey="worklog.workTo" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
			<display:column property="hours" titleKey="worklog.hours" format="{0,number,#,##0.00}" total="true"/>
	
			<display:footer media="html">
				<tr>
					<th><fmt:message key="worklog.total"/></th>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
					<th><strong><fmt:formatNumber value="${worklogTotals.column10}" type="number" maxFractionDigits="2" minFractionDigits="2"/></strong></th>
				</tr>
			</display:footer>
			<display:footer media="pdf"><fmt:message key="worklog.total" />:                                                                                                                                                                                                                               <fmt:formatNumber value="${worklogTotals.column8}" type="number" maxFractionDigits="2" minFractionDigits="2"/></display:footer>
			<display:footer media="rtf"><fmt:message key="worklog.total" />:<fmt:formatNumber value="${worklogTotals.column10}" type="number" maxFractionDigits="2" minFractionDigits="2"/></display:footer>
		</display:table>

	</div>

</div>
