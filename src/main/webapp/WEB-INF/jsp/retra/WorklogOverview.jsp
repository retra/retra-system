<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="WorklogOverview.do?fkprm=true" />

<!-- TODO sigl: this is only temporary solution -->
<jsp:include page="WorkWithHistoryData.jsp?requestURI=${requestURI}" />

<%-- filter --%>
<form action="${requestURI}" method="post" class="invisibleInPrint">
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
			<td rowspan="4" class="buttons">
				<input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="8"/>
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
				valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" 
				tabindex="4">
					<vc:select-option value="">--- <fmt:message key="worklog.allProjects" /> ---</vc:select-option>
				</vc:select>
			</td>
		</tr>
		<tr>			
			<th><fmt:message key="worklog.invoice.code" /></th>
			<td>
				<input type="text" name="worklogFilterInvoiceCode" value="${worklogFilterInvoiceCode}" maxlength="30"/>
			</td>
			<th><fmt:message key="worklog.activity" /></th>
			<td>
				<vc:select name="worklogFilterActivity" valueObjects="${activities}" selected="${worklogFilterActivity}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName">
					<vc:select-option value="">--- <fmt:message key="worklog.allActivities" /> ---</vc:select-option>
				</vc:select> <!-- tabindex="5" -->
			</td>
		</tr>
		<tr>
			<th><fmt:message key="worklog.invoice.relation" /></th>
			<td>
				<select name="worklogFilterInvoiceRelation">
					<c:forEach items="${invoiceRelations}" var="relation">
						<c:choose>
							<c:when test="${relation.code == worklogFilterInvoiceRelation}">
								<option value="${relation.code}" selected="selected"><fmt:message key="invoice.relation.${relation.code}" /></option>
							</c:when>
							<c:otherwise>
								<option value="${relation.code}"><fmt:message key="invoice.relation.${relation.code}" /></option>
							</c:otherwise>
						</c:choose>
					</c:forEach>				
				</select>
			</td>
			<td colspan="2">
				<input class="invisible" type="submit" name="filter" class="button" value="firstFilterNotVissible" />
				<input type="submit" name="filterCurrentMonth" class="button"  value="Current Month" tabindex="6"/>
				<input type="submit" name="filterPreviousMonth" class="button" value="Previous Month" tabindex="7"/>
			</td>
		</tr>
	</table>
	</div>
</form>

<c:set var="WEEK" value="${0}" />
<c:set var="DAY" value="${1}" />
<c:set var="PROJECT" value="${2}" />
<c:set var="ACTIVITY" value="${3}" />

<c:set var="weekProjection" value="${worklogGroupped.projectionFor[WEEK]}" />
<c:set var="weekProjectionList" value="${weekProjection.list}" />


<c:set var="projectProjection" value="${worklogGroupped.projectionFor[PROJECT]}" />
<c:set var="projectProjectionList" value="${projectProjection.list}" />

<c:set var="activityProjection" value="${worklogGroupped.projectionFor[ACTIVITY]}" />
<c:set var="activityProjectionList" value="${activityProjection.list}" />

	<div class="tabber">
	
		<h2 class="visibleOnlyInPrint">${employee.user.contactInfo.displayName}: ${worklogFilterFrom} - ${worklogFilterTo}</h2>
	
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
					<span class="invisible">${worklog.project.code}</span>
					<acronym title="${worklog.project.name}">${worklog.project.code}</acronym>
				</display:column>
				<display:column property="project.code" titleKey="worklog.project" media="csv excel xml pdf rtf"/>

				<display:column titleKey="worklog.component" media="html">
					<!-- this is hack for sorting -->
					<span class="invisible">${worklog.component.code}</span>
					<acronym title="${worklog.component.name}">${worklog.component.code}</acronym>
				</display:column>
				<display:column property="component.code" titleKey="worklog.component" media="csv excel xml rtf"/>			

				<display:column titleKey="worklog.activity" media="html">
					<!-- this is hack for sorting -->
					<span class="invisible">${worklog.activity.code}</span>
					<acronym title="${worklog.activity.name}">${worklog.activity.code}</acronym>
				</display:column>
				<display:column property="activity.code" titleKey="worklog.activity" media="csv excel xml pdf rtf"/>

				<display:column titleKey="worklog.invoice" media="html" class="nowrap">
					<c:if test="${worklog.invoice != null}">
						<span class="invisible">${worklog.invoice.code}</span>
						<acronym title="${worklog.invoice.name}">${worklog.invoice.code}</acronym>&nbsp;-&nbsp;<img src="${imgRoot}/<fmt:message key='invoice.state.img.${worklog.invoice.state}' />" alt="<fmt:message key='invoice.state.${worklog.invoice.state}' />" title="<fmt:message key='invoice.state.${worklog.invoice.state}' />" align="absmiddle"/>
					</c:if>
				</display:column>
				<display:column property="invoice.code" titleKey="worklog.invoice" media="csv excel xml rtf"/>
			
				<display:column property="employee.user.contactInfo.displayName" titleKey="worklog.employee"/>
			
				<display:column property="description" titleKey="worklog.description"/>
				<display:column property="workFrom" titleKey="worklog.workFrom" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
				<display:column property="workTo" titleKey="worklog.workTo" decorator="cz.softinel.retra.core.utils.decorator.HourDecorator"/>
				<display:column property="hours" titleKey="worklog.hours" total="true" format="{0,number,#,##0.00}"/>

				<!-- action columns -only for html (parameter media) -->
				<display:column title="" url="/WorklogView.do?fkprm=true" paramId="worklogPk" paramProperty="pk" media="html" class="action" headerClass="action">
				    <fmt:message key="action.view"/>
			    </display:column>
				
			    <c:if test="${securityContext.loggedEmployee.pk == employee.pk}">
				
					<display:column titleKey="worklog.create.action" url="/WorklogEdit.do?fkprm=true" paramId="worklogPk" paramProperty="pk" media="html" class="action"  headerClass="action">
			    		<c:if test="${worklog.editableAccordingToInvoice}">
			    		<fmt:message key="action.edit"/>
			    		</c:if>
				    </display:column>
					<display:column title="" url="/WorklogDelete.do?fkprm=true" paramId="worklogPk" paramProperty="pk" media="html" class="action" headerClass="action" >
			    		<c:if test="${worklog.editableAccordingToInvoice}">
				    	<span onclick="JavaScript: {return deleteConfirm('<fmt:message key="delete.confirmation" />')}"><fmt:message key="action.delete"/></span>
			    		</c:if>
				    </display:column>
				
			    </c:if>

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
						<th><strong><fmt:formatNumber value="${worklogTotals.column10}" type="number" pattern="#,##0.00"/></strong></th>
						<th></th>
					    <c:if test="${securityContext.loggedEmployee.pk == employee.pk}">
						<th></th>
						<th></th>
				    	</c:if>
				    </tr>
				</display:footer>
				<display:footer media="pdf"><fmt:message key="worklog.total" />:                                                                                                                                                                                                                               <fmt:formatNumber value="${worklogTotals.column8}" type="number" maxFractionDigits="2" minFractionDigits="2"/></display:footer>
				<display:footer media="rtf"><fmt:message key="worklog.total" />:<fmt:formatNumber value="${worklogTotals.column10}" type="number" pattern="#,##0.0#"/></display:footer>
			</display:table>
	
		</div>
	
	<div class="tabbertab">
		<h2>Project overview</h2>
		<table class="groupTable">
		  <thead>		
			<tr>
				<th>Day \ Project</th>
				<c:forEach items="${projectProjectionList}" var="project">
					<th>${project.key}</th>
				</c:forEach>
				<th>Hours / day</th>
			</tr>
		  </thead>
			<c:forEach items="${weekProjectionList}" var="week">
			<c:forEach items="${week.projectionFor[1].list}" var="day">
				<tr class="even">
					<th><fmt:formatDate value="${day.key}" pattern="${TypeFormats.DATE_FORMAT_DOW}"/></th>
					<c:forEach items="${projectProjectionList}" var="project">
						<td style='text-align:right;'>
							<fmt:formatNumber value="${worklogGroupped.map[week.key][day.key][project.key][false].sum}" maxFractionDigits="2" minFractionDigits="2" />
						</td>
					</c:forEach>
					<th style='text-align:right;'><fmt:formatNumber value="${day.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</c:forEach>
				<tr class="odd totalRow">
					<th>Week ${week.key}</th>
					<c:forEach items="${projectProjectionList}" var="project">
						<td style='text-align:right;'>
							<fmt:formatNumber value="${worklogGroupped.map[week.key][false][project.key][false].sum}" maxFractionDigits="2" minFractionDigits="2" />
						</td>
					</c:forEach>
					<th style='text-align:right;'><fmt:formatNumber value="${week.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</c:forEach>
			<tr class="tableBottom">
				<th><fmt:message key="worklog.total"/></th>
				<c:forEach items="${projectProjectionList}" var="project">
					<th style='text-align:right;'><fmt:formatNumber value="${project.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</c:forEach>
				<th style='text-align:right;'><fmt:formatNumber value="${worklogGroupped.map[false][false][false][false].sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
			</tr>
		</table>
	</div>

	<div class="tabbertab">
		<h2>Activity overview</h2>
		<table class="groupTable">
		  <thead>
			<tr>
				<th>Date</th>
				<th>Project</th>
				<th>Activity</th>
				<th>Hours</th>
			</tr>
		  </thead>
			<c:forEach items="${weekProjectionList}" var="week">
			<c:forEach items="${week.projectionFor[1].list}" var="day">
			<c:forEach items="${day.projectionFor[2].list}" var="project">
			<c:forEach items="${project.projectionFor[3].list}" var="activity">
				<tr class="even">
					<th><fmt:formatDate value="${day.key}" pattern="${TypeFormats.DATE_FORMAT_DOW}"/></th>
					<td>${project.key}</td>
					<td>${activity.key}</td>
					<th style='text-align:right;'><fmt:formatNumber value="${activity.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</c:forEach>
			</c:forEach>
				<tr class="subTotalRow">
					<th>&nbsp;</th>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<th style='text-align:right;'><fmt:formatNumber value="${day.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</c:forEach>
				<tr class="odd totalRow">
					<th>Week ${week.key}</th>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<th style='text-align:right;'><fmt:formatNumber value="${week.value.sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
				</tr>
			</c:forEach>
			<tr class="tableBottom">
				<th>Total</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th style='text-align:right;'><fmt:formatNumber value="${worklogGroupped.map[false][false][false][false].sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
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
					<th>${activity.key}</th>
				</c:forEach>
				<th><fmt:message key="worklog.total"/></th>
			</tr>
		  </thead>
			<c:forEach items="${projectProjectionList}" var="project">
				<tr class="odd">
					<th>${project.key}</th>
					<c:forEach items="${activityProjectionList}" var="activity">
						<td style='text-align:right;'>
							<fmt:formatNumber value="${worklogGroupped.map[false][false][project.key][activity.key].sum}" maxFractionDigits="2" minFractionDigits="2" />
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
				<th style='text-align:right;'><fmt:formatNumber value="${worklogGroupped.map[false][false][false][false].sum}" maxFractionDigits="2" minFractionDigits="2" /></th>
			</tr>
		</table>
	</div>

	</div>

<form action="" method="post" class="invisibleInPrint">
	<div class="printer">
	<table>
		<tr>
			<td>
				<input type="submit" name="print" class="button" value="<fmt:message key="button.print"/>" onclick="JavaScript: {return pagePrint();}"/>
				<a class="button" href="HelpPrint.do?fkprm=true" title="<fmt:message key="workog.help.print"/>"><fmt:message key="workog.help.print"/></a>
			</td>
		</tr>
	</table>
	</div>
</form>

<div id="signatureDiv" class="visibleOnlyInPrint">
	<table>
		<tr>
			<td>..............................................</td>
			<td>..............................................</td>
		</tr>
		<tr>
			<td><fmt:message key="workog.signature" /></td>
			<td><fmt:message key="workog.signature" /></td>
		</tr>
	</table>
</div>
