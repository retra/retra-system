// modalBox 0.1.0
// requires: lib.elm, lib.evt, lib.dhtml
// 
//	by Lokesh Dhakar - http://www.huddletogether.com
//	more info: http://huddletogether.com/projects/lightbox/
//
//	Licensed under the Creative Commons Attribution 2.5 License - http://creativecommons.org/licenses/by/2.5/
//	(basically, do anything you want, just leave my name and link)
// 
// provides the modal window

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.modalBox == "undefined") {
	lib.modalBox = {};
};




lib.modalBox.name = "lib.modalBox";
lib.modalBox.initEventPriority = 10;
lib.modalBox.openEventPriority = 10;
lib.modalBox.closeEventPriority = 10;

lib.modalBox.box       = {}; // the modal window
lib.modalBox.overlay   = {}; // semi-transparent overlay layer
lib.modalBox.hourglass = {}; // "wait" animation
lib.modalBox.closeElm  = {}; // closing object (usually a button)




lib.modalBox.box = {
	node: null,
	
	template: {
		id:           "jsModalBox",
		tagName:      "div",
		style: {
			position: "absolute",
			zIndex:   "100",
			display:  "none"
		}
	},
	
	visible: false,
	
	onClickEvtId: null
};


lib.modalBox.box.create = function () {
	// create the box
	// overlay must be created to perform this operation
	
	lib.modalBox.box.node = lib.elm.get(lib.modalBox.box.template.id);
	if (lib.modalBox.box.node == null) {
		// create the box
		lib.modalBox.box.node = lib.elm.create(lib.modalBox.box.template);
		lib.modalBox.overlay.node.appendChild(lib.modalBox.box.node);
	};
	return lib.modalBox.box.node; // the overlay has been created
};

lib.modalBox.box.center = function () {
	return lib.dhtml.center(lib.modalBox.box.node, false);
};

lib.modalBox.box.flush = function () {
	// removes the box content
	lib.modalBox.box.node.innerHTML = "";
	return true;
};

lib.modalBox.box.show = function () {
	lib.modalBox.box.center();
	lib.modalBox.box.visible = true;
	lib.modalBox.box.node.style.display = "";
	if (lib.modalBox.box.onClickEvtId == null) {
		lib.modalBox.box.onClickEvtId = lib.evt.add(lib.modalBox.box.node, "click", lib.modalBox.close);
	};
	return true;
};

lib.modalBox.box.hide = function () {
	lib.modalBox.box.visible = false;
	lib.modalBox.box.node.style.display = "none";
	if (lib.modalBox.box.onClickEvtId) {
		lib.evt.remove(lib.modalBox.box.onClickEvtId);
		lib.modalBox.box.onClickEvtId = null;
	};
	return true;
};

lib.modalBox.box.getSize = function () {
	var result = [0, 0];
	if (lib.modalBox.box.node != null) {
		result = lib.dhtml.getSize(lib.modalBox.box.node);
	};
	return result;
};




lib.modalBox.overlay = {
	node: null,
	
	template: {
		id:           "jsModalBoxOverlay",
		tagName:      "div",
		style: {
			position: "absolute",
			top:      "0",
			left:     "0",
			width:    "0", // this will be set later
			height:   "0", // this will be set later
			zIndex:   "90",
			display:  "none"
		}
	},
	
	visible: false,
	
	onResizeEvtId: null
};

lib.modalBox.overlay.create = function () {
	// create the overlay
	var objBody = lib.elm.findFirst({tagName: "body"});
	
	lib.modalBox.overlay.node = lib.elm.get(lib.modalBox.overlay.template.id);
	if (lib.modalBox.overlay.node == null) {
		// create overlay
		lib.modalBox.overlay.node = lib.elm.create(lib.modalBox.overlay.template);
		objBody.insertBefore(lib.modalBox.overlay.node, objBody.firstChild);
	};
	return lib.modalBox.overlay.node; // the overlay has been created
};

lib.modalBox.overlay.resize = function () {
	// resize to overlay the viewport
	var viewHeight = lib.dhtml.getViewSize()[1];
	var pageHeight = lib.dhtml.getPageSize()[1];
	var boxHeight = lib.modalBox.box.getSize()[1];
	var height = viewHeight;
	
/*
	var height = pageHeight; // 35 is magic number, don't ask me
	if (pageHeight < viewHeight) {
		height = viewHeight;
	};
*/
	lib.dhtml.setPos(lib.modalBox.overlay.node, [0, 0]); // set overlay position
	lib.dhtml.setSize(lib.modalBox.overlay.node, [0, height]); // set overlay height
	lib.modalBox.overlay.node.style.width = "100%"; // set overlay width
	// lib.modalBox.overlay.center();
	return true;
};

/*
lib.modalBox.overlay.center = function () {
	return lib.dhtml.center(lib.modalBox.overlay.node, true);
};
*/

lib.modalBox.overlay.show = function () {
	// show the overlay
	// TODO: resize the overlay to fit the viewport
	lib.modalBox.overlay.resize();
	lib.modalBox.overlay.visible = true;
	lib.modalBox.overlay.node.style.display = "";
	if (lib.modalBox.overlay.onResizeEvtId == null) {
		lib.modalBox.overlay.onResizeEvtId = lib.evt.add(window, "resize", lib.modalBox.overlay.resize);
	};
	return true;
};

lib.modalBox.overlay.hide = function () {
	// hide the overlay
	lib.modalBox.overlay.visible = false;
	lib.modalBox.overlay.node.style.display = "none";
	if (lib.modalBox.overlay.onResizeEvtId) {
		lib.evt.remove(lib.modalBox.overlay.onResizeEvtId);
		lib.modalBox.overlay.onResizeEvtId = null;
	};
	return true;
};




lib.modalBox.hourglass = {
	node: null,
	
	template: {
		id:           "jsModalBoxHourglass",
		tagName:       "div",
		innerText: "loading...", // !!! i18n - localize this
		style: {
			position:  "absolute", // position is absolute to lib.modalBox.box (!)
			top:       "0",
			left:      "0",
			width:     "auto",
			height:    "1.5em",
			zIndex:    "90",
			display:   "block"
		}
	},
	
	visible: false,
	
	onResizeEvtId: null,
	onClickEvtId: null
};

lib.modalBox.hourglass.create = function () {
	// create the hourglass element
	// overlay must be created to perform this operation
	
	lib.modalBox.hourglass.node = lib.elm.get(lib.modalBox.hourglass.template.id);
	if (lib.modalBox.hourglass.node == null) {
		// create the hourglass element
		lib.modalBox.hourglass.node = lib.elm.create(lib.modalBox.hourglass.template);
		lib.modalBox.overlay.node.appendChild(lib.modalBox.hourglass.node);
	};
	
	return lib.modalBox.hourglass.node; // the hourglass has been created
};

lib.modalBox.hourglass.center = function () {
	return lib.dhtml.center(lib.modalBox.hourglass.node, true);
};

lib.modalBox.hourglass.show = function () {
	// displays the hourglass
	lib.modalBox.hourglass.center();
	lib.modalBox.hourglass.visible = true;
	lib.modalBox.hourglass.node.style.display = "";
	if (lib.modalBox.hourglass.onResizeEvtId == null) {
		lib.modalBox.hourglass.onResizeEvtId = lib.evt.add(window, "resize", lib.modalBox.hourglass.center);
	};
	if (lib.modalBox.hourglass.onClickEvtId == null) {
		lib.modalBox.hourglass.onClickEvtId = lib.evt.add(lib.modalBox.hourglass.node, "click", lib.modalBox.close);
	};
	return true;
};
	
lib.modalBox.hourglass.hide = function () {
	// hides the hourglass
	lib.modalBox.hourglass.visible = false;
	lib.modalBox.hourglass.node.style.display = "none";
	if (lib.modalBox.hourglass.onResizeEvtId) {
		lib.evt.remove(lib.modalBox.hourglass.onResizeEvtId);
		lib.modalBox.hourglass.onResizeEvtId = null;
	};
	if (lib.modalBox.hourglass.onClickEvtId) {
		lib.evt.remove(lib.modalBox.hourglass.onClickEvtId);
		lib.modalBox.hourglass.onClickEvtId = null;
	};
	return true;
};




lib.modalBox.init = function () {
	lib.modalBox.overlay.create();
	lib.modalBox.hourglass.create();
	lib.modalBox.box.create();
	return true;
};

lib.modalBox.add = function (elm, evType, loadBoxContentFn) {
	// adds the modalBox functionality to specified element's event evType
	lib.data.set(elm, "loadBoxContentFn", loadBoxContentFn, lib.modalBox.name);
	var evId = lib.evt.add(elm, evType, lib.modalBox.open);
	return evId;
};

lib.modalBox.open = function (elm, e, evId) {
	lib.modalBox.overlay.show();
	lib.modalBox.hourglass.show();
	lib.evt.add(elm, "modalBoxOpen", lib.modalBox.onBoxOpen);
	
	var loadBoxContentFn = lib.data.get(elm, "loadBoxContentFn", lib.modalBox.name);
	loadBoxContentFn(elm, e, evId); // this function shall load the box content and then run the lib.modalBox.onBoxOpened method
	lib.evt.cancel(e);
	return true;
};

lib.modalBox.loadBoxContentImage = function (elm, e, evId) {
	// this is the sample function that loads the image
	var newImage = new Image;
	lib.modalBox.box.flush();
	lib.evt.add(newImage, "load", lib.modalBox.onBoxOpen);
	lib.modalBox.box.node.appendChild(newImage);
	newImage.src = elm.href;
	return true;
};

lib.modalBox.onBoxOpen = function (elm, e, evId) {
	lib.modalBox.hourglass.hide();
	lib.modalBox.box.show();
	lib.modalBox.overlay.resize();
	return true;
};

lib.modalBox.close = function () {
	lib.modalBox.box.hide();
	lib.modalBox.overlay.hide();
	return true;
};


