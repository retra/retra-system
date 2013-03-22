// lib.suggest 1.0.3
// requires: lib.data, lib.cls, lib.browser, lib.evt, lib.elm, lib.dhtml
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// Adds a "suggest" feature to a input field
// pulldown has class "libGuiSuggestPullDn" and may be styled by CSS

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.suggest == "undefined") {
	lib.suggest = {};
};




lib.suggest.name = "lib.suggest";
lib.suggest.cls         = "scGuiSuggest";
lib.suggest.optionsAttr = "scGuiSuggestOptions";
lib.suggest.maxItems    = 10; // default maximum, can be changed individually
lib.suggest.keyTimeout  = 100; // how long is the minimal interval between two keyboard events, in miliseconds

lib.suggest.openEventPriority  = 10,
lib.suggest.closeEventPriority = 10,
lib.suggest.keyEventPriority   = 10,
lib.suggest.clickEventPriority = 10,

lib.suggest.keyCodes = {
	right: 39,
	up:    38,
	down:  40,
	enter: 13,
	tab:   9,
	esc:   27
};


lib.suggest.opener = {
	open: {
		className: "scGuiSuggestOpen",
		style: {
			zIndex: 110
		}
	},
	
	closed: {
		className: "scGuiSuggestClosed",
		style: {
			zIndex: 10
		}
	}
};

lib.suggest.pullDn = {
	elm: {
		tagName: "div",
		className: "scGuiSuggestPullDn"
	},
	
	item: {
		elm: {
			tagName: "div",
			className: "scGuiSuggestItem"
		},
		
		active: {
			className: "scGuiSuggestActiveItem"
		}
	},
	
	envelope: {
		elm: {
			tagName: "span",
			style: {
				display:  "block",
				position: "absolute",
				top: "0",
				left: "0"
			}
		}
	},
	open: {
		style: {
			display: "block"
		}
	},
	closed: { style: { display: "none" } }
};

lib.suggest.envelope = {
	elm: {
		tagName: "span"
	}
};




lib.suggest.add = function (inputElm, options, clsPullDn, clsOpen, clsClosed, maxItems) {
	// array "options" contains all suggestions
	
	if ((typeof clsPullDn == "undefined") || (clsPullDn == null)) { clsPullDn = lib.suggest.pullDn.className; }
	if ((typeof clsOpen == "undefined") || (clsOpen == null)) { clsOpen = lib.suggest.opener.open.className; }
	if ((typeof clsClosed == "undefined") || (clsClosed == null)) { clsClosed = lib.suggest.opener.closed.className; }
	
	if ((typeof options == "undefined") || (options == null)) {
		var optionsStr = lib.elm.getAttribute(inputElm, lib.suggest.optionsAttr);
		if (optionsStr) {
			options = lib.string.getValue(optionsStr);
		} else {
			options = [];
		}
	};
	
	var env = lib.elm.envelope(inputElm, lib.suggest.envelope.elm);
	// HACK start: this is for Safari
	if (lib.browser.safari) { // really nasty
		env.style.display  = "inline-block";
		env.style.position = "relative";
	}; // HACK end
	
	var pullDn = lib.suggest._createPullDn(inputElm, clsPullDn, clsOpen, clsClosed);
	lib.data.set(inputElm, "pullDn", pullDn, lib.suggest.name);
	
	lib.suggest.setOptions(inputElm, options, maxItems);
	lib.suggest._addEvents(inputElm);
	
	// removes the default autocomplete feature
	inputElm.setAttribute("autocomplete", "off");
	
	return pullDn;
};

lib.suggest.setOptions = function (inputElm, options, maxItems) {
	if ((typeof maxItems == "undefined") || (maxItems <= 0)) {
		maxItems = lib.suggest.maxItems;
	};
	lib.data.set(inputElm, "maxItems", maxItems, lib.suggest.name);
	lib.data.set(inputElm, "options", options, lib.suggest.name);
	return true;
};

lib.suggest._addEvents = function (inputElm) {
	var openEventPriority = lib.suggest.openEventPriority;
	var closeEventPriority = lib.suggest.closeEventPriority;
	var keyEventPriority = lib.suggest.keyEventPriority;
	lib.evt.add(inputElm, "click", lib.suggest.open, openEventPriority);
	lib.evt.add(inputElm, "keyup", lib.suggest.open, openEventPriority);
	lib.evt.add(inputElm, "blur", lib.suggest.close, closeEventPriority);
	
	// allows to select items by cursor keys
	lib.evt.add(inputElm, "keydown", lib.suggest._onKeyDown, keyEventPriority);
	
	return true;
};

lib.suggest._createPullDn = function (inputElm, clsPullDn, clsOpen, clsClosed) {
	// create pulldown for elm
	// WARNING - elm.style.position will be set to "relative"
	
	var env = inputElm.parentNode; // this is the block enveloping the input
	var pullDnEnv = lib.elm.create(lib.suggest.pullDn.envelope.elm);
	env.appendChild(pullDnEnv);
	lib.data.set(inputElm, "pullDnEnv", pullDnEnv, lib.suggest.name);
	
	var pullDn = lib.elm.create(lib.suggest.pullDn.elm);
	pullDnEnv.appendChild(pullDn);
	lib.data.set(inputElm, "pullDn", pullDn, lib.suggest.name);
	
	lib.data.set(pullDn, "pullDnOpener", inputElm, lib.suggest.name);
	lib.data.set(inputElm, "clsPullDn", clsPullDn), lib.suggest.name;
	lib.cls.add(pullDn, clsPullDn);
	lib.data.set(inputElm, "clsOpen", clsOpen, lib.suggest.name);
	lib.data.set(inputElm, "clsClosed", clsClosed, lib.suggest.name);
	
	lib.suggest.close(inputElm);
	
	return pullDn;
};

lib.suggest._createItems = function (inputElm) {
	var pullDn = lib.data.get(inputElm, "pullDn", lib.suggest.name);
	pullDn.innerHTML = ""; // delete the content
	lib.data.set(inputElm, "activeItemIndex", null, lib.suggest.name);
	var options = lib.data.get(inputElm, "options", lib.suggest.name);
	var maxItems = lib.data.get(inputElm, "maxItems", lib.suggest.name);
	var items = new Array();
	lib.data.set(inputElm, "items", items, lib.suggest.name);
	var value = lib.elm.getValue(inputElm);
	var count = 0;
	addingItems:
		for (var i = 0, l = options.length; i < l; i++) {
			if ((value == "") || (options[i].indexOf(value) == 0)) {
				var newItem = lib.elm.create(lib.suggest.pullDn.item.elm);
				lib.array.push(items, newItem);
				newItem.innerHTML = options[i];
				lib.data.set(newItem, "inputElm", inputElm, lib.suggest.name);
				lib.data.set(newItem, "value", options[i], lib.suggest.name);
				pullDn.appendChild(newItem);
				lib.evt.add(newItem, "mousedown", lib.suggest.setValue, lib.suggest.clickEventPriority);
				count ++;
				if (count >= maxItems) {
					break addingItems;
				}
			}
		};
	return items;
};

lib.suggest._moveCursor = function (inputElm, keyCode) {
	var items = lib.data.get(inputElm, "items", lib.suggest.name);
	var activeItemIndex = lib.data.get(inputElm, "activeItemIndex", lib.suggest.name);
	if (items.length > 0) {
		
		if (activeItemIndex == null) {
			activeItemIndex = -1;
		};
		var recentActiveItemIndex = activeItemIndex;
		if (keyCode == lib.suggest.keyCodes.down) {
			// move active item 1 place down
			activeItemIndex = activeItemIndex + 1;
		} else if (keyCode == lib.suggest.keyCodes.up){
			// move active item 1 place up
			activeItemIndex = activeItemIndex - 1;
		};
		if (activeItemIndex < 0) {
			activeItemIndex = items.length - 1;
		};
		if (activeItemIndex >= items.length) {
			activeItemIndex = 0;
		};
		if (recentActiveItemIndex >= 0) {
			lib.cls.remove(items[recentActiveItemIndex], lib.suggest.pullDn.item.active.className);
		};
		lib.data.set(inputElm, "activeItemIndex", activeItemIndex, lib.suggest.name);
		lib.cls.add(items[activeItemIndex], lib.suggest.pullDn.item.active.className);
	};
	return activeItemIndex;
};

lib.suggest._onKeyDown = function (inputElm, e) {
	var keyCode = lib.evt.getKey(e);
	var items = lib.data.get(inputElm, "items", lib.suggest.name);
	var activeItemIndex = lib.data.get(inputElm, "activeItemIndex", lib.suggest.name);
	
	// move cursor
	if ((keyCode == lib.suggest.keyCodes.down) || (keyCode == lib.suggest.keyCodes.up)) {
		activeItemIndex = lib.suggest._moveCursor(inputElm, keyCode);
		lib.evt.cancel(e);
	};
	
	// select item
	if ((keyCode == lib.suggest.keyCodes.enter) || (keyCode == lib.suggest.keyCodes.right) || (keyCode == lib.suggest.keyCodes.tab)) {
		lib.elm.setValue(inputElm, lib.data.get(items[activeItemIndex], "value", lib.suggest.name));
		lib.evt.cancel(e);
	};
	
	return true;
};

lib.suggest.setValue = function (selectedItem) {
	var inputElm = lib.data.get(selectedItem, "inputElm", lib.suggest.name);
	var value = lib.data.get(selectedItem, "value", lib.suggest.name);
	lib.elm.setValue(inputElm, value);
	return true;
};

lib.suggest.open = function (inputElm, e) {
	var lastValue = lib.data.get(inputElm, "lastValue", lib.suggest.name);
	var currentValue = lib.elm.getValue(inputElm);
	if ((lastValue == null) || (lastValue != currentValue)) {
		var items = lib.suggest._createItems(inputElm);
			var pullDn = lib.data.get(inputElm, "pullDn", lib.suggest.name);
			if (items.length > 1) {
				lib.elm.applyStyle(pullDn, lib.suggest.opener.open.style);
				lib.suggest._openPullDn(inputElm);
			} else {
				lib.suggest.close(inputElm);
			}
			lib.data.set(inputElm, "lastValue", currentValue, lib.suggest.name);
	};
	return true;
};

lib.suggest._setPullDnPosition = function (inputElm) {
	// positions the pulldown block to the bottom left corner of the elm
	var pullDn = lib.data.get(inputElm, "pullDn", lib.suggest.name);
	var pullDnEnv = pullDn.parentNode;
	
	var pos = lib.dhtml.getPos(inputElm);
	var size = lib.dhtml.getSize(inputElm);
	var newPos = [pos[0], pos[1] + size[1]];
	lib.dhtml.setPos(pullDnEnv, newPos);
	
	return true;
};

lib.suggest._openPullDn = function (inputElm) {
	// open pulldn
	
	lib.suggest._setPullDnPosition(inputElm);
	
	var pullDn    = lib.data.get(inputElm, "pullDn", lib.suggest.name);
	var clsOpen   = lib.data.get(inputElm, "clsOpen", lib.suggest.name);
	var clsClosed = lib.data.get(inputElm, "clsClosed", lib.suggest.name);
	
	if (clsClosed != null) { lib.cls.remove(inputElm, clsClosed); }
	if (clsOpen != null) { lib.cls.add(inputElm, clsOpen); }
	lib.elm.applyStyle(inputElm, lib.suggest.opener.open.style);
	lib.elm.applyStyle(pullDn, lib.suggest.pullDn.open.style);
	
	return true;
};

lib.suggest.close = function (inputElm) {
	var pullDn = lib.data.get(inputElm, "pullDn", lib.suggest.name);
	lib.elm.applyStyle(pullDn, lib.suggest.pullDn.closed.style);
	lib.data.set(inputElm, "lastValue", null, lib.suggest.name);
	return true;
};
