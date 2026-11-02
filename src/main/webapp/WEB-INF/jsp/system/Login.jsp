<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<div class="component dashboardComponentHalf">
	<h2>Login</h2>
	<form action="LoginAction.do?fkprm=true&originalUrl=${originalUrl}" method="post">
		<p>Username</p>
		<p>
			<input type="text" name="loginName" value="${fn:escapeXml(loginName)}" />
		</p>
		<p>Password</p>
		<p>
			<input type="password" name="password" value="" />
		</p>
		<p>
			<input type="checkbox" name="permanentPassword" id="permanentPassword" />
			<label for="permanentPassword">Remember me...</label>
		</p>
		<p>
			<input type="submit" name="submit" class="button" value="<fmt:message key="button.login"/>" />
		</p>
	</form>
	<c:if test="${keycloakEnabled}">
		<p>
			<a class="button buttonKeycloak" href="KeycloakLogin.do?fkprm=true&originalUrl=${originalUrl}">
				<img src="${commonImgRoot}/keycloak.png" alt="" class="keycloakIcon" width="24" height="24" />
				${fn:escapeXml(keycloakButtonLabel)}
			</a>
		</p>
		<c:if test="${param.keycloakError != null}">
			<p class="error"><fmt:message key="error.login.keycloak"/></p>
		</c:if>
	</c:if>
</div>