<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<div class="component dashboardComponentHalf">
	<p><img src="${imgRoot}/retra-logo.png" alt="<fmt:message key="header.title"/>" title="<fmt:message key="header.title"/>" /></p>
	<p><fmt:message key="about.text" /></p>
	<table>
		<tr valign="top">
			<th><fmt:message key="about.version.label" />: </th>
			<td><fmt:message key="about.version.major" />.<fmt:message key="about.version.minor" />.<fmt:message key="about.version.build" /></td>
		</tr>
		<tr valign="top">
			<th><fmt:message key="about.releasedOn.label" />: </th>
			<td><fmt:message key="about.releasedOn" /></td>
		</tr>
		<tr valign="top">
			<th><fmt:message key="about.authors.label" />: </th>
			<td><fmt:message key="about.authors" /></td>
		</tr>
		<tr valign="top">
			<th><fmt:message key="about.contributors.label" />: </th>
			<td><fmt:message key="about.contributors" /></td>
		</tr>
	</table>
</div>