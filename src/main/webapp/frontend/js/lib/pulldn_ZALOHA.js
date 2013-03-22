// lib.pulldn 0.0.1
// requires: lib.evt, lib.Box, lib.dhtml
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

var lib = lib || {};
lib.pulldn = lib.pulldn || {};
lib.pulldn.opener = lib.pulldn.opener || {};
lib.pulldn.pulldnBox = lib.pulldn.pulldnBox || {};




lib.pulldn.name = "lib.pulldn";
lib.pulldn.opener.openClassName = "libPulldnOpen";
lib.pulldn.pulldnBox.openClassName = "libPulldnOpen";







lib.pulldn.init = function () {
	
	return true;
};

lib.pulldn.add = function (elm, contentElm, chainId) {
	lib.elm.envelopeChildren(elm, lib.pulldn.opener.template);
	var contentBoxEnvelopeElm = lib.elm.create(lib.pulldn.pulldnBox.template, [contentElm]);
	elm.appendChild(contentBoxEnvelopeElm);
	
	lib.data.set(elm, "pulldnBoxElm", contentBoxEnvelopeElm, lib.pulldn.name );
	lib.data.set(contentBoxEnvelopeElm, "openerElm", elm, lib.pulldn.name);
	
	lib.data.set(elm, "openClassName", lib.pulldn.opener.openClassName, lib.pulldn.name );
	lib.data.set(contentBoxEnvelopeElm, "openClassName", lib.pulldn.pulldnBox.openClassName, lib.pulldn.name );
	
	lib.evt.add(elm, "mouseover", lib.pulldn.open);
	lib.evt.add(elm, "mouseout", lib.pulldn.close);
/*
	// do this as first
	lib.evt.add(elm, "mouseover", lib.toolTip._open);
	lib.evt.add(elm, "mouseout", lib.toolTip._close);
	
	if (!lib.toolTip.box) {
		lib.toolTip.box = lib.toolTip._createBox();
	}
	return true;
*/
	return chainId;
};

lib.pulldn._setOpen = function (elm) {
//	var openerOpenClassName = lib.data.get(elm, "openClassName", lib.pulldn.name );
openerOpenClassName = "libPulldnOpen";
	lib.cls.add(elm, openerOpenClassName);
	
	var contentOpenElm = lib.data.get(elm, "pulldnBoxElm", lib.pulldn.name );
//	var contentOpenClassName = lib.data.get(contentOpenElm, "openClassName", lib.pulldn.name );
contentOpenClassName = "libPulldnOpen";
	lib.cls.add(contentOpenElm, contentOpenClassName);
	
	return true;
};

lib.pulldn._setClosed = function (elm) {
//	var openerOpenClassName = lib.data.get(elm, "openClassName", lib.pulldn.name );
openerOpenClassName = "libPulldnOpen";
	lib.cls.remove(elm, openerOpenClassName);
	
	var contentOpen = lib.data.get(elm, "pulldnBoxElm", lib.pulldn.name );
contentOpenClassName = "libPulldnOpen";
	// var contentOpenClassName = lib.data.get(contentOpen, "openClassName", lib.pulldn.name );
	lib.cls.remove(contentOpen, contentOpenClassName);

	return true;
};

lib.pulldn._createPulldnBox = function (contentElm) {
/*
	// run onload !
	lib.toolTip.boxContentElm = lib.elm.create(lib.toolTip.boxContentTemplate);
	lib.toolTip.box = lib.Box.newInstance(lib.toolTip.boxContentElm);
	lib.toolTip.box.hide();
	lib.dhtml.setAbsolute(lib.toolTip.box.envelope);
	return lib.toolTip.box;
*/
	return pulldnBox;
};

lib.pulldn._setPulldnBoxPosition = function (elm, e) {
/*
	// set box position
	var mousePosition = lib.dhtml.getMouseEvtPos(e);
	mousePosition = lib.vector.add(mousePosition, lib.toolTip.translationVector);
	lib.dhtml.setPos( lib.toolTip.box.envelope, mousePosition );
	return mousePosition;
*/
};

lib.pulldn.open = function (elm, e, evId) {
	lib.pulldn._setOpen(elm);
/*
	var title, toolTipImgSrc;
	
	title = lib.elm.getAttribute(elm, "title");
	toolTipImgSrc = lib.elm.getAttribute(elm, lib.toolTip.toolTipImgAttribute);
	
	if (title || toolTipImgSrc) {
		lib.toolTip._updateBox(title, toolTipImgSrc);
		
		lib.toolTip.tooltipText = elm.title;
		elm.title = "";
		
		lib.toolTip._setBoxPosition(null, e);
		
		// move box when mouse moves
		lib.toolTip.mouseMoveEvId = lib.evt.add(elm, "mousemove", lib.toolTip._setBoxPosition); 
		
		// show the box
			lib.toolTip.box.show();
	} 
*/
	return true;
};

lib.pulldn.close = function (elm) {
	lib.pulldn._setClosed(elm);
/*	
	lib.toolTip.box.hide();
	try {
		lib.evt.remove(lib.toolTip.mouseMoveEvId);
	} catch (ex) {}
	
	if (lib.toolTip.tooltipText) {
		elm.title = lib.toolTip.tooltipText || "";
		lib.toolTip.tooltipText = null;
	}
*/
	return true;
};

lib.pulldn.closeChain = function (chainId) {
/*
		for (var i = 0, l = lib.pulldn.chains[chainId].length; i < l; i++) {
			lib.pulldn.close(lib.pulldn.chains[chainId][i]);
		};
*/
	return true;
};






lib.pulldn.opener.template = {
	tagName: "div",
//	style: { "border": "1px solid green" },
	children: [
		{tagName: "hr"},
		{contentNode: 0},
		{tagName: "hr"}
	]
};

lib.pulldn.pulldnBox.template = {
	tagName: "div",
/*
	style: {
		color: "#606060",
		border:"1px solid silver",
		backgroundColor: "snow",
		filter:"alpha(opacity=50)",
		opacity:"0.85",
		padding: "3px"
	},
*/	children: [
		{ contentNode: 0 }
	]
};
