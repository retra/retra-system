// lib.TimeSelect 0.1.0
// requires: elm.js, array.js, string.js, data.js, dhtml.js, vector.js, evt.js, cls.js, object.js, Class.js, Box.js, Popbox.js
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

var lib = lib || {};
lib.TimeSelect = lib.TimeSelect || {};




lib.TimeSelect.name = "lib.TimeSelect";
lib.TimeSelect.openedTimeSelect = null;




lib.TimeSelect = lib.PopBox.extend({
	
	name: "lib.TimeSelect",
	inputElm:     null,
	openerButton: null,
	
	hourSelectTemplate:    null,
	hourSelectContent:     null,
	hourSelectClickEvId:   null,
	hourSelectMapping:     null, // function that transforms click coordinates to hours
	
	minuteSelectTemplate:  null,
	minuteSelectContent:   null,
	minuteSelectClickEvId: null,
	minuteSelectMapping:   null, // function that transforms click coordinates to minutes
	
	previousInputValue:    null,
	
	hour: null,
	step: null,
	timeSelectEvId: null,
	
	init: function (inputElm) {
		this.hourSelectContent = lib.elm.create(lib.TimeSelect.hourSelectTemplate);
		lib.evt.add(this.hourSelectContent, "click", this._selectHour, null, this);
		
		this.minuteSelectContent = lib.elm.create(lib.TimeSelect.minuteSelectTemplate);
		lib.evt.add(this.minuteSelectContent, "click", this._selectMinute, null, this);
		
		var contentElm = this.hourSelectContent;
		this.inputElm = inputElm;

		lib.PopBox.init.apply(this, [contentElm]);
		
		this._addInputButton();		
		this._showHours();
		this._hideMinutes();
		
		return true;
	},
	
	_addInputButton: function () {
		// add the input button
		this.openerButton = lib.elm.create( {tagName:"input", type:"image", value:"openTimeSelect", src: "../images/openerButton_v2.png", className: "timeSelectButton"} );
		var inputParent = lib.elm.getParent(this.inputElm);
		inputParent.insertBefore(this.openerButton, this.inputElm.nextSibling);
		lib.evt.add(this.openerButton, "click", this.open, null, this);
		return true;
	},
	
	open: function (elm, e, evId) {
		elm = elm || null;
		e = e || null;
		evId = evId || null;
		if (lib.TimeSelect.openedTimeSelect) {
			lib.TimeSelect.openedTimeSelect.close();
		}
		lib.TimeSelect.openedTimeSelect = this;
		this._showHours();
		this._hideMinutes();
		
		var openingElm = this.openerButton;
		lib.PopBox.open.apply(this, [openingElm, e, evId]);
		
		this.previousInputValue = this.inputElm.value;
		
		return true;
	},
	
	close: function (closingElm, e, evId) {
		lib.PopBox.close.apply(this, arguments);
		return true;
	},
	
	_showMinutes: function () {
		this.minuteSelectContent.style.display = "";
		this.replaceContent(this.minuteSelectContent);
		return true;
	},
		
	_hideMinutes: function () {
		this.minuteSelectContent.style.display = "none";
		return true;

	},
	
	_showHours: function () {
		this.hourSelectContent.style.display = "";
		this.replaceContent(this.hourSelectContent);
		return true;

	},
	
	_hideHours: function () {
		this.hourSelectContent.style.display = "none";				
	},

	_selectHour: function (elm, e, evId) {
		var mousePos = lib.dhtml.getMouseEvtPos(e);
		var zeroPos = lib.dhtml.getPos(elm);
		var pos = [mousePos[0] - zeroPos[0], mousePos[1] - zeroPos[1]];
		var hour = lib.TimeSelect.hourSelectMapping(pos[0], pos[1]);
		
		// printing to input field
		if (hour >= 0) {
			
			if (hour <= 9) {
				hour = hour.toString();
				hour = "0" + hour;
			}
			
			this.inputElm.value = hour;		
			this._showMinutes();
			this._hideHours();
		} else if (hour == "exit") {
			this.inputElm.value = this.previousInputValue;
			this.close();
		}
		
		return true;
	},
	
	_selectMinute: function (elm, e, evId) {
		var mousePos = lib.dhtml.getMouseEvtPos(e);
		var zeroPos = lib.dhtml.getPos(elm);
		var pos = [mousePos[0] - zeroPos[0], mousePos[1] - zeroPos[1]];
		var minute = lib.TimeSelect.minuteSelectMapping(pos[0], pos[1]);
		
		// printing to input field
		if (minute >= 0) {
			
			if (minute <= 9) {
				minute = minute.toString();
				minute = "0" + minute;
			}
			
			this.inputElm.value = this.inputElm.value + ":" + minute;		
			this.close();
		} else if (minute == "exit") {
			this.inputElm.value = this.previousInputValue;
			this.close();
		}
				
		return true;
	}
	
});

lib.TimeSelect.hourSelectTemplate = {tagName: "img", src: "../images/hodiny_v5.png", alt: "Click to select hours."};

lib.TimeSelect.hourSelectMapping = function (posX, posY) {

	var imageHourInnerDivision   = 20;
	var imageHourMiddleDivision  = 40;
	var imageHourOuterDivision   = 60;

	// prepare data for selecting hours
	var results = lib.TimeSelect._prepareForSelect(posX, posY);
	var a = results[0];
	var b = results[1];
	var c = results[2];
	var alpha = results[3]; 

	var hour;
	
	if (alpha === 0) {
		if (b < 0) {
			hour = 9;
		} else {
			hour = 3;
		} 
	} else {
		if ((alpha > 0) && (alpha < 0.25) && (b < 0)) { hour = 9; }
		else if ((alpha >= 0.25) && (alpha < 0.785) && (a < 0) && (b < 0)) { hour = 10; }
		else if ((alpha >= 0.785) && (alpha < 1.316) && (a < 0) && (b < 0)) { hour = 11; }
		else if ((alpha >= 1.316) && (a < 0)) { hour = 0; }
		else if ((alpha < 1.316) && (alpha >= 0.8) && (a < 0) && (b > 0)) { hour = 1; }
		else if ((alpha < 0.8) && (alpha >= 0.26) && (a < 0) && (b > 0)) { hour = 2; }
		else if ((alpha < 0.26) && (b > 0)) { hour = 3; }
		else if ((alpha >= 0.23) && (alpha < 0.799) && (a > 0) && (b > 0)) { hour = 4; }
		else if ((alpha >= 0.799) && (alpha < 1.316) && (a > 0) && (b > 0)) { hour = 5; }
		else if ((alpha >= 1.316) && (a > 0)) { hour = 6; }
		else if ((alpha <= 1.316) && (alpha > 0.772) && (a > 0) && (b < 0)) { hour = 7; }
		else if ((alpha <= 0.772) && (alpha > 0.25) && (a > 0) && (b < 0)) { hour = 8; }
	}
	
	if ( c > imageHourOuterDivision || c < imageHourInnerDivision) {
		hour = -1;
	} else if ( c > imageHourMiddleDivision ) {
		hour = hour + 12;
	}
	
	// creating selection place for X (cancel) button
	if (( b > 47 )&&( b < 60) && (a > -60) && (a < -47)) {
		hour = "exit";
	}
	
	return hour;
};

lib.TimeSelect.minuteSelectTemplate = {tagName: "img", src: "../images/minuty_v5.png", alt: "Click to select minutes."};

lib.TimeSelect.minuteSelectMapping = function (posX, posY) {

	var imageHourInnerDivision   = 20;
	var imageHourOuterDivision   = 60;

	// prepare data for selecting minutes
	var results = lib.TimeSelect._prepareForSelect(posX, posY);
	var a = results[0];
	var b = results[1];
	var c = results[2];
	var alpha = results[3]; 

	var minute;
	
	if (alpha === 0) {
		if (b < 0) {
			minute = 45;
		} else {
			minute = 15;
		} 
	} else {
		if ((alpha > 0) && (alpha < 0.32) && (b < 0)) { minute = 45; }
		else if ((alpha >= 0.32) && (alpha < 0.785) && (a < 0) && (b < 0)) { minute = 50; }
		else if ((alpha >= 0.785) && (alpha < 1.25) && (a < 0) && (b < 0)) { minute = 55; }
		else if ((alpha >= 1.25) && (a < 0)) { minute = 0; }
		else if ((alpha < 1.25) && (alpha >= 0.785) && (a < 0) && (b > 0)) { minute = 5; }
		else if ((alpha < 0.785) && (alpha >= 0.32) && (a < 0) && (b > 0)) { minute = 10; }
		else if ((alpha < 0.32) && (b > 0)) { minute = 15; }
		else if ((alpha >= 0.32) && (alpha < 0.785) && (a > 0) && (b > 0)) { minute = 20; }
		else if ((alpha >= 0.785) && (alpha < 1.25) && (a > 0) && (b > 0)) { minute = 25; }
		else if ((alpha >= 1.25) && (a > 0)) { minute = 30; }
		else if ((alpha <= 1.25) && (alpha > 0.785) && (a > 0) && (b < 0)) { minute = 35; }
		else if ((alpha <= 0.785) && (alpha > 0.32) && (a > 0) && (b < 0)) { minute = 40; }
	}
	
	if ( c > imageHourOuterDivision || c < imageHourInnerDivision) {
		minute = -1;
	}
	
	// creating selection place for X (cancel) button
	if (( b > 47 )&&( b < 60) && (a > -60) && (a < -47)) {
		minute = "exit";
	}
		return minute;
};

lib.TimeSelect._prepareForSelect = function (posX, posY) {

	var pos = [posX , posY]; // mouse Evt position relative to content zero
	var contentSize = [120, 120]; // size of the image
	var c = null;
	
	var getAngle = function (x, y) {
	
		// makes vector values positive
		var vectorDirectionPlus = [Math.max(x, -x), Math.max(y, -y)];
		
		// c2 = a2 + b2
		c = Math.sqrt(Math.pow(vectorDirectionPlus[0], 2) + Math.pow(vectorDirectionPlus[1], 2));
		
		// angle = asin (a/c)
		var angle = Math.asin( vectorDirectionPlus[1] / c ) ;	
		return angle;
	};
	
	var contentOrigin = [contentSize[0] / 2, contentSize[1] / 2];
	
	// gets a and b
	var vectorDirection = [ pos[0] - contentOrigin[0] , pos[1] - contentOrigin[1] ];
	var a = vectorDirection[1];
	var b = vectorDirection[0];			

	// gets alpha (rad)
	var alpha = getAngle(b, a);
	
	// creating results
	var results = [];
	results[0] = a;
	results[1] = b;
	results[2] = c;
	results[3] = alpha;
	
	return results;
};
