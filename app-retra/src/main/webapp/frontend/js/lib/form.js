// lib.form 1.2.1
// requires: lib.data, lib.string, lib.array, lib.elm, lib.evt, lib.field
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.form == "undefined") {
	lib.form = {};
};




lib.form.name                           = "lib.form";
lib.form.submitButtonClickEventPriority = 10;
lib.form.groupAttribute                 = "formGroups";
lib.form.invalidMsg                     = "Error: some form fields are not filled correctly"; // localize this




lib.form.init = function (validationRules) {
	
	if (typeof validationRules == "undefined") {
		validationRules = null;
	};
	lib.form.initInputs(validationRules);
	return true;
};

lib.form.initInputs = function (validationRules) {
	if (typeof validationRules == "undefined") {
		validationRules = null;
	};
	var inputFields = lib.elm.getByTag("input");
	if (inputFields) {
		for (var i = 0, l = inputFields.length; i < l; i++) {
			var fieldType = lib.field.getType(inputFields[i]);
			if (fieldType) {
				if (fieldType != "hidden") {
					lib.field.init(inputFields[i]);
					lib.form.addField(inputFields[i], validationRules);
				}
			} else {
				lib.form.addButton(inputFields[i]);
			}
		}
	};
	var buttons = lib.elm.getByTag("button");
	if (buttons) {
		for (var i = 0, l = buttons.length; i < l; i++) {
			lib.form.addButton(buttons[i]);
		}
	};
	var textAreas = lib.elm.getByTag("textarea");
	if (textAreas) {
		for (var i = 0, l = textAreas.length; i < l; i++) {
			lib.field.init(textAreas[i]);
			lib.form.addField(textAreas[i], validationRules);
		}
	};
	var selects = lib.elm.get("select");
	if (selects) {
		for (var i = 0, l = selects.length; i < l; i++) {
			lib.field.init(selects[i]);
			lib.form.addField(selects[i], validationRules);
		}
	};
	return true;
};

lib.form.addButton = function (buttonElm) {
	// update the list of buttons
	if (buttonElm.form) {
		// register the button into the form
		var formElm = buttonElm.form;
		var buttons = lib.data.get(formElm, "buttons", lib.form.name);
		if (buttons == null) {
			buttons = [];
		};
		lib.array.push(buttons, buttonElm);
		lib.data.set(formElm, "buttons", buttons, lib.form.name);
	};
	// identify type and add events
	var buttonType = lib.elm.getAttribute(buttonElm, "type");
	if (buttonType) {
		if ((buttonType == "submit") || (buttonType == "image")) {
			var evId = lib.evt.add(buttonElm, "click", lib.form.submitOnClick, lib.form.submitButtonClickEventPriority);
			lib.data.set(buttonElm, "submitClickEvId", evId, lib.form.name);
		}
	};
	return true;
};

lib.form.removeButton = function (buttonElm) {
	// removes the lib.form functionality from the button
	if (buttonElm.form) {
		var formElm = buttonElm.form;
		var buttons = lib.data.get(formElm, "buttons", lib.form.name);
		if (buttons != null) {
			lib.array.condense(buttons, buttonElm);
		};
		lib.data.set(formElm, "buttons", buttons, lib.form.name);
	};
	var evId = lib.data.get(buttonElm, "submitClickEvId", lib.form.name);
	if (evId != null) {
		lib.evt.remove(evId);
	};
	return true;
};

lib.form.addField = function (fieldElm, validationRules) {
	if (typeof validationRules == "undefined") {
		validationRules = null;
	};
	if (lib.field) {
		lib.field.addRuleSet(fieldElm, validationRules);
	};
	// add the field to the list
	if (fieldElm.form) {
// TODO - what about uncached variant ?
		var fields = lib.data.get(fieldElm.form, "fields", lib.form.name);
		if (fields == null) {
			fields = [];
		};
		lib.array.push(fields, fieldElm);
		lib.data.set(fieldElm.form, "fields", fields, lib.form.name);
		// add the field to the fieldgroup
		var formGroupNames = lib.form.getFormGroupNames(fieldElm);
		if (formGroupNames) {
			var formGroups = lib.data.get(fieldElm.form, "formGroups", lib.form.name);
			if (formGroups == null) {
				formGroups = {};
			}
			for (var i = 0, l = formGroupNames.length; i < l; i++) {
				if ( (typeof formGroups[formGroupNames[i]] == "undefined") || (formGroups[formGroupNames[i]] == null) ) {
					formGroups[formGroupNames[i]] = [];
				};
				lib.array.push(formGroups[formGroupNames[i]], fieldElm);
			};
			lib.data.set(fieldElm.form, "formGroups", formGroups, lib.form.name);
		}
	};
	return true;
};

lib.form.removeField = function (fieldElm) {
	// remove validation rules
	lib.field.removeRules(fieldElm);
	if (fieldElm.form) {
		// remove the field from the list
		var fields = lib.data.get(fieldElm.form, "fields", lib.form.name);
		if (fields == null) {
			fields = [];
		};
		fields = lib.array.condense(fields, fieldElm); // this removes the fieldElm and makes the field dense
		lib.data.set(fieldElm.form, "fields", fields, lib.form.name);
		// remove the field from the fieldgroup
		var formGroupNames = lib.form.getFormGroupNames(fieldElm);
		if (formGroupNames) {
			var formGroups = lib.data.get(fieldElm.form, "formGroups", lib.form.name);
			if (formGroups =! null) {
				for (var i = 0, l = formGroupNames.length; i < l; i++) {
					lib.array.condense(formGroups[formGroupNames[i]], fieldElm);
				};
				lib.data.set(fieldElm.form, "formGroups", formGroups, lib.form.name);
			}
		}
	};
	return true;
};

lib.form.getFormGroupNames = function (inputElm) {
	var groups = null;
	var groupsStr = lib.elm.getAttribute(inputElm, lib.form.groupAttribute);
	if (groupsStr != null) {
		groups = lib.string.getValue(groupsStr);
	};
	return groups;
};

lib.form.getFieldsByFormGroup = function (formElm, formGroupName, refreshCache) {
	if (typeof refreshCache == "undefined") { refreshCache = false; };
/* TODO - what if we have to refresh cache?
	var fieldGroups = lib.data.get(fieldElm, lib.field.name, "fieldGroups", lib.field.name); // quick identify
	if ((result == null) || refreshCache) {
	
	};
*/
	var formGroups = lib.data.get(formElm, "formGroups", lib.form.name);
	var fields = formGroups[formGroupName];
	return fields;
};

lib.form.check = function (formElm) {
	// check all form fields
	var fields = lib.data.get(formElm, "fields", lib.form.name);
	var result = true;
	if (fields != null) {
		for (var i = 0, l = fields.length; i < l; i++) {
			var field = fields[i];
			result = lib.field.check(field) && result;
		}
	};
	return result;
};

lib.form.getInvalidFields = function (formElm, formGroupNames) {
	// returns all invalid fields from selected groups. If no groups are selected, checks all fields
	if (typeof formGroupNames == "undefined") { formGroupNames = null; };
	var invalidFields = [];
	if (formGroupNames) {
		for (var i1 = 0, l1 = formGroupNames.length; i1 < l1; i1++) {
			var formGroupFields = lib.form.getFieldsByFormGroup(formElm, formGroupNames[i1]);
			for (var i2 = 0, l2 = formGroupFields.length; i2 < l2; i2++) {
				var field = formGroupFields[i2];
				if (!lib.field.check(field)) {
					lib.array.push(invalidFields, field);
				}
			}
		}
	} else { // no group names set, validate whole form
		var fields = lib.data.get(formElm, "fields", lib.form.name);
		if (fields != null) {
			for (var i = 0, l = fields.length; i < l; i++) {
				var field = fields[i];
				if (!lib.field.check(field)) {
					lib.array.push(invalidFields, field);
				}
			}
		}
	};
	return invalidFields;
};

lib.form.submitOnClick = function (buttonElm, e, evId) {
	// validates the form when the user hits "submit" button
	var isValid = true;
	
	if (lib.field) {
		var formGroupNames = lib.form.getFormGroupNames(buttonElm);
		var invalidFields = lib.form.getInvalidFields(buttonElm.form, formGroupNames);
		if (invalidFields.length > 0) {
			isValid = false;
		};
		if (!isValid) {
			lib.form.outputInvalidFieldsReport(invalidFields);
			lib.evt.cancel(e);
		}
	};
	return isValid;
};

lib.form.submit = function (formElm, formGroupNames) {
	// submits the form. If there are no formGroups specified, it validates all fields
	if (typeof formGroupNames == "undefined") { formGroupNames = null; };
	var isValid = true;
	if (lib.field) {
		var invalidFields = lib.form.getInvalidFields(formElm, formGroupNames);
		if (invalidFields.length > 0) { isValid = false; };
		if (!isValid) {
			lib.form.outputInvalidFieldsReport(invalidFields);
		}
	};
	if (isValid) {
		formElm.submit();
	};
	return isValid;
};

lib.form.outputInvalidFieldsReport = function () {
	alert(lib.form.invalidMsg);
	return true;
};
