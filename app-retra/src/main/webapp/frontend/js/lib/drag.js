// lib.drag 1.5.0
// requires: lib.data, lib.dhtml, lib.evt
//
// derived from dom-drag.js, www.youngpup.net

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.drag == "undefined") {
	lib.drag = {};
};




lib.drag.name =    "lib.drag"; // library name
lib.drag.obj       = null; // object that is being dragged right now
lib.drag.dragEvtId = null; // id of event that takes care of dragging
lib.drag.dropEvtId = null; // id of event that takes care of dropping




lib.drag.add = function (elm, handleElm, movementMappingFn) {
	// adds drag/drop functionality to elm
	// if handle is specified, obj may be only dragged by handle
	if ((typeof handleElm == "undefined") || (handleElm == null)) {
		handleElm = elm;
	};
	if ((typeof movementMappingFn == "undefined") || (movementMappingFn == null)) {
		movementMappingFn = lib.drag.defaultMovementMappingFn;
	};
	lib.data.set(handleElm, "object", elm, lib.drag.name);
	lib.data.set(elm, "object", elm, lib.drag.name);
	lib.data.set(elm, "handle", handleElm, lib.drag.name);
	lib.data.set(elm, "movementMappingFn", movementMappingFn, lib.drag.name);
	
//	var pos = lib.dhtml.getPos(elm);
//	lib.dhtml.setPos(elm, pos);
//	lib.drag.enable(elm);
	var startDragEvtId = lib.evt.add(handleElm, "mousedown", lib.drag.onMouseDown);
	lib.data.set(elm, "startDragEvtId", startDragEvtId, lib.drag.name);
	
	// custom ondrag event handlers - override them if you need them
	lib.evt.addType(elm, "dragstart");
	lib.evt.addType(elm, "dragend");
	lib.evt.addType(elm, "drag");
	return true;
};

lib.drag.remove = function (elm) {
	// removes the drag/drop functionality from the elm
	var handleElm = lib.data.get(elm, "handle", lib.drag.name);
	lib.data.set(handleElm, "object", null, lib.drag.name);
	lib.data.set(elm, "object", null, lib.drag.name);
	lib.data.set(elm, "handle", null, lib.drag.name);
	lib.data.set(elm, "movementMappingFn", null, lib.drag.name);
	
	var startDragEvtId = lib.data.get(elm, "startDragEvtId", lib.drag.name);
	lib.evt.remove(startDragEvtId);
	lib.data.set(elm, "startDragEvtId", null, lib.drag.name);
	
	return true;
};

lib.drag.onMouseDown = function (elm, e, evtId) {
	// find drag/drop object
	lib.drag._preventDragSelect(true);
	var obj = elm;
	while (lib.data.get(obj, "object", lib.drag.name) == null) {
		if (obj.parentNode) {
			obj = obj.parentNode;
		} else {
			return false; // failed searching
		};
	};
	obj = lib.data.get(obj, "object", lib.drag.name);
	lib.drag.obj = obj;
	
	// where the obj was clicked?
	var clickPos = lib.dhtml.getMouseEvtPos(e);
	var objPos = lib.dhtml.getPos(obj);
	
	var dragPos = lib.vector.getDirection(objPos, clickPos);
	lib.data.set(obj, "dragPos", dragPos, lib.drag.name);
	
	// run custom event
	lib.evt.run(obj, "dragstart");
	
	if (lib.drag.dragEvtId == null) {
		lib.drag.dragEvtId = lib.evt.add(document, "mousemove", lib.drag.onMouseMove);
	};
	if (lib.drag.dropEvtId == null) {
		lib.drag.dropEvtId = lib.evt.add(document, "mouseup", lib.drag.onMouseUp);
	};
	lib.evt.cancel(e);
	return true;
};

lib.drag.defaultMovementMappingFn = function(elm, mousePos) {
	// reads mouse coordinates and returns corresponding coordinates of the elm
	return mousePos;
};

lib.drag.onMouseMove = function (doc, e, evtId) {
	
	if (lib.drag.obj) {
		var obj = lib.drag.obj;
		
		// move obj
		lib.drag._drag(obj, e);
		
		// run custom event
		lib.evt.run(obj, "drag");
		
		return true;
	};
	lib.evt.cancel(e);
	return false;
};

lib.drag.onMouseUp = function (doc, e, evtId) {
	lib.drag._preventDragSelect(false);
	if (lib.drag.obj) {
		var obj = lib.drag.obj;
		
		// run custom event
		lib.evt.run(obj, "dragend");
		
		lib.drag._drop(obj, e);
		return true;
	};
	return false;
};

lib.drag._drag = function (elm, e) {
	var ePos = lib.dhtml.getMouseEvtPos(e);
	var clickPos = lib.data.get(elm, "dragPos", lib.drag.name);
	var mapPos = [ ePos[0] - clickPos[0], ePos[1] - clickPos[1] ];
	
	var movementMappingFn = lib.data.get(elm, "movementMappingFn", lib.drag.name);
	var newPos = movementMappingFn(elm, mapPos);
	
	lib.dhtml.setPos(elm, newPos);
	return true;
};

lib.drag._drop = function (obj, e) {
	// remove drag and drop event handlers
	lib.drag.obj = null;
	if (lib.drag.dragEvtId) {
		lib.evt.remove(lib.drag.dragEvtId);
		lib.drag.dragEvtId = null;
	};
	if (lib.drag.dropEvtId) {
		lib.evt.remove(lib.drag.dropEvtId);
		lib.drag.dropEvtId = null;
	};
	return true;
};

lib.drag._preventDragSelect = function (preventContentSelection) {
	// prevent IE text selection while dragging!!!
	// thanks to Todd Ditchendorf
	// http://www.ditchnet.org/wp/2005/06/15/ajax-freakshow-drag-n-drop-events-2/
	if (typeof document.body != "undefined") {
		if (typeof document.body.ondrag != "undefined") {
			document.body.ondrag = function () {return !preventContentSelection;}
		};
		if (typeof document.body.onselectstart != "undefined") {
			document.body.onselectstart = function () {return !preventContentSelection;}
		};
	};
	return true;
};
