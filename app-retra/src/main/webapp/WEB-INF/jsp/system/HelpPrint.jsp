<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%> 

<div class="component dashboardComponentFull">
	<h4>Firefox, SeaMonkey, Mozilla</h4>
	<p class="justify">
		Format - Orientation: Portrait<br />
		Format - Scale: Shrink To Page Width (checked)<br />
		Options: Print Background (colors &amp; images) (checked)<br />
		Margins (millimeters): Top 10, Bottom 10, Left 5, Right 5<br />
		Headers &amp; Footers: all -- blank --
	</p>
	<h4>Opera</h4>
	<p class="justify">
		Paper and orientation: A4, Portrait<br />
		Print page background (checked)<br />
		Print headers and footers (unchecked)<br />
		Fit to paper width (checked)<br />
		Scale print to 100%<br />
		Page margins (in centimeters) Top 0.5, Bottom 0.5, Left 0.5, Right 0.5
	</p>
	<h4>Internet Explorer 6, 7 (not recommended)</h4>
	<p class="justify">
		Internet Explorer 6, 7 (not recommended)<br />
		Internet options (tab Advanced) - Printing: Print background colors and images (checked) 
		Size A4<br />
		Headers (empty)<br />
		Footers (empty)<br />
		Oritentation: Portrait<br />
		Margins (milimeters): Top 10, Bottom 10, Left 5, Right 5
	</p>
</div>

<%--
<form action="" method="post" class="invisibleInPrint" >
	<table class="formTable">
		<tr class="buttons">
			<td>
				<input type="submit" name="back" class="button" value="<fmt:message key="button.back"/>" onclick="JavaScript: {return goBack();}"/>
			</td>
		</tr>
	</table>
</form>
--%>