
TimeSelector = function(controlId) {
	this.controlId = controlId;
	this.mainElementId = controlId + "_mainElementTimeSelector";
}

TimeSelector.prototype.getMainElement = function() {
	return document.getElementById(this.mainElementId)
}

TimeSelector.prototype.getControl = function() {
	return document.getElementById(this.controlId)
}

TimeSelector.prototype.generate = function() {
	document.write("<span id='"+this.mainElementId+"' style='position:relative;'>NOT INITIALIZED</span>");
	this.renderIcon();
}

TimeSelector.prototype.getIconHtml = function() {
	return ""; // "<img src='gui/skin01/img/hoursIco.gif' onClick='TimeSelector.renderHours(\""+this.controlId+"\")'/>";
}

TimeSelector.prototype.renderIcon = function() {
	mainElement = this.getMainElement();
	mainElement.innerHTML = this.getIconHtml();
	
}

TimeSelector.prototype.renderHours = function() {
	mainElement = this.getMainElement();
	content = this.getIconHtml();
	content += "<div style='position: absolute;'>";
	content += "<table cellpading='0' cellspacing='1' border='0' class='TimeSelectorTable'>";
	content += "<tr height='3'>";
	for (s=0; s<6; s++) {
		content += "<th style='border-left: 9px solid red; border-right: 9px solid red;'></th>";
	}
	content += "</tr>";
	for (r=0; r<4; r++) {
		content += "<tr height='22'>";
		for (s=0; s<6; s++) {
			hour = r*6 + s;
			content += "<td class='TimeSelectorCell' onClick='TimeSelector.selectHour(\""+this.controlId+"\",\""+hour+"\")'>" + hour + "</td>";
		}
		content += "</tr>";
	}
	content += "</table>";
	content += "</div>";
	mainElement.innerHTML = content;
}

TimeSelector.prototype.selectHour = function(hour) {
	control = this.getControl();
	control.value = hour + ":";
}

TimeSelector.prototype.renderMinutes = function() {
	mainElement = this.getMainElement();
	content = this.getIconHtml();
	content += "<div style='position: absolute;'>";
	content += "<table cellpading='0' cellspacing='1' border='0' class='TimeSelectorTable'>";
	content += "<tr height='3'>";
	for (s=0; s<6; s++) {
		content += "<th style='border-left: 9px solid red; border-right: 9px solid red;'></th>";
	}
	content += "</tr>";
	content += "<tr height='22'>";
	content += this.getMinuteTD("50", 1) + this.getMinuteTD("55", 1) + this.getMinuteTD("00", 2) + this.getMinuteTD("05", 1) + this.getMinuteTD("10", 1);
	content += "</tr>";
	content += "<tr height='22'>";
	content += this.getMinuteTD("45", 2) + this.getMinuteTD("15", 2);
	content += "</tr>";
	content += "<tr height='22'>";
	content += this.getMinuteTD("30", 2);
	content += "</tr>";
	content += "<tr height='22'>";
	content += this.getMinuteTD("40", 1) + this.getMinuteTD("35", 1) + this.getMinuteTD("25", 1) + this.getMinuteTD("20", 1);
	content += "</tr>";
	content += "</table>";
	content += "</div>";
	mainElement.innerHTML = content;
}

TimeSelector.prototype.getMinuteTD = function(minute, size) {
	return "<td colspan='"+size+"' rowspan='"+size+"' class='TimeSelectorCell' onClick='TimeSelector.selectMinute(\""+this.controlId+"\",\""+minute+"\")'>"+minute+"</td>";
}

TimeSelector.prototype.selectMinute = function(minute) {
	control = this.getControl();
	control.value = control.value + minute;
}


TimeSelector.renderIcon = function(controlId) {
	ts = new TimeSelector(controlId);
	ts.renderIcon();
}

TimeSelector.renderHours = function(controlId) {
	ts = new TimeSelector(controlId);
	ts.renderHours();
}

TimeSelector.selectHour = function(controlId, hour) {
	ts = new TimeSelector(controlId);
	ts.selectHour(hour);
	ts.renderMinutes();
}

TimeSelector.selectMinute = function(controlId, minute) {
	ts = new TimeSelector(controlId);
	ts.selectMinute(minute);
	ts.renderIcon();
}
