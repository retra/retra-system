<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<c:set var="requestURI" value="ChangeVisualConfigurationStore.do?fkprm=true" />

<form action="${requestURI}" method="post" >

	<table class="formTable">
		<tr>
			<spring:bind path="visualConfigurationForm.skinName">
				<th>Skin</th>
				<td>
					<vc:select name="${status.expression}" valueObjects="${skins}" selected="${status.value}" 
					valueProperty="name" labelProperty="title" >
						<vc:select-option value="">--- default ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="visualConfigurationForm.timeSelectorImplementation">
				<th>Time Selector</th>
				<td>
					<vc:select name="${status.expression}" valueObjects="${timeSelectors}" selected="${status.value}" 
					valueProperty="name" labelProperty="title" >
						<vc:select-option value="">--- default ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
	</table>
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="save" class="invisible" value="firstSaveNotVissible" />
				<input type="submit" name="cancel" class="button" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" name="save" class="button" value="<fmt:message key="button.save"/>" />
			</td>
		</tr>
	</table>
</form>
