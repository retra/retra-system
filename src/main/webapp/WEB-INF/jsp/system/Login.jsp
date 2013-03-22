<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<div class="component dashboardComponentHalf">
	<h2>Login</h2>
	<form action="LoginAction.do?fkprm=true&originalUrl=${originalUrl}" method="post">
		<p>Username</p>
		<p>
			<input type="text" name="loginName" value="${loginName}" />
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
</div>