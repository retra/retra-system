// lib.mouse 1.4.2
// requires: lib.evt, lib.cls
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// mouse related actions

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.mouse == "undefined") {
	lib.mouse = {};
};

if (typeof lib.mouse.hover == "undefined") {
	lib.mouse.hover = {};
};




lib.mouse.name = "lib.mouse";
lib.mouse.x = 0;
lib.mouse.y = 0;




lib.mouse.init = function () {
	// test if function has been called. If it was, exit.
	if (arguments.callee.done) {
		return false;
	} else {
		arguments.callee.done = true;
	};
	// track the mouse position
	lib.evt.add(document, "mousemove", lib.mouse._savePos);
	return true;
};

lib.mouse._savePos = function (doc, e) {
	var pos = lib.evt.getMousePosition(e);
	lib.mouse.x = pos.x;
	lib.mouse.y = pos.y;
	return true;
};

lib.mouse.getPos = function () {
	return [lib.mouse.x, lib.mouse.y];
};




// this adds the missing MSIE ability to generate :hover pseudoclass
lib.mouse.hover.className = "hover";




lib.mouse.hover.add = function (elm) {
	// adds hover functionality to element, returns event handler's id
	lib.evt.add(elm, "mouseover", lib.mouse.hover._set);
	lib.evt.add(elm, "mouseout",  lib.mouse.hover._clear);
	return true;
};

lib.mouse.hover._set = function (elm) {
	return lib.cls.add(elm, lib.mouse.hover.className);
};

lib.mouse.hover._clear = function (elm) {
	return lib.cls.remove(elm, lib.mouse.hover.className);
};




// use this if you need to track mouse movements:
// lib.evt.add(window, "load", lib.mouse.init);
