<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<table class="dashboardComponents" cellspacing="0" cellpadding="0">
	<tr valign="top">
		<td>
			<div class="component dashboardComponent">
				<h2><fmt:message key="settings.actionsAndTools" /></h2>
				<p>
					<strong>ACTION: </strong>Reload <strong>my</strong> assigned projects => <a href="ReloadLoggedEmployee.do?fkprm=true"><img src="${imgRoot}/reload.png" alt='<fmt:message key="settings.reload" />' title='<fmt:message key="settings.reload" />' style="vertical-align: middle;" /></a>
				</p>
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td>
			<div class="component dashboardComponent">
				<h2><fmt:message key="menu.settings" /></h2>
				<p><a href="EmployeeChangePassword.do?fkprm=true"><fmt:message key="menu.settingsChangePassword" /></a></p>
				<p><a href="EmployeeChangeContactInfo.do?fkprm=true"><fmt:message key="menu.settingsChangeContactInfo" /></a></p>
				<p><a href="ChangeVisualConfiguration.do?fkprm=true"><fmt:message key="menu.settingsVisualConfiguration" /></a></p>
			</div>
		</td>
	</tr>
</table>