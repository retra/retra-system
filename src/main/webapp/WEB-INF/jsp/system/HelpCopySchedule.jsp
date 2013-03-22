<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<div class="component dashboardComponentFull">
	<h4>Day</h4>
	<p class="justify">
		Copy day means that application get as reference given date (Copy from (date))
		and try to copy it to destination period (Copy to - start and Copy to - end). This
		type of copy do not copy to weekend days (Saturday, Sunday).
	</p>
	<h4>Month</h4>
	<p class="justify">
		Copy week means that application get as reference given date and next 7 days from this\
		date (Copy from (date)) and try to copy it to destination period (Copy to - start and Copy to - end).
		This type of copy find reference for each week day (Monday, Tuesday etc.) and to destination
		copy data according to this references. Means Monday to Monday, Tuesday to Tuesday etc. Also
		copy weekend days (Saturday, Sunday). If some day of week is not defined in references, than copy
		for all these days will be not done.
	</p>
	<h4>General</h4>
	<p class="justify">
		Copied are type of schedule (holidays, internal projects etc.) and times for working from and to.
	</p>
</div>

<%--
<form action="" method="post" >
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" onclick="JavaScript: {return goBack();}"/>
			</td>
		</tr>
	</table>
</form>
--%>