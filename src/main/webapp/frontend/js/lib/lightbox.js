// Hourglass 0.0.1
// requires: PopBox
//
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// provides the popup window

var lib = lib || {};
lib.Lightbox = lib.Lightbox || {};



lib.Lightbox = lib.ModalBox.extend({

/*	defaultContentTemplate: {
		tagName: "img",
		className: "jsLibHourglass",
		src: "images/hourglass.gif",
		alt: "Please wait..."
	}, */
	
	init: function () {
		// template = template || lib.Lightbox.defaultContentTemplate;
		var fakeContent = lib.elm.create( {tagName: "span", innerText: "fakeContent"} );
		lib.ModalBox.init.apply(this, [fakeContent]);
		return true;
	},
	
	open: function (openingElm, e, evId) {
		var src = lib.elm.getAttribute(openingElm, "href");
		var newContent = lib.elm.create({tagName: "img", src: src, alt: ""});
		var parentElm = this.envelope.parentNode;
		this.content = newContent; 
		var newEnvelope = lib.elm.create(this.envelopeTemplate, [this.content]);
		parentElm.replaceChild(newEnvelope, this.envelope);
		this.envelope = newEnvelope;
		lib.ModalBox.open.apply(this, [openingElm, e, evId]);
		return true;
	}

});
