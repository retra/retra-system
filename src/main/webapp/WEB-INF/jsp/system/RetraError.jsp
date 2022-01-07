<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<div class="component dashboardComponentHalf">
	<h2><fmt:message key="retra.error"/></h2>
	<form action="Newsboard.do?fkprm=true" method="post">
		<p><fmt:message key="retra.error.id"/>: ${fn:escapeXml(errId)}</p>
		<p>
			<input type="submit" name="submit" class="button" value="<fmt:message key="newsboard.label"/>" />
		</p>
	</form>
</div>