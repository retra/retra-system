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
	<select class="jquery-doubleselect-leftSelect" name="${status.expression}" multiple="multiple">
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

<script>
(function() {
	var input = $('<label for="filterProject" style="padding-right: 20px;">Filter Projects</label><input id="filterProject" type="text" style="width: 418px" />');

	$(input).insertBefore(".jquery-doubleselect-leftSelect");


	jQuery.fn.filterByText = function(textbox) {
		return this.each(function() {
			var select = this;
			var options = [];
			$(select).find('option').each(function() {
				options.push({value: $(this).val(), text: $(this).text()});
			});
			$(select).data('options', options);

			$(textbox).bind('change keyup', function() {
				var options = $(select).empty().data("options");
				var search = $.trim($(this).val());
				var regex = new RegExp(search, "gi");

				$.each(options, function(i) {
					var option = options[i];
					if (option.text.match(regex) !== null) {
						$(select).append($('<option>').text(option.text).val(option.value));
					}
				});
			});
		});
	};

	$(function() {
		$('.jquery-doubleselect-leftSelect').filterByText(input);
	});


	$(".jquery-doubleselect-middle input[type=button]").click(function() {
		$(".jquery-doubleselect-originalSelect option").each(function() {
			$(this)[0].selected = false;
		});

		$(".jquery-doubleselect-rightSelect option").each(function(e) {
			var self = this;
			$(".jquery-doubleselect-originalSelect option").each(function() {
				if ($(this).val() == $(self).val()) {
					$(this)[0].selected = true;
				}
			});
		});
	});

}).apply(this);
</script>
