// ModalBox 0.1.0
// requires: lib.elm, lib.evt, lib.dhtml
// 
//	by Lokesh Dhakar - http://www.huddletogether.com
//	more info: http://huddletogether.com/projects/lightbox/
//
//	Licensed under the Creative Commons Attribution 2.5 License - http://creativecommons.org/licenses/by/2.5/
//	(basically, do anything you want, just leave my name and link)
// 
// provides the modal window




var lib = lib || {};
lib.ModalBox = lib.ModalBox || {};




lib.ModalBox = lib.PopBox.extend({
	name: "lib.ModalBox",
	openBoxStyle: {
		zIndex: "1001",
		backgroundColor: "white",
		padding: "5px",
		cursor: "pointer"
	},
	
	_onResizeEvtId: null,
	_onMoveEvtId: null,
	

	init: function (boxContent) {
		lib.overlay.create();
		lib.PopBox.init.apply(this, [boxContent]);
		return true;
	},
	
	open: function (openingElm, e, evId) {
		lib.PopBox.open.apply(this, [openingElm, e, evId]);
		lib.dhtml.center(this.envelope);
		if (this._onResizeEvtId === null) {
			this._onResizeEvtId = lib.evt.add(window, "resize", this._center, null, this);
		}
		lib.elm.applyStyle(this.envelope, lib.ModalBox.openBoxStyle);
		lib.overlay.show();
		this.addCloseAction(lib.overlay.node, "click");
		// lib.evt.add(this.envelope, "click", resize);
		return true;
	},
	
	close: function (closingElm, e, evId) {
		lib.PopBox.close.apply(this, [closingElm, e, evId]);
		lib.overlay.hide();
		return true;
	},
	
	_center: function () {
		return lib.dhtml.center(this.envelope);
	}

});






/*

lib.modalBox.name = "lib.modalBox";
lib.modalBox.initEventPriority = 10;
lib.modalBox.openEventPriority = 10;
lib.modalBox.closeEventPriority = 10;

lib.modalBox.box       = {}; // the modal window
lib.modalBox.overlay   = {}; // semi-transparent overlay layer
lib.modalBox.hourglass = {}; // "wait" animation
lib.modalBox.closeElm  = {}; // closing object (usually a button)




lib.modalBox.init = function () {
	
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

*/
