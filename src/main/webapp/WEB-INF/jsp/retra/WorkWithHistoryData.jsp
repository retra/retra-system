<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<div id="showHistoryData">
	<div id="showHistoryDataForm">
		<form action="SetShowHistoryData.do?fkprm=true" method="post">
			<input type="hidden" name="afterSetAction" value="${param.requestURI}" />
			<div class="filter">
				<table class="filterTable">
					<tr>
						<th width="130">Work with history data</th>
						<td width="15">
							<c:choose>
								<c:when test="${showHistoryData}"><input type="checkbox" name="showHistoryData" checked="checked" /></c:when>
								<c:otherwise><input type="checkbox" name="showHistoryData" /></c:otherwise>
							</c:choose>
						</td>
						<td width="30"><input type="submit" value="Set" /></td>
						<td class="right">
							Actual value:
							<c:choose>
								<c:when test="${showHistoryData}">Work with history data</c:when>
								<c:otherwise>Don't work with history data</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</div>