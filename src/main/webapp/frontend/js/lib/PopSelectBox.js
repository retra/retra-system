// popSelectBox 0.0.1
// requires: lib.elm, lib.evt, lib.dhtml, lib.Class, lib.PopBox
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// provides the popup select window

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.PopBox == "undefined") {
	lib.PopSelectBox = {};
};






lib.PopSelectBox = lib.PopBox.extend({
	options: null,
	valueInput: null,
	valueLabel: null,
	
	activeItemCls: "jsLibPopSelectOptionActive",
	changedLabelCls: "jsLibPopSelectOptionChanged",
	changedLabelTitle: "This item has been changed",
	
	init: function (parentElm, valueInput, valueLabel, optionsArray) {
		this.options = optionsArray || [];
		this.valueInput = valueInput || null;
		this.valueLabel = valueLabel || null;
		if (this.valueInput) {
			var content = this._createContent();
			lib.PopBox.init.apply(this, [content, parentElm]);
		};
		return true;
	},
	
	_createContent: function () {
		// creates the list
		var ul = lib.elm.create({tagName: "ul"});
		for (var i = 0, l = this.options.length; i < l; i++) {
			var li = lib.elm.create({tagName: "li", innerText: this.options[i][1], value: this.options[i][0] });
			lib.evt.add(li, "mouseover", function (elm) { return lib.cls.add(elm, lib.PopSelectBox.activeItemCls); } );
			lib.evt.add(li, "mouseout", function (elm) { return lib.cls.remove(elm, lib.PopSelectBox.activeItemCls); } );
			ul.appendChild(li);
			
			var updateClosure = function (inputElm, labelElm, value, labelText) {
				var updateFn = function (elm, e, evId) {
					lib.elm.setValue(inputElm, value);
					labelElm.innerHTML = labelText;
					lib.cls.add(labelElm, lib.PopSelectBox.changedLabelCls);
					labelElm.title = lib.PopSelectBox.changedLabelTitle;
					return true;
				};
				return updateFn;
			};
			update = updateClosure(this.valueInput, this.valueLabel, this.options[i][0], this.options[i][1]);
			this.addCloseAction(li, "click", update);
		};
		return ul;
	},
	
	_addAction: function (elm, action, actionHandler, onActionFn) {
		if (this.valueInput) {
			lib.PopBox._addAction.apply(this, arguments);
		};
		return true;
	}
});
