// lib.overlay 0.0.1
// requires: lib.elm, lib.evt, lib.dhtml
// 
//	by Lokesh Dhakar - http://www.huddletogether.com
//	more info: http://huddletogether.com/projects/lightbox/
//
//	Licensed under the Creative Commons Attribution 2.5 License - http://creativecommons.org/licenses/by/2.5/
//	(basically, do anything you want, just leave my name and link)
// 
// provides the overlay layer

var lib = lib || {};
lib.overlay = lib.overlay || {};




lib.overlay.name = "lib.overlay";
lib.overlay.node = null; // overlay HTML element
lib.overlay.template = {
	id:           "jsOverlay",
	tagName:      "div",
	style: {
		position: "absolute",
		top:      "0",
		left:     "0",
		width:    "0", // this will be set later
		height:   "0", // this will be set later
		zIndex:   "999",
		backgroundColor: "#000",
		filter:   "alpha(opacity=30)",
		opacity:  "0.30",
		display:  "none"
	}
};
lib.overlay.visible = false;
lib.overlay._onResizeEvtId = null;
lib.overlay._onMoveEvtId = null;




lib.overlay.create = function () {
	// create the overlay
	var objBody = lib.elm.findFirst({tagName: "body"});
	lib.overlay.node = lib.elm.get(lib.overlay.template.id);
	if (lib.overlay.node === null) {
		// create overlay
		lib.overlay.node = lib.elm.create(lib.overlay.template);
		objBody.insertBefore(lib.overlay.node, objBody.firstChild);
	}
	return lib.overlay.node; // the overlay has been created
};

lib.overlay.show = function () {
	// show the overlay
	// TODO: resize the overlay to fit the viewport
	lib.overlay.resize();
	lib.overlay.visible = true;
	lib.overlay.node.style.display = "";
	if (lib.overlay._onResizeEvtId === null) {
		lib.overlay._onResizeEvtId = lib.evt.add(window, "resize", lib.overlay.resize);
	}
	if (lib.overlay._onMoveEvtId === null) {
		lib.overlay._onMoveEvtId = lib.evt.add(window, "scroll", lib.overlay._correctSize);
	}
	return true;
};

lib.overlay.hide = function () {
	// hide the overlay
	lib.overlay.visible = false;
	lib.overlay.node.style.display = "none";
	if (lib.overlay.onResizeEvtId) {
		lib.evt.remove(lib.overlay.onResizeEvtId);
		lib.overlay.onResizeEvtId = null;
	}
	return true;
};
lib.overlay.resize = function () {
	// resize to overlay the viewport
	var viewSize = lib.dhtml.getPageSize();
	lib.dhtml.setPos(lib.overlay.node, [0, 0]);
	lib.overlay.node.style.height = viewSize[1] + "px";
	lib.overlay.node.style.width = "100%";
	return true;
};


lib.overlay._correctSize = function () {
	// resize overlay to fit window
	var pageSize = lib.dhtml.getPageSize();
	var viewSize = lib.dhtml.getViewSize();
	var viewPos = lib.dhtml.getViewPos();
	var overlaySize = lib.dhtml.getSize(lib.overlay.node);

	if ( (pageSize[1]) > overlaySize[1] ) {
		lib.overlay.node.style.height = pageSize[1] + "px";
	}
	if ( (viewSize[1] + viewPos[1]) > pageSize[1] ) {
		lib.overlay.node.style.height = (viewSize[1] + viewPos[1]) + "px";
	}

};
