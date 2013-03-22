<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<script src="${commonJsRoot}/userprojectassign.js" type="text/javascript"></script>

<c:set var="requestURI" value="ProjectEdit.do?fkprm=true" />

<form action="${requestURI}" method="post" >
		<spring:bind path="projectForm.pk">
				<input type="hidden" name="${status.expression}" value="${status.value}" />
		</spring:bind>
	
	<table class="formTable">
		<tr>
			<spring:bind path="projectForm.parentPk">
				<th><fmt:message key="entity.project.parent" /></th>
				<td>
					<vc:select name="${status.expression}" valueObjects="${projects}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName"/>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>	
		<tr>
			<spring:bind path="projectForm.code">
				<th><fmt:message key="entity.project.code" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" maxlength="30"/>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="projectForm.name">
				<th><fmt:message key="entity.project.name" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" maxlength="250"  size="75"/>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>		
		<tr>
			<spring:bind path="projectForm.category">
				<th><fmt:message key="entity.project.category" /></th>
				<td>
					<vc:select name="${status.expression}" valueObjects="${categories}" selected="${status.value}" 
					valueProperty="pk" labelProperty="codeAndName" orderBy="codeAndName">
						<vc:select-option value=""><fmt:message key="project.category.unassigned" /></vc:select-option>
					</vc:select>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<spring:bind path="projectForm.estimation">
				<th><fmt:message key="entity.project.estimation" /></th>
				<td>
					<input type="text" name="${status.expression}" value="${status.value}" maxlength="15" size="10" />
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>		
		<tr>
			<spring:bind path="projectForm.workEnabled">
				<th><fmt:message key="entity.project.workEnabled" /></th>
				<td>
					<c:choose>
						<c:when test="${status.value != null && status.value == true}"><input type="checkbox" name="${status.expression}" checked="checked" /></c:when>
						<c:otherwise><input type="checkbox" name="${status.expression}" /></c:otherwise>
					</c:choose>
					<c:forEach items="${status.errorMessages}">
						<span class="error"><fmt:message key="error.sign" /></span>
					</c:forEach>
				</td>
			</spring:bind>
		</tr>
	</table>
	
	<br><center><b>Employees</b></center><br>
	
	<spring:bind path="projectForm.employees">
	<select name="${status.expression}" multiple="multiple">
           <c:forEach items="${employees}" var="employee">
               <c:forEach items="${status.value}" var="currentEmployee">
               
                    <c:if test="${currentEmployee.pk == employee.pk}">
                       <c:set var="selected" value="true"/>
                   </c:if>

               </c:forEach>
           <option value="${employee.pk}"
             <c:if test="${selected}">selected="selected"</c:if>>
               ${employee.user.contactInfo.displayName}
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
