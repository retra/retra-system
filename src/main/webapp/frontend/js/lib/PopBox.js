// popBox 0.0.6
// requires: lib.elm, lib.evt, lib.dhtml, lib.Class, lib.Box
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// provides the popup window

var lib = lib || {};
lib.PopBox = lib.PopBox || {};




lib.PopBox = lib.Box.extend({
	
	name: "lib.PopBox",
	
	// public properties
	isOpen:   null,
	envelopeTemplate: {
		tagName:   "div",
		className: "jsLibPopBox",
		style: {
			zIndex: "1001"
		},
		children: [ {contentNode:0} ]
	},
	
	init: function (contentElm, parentElm, envelopeTemplate, contentTemplate) {
		parentElm = parentElm || document.body;
		envelopeTemplate = envelopeTemplate || lib.PopBox.envelopeTemplate;
		contentTemplate = contentTemplate || null;
		
		lib.Box.init.apply(this, [contentElm, parentElm, envelopeTemplate, contentTemplate]);
		
		// the box starts closed
		this.close(); // set the box closed
		
		// set custom event handlers
		lib.evt.addType(this, "boxopen");
		lib.evt.addType(this, "boxclose");
		
		// register the instance
		lib.array.insert(lib.PopBox.boxNodes, this);
		return true;
	},
	
	open: function (openingElm, e, evId) {
		e = e || null;
		this.envelope.style.display = "";
		if (e) {
			var pos = lib.evt.getMousePosition(e);
			if (pos) {
				lib.dhtml.setAbsolute(this.envelope);
				lib.dhtml.setPos(this.envelope, [pos.x, pos.y]);
			}
		};
		lib.evt.run(this, "popboxopen");
		this.isOpen = true;
		if (e) {
			lib.evt.cancel(e);
		};
		return true;
	},
	
	close: function (closingElm, e, evId) {
		e = e || null;
		this.envelope.style.display = "none";
		lib.evt.run(this, "popboxclose");
		this.isOpen = false;
		if (e) {
			lib.evt.cancel(e);
		};
		return true;
	},
	
	toggle: function (togglingElm, e, evId) {
		if (this.isOpen) {
			this.close(togglingElm, e, evId);
		} else {
			this.open(togglingElm, e, evId);
		};
		return true;
	},
	
	addOpenAction: function (openerElm, action, onOpenFn) {
		onOpenFn = onOpenFn || null;
		return this._addAction(openerElm, action, this.open, onOpenFn);
	},
	
	addCloseAction: function (closerElm, action, onCloseFn) {
		onCloseFn = onCloseFn || null;
		return this._addAction(closerElm, action, this.close, onCloseFn);
	},
	
	addToggleAction: function (togglerElm, action, onToggleFn) {
		onToggleFn = onToggleFn || null;
		return this._addAction(togglerElm, action, this.toggle, onToggleFn);
	}
});
