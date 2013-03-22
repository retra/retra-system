// lib.showSwitch 0.0.6
// requires: lib.elm, lib.evt, lib.cls, lib.data, lib.field
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// turns on and off specified elements

if (typeof lib == "undefined") {
	var lib = {};
};




lib.showSwitch = {
	className:   "jsShowSwitch",
	onAttr:      "jsShowSwitchOn", // this attribute contains array of attributes that have to be turned on when box is checked
	offAttr:     "jsShowSwitchOff", // this attribute contains array of attributes that have to be turned on when box is unchecked
	states: {
		enabled:  { name: "enabled",  className: "jsShowSwitchEnabled",  disabled: false, oposite: "disabled" },
		disabled: { name: "disabled", className: "jsShowSwitchDisabled", disabled: true,  oposite: "enabled" }
	},
	
	switchControls: [],
	
	initForm: function (formElm) {
		if (typeof formElm.elements != "undefined") {
			var elements = formElm.elements;
			for (var i = 0, l = elements.length; i < l; i++) {
				if (lib.cls.has(elements[i], lib.showSwitch.className)) {
					lib.showSwitch.initControl(elements[i]);
				}
			};
			lib.evt.add(formElm, "reset", lib.showSwitch.run);
			lib.showSwitch.run(formElm);
			return true;
		};
		return false; // formElm not a form
	},
	
	initControl: function (fieldElm) {
		var fieldType = lib.field.identifyType(fieldElm);
		switch (fieldType) {
			case "select":
				lib.showSwitch.addControl(fieldElm);
				lib.evt.add(fieldElm, "change", lib.showSwitch.runSelect);
				lib.evt.add(fieldElm, "click", lib.showSwitch.runSelect);
				lib.evt.add(fieldElm, "keyup", lib.showSwitch.runSelect);
				break;
			case "checkBox":
				lib.showSwitch.addControl(fieldElm);
				lib.evt.add(fieldElm, "click", lib.showSwitch.runChkBox);
				break;
			case "radioButton":
				lib.showSwitch.addControl(fieldElm);
				lib.evt.add(fieldElm, "change", lib.showSwitch.runRadioBtn);
				lib.evt.add(fieldElm, "click", lib.showSwitch.runRadioBtn);
				break;
		};
		return true;
	},
	
	addControl: function (fieldElm) {
		// assign the element as a "showSwitcher"
		lib.cls.add(fieldElm, lib.showSwitch.className);
		lib.array.push(lib.showSwitch.switchControls, fieldElm);
		return true;
	},
	
	set: function (elm, statusName) {
		// enables or disables one block
		var opositeStatusName = lib.showSwitch.states[statusName].oposite;
		lib.cls.remove(elm, lib.showSwitch.states[opositeStatusName].className);
		lib.cls.add(elm, lib.showSwitch.states[statusName].className);
		var tagName = elm.tagName.toLowerCase();
		if ((tagName == "input") || (tagName == "select") || (tagName == "texatrea") || (tagName == "button")) {
			 elm.disabled = lib.showSwitch.states[statusName].disabled;
		} else {
			var inputTypes = ["input", "select", "textarea", "button"];
			for (var i = 0, l = inputTypes.length; i < l; i++) {
				var inputFields = lib.elm.getByTag(inputTypes[i], elm);
				for (var i2 = 0, l2 = inputFields.length; i2 < l2; i2++) {
					inputFields[i2].disabled = lib.showSwitch.states[statusName].disabled;
				}
			}
		};
		return true;
	},
	
	setBlocks: function (blockArray, statusName) {
		// enables or disables a bunch of blocks
		if (blockArray) {
			for (var i = 0, l = blockArray.length; i < l; i++) {
				var blockNode = lib.elm.get(blockArray[i]);
				if (blockNode != null) {
					lib.showSwitch.set(blockNode, statusName);
				}
			};
			return true;
		};
		return false;
	},
	
	runChkBox: function (chkBoxElm) {
		var blockIdArray = null;
		if (chkBoxElm.checked) {
			// turn off blocks from offAttr
			blockIdArray = lib.string.getValue(lib.elm.getAttribute(chkBoxElm, lib.showSwitch.offAttr));
			lib.showSwitch.setBlocks(blockIdArray, "disabled");
			
			// turn on blocks from onAttr
			blockIdArray = lib.string.getValue(lib.elm.getAttribute(chkBoxElm, lib.showSwitch.onAttr));
			lib.showSwitch.setBlocks(blockIdArray, "enabled");
		} else {
			// turn on blocks from offAttr
			blockIdArray = lib.string.getValue(lib.elm.getAttribute(chkBoxElm, lib.showSwitch.offAttr));
			lib.showSwitch.setBlocks(blockIdArray, "enabled");
			
			// turn off blocks from onAttr
			blockIdArray = lib.string.getValue(lib.elm.getAttribute(chkBoxElm, lib.showSwitch.onAttr));
			lib.showSwitch.setBlocks(blockIdArray, "disabled");

		};
		return true;
	},
	
	runSelect: function (selectElm) {
		var blockIdArray = null;
		// process the data from the SELECt tag
		blockIdArray = lib.string.getValue(lib.elm.getAttribute(selectElm, lib.showSwitch.offAttr));
		lib.showSwitch.setBlocks(blockIdArray, "disabled");
		blockIdArray = lib.string.getValue(lib.elm.getAttribute(selectElm, lib.showSwitch.onAttr));
		lib.showSwitch.setBlocks(blockIdArray, "enabled");
		
		// now turn on the selected
		for (var i = 0, l = selectElm.options.length; i < l; i++) {
			if (selectElm.options[i].selected) {
				// turn off blocks from offAttr
				blockIdArray = lib.string.getValue(lib.elm.getAttribute(selectElm.options[i], lib.showSwitch.offAttr));
				lib.showSwitch.setBlocks(blockIdArray, "disabled");
				
				// turn on blocks from onAttr
				blockIdArray = lib.string.getValue(lib.elm.getAttribute(selectElm.options[i], lib.showSwitch.onAttr));
				lib.showSwitch.setBlocks(blockIdArray, "enabled");
			}
		};
		return true;
	},
	
	runRadioBtn: function (radioBtnElm) {
		var blockIdArray = null;
		if (radioBtnElm.checked) {
			blockIdArray = lib.string.getValue(lib.elm.getAttribute(radioBtnElm, lib.showSwitch.offAttr));
			lib.showSwitch.setBlocks(blockIdArray, "disabled");
			blockIdArray = lib.string.getValue(lib.elm.getAttribute(radioBtnElm, lib.showSwitch.onAttr));
			lib.showSwitch.setBlocks(blockIdArray, "enabled");
		};
		return true;
	},
	
	run: function () {
		// switches all chkBoxes, radioButtons and selects in the whole form
		var elements = lib.showSwitch.switchControls;
		for (var i = 0, l = elements.length; i < l; i++) {
			var fieldElm = elements[i];
			if (lib.cls.has(fieldElm, lib.showSwitch.className)) {
				var fieldType = lib.field.identifyType(fieldElm);
				switch (fieldType) {
					case "select":
						lib.showSwitch.runSelect(fieldElm);
						break;
					case "checkBox":
						lib.showSwitch.runChkBox(fieldElm);
						break;
					case "radioButton":
						lib.showSwitch.runRadioBtn(fieldElm);
						break;
				};
			} // end if
		}; // end for
		return true;
	}
};
