// lib.TimeSelect.compact 0.1.0
// requires only images for timeselect mapping
//
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/




var lib = lib || {};
lib.TimeSelect = lib.TimeSelect || {};
lib.PopBox = lib.PopBox || {};
lib.Box = lib.Box || {};
lib.elm = lib.elm || {};
lib.evt = lib.evt || {};
lib.array = lib.array || {};
lib.dhtml = lib.dhtml || {};
lib.Class = lib.Class || {};
lib.object = lib.object || {};
lib.data = lib.data || {};
lib.vector = lib.vector || {};
lib.addTimeSelect = lib.addTimeSelect || {};
lib.cls = lib.cls || {};




lib.TimeSelect.name = "lib.TimeSelect";
lib.PopBox.name = "lib.PopBox";
lib.Box.name = "lib.Box";
lib.elm.name = "lib.elm";
lib.evt.name = "lib.evt";
lib.array.name = "lib.array";
lib.dhtml.name = "lib.dhtml";
lib.Class.name = "lib.Class";
lib.object.name = "lib.object";
lib.data.name = "lib.data";
lib.vector.name = "lib.vector";
lib.addTimeSelect.name = "lib.addTimeSelect";
lib.cls.name = "lib.cls";




lib.TimeSelect.openedTimeSelect = null;
lib.elm.xHtmlNameSpace = "http://www.w3.org/1999/xhtml";
lib.evt.defaultPriority = 100; // when the priority is not set, sets this priority
lib.evt._handlers = []; // array storing all event handling functions. You can find them by evtId.
lib.evt._flushData = []; // this is needed for flushing event handlers on unload (to prevent memory leaks)
lib.data.attribute = "_jsData"; // name of DOM element attribute containing all data from sc framework
lib.data._flushData = []; // this is needed for flushing data on unload (to prevent memory leaks)
lib.object.defaultDepth = "10";




// --- adding TimeSelect ---

lib.addTimeSelect.addByElm = function (elm) {
	// adds Time Select feuture to element elm
	lib.TimeSelect.newInstance(elm);
	
	return true;
};

lib.addTimeSelect.addById = function (elmId) {
	// adds Time Select feature to element by its id
	lib.TimeSelect.newInstance(lib.elm.get(elmId));
	return true;
};

lib.addTimeSelect.addByClass = function (elmClassName) {
	// adds Time Select feature to elements by class name
	
	var elements = lib.elm.getByClass(elmClassName);
	var i, l;
	for (i = 0, l = elements.length; i < l; i++ ) {
		lib.TimeSelect.newInstance(elements[i]);
	}	
	return true;
};




// --- lib.object ---

lib.object.clone = function (obj, depth) {
	// returns copy of the source object obj. 
	// If deep is set to true (default), all references are cloned, too. 
	// If deep is set to false, references are left intact.
	if (typeof depth != "number") {
		depth = lib.object.defaultDepth; // deep clonning is default
	}
	var result;
	if (typeof obj == "object") {
		result = {};
		for (var prop in obj) {
			if ((typeof obj[prop] != "object") || (depth < 1)) {
				// simple types are copied by assignment as well as too deep references
				result[prop] = obj[prop];
			} else {
				// referenced objects are cloned
				result[prop] = lib.object.clone(obj[prop], depth - 1);
			}
		}
	} else {
		result = obj;
	}
	return result;
};


 

// ---------------------------- Classes ----------------------------

// --- lib.Class ---

lib.Class.extend = function (newFeatures) {
	// create a new class (the child)
	var newClassObj = lib.object.clone(this);
	// insert own properties and methods, overriding inherited ones
	for (var prop in newFeatures) {
		newClassObj[prop] = newFeatures[prop];
	}
	return newClassObj;
};

lib.Class.newInstance = function () {
	var newInstanceObj = {};
	var prop;
	// insert inherited properties and methods
	for (prop in this) {
		if ((prop != "extend") && (prop != "newInstance")) {
			if (typeof this[prop] == "function") {
				// add method
				newInstanceObj[prop] = this[prop];
			} else {
				// add property
				if ( (this[prop] instanceof Object) || (this[prop] instanceof Array) ) {
					// assume the object is a JSON data object literal
					newInstanceObj[prop] = lib.object.clone(this[prop]);
				} else {
					// object is primitive type or general reference (except Object and Array)
					newInstanceObj[prop] = this[prop];
				}
				// the value of the instance property is not copied - it must be set in the constructor
				// if you need the static properties, you have to acces them Class.prop = ...
			}
		}
	}
	if (typeof newInstanceObj.init == "function") {
		newInstanceObj.init.apply(newInstanceObj, arguments);
	}
	return newInstanceObj;
};

lib.Class.init = function () {
	// default constructor
	return true;
};

// --- lib.box ---

lib.Box = lib.Class.extend({
	
	name: "lib.Box",
	
	// public properties
	content:  null,
	envelope: null,
	
	// class properties, don't use in instances
	boxNodes: [],
	envelopeTemplate: {
		tagName: "div",
		className: "jsLibBox",
		children: [ {contentNode:0} ]
	},
	defaultContentTemplate: {
		tagName: "div",
		className: "jsLibBoxContent"
	},
	
	// constructor
	init: function (contentElm, parentElm, envelopeTemplate, contentTemplate) {
		parentElm = parentElm || document.body;
		this.envelopeTemplate = envelopeTemplate || lib.Box.envelopeTemplate;
		contentTemplate = contentTemplate || lib.Box.defaultContentTemplate;
		
		lib.Class.init.apply(this, arguments);
		
		this.content = contentElm || lib.elm.create(contentTemplate);
		this.envelope = lib.elm.create(this.envelopeTemplate, [this.content]);
		parentElm.appendChild(this.envelope);
		
		// register the instance
		lib.array.insert(lib.Box.boxNodes, this);
		
		return true;
	},
	
	_addAction: function (elm, action, actionHandler, onActionFn) {
		// this helps to add some box actions to external elements
		// onActionFn receives following parameters:
		// - elm (the element that caused the event),
		// - e (the event itself),
		// - evId (event id),
		// - PopBox (this object)
		action = action || "click";
		actionHandler = actionHandler || function () {};
		onActionFn = onActionFn || function () {};
		var actionClosure = function (actionHandler, onActionFn, contextObj) {
			var actionFn = function (elm, e, evId) {
				actionHandler.apply(contextObj, [elm, e, evId]);
				onActionFn.apply(contextObj, [elm, e, evId, contextObj]);
				if (e) {
					lib.evt.cancel(e);
				}
				return true;
			};
			return actionFn;
		};
		return lib.evt.add(elm, action, actionClosure(actionHandler, onActionFn, this), null, this);
	},
	
	show: function () {
		this.envelope.style.display = "";
		return true;
	},
	
	hide: function () {
		this.envelope.style.display = "none";
		return true;
	},
	
	replaceContent: function (newContentElm) {
		this.content.parentNode.replaceChild(newContentElm, this.content);
		this.content = newContentElm;
		return true;
	}
});

// --- lib.PopBox ---

lib.PopBox = lib.Box.extend({
	
	name: "lib.PopBox",
	
	// public properties
	isOpen:   null,
	envelopeTemplate: {
		tagName:   "div",
		className: "jsLibPopBox",
		style: {
			zIndex: "1001"
		},
		children: [ {contentNode:0} ]
	},
	
	init: function (contentElm, parentElm, envelopeTemplate, contentTemplate) {
		parentElm = parentElm || document.body;
		envelopeTemplate = envelopeTemplate || lib.PopBox.envelopeTemplate;
		contentTemplate = contentTemplate || null;
		
		lib.Box.init.apply(this, [contentElm, parentElm, envelopeTemplate, contentTemplate]);
		
		// the box starts closed
		this.close(); // set the box closed
		
		// set custom event handlers
		lib.evt.addType(this, "boxopen");
		lib.evt.addType(this, "boxclose");
		
		// register the instance
		lib.array.insert(lib.PopBox.boxNodes, this);
		return true;
	},
	
	open: function (openingElm, e, evId) {
		e = e || null;
		this.envelope.style.display = "";
		var pos;
		if (e) {
			pos = lib.evt.getMousePosition(e);
			if (pos) {
				lib.dhtml.setAbsolute(this.envelope);
				lib.dhtml.setPos(this.envelope, [pos.x, pos.y]);
			}
		}
		lib.evt.run(this, "popboxopen");
		this.isOpen = true;
		if (e) {
			lib.evt.cancel(e);
		}
		return true;
	},
	
	close: function (closingElm, e, evId) {
		e = e || null;
		this.envelope.style.display = "none";
		lib.evt.run(this, "popboxclose");
		this.isOpen = false;
		if (e) {
			lib.evt.cancel(e);
		}
		return true;
	},
	
	toggle: function (togglingElm, e, evId) {
		if (this.isOpen) {
			this.close(togglingElm, e, evId);
		} else {
			this.open(togglingElm, e, evId);
		}
		return true;
	},
	
	addOpenAction: function (openerElm, action, onOpenFn) {
		onOpenFn = onOpenFn || null;
		return this._addAction(openerElm, action, this.open, onOpenFn);
	},
	
	addCloseAction: function (closerElm, action, onCloseFn) {
		onCloseFn = onCloseFn || null;
		return this._addAction(closerElm, action, this.close, onCloseFn);
	},
	
	addToggleAction: function (togglerElm, action, onToggleFn) {
		onToggleFn = onToggleFn || null;
		return this._addAction(togglerElm, action, this.toggle, onToggleFn);
	}
});

// --- lib.TimeSelect ---
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





// ---------------------------- support functions ----------------------------

// --- lib.elm ---

lib.elm.create = function (config, contentNodes) {
	// creates element tree defined by config object.
	contentNodes = contentNodes || [];
	config = config || {};
	var result = null;
	var i1, i2, i3, l2, l3;
	var child = null;
	var newElement = null;
	
	// config = { contentNode: x }
	var childConfig;
	var isChildContentNode;
	var contentNodesItem;
	
	if (typeof config.contentNode == "number") {
		if (contentNodes[config.contentNode]) {
			result = contentNodes[config.contentNode];
		} else {
			result = null;
		}
	} else { // config = { tagName: ... }
		config.innerText   = config.innerText || null;
		config.tagName     = config.tagName || null;
		if (config.innerText) {
			// create text node
			result = document.createTextNode(config.innerText);
		}
		if (config.tagName) {
			// create standard HTML element
			if (document.createElementNS) { // Gecko, standard
				newElement = document.createElementNS(lib.elm.xHtmlNameSpace, config.tagName);
			} else {
				newElement = document.createElement(config.tagName);
			}
			if (result !== null) {
				newElement.appendChild(result);
			}
			for (i1 in config) {
				switch (i1) {
					case "tagName": break; // already used
					case "innerText": break; // already used
					case "contentNode": break; // already used
					case "children": // create childNodes
						for (i2 = 0, l2 = config.children.length; i2 < l2; i2++) {
							childConfig = config.children[i2];
							
							isChildContentNode = (typeof childConfig.contentNode == "number") && (typeof contentNodes[childConfig.contentNode] == "object");
							
							if (isChildContentNode) {
								contentNodesItem = contentNodes[childConfig.contentNode];
								
								if (contentNodesItem instanceof Array) {
									// this means the actual contentNode is not 1 HTML element but an array of them
									for (i3 = 0, l3 = contentNodesItem.length; i3 < l3; i3++) {
										newElement.appendChild(contentNodesItem[i3]);
									}
								} else if (contentNodesItem !== null) {
									// this means the contentNode is single HTML element
									newElement.appendChild(contentNodesItem);
								}
							} else {
								child = lib.elm.create(childConfig, contentNodes); // child == null or array or html element
								if (child !== null) {
									newElement.appendChild(child);
								}
							}
						}
						break;
					case "style": // style definitions
						lib.elm.applyStyle(newElement, config[i1]);
						break;
					case "className":
						newElement.className = config[i1];
						break;
					case "htmlFor":
						newElement.setAttribute("for", config[i1]);
						break;
					default: // add standard attribute
						newElement.setAttribute(i1, config[i1]);
						break;
				}
			}
			if (newElement) {
				result = newElement;
			}
		}
	}
	return result;
};


lib.elm.getParent = function (elm) {
	// returns parent element. If there is no parent element it returns null
	elm = elm || null;
	elm = lib.elm.get(elm);
	if (typeof elm.parentNode == "undefined") {
		return null;
	} else {
		return elm.parentNode;
	}
};

lib.elm.get = function (obj) {
	// HtmlElement lib.elm.get(HtmlElement|String)
	// Returns HTML element that belongs to object obj.
	var result;
	if (typeof obj == "undefined") {
		result = null;
	} else if (typeof obj == "string") {
		// if obj is String, returns object with id obj
		return document.getElementById(obj);
	}
	return obj; // if obj is HtmlElement, returns it unchanged
};

lib.elm.applyStyle = function (elm, styleCfg) {
	// apply the style defined in JSON array on element
	elm = elm || null;
	elm = lib.elm.get(elm);
	for (var i in styleCfg) {
		if (typeof styleCfg[i] == "string") {
			elm.style[i] = styleCfg[i];
		}
	}
	return true;
};

lib.elm.getByClass = function (className, parentElm, tagName) {
	// not very effective. If you can, use only when parentElement and tagName is specified
	// thanx http://www.dustindiaz.com/getelementsbyclass for inspiration
	parentElm = parentElm || null;
	parentElm = (parentElm) ? lib.elm.get(parentElm) : document;
	var result = [];
	tagName = (tagName) ? tagName : "*";
	var allElm = lib.elm.getByTag(tagName, parentElm);
	var c = 0;
	for (var i = 0, l = allElm.length; i < l; i++ ) {
		if (lib.cls.has(allElm[i], className)) {
			result[c++] = allElm[i];
		}
	}
	return result;
};

lib.elm.getByTag = function (tagName, parentElm) {
	// returns array of elements with given tagName
	parentElm = parentElm || null;
	parentElm = (parentElm) ? lib.elm.get(parentElm) : document;
	var result = null;
	if (parentElm.all && (tagName == "*")) { // MSIE hack
		result = parentElm.all;
	}
	if (parentElm.getElementsByTagName) { // standard way
		result = parentElm.getElementsByTagName(tagName);
	}
	return result;
};

// --- lib.evt ---

lib.evt.add = function (elm, evType, fn, priority, callbackContext) {
	// Attaches event listener to element elm
	// returns event id number
	if ((typeof priority == "undefined") || (priority === null)) {
		priority = lib.evt.defaultPriority;
	}
	if ((typeof callbackContext == "undefined") || (callbackContext === null)) {
		callbackContext = elm;
	}
	
	// create a new queue - f (elm, evType)
	var evQue = lib.data.get(elm, evType, lib.evt.name);
	var evName;
	
	if (evQue === null) {
		evQue = [];
		lib.data.set(elm, evType, evQue, lib.evt.name);
		
		evName = "on" + evType;
		
		// if there is an event handler, preserve it
		if (typeof elm[evName] == "function") {
			lib.data.set(elm, evName, elm[evName], lib.evt.name);
		}
		
		// add a real event handler
		// don't use listeners, or you will be unable to handle custom events
		elm[evName] = lib.evt._handler; // add event handler
		lib.array.push(lib.evt._flushData, {elm: elm, evName: evName});
	}
	// add event data record to the queue 
	var handlerData = {
		elm:        elm,
		evType:     evType,
		priority:   priority,
		context:    callbackContext
	};
	var handlerId = lib.array.insert(lib.evt._handlers, handlerData); // register this handler
	handlerData.id = handlerId;
	handlerData.fn = fn;
	
	lib.array.push(evQue, handlerData);
	lib.array.sortBy(evQue, "priority"); // .reverse();  lowest priority goes first
	
	return handlerData.id;
};

lib.evt.addType = function (elm, evType) {
	// makes the element capable to process another event type.
	if ((typeof elm["on" + evType] == "undefined") || (elm["on" + evType] === null)) {
		elm["on" + evType] = function () {}; // default handler (empty)
	}
	return true;
};

lib.evt.getMousePosition = function (e) {
	// returns object { int x, int y } that contains the position of the mouse cursor on the screen
	// when the event has been triggered.
	if (!e) { e = window.event; }
	var pos = { x:0, y:0 };
	if (e.pageX || e.pageY) {
		// Mozilla etc.
		pos.x = e.pageX;
		pos.y = e.pageY;
	} else if (e.clientX || e.clientY) {
		// MSIE
		pos.x = e.clientX;
		pos.y = e.clientY;
		if ( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
			// DOM compliant
			pos.x += document.body.scrollLeft;
			pos.y += document.body.scrollTop;
		} else if ( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
			//IE6 standards compliant mode
			pos.x += document.documentElement.scrollLeft;
			pos.y += document.documentElement.scrollTop;
		}
	}
	return pos;
};

lib.evt.cancel = function (e) {
	// stops event propagation
	// and cancels processing of default event handler
	lib.evt.stop(e);
	if (e.preventDefault) {
		e.preventDefault();
	} else {
		e.returnValue = false;
	}
	return true;
};

lib.evt.run = function (elm, evType) {
	// runs event handlers of evType on element elm
	var evQue = lib.data.get(elm, evType, lib.evt.name);
	var e;
	if ((typeof evQue != "undefined") && (evQue !== null)) {
		e = lib.evt.create(evType);
		return lib.evt._process(elm, e);
	}
	return false;
};

lib.evt._handler = function (e) {
	if (typeof e == "undefined") { e = window.event; }
	var elm = this; // handler is attached directly to the trigger object
	return lib.evt._process(elm, e);
};

lib.evt._process = function (elm, e) {
	// process the event
	var evType = lib.evt.getType(e);
	var evQue = lib.data.get(elm, evType, lib.evt.name);
	
	// first, process preserved original event handler
	var fn = lib.data.get(elm, "on" + evType, lib.evt.name);
	if (fn !== null) {
		fn.apply(elm, [e]);
	}
	var i;
	if (evQue) {
		// now process standard event queue
		processEvent:
			for (i = 0, l = evQue.length; i < l; i++) {
				if (lib.data.get(e, "stop", lib.evt.name)) {
					break processEvent;
				} else if (evQue[i]) {
					evQue[i].fn.apply(evQue[i].context, [evQue[i].elm, e, evQue[i].id]);
				}
			}
	}
	return true;
};

lib.evt.getType = function (e) {
	// returns the type of the event ("click", "submit" etc.)
	return e.type;
};

lib.evt.stop = function (e) {
	// stops event propagation
	if (e.stopPropagation) {
		e.stopPropagation();
	}
	if (typeof e.cancelBubble == "boolean") {
		e.cancelBubble = true;
	}
	lib.data.set(e, "stop", true, lib.evt.name);
	return true;
};

// --- lib.dhtml ---

lib.dhtml.getMouseEvtPos = function (e) {
	// returns the position of the event generated by mouse in absolute coordinates
	var pos = lib.evt.getMousePosition(e);
	return [pos.x, pos.y];
};

lib.dhtml.getPos = function (elm) {
	// from http://www.quirksmode.org/js/findpos.html
	// many thanks to Peter Paul Koch
	// element's position relative to page
	var offsetModifier = lib.dhtml._getOffsetModifier(elm);
	var x = elm.offsetLeft + offsetModifier[0];
	var y = elm.offsetTop + offsetModifier[1];
	return [x, y];
};

lib.dhtml.setAbsolute = function (elm) {
	// changes the positioning of elm to absolute.
	elm.style.margin = "0"; // prevent positioning bugs
	elm.style.position = "absolute";
	return true;
};

lib.dhtml.setPos = function (elm, newPos) {
	// moves obj to specified coordinates
	var offsetModifier = lib.dhtml._getOffsetModifier(elm);
	var pos = lib.vector.subtract(newPos, offsetModifier);
	elm.style.left = pos[0] + "px";
	elm.style.top  = pos[1] + "px";
	return true;
};

lib.dhtml._getOffsetModifier = function (elm) {
	var result = [0, 0];
	var currentElm;
	if (elm.offsetParent) {
		currentElm = elm;
		while (currentElm.offsetParent) {
			currentElm = currentElm.offsetParent;
			result[0] += currentElm.offsetLeft;
			result[1] += currentElm.offsetTop;
		}
	} else if (typeof elm.x != "undefined") {
		result[0] = elm.x;
		result[1] = elm.y;
	}
	return result;
};

// --- lib.array ---

lib.array.insert = function (arr, newValue, emptyValue) {
	// inserts the newValue to array to first empty place (place that has value emptyValue)
	// if there is no such value, adds newValue to the end of the array --- RETURNS i (1)
	// if emptyValue is not specified, replaces "null" values by default -- RETURNS new arr.length
	if (typeof emptyValue == "undefined") {
		emptyValue = null;
	}
	var i, l;
	searchForHole:
		for (i = 0, l = arr.length; i < l; i++) {
			if (arr[i] == emptyValue) {
				break searchForHole;
			}
		}
	arr[i] = newValue;
	return i;
};

lib.array.push = function (arr, item) {
	// add an element to the end of an array, return the new array length
	var arg = arguments;
	var len = arr.length;
	for (var i = 1, l = arg.length; i < l; i++) {
		arr[len + i - 1] = arg[i];
	}
	return arr.length;
};

lib.array.sortBy = function (arr, indexName) {
	// sorts multidimensional array using index "indexName" as a key for sorting
	// elements that don't have this index are put to the end of the array
	var sortFn = function (a, b) {
		var result = 0;
		if (a && b && a[indexName] && b[indexName]) {
			if (a[indexName] < b[indexName]) {
				result = -1;
			} else if ( a[indexName] > b[indexName] ) {
				result = 1;
			}
		} else if (b && b[indexName]) {
			result = -1;
		} else if ((b === null) && (a !== null)) {
			result = -1;
		}
		return result;
	};
	return arr.sort(sortFn);
};

// --- lib.data ---

lib.data.set = function (elm, fieldName, data, nameSpace) {
	if (typeof nameSpace == "undefined") {
		nameSpace = lib.data.name;
	}
	if (typeof elm[lib.data.attribute] == "undefined") {
		elm[lib.data.attribute] = {};
		lib.array.push(lib.data._flushData, {elm: elm});
	}
	if (typeof elm[lib.data.attribute][nameSpace] == "undefined") {
		elm[lib.data.attribute][nameSpace] = {};
	}
	elm[lib.data.attribute][nameSpace][fieldName] = data;
	return true;
};

lib.data.get = function (elm, fieldName, nameSpace) {
	if (typeof nameSpace == "undefined") {
		nameSpace = lib.data.name;
	}
	var data = null;
	if (
		(typeof elm[lib.data.attribute] != "undefined") &&
		(typeof elm[lib.data.attribute][nameSpace] != "undefined") &&
		(typeof elm[lib.data.attribute][nameSpace][fieldName] != "undefined")
	) {
		data = elm[lib.data.attribute][nameSpace][fieldName];
	}
	return data;
};

// --- lib.vector ---

lib.vector.adjustDimension = function () {
	// modifies all vectors (arrays) entered as arguments to have the equal dimension
	var dimension = 0;
	var i, l, vector;
	for (i = 0, l = arguments.length; i < l; i++) {
		if (arguments[i].length > dimension) {
			dimension = arguments[i].length;
		}
	}
	for (i = 0, l = arguments.length; i < l; i++) {
		if (arguments[i].length < dimension) {
			vector = arguments[i];
			for (i = 0, l = dimension; i < l; i++) {
				if ((typeof vector[i] == "undefined") || (typeof vector[i] === null)) {
					vector[i] = 0;
				}
			}
		}
	}
	return true;
};

lib.vector.subtract = function (vectorA, vectorB) {
	// subtracts vector B from vector A, returns the final vector
	lib.vector.adjustDimension(vectorA, vectorB);
	var result = [];
	for (var i = 0, l = vectorA.length; i < l; i++) {
		result[i] = vectorA[i] - vectorB[i];
	}
	return result;
};

// --- lib.cls ---

lib.cls.get = function (elm) {
	// Array lib.cls.get(HtmlElement elm)
	// Returns all classes of the element elm as Array of Strings
	var cls = ""; // String
	if (elm && (typeof elm.className != "undefined")) {
		cls = elm.className.replace(/\s+/g, " ");
		if (cls === "") {
			return [];
		}
		return cls.split(" ");
	}
	return null;
};

lib.cls.has = function (elm, cls) {
	// Boolean lib.cls.has(HtmlElement elm, String cls)
	// Returns true if element elm contains the class cls
	
	var actCls, i, l;
	if ((typeof cls == "string") && (actCls = lib.cls.get(elm))) {
		for (i = 0, l = actCls.length; i < l; i++) {
			if (actCls[i] == cls) {
				return true;
			}
		}
	}
	return false;
}; 
