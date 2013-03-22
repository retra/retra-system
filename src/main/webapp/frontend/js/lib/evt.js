// lib.evt 1.1.7
// requires: lib.array, lib.data
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// event handling function has three arguments - fn(elm, e, evtId)
// these arguments are filled automatically when the event is handled
// elm - trigger DOM element, e - triggering event, evtId - event handler ID
// 

var lib = lib || {};
lib.evt = lib.evt || {};




lib.evt.name = "lib.evt";
lib.evt.defaultPriority = 100; // when the priority is not set, sets this priority

lib.evt._handlers = []; // array storing all event handling functions. You can find them by evtId.
lib.evt._flushData = []; // this is needed for flushing event handlers on unload (to prevent memory leaks)




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

lib.evt.remove = function (evtId) {
	// removes the event specified by id
	var elm = lib.evt._handlers[evtId].elm;
	var evType = lib.evt._handlers[evtId].evType;
	lib.evt._handlers[evtId] = null;
	var evQue = lib.data.get(elm, evType, lib.evt.name);
	search:
		for (var i = 0, l = evQue.length; i < l; i++) {
			if (evQue[i] && (evQue[i].id == evtId)) {
				evQue[i] = null;
				break search;
			}
		}
//	lib.array.sortBy(evQue, "priority").reverse();
	return true;
};

lib.evt.getTarget = function (e) {
	// gets event target element
	var result = window;
	if (e.target) {
		result = e.target;
	} else if (e.srcElement) {
		result = e.srcElement;
	}
	if (result.nodeType && (result.nodeType == 3)) { // defeat Safari bug, thanks to PPK
		result = result.parentNode;
	}
	return result;
};

lib.evt.getType = function (e) {
	// returns the type of the event ("click", "submit" etc.)
	return e.type;
};

lib.evt.getPriority = function (evtId) {
	return lib.evt._handlers[evtId].priority;
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

lib.evt.getKey = function (e) {
	// if the event type is "keyPress", "keyDn" or "keyUp", returns the key code
	if (window.event) {
		return window.event.keyCode;
	} else if (e) {
		return e.which;
	} else {
		return null;
	}
};

lib.evt.getHandlerData = function (evtId) {
	// returns a reference to the data object that contains all object data
	return lib.evt._handlers[evtId];
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

lib.evt.flush = function () {
	// this shall be called on unload to prevent memory leaks in MSIE
	for (var i = 0, l = lib.evt._flushData.length; i < l; i++) {
		if (lib.evt._flushData[i].elm) {
			lib.evt._flushData[i].elm[lib.evt._flushData[i].evName] = null; // remove the event handler
		}
	}
	return true;
};

lib.evt.create = function (evType, props) {
	// creates and returns a custom event
	var evt = {};
	if (props) {
		evt = props;
	}
	evt.type = evType;
	evt.preventDefault = function () {}; // custom event doesn't bubble
	evt.isCustomEvent = true;
	return evt;
};

lib.evt.addType = function (elm, evType) {
	// makes the element capable to process another event type.
	if ((typeof elm["on" + evType] == "undefined") || (elm["on" + evType] === null)) {
		elm["on" + evType] = function () {}; // default handler (empty)
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

lib.evt._process = function (elm, e) {
	// process the event
	var evType = lib.evt.getType(e);
	var evQue = lib.data.get(elm, evType, lib.evt.name);
	
	// first, process preserved original event handler
	var fn = lib.data.get(elm, "on" + evType, lib.evt.name);
	if (fn !== null) {
		fn.apply(elm, [e]);
	}
	if (evQue) {
		// now process standard event queue
		processEvent:
			for (var i = 0, l = evQue.length; i < l; i++) {
				if (lib.data.get(e, "stop", lib.evt.name)) {
					break processEvent;
				} else if (evQue[i]) {
					evQue[i].fn.apply(evQue[i].context, [evQue[i].elm, e, evQue[i].id]);
				}
			}
	}
	return true;
};

lib.evt._handler = function (e) {
	if (typeof e == "undefined") { e = window.event; }
	var elm = this; // handler is attached directly to the trigger object
	return lib.evt._process(elm, e);
};




// prevent memory leaks
lib.evt.add(window, "unload", lib.data.flush);
lib.evt.add(window, "unload", lib.evt.flush);
