<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="ScheduleOverviewList.do?fkprm=true" />

<%-- filter --%>
<form action="${requestURI}" method="post" class="invisibleInPrint">
	<div class="filter">
	<table class="filterTable">
		<tr>
			<th><fmt:message key="schedule.month" /></th>
			<td>
				<vc:select name="scheduleFilterMonth" valueObjects="${months}" selected="${scheduleFilterMonth}"
				valueProperty="value" labelProperty="label" orderBy="label" tabindex="1" />
			</td>
			<th><fmt:message key="schedule.employee" /></th>
			<td>
				<vc:select name="scheduleFilterEmployee" valueObjects="${employees}" selected="${scheduleFilterEmployee}" 
				valueProperty="pk" labelProperty="user.contactInfo.displayName" orderBy="user.contactInfo.displayName"
				tabindex="2" /> 
			</td>
			<td rowspan="2" class="buttons"><input type="submit" name="filter" class="button" value="<fmt:message key="button.filter"/>" tabindex="4"/></td>
		</tr>
		<tr>
			<th><fmt:message key="schedule.year" /></th>
			<td>
				<vc:select name="scheduleFilterYear" valueObjects="${years}" selected="${scheduleFilterYear}"
				valueProperty="value" labelProperty="label" orderBy="label" tabindex="3" />
			</td>
			<td colspan="2"></td>
		</tr>
	</table>
	</div>
</form>

<div class="nextPrevious invisibleInPrint">[<a href="${requestURI}&${parametersPrevious}"><fmt:message key="action.previous"/></a>]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<a href="${requestURI}&${parametersNext}"><fmt:message key="action.next"/></a>]</div>
<display:table name="schedules" requestURI="${requestURI}" id="scheduleOverview" export="true" >
	<!-- set names of files (do not want default names) -->
	<display:setProperty name="export.csv.filename" value="${employee.user.contactInfo.displayName}-schedule.csv" />
	<display:setProperty name="export.excel.filename" value="${employee.user.contactInfo.displayName}-schedule.xls" />
	<display:setProperty name="export.xml.filename" value="${employee.user.contactInfo.displayName}-schedule.xml" />
	<display:setProperty name="export.pdf.filename" value="${employee.user.contactInfo.displayName}-schedule.pdf" />
	<display:setProperty name="export.rtf.filename" value="${employee.user.contactInfo.displayName}-schedule.rtf" />

	<!-- header -->
	<display:caption>${employee.user.contactInfo.displayName} - <fmt:message key="month.${scheduleFilterMonth}"/> ${scheduleFilterYear}</display:caption>
	
	<!-- data columns -->
	<display:column property="monday" titleKey="day.2" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" class="day ${scheduleOverview.monday.cssClass}" headerClass="day" media="html" />
	<display:column property="tuesday" titleKey="day.3" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" class="day ${scheduleOverview.tuesday.cssClass}" headerClass="day" media="html" />
	<display:column property="wednesday" titleKey="day.4" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" class="day ${scheduleOverview.wednesday.cssClass}" headerClass="day" media="html" />
	<display:column property="thursday" titleKey="day.5" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" class="day ${scheduleOverview.thursday.cssClass}" headerClass="day" media="html" />
	<display:column property="friday" titleKey="day.6" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" class="day ${scheduleOverview.friday.cssClass}" headerClass="day" media="html" />
	<display:column property="saturday" titleKey="day.7" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" class="day ${scheduleOverview.saturday.cssClass}" headerClass="day" media="html" />
	<display:column property="sunday" titleKey="day.1" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" class="day ${scheduleOverview.sunday.cssClass}" headerClass="day" media="html" />

	<display:column property="monday" titleKey="day.2" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" media="csv excel pdf xml rtf" />
	<display:column property="tuesday" titleKey="day.3" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" media="csv excel pdf xml rtf" />
	<display:column property="wednesday" titleKey="day.4" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" media="csv excel pdf xml rtf" />
	<display:column property="thursday" titleKey="day.5" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" media="csv excel pdf xml rtf" />
	<display:column property="friday" titleKey="day.6"  decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" media="csv excel pdf xml rtf" />
	<display:column property="saturday" titleKey="day.7" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" media="csv excel pdf xml rtf" />
	<display:column property="sunday" titleKey="day.1" decorator="cz.softinel.retra.core.utils.decorator.ScheduleOverviewDecorator" media="csv excel pdf xml rtf" />

</display:table>

<div class="printer invisibleInPrint"">
	<table class="invisibleInPrint" width="100%">
		<tr>
			<td class="center scheduleDefault"><fmt:message key="schedule.default" /></td>
			<c:forEach items="${types}" var="type"><td class="center ${type.cssClass}">${type.name}</td></c:forEach>
			<td class="center scheduleWeekend"><fmt:message key="schedule.weekend.holidays" /></td>
		</tr>
	</table>
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