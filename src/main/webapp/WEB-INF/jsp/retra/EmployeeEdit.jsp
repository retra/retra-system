<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<script src="${commonJsRoot}/userprojectassign.js" type="text/javascript"></script>

<c:set var="requestURI" value="EmployeeEdit.do?fkprm=true" />

<form action="${requestURI}" method="post" >

			<spring:bind path="employeeForm.pk">
				<input type="hidden" name="${status.expression}" value="${status.value}" />
			</spring:bind>

	<table class="formTable">
		<tr>
			<spring:bind path="employeeForm.user.contactInfo.firstName">
				<th><fmt:message key="entity.contactInfo.firstName" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.contactInfo.lastName">
				<th><fmt:message key="entity.contactInfo.lastName" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.contactInfo.email">
				<th><fmt:message key="entity.contactInfo.email" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.contactInfo.web">
				<th><fmt:message key="entity.contactInfo.web" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.contactInfo.phone1">
				<th><fmt:message key="entity.contactInfo.phone1" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.contactInfo.phone2">
				<th><fmt:message key="entity.contactInfo.phone2" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.contactInfo.fax">
				<th><fmt:message key="entity.contactInfo.fax" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.contactInfo.jirauser">
				<th><fmt:message key="entity.contactInfo.jirauser" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.login.name">
				<th><fmt:message key="entity.login.name" /></th>
				<td>
					<input type="hidden" name="${status.expression}" value="${status.value}" />
					${status.value}
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="employeeForm.user.login.ldapLogin">
				<th><fmt:message key="entity.login.ldapLogin" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
	</table>
	
	
	<br><center><b>Projects</b></center><br>
	
	<spring:bind path="employeeForm.projects">
	<select name="${status.expression}" multiple="multiple">
           <c:forEach items="${projects}" var="project">
               <c:forEach items="${status.value}" var="currentProject">
               
                    <c:if test="${currentProject.pk == project.pk}">
                       <c:set var="selected" value="true"/>
                   </c:if>

               </c:forEach>
           <option value="${project.pk}"
             <c:if test="${selected}">selected="selected"</c:if>>
               ${project.codeAndName}
           </option>
           <c:remove var="selected"/>
         </c:forEach>
       </select>
       <c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
	   </c:forEach>
	
	</spring:bind>
	
	<br>
	
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
