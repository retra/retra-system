// Hourglass 0.0.1
// requires: lib.ModalBox
//
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// provides the popup window




var lib = lib || {};
lib.Hourglass = lib.Hourglass || {};




lib.Hourglass = lib.ModalBox.extend({

	name: "lib.Hourglass",

	openBoxStyle: {
		zIndex: "1001",
		backgroundColor: "white",
		padding: "20px",
		cursor: "pointer",
	},

	defaultContentTemplate: {
		tagName: "img",
		className: "jsLibHourglass",
		src: "images/hourglass.gif",
		alt: "Please wait..."
	},
	
	init: function (parentElm, template) {
		parentElm = parentElm || document.body;
		template = template || lib.Hourglass.defaultContentTemplate;
		lib.ModalBox.init.apply(this, [null, parentElm, lib.Box.envelopeTemplate, template]);
		return true;
	},
	
	open: function (openingElm, e, evId) {
		lib.PopBox.open.apply(this, [openingElm, e, evId]);
		lib.dhtml.center(this.envelope);
		if (this._onResizeEvtId === null) {
			this._onResizeEvtId = lib.evt.add(window, "resize", this._center, null, this);
		}
		lib.elm.applyStyle(this.envelope, lib.Hourglass.openBoxStyle);
		lib.overlay.show();
		this.addCloseAction(this.envelope, "click");
		this.addCloseAction(lib.overlay.node, "click");
		return true;
	}

	

	

});
