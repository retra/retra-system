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
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p0">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a0">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p1">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a1">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p2">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a2">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p3">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a3">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p4">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a4">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p5">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a5">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p6">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a6">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p7">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a7">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p8">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a8">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>
		<tr>
				<th>Project / Default activity</th>
				<td>
			<spring:bind path="visualConfigurationForm.p9">
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
						valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk"> 
						<vc:select-option value="">--- project ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
			<spring:bind path="visualConfigurationForm.a9">
					<vc:select name="${status.expression}" valueObjects="${activities}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName" parentProperty="parent.pk">
						<vc:select-option value="">--- default activity ---</vc:select-option>
					</vc:select>					
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
			</spring:bind>
				</td>
		</tr>

	</table>
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="save" class="button" value="<fmt:message key="button.save"/>" />
			</td>
		</tr>
	</table>
</form>
