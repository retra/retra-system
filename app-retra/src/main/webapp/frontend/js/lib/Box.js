// Box 0.0.3
// requires: lib.Class, lib.elm, lib.evt, lib.dhtml, 
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// provides the popup window

var lib = lib || {};
lib.Box = lib.Box || {};




lib.Box = lib.Class.extend({
	
	name: "lib.Box",
	
	// public properties
	content:  null,
	envelope: null,
	
	// class properties, don't use in instances
	boxNodes: [],
	envelopeTemplate: {
		tagName: "div",
		className: "jsLibBox",
		children: [ {contentNode:0} ]
	},
	defaultContentTemplate: {
		tagName: "div",
		className: "jsLibBoxContent"
	},
	
	// constructor
	init: function (contentElm, parentElm, envelopeTemplate, contentTemplate) {
		parentElm = parentElm || document.body;
		this.envelopeTemplate = envelopeTemplate || lib.Box.envelopeTemplate;
		contentTemplate = contentTemplate || lib.Box.defaultContentTemplate;
		
		lib.Class.init.apply(this, arguments);
		
		this.content = contentElm || lib.elm.create(contentTemplate);
		this.envelope = lib.elm.create(this.envelopeTemplate, [this.content]);
		parentElm.appendChild(this.envelope);
		
		// register the instance
		lib.array.insert(lib.Box.boxNodes, this);
		
		return true;
	},
	
	_addAction: function (elm, action, actionHandler, onActionFn) {
		// this helps to add some box actions to external elements
		// onActionFn receives following parameters:
		// - elm (the element that caused the event),
		// - e (the event itself),
		// - evId (event id),
		// - PopBox (this object)
		action = action || "click";
		actionHandler = actionHandler || function () {};
		onActionFn = onActionFn || function () {};
		var actionClosure = function (actionHandler, onActionFn, contextObj) {
			var actionFn = function (elm, e, evId) {
				actionHandler.apply(contextObj, [elm, e, evId]);
				onActionFn.apply(contextObj, [elm, e, evId, contextObj]);
				if (e) {
					lib.evt.cancel(e);
				}
				return true;
			};
			return actionFn;
		};
		return lib.evt.add(elm, action, actionClosure(actionHandler, onActionFn, this), null, this);
	},
	
	show: function () {
		this.envelope.style.display = "";
		return true;
	},
	
	hide: function () {
		this.envelope.style.display = "none";
		return true;
	},
	
	replaceContent: function (newContentElm) {
		this.content.parentNode.replaceChild(newContentElm, this.content);
		this.content = newContentElm;
		return true;
	}
});
