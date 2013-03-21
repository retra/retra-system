<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../Includes.jsp"%>

<p>
	<h2>Importing worklog data from Outlook Express Calendar</h2>
	<br />
	<p align="justify">
		Here we assume that your worklog is being scheduled in Outlook Express Calendar (as appointment) and that description
		of what you have done is copied to 'Subject' line.
	</p>
	<br />
	<ul>
	<li>Navigate: File -> Import and export -> Export to a file -> Tab separated values (Windows)</li> 
	<li>Choose calendar</li>
	<li>Choose destination file</li>
	<li>Click on button 'Map custom fields' (you will have to do this step only once)</li>
	<li>You have to select "default" mapping or following fields (in the exact order, when default mapping fails)</li>
	<br />
	<ol>
		<li>Subject (Předmět)</li>
		<li>Start Date (Počáteční datum)</li>
		<li>Start Time (Začátek)</li>
		<li>End Date (Koncové datum)</li>
		<li>End Time (Konec)</li>
		<li>Category (Kategorie)</li>
		<li>Show Time As field (Zobrazit čas jako)</li>
	</ol>
	<br />
	<li>Click on 'Finish' button</li>
	<li>Select time interval</li>
	<li>Import file to Retra</li>
	</ul>
</p>
<br /><br />