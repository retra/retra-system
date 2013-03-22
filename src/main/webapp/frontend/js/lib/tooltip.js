// lib.tooltip 0.0.4
// requires: lib.evt, lib.Box, lib.dhtml
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

var lib = lib || {};
lib.tooltip = lib.tooltip || {};




lib.tooltip.name = "lib.tooltip";
lib.tooltip.tooltipImgAttribute = "tooltipImg";
lib.tooltip.box = null;
lib.tooltip.boxContentElm = null;
lib.tooltip.envelopeTemplate = {
	tagName:   "div",
	className: "jsLibTooltip",
	style: {
		zIndex: "2000"
	},
	children: [ {contentNode:0} ]
};
lib.tooltip.boxContentTemplate = {
	tagName: "div",
	style: {
		color: "#000",
		border:"1px solid #999",
		backgroundColor: "#fff",
		filter:"alpha(opacity=80)",
		opacity:"0.80",
		padding: "3px"
	},
	children: [
		{ contentNode: 1 },
		{ tagName: "div", children: [ { contentNode: 0 } ] }
	]
};
lib.tooltip.mouseMoveEvId = null;
lib.tooltip.tooltipText = null;
lib.tooltip.translationVector = [10, 10];




lib.tooltip.init = function () {

	return true;
};

lib.tooltip.add = function (elm) {
	// do this as first
	lib.evt.add(elm, "mouseover", lib.tooltip._open);
	lib.evt.add(elm, "mouseout", lib.tooltip._close);
	
	if (!lib.tooltip.box) {
		lib.tooltip.box = lib.tooltip._createBox();
	}
	
	return true;
};

lib.tooltip._createBox = function (envelopeTemplate) {
	// run onload !
	envelopeTemplate = envelopeTemplate || lib.tooltip.envelopeTemplate;
	lib.tooltip.boxContentElm = lib.elm.create(lib.tooltip.boxContentTemplate);
	lib.tooltip.box = lib.Box.newInstance(lib.tooltip.boxContentElm, null, envelopeTemplate);
	lib.tooltip.box.hide();
	lib.dhtml.setAbsolute(lib.tooltip.box.envelope);
	
	return lib.tooltip.box;
};

lib.tooltip._updateBox = function (title, tooltipImgSrc) {
	var titleNode = null;
	var imageNode = null;
	if (title) {
		titleNode = lib.elm.create( {innerText: title } );
	}
	if (tooltipImgSrc) {
		imageNode = lib.elm.create( {tagName:"img", src: tooltipImgSrc, alt: "image " + tooltipImgSrc});
	}
	var newBoxContentElm = lib.elm.create(lib.tooltip.boxContentTemplate, [titleNode, imageNode]);
	lib.tooltip.box.content.parentNode.replaceChild(newBoxContentElm, lib.tooltip.box.content);
	lib.tooltip.box.content = newBoxContentElm;
	return true;
};

lib.tooltip.remove = function (elm) {
	// TODO
	return true;
};


lib.tooltip._setBoxPosition = function (elm, e) {
	// set box position
	var mousePosition = lib.dhtml.getMouseEvtPos(e);
	mousePosition = lib.vector.add(mousePosition, lib.tooltip.translationVector);
	lib.dhtml.setPos( lib.tooltip.box.envelope, mousePosition );
	return mousePosition;
};

lib.tooltip._open = function (elm, e, evId) {
	var title, tooltipImgSrc;
	
	title = lib.elm.getAttribute(elm, "title");
	tooltipImgSrc = lib.elm.getAttribute(elm, lib.tooltip.tooltipImgAttribute);
	
	if (title || tooltipImgSrc) {
		lib.tooltip._updateBox(title, tooltipImgSrc);
		
		lib.tooltip.tooltipText = elm.title;
		elm.title = "";
		
		lib.tooltip._setBoxPosition(null, e);
		
		// move box when mouse moves
		lib.tooltip.mouseMoveEvId = lib.evt.add(elm, "mousemove", lib.tooltip._setBoxPosition); 
		
		// show the box
		lib.tooltip.box.show();
	}
	return true;
};

lib.tooltip._close = function (elm) {
	lib.tooltip.box.hide();
	try {
		lib.evt.remove(lib.tooltip.mouseMoveEvId);
	} catch (ex) {}
	
	if (lib.tooltip.tooltipText) {
		elm.title = lib.tooltip.tooltipText || "";
		lib.tooltip.tooltipText = null;
	}
	return true;
};
