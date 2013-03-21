<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<table class="dashboardComponents" cellspacing="0" cellpadding="0">
	<tr valign="top">
		<td>
			<div class="tabber">
				<c:forEach items="${miraJokes}" var="joke">
				<div class="tabbertab">
					<div class="component dashboardComponentFull">
						<h2>${joke.label}</h2>
						${joke.html}
					</div>
				</div>
				</c:forEach>
			</div>
		</td>
	</tr>
	<tr valign="top">
		<td>
			<div class="component dashboardComponentFull">
				<h2><fmt:message key="news.label"/></h2>
				<c:forEach items="${news}" var="newsItem">
					<h4>${newsItem.newsDate} - ${newsItem.newsTitle}</h4>
					<p class="justify">${newsItem.newsBody}</p>
					<p class="sign">${newsItem.newsAuthor}</p>
				</c:forEach>
			</div>
		</td>
	</tr>
</table>