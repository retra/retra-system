// lib.field 1.4.0
// requires: lib.data, lib.object, lib.string, lib.elm, lib.evt, lib.cls
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.field == "undefined") {
	lib.field = {};
};




lib.field.name               = "lib.field";
lib.field.invalidClass       = "invalid";
lib.field.configAttribute    = "libFieldCfg";
lib.field.checkEventPriority = 10;




lib.field.init = function (fieldElm) {
	// run this after the input field is created
	lib.data.set(fieldElm, "loadValue", lib.elm.getValue(fieldElm), lib.field.name);
	var type = lib.field.identifyType(fieldElm);
	// special init for radiobuttons
	if (type == "radioButton") {
		lib.field.acquireRadioSet(fieldElm);
	};
	lib.evt.add(fieldElm, "focus", lib.field.onFocus);
	// add check events
	var priority = lib.field.checkEventPriority;
	switch (type) {
		case "checkBox":
			lib.evt.add(fieldElm, "click", lib.field.onChange, priority);
			break;
		case "radioButton":
			var radioSet = lib.field.getRadioSet(fieldElm); // radioSet has been acquired at addRuleSet method
			for (var i = 0, l = radioSet.length; i < l; i++) {
				lib.evt.add(radioSet[i], "click", lib.field.onChange, priority);
			};
			break;
		case "select":
			lib.evt.add(fieldElm, "change", lib.field.onChange, priority);
			break;
		case "textField":
			lib.evt.add(fieldElm, "focus", lib.field.onChange, priority);
			lib.evt.add(fieldElm, "blur", lib.field.onChange, priority);
			lib.evt.add(fieldElm, "keyup", lib.field.onChange, priority);
			break;
	};
	return true;
};

lib.field.identifyType = function (fieldElm) {
	var tagName = lib.elm.getAttribute(fieldElm, "tagName").toLowerCase();
	var result = null;
	if (tagName == "select") {
		result = "select";
	} else if (tagName == "textarea") {
		result = "textField";
	} else {
		var type = lib.elm.getAttribute(fieldElm, "type").toLowerCase();
		if ((type == "text") || (type == "password")) {
			result = "textField";
		} else if (type == "checkbox") {
			result = "checkBox";
		} else if (type == "radio") {
			result = "radioButton";
		} else if (type == "hidden") {
			result = "hidden";
		};
	};
	if (result != null) {
		lib.data.set(fieldElm, "type", result, lib.field.name);
	};
	return result;
};

lib.field.getType = function (fieldElm, refreshCache) {
	if (typeof refreshCache == "undefined") { refreshCache = false; };
	var result = lib.data.get(fieldElm, "type", lib.field.name); // quick identify
	if ((result == null) || refreshCache) {
		result = lib.field.identifyType(fieldElm);
	};
	return result;
};

lib.field.isModified = function (fieldElm) {
	// was fieldElm changed since initialization?
	var loadValue = lib.data.get(fieldElm, "loadValue", lib.field.name);
	return (lib.elm.getValue(fieldElm) == loadValue);
};

lib.field.isEnabled = function (fieldElm) {
	var result = true;
	if (fieldElm.disabled) {
		result = false;
	};
	return result;
};

lib.field.acquireRadioSet = function (radioBtnElm) {
	// for radiobutton, returns whole set of radiobuttons with the same name
	var radioSet = [radioBtnElm]; // this is for case there is no form
	var name = lib.elm.getAttribute(radioBtnElm, "name");
	var type = lib.elm.getAttribute(radioBtnElm, "type").toLowerCase();
	if (name && (type == "radio") && radioBtnElm.form) {
		radioSet = radioBtnElm.form[name];
	};
	lib.data.set(radioBtnElm, "radioSet", radioSet, lib.field.name);
	return radioSet;
};

lib.field.getRadioSet = function (radioBtnElm, refreshCache) {
	// for radiobutton, returns whole set of radiobuttons with the same name 
	// use only if you are sure you have called acquireRadioSet before
	if (typeof refreshCache == "undefined") { refreshCache = false; };
	var result = lib.data.get(radioBtnElm, "radioSet", lib.field.name); // quick identify
	if ((result == null) || refreshCache) {
		result = lib.field.acquireRadioSet(radioBtnElm);
	};
	return result;
};

lib.field.readConfig = function (fieldElm) {
	// reads config from custom attribute and classNames
	var cfg = {};
	var fieldType = lib.field.getType(fieldElm);
	if (fieldType == "radioButton") {
		cfg = lib.field._readRadioSetConfig(fieldElm);
	} else {
		// get config from custom attribute
		var cfgString = lib.elm.getAttribute(fieldElm, lib.field.configAttribute);
		if (cfgString != null) {
			cfg = lib.string.getValue(cfgString);
		};
		// get config from classNames
		var classNames = lib.cls.get(fieldElm);
		for (var i = 0, l = classNames.length; i < l; i++) {
			if (typeof cfg[classNames[i]] == "undefined") {
				cfg[classNames[i]] = true;
			}
		};
		lib.data.set(fieldElm, "cfg", cfg, lib.field.name);
	};
	return cfg;
};

lib.field._readRadioSetConfig = function (fieldElm) {
	// read configuration from radiobutton set
	var cfg = {};
	var fields = lib.field.getRadioSet(fieldElm);
	// collect configuration data from all radiobuttons
	for (var i = 0, l = fields.length; i < l; i++) {
		var elm = fields[i];
		// get config from custom attribute
		var cfgString = lib.elm.getAttribute(elm, lib.field.configAttribute);
		if (cfgString != null) {
			var cfgObj = lib.string.getValue(cfgString);
			for (cfgName in cfgObj) {
				cfg[cfgName] = cfgObj[cfgName];
			}
		};
		// get config from classNames
		var classNames = lib.cls.get(elm);
		for (var i1 = 0, l1 = classNames.length; i1 < l1; i1++) {
			if (typeof cfg[classNames[i1]] == "undefined") {
				cfg[classNames[i1]] = true;
			}
		}
	};
	// bind configuration to all radiobuttons
	for (var i = 0, l = fields.length; i < l; i++) {
		lib.data.set(fields[i], "cfg", cfg, lib.field.name);
	};
	return cfg;
};

lib.field.getConfig = function (fieldElm) {
	// returns the element configuration read by last readCfg operation
	var result = lib.data.get(fieldElm, "cfg", lib.field.name);
	return result;
};

lib.field.getConfigRule = function (fieldElm, ruleName) {
	// returns a rule specified by ruleName
	var cfg = lib.field.getConfig(fieldElm);
	var result = null;
	if ( (typeof cfg[ruleName] != "undefined") && (cfg[ruleName] != null) ) {
		result = cfg[ruleName];
	};
	return result;
};

lib.field.setRule = function (fieldElm, ruleName, ruleObj) {
	// adds rule to the element
	var rules = lib.data.get(fieldElm, "rules", lib.field.name);
	if (rules == null) { rules = {}; };
	rules[ruleName] = lib.object.clone(ruleObj);
	var fieldType = lib.field.getType(fieldElm);
	if (fieldType == "radioButton") {
		// set rule to a set of radiobuttons
		var radioSet = lib.field.getRadioSet(fieldElm);
		for (var i = 0, l = radioSet.length; i < l; i++) {
			// all radiobuttons have common rules (no clonning needed)
			lib.data.set(radioSet[i], "rules", rules, lib.field.name);
		}
	} else {
		// set rule to normal input field
		lib.data.set(fieldElm, "rules", rules, lib.field.name);
	};
	return true;
};

lib.field.removeRule = function (fieldElm, ruleName) {
	// removes rule (identified by ruleName) from the element
	var rules = lib.data.get(fieldElm, "rules", lib.field.name);
	if ((rules != null) && (typeof rules[ruleName] != "undefined") && (rules[ruleName] != null)) {
		rules[ruleName] = null;
		return true; // rule has been removed
	};
	return false; // rule was not found
};

lib.field.getRules = function (fieldElm) {
	return lib.data.get(fieldElm, "rules", lib.field.name);
};

lib.field.addRuleSet = function (fieldElm, ruleSet, cloneRules) {
	// applies ruleSet to elm. Adds only validation rules that have some config rule
	if ((typeof cloneRules == "undefined") || (cloneRules == null)) {
		cloneRules = false;
	};
	var rules = lib.field.getRules(fieldElm);
	if (rules == null) { rules = {}; };
	var cfg = lib.field.readConfig(fieldElm);
	if (cfg) {
		for (ruleName in cfg) {
			if ((typeof ruleSet[ruleName] != "undefined") && (ruleSet[ruleName] != null)) {
				if (!cloneRules) {
					// add rules as reference links
					rules[ruleName] = ruleSet[ruleName];
				} else {
					// add rules as standalone objects
					rules[ruleName] = lib.object.clone(ruleSet[ruleName]);
				};
				// default value
				if (typeof rules[ruleName].def != "undefined") {
					lib.data.set(fieldElm, "defaultValue", rules[ruleName].def, lib.field.name);
				}
			}
		}
	};
	
	var fieldType = lib.field.getType(fieldElm);
	if (fieldType == "radioButton") {
		// set rules to a set of radiobuttons
		var radioSet = lib.field.getRadioSet(fieldElm);
		for (var i = 0, l = radioSet.length; i < l; i++) { 
			lib.data.set(radioSet[i], "rules", rules, lib.field.name);
		}
	} else {
		// set rules to normal input
		lib.data.set(fieldElm, "rules", rules, lib.field.name);
	};
	lib.field.check(fieldElm);
	return true;
};

lib.field.removeRules = function (fieldElm) {
	// removes all validation rules from the element elm
	lib.data.set(fieldElm, "rules", null, lib.field.name);
	return true;
};

lib.field.check = function (fieldElm, e, evId) {
	// checks field validity
	if (lib.field.isEnabled(fieldElm)) {
		var rules = lib.data.get(fieldElm, "rules", lib.field.name);
		if (rules != null) {
			var cfg = lib.field.getConfig(fieldElm);
			var result = true;
			for (ruleName in rules) {
				if ( (typeof rules[ruleName] != "undefined") && (rules[ruleName] != null) ) {
					// prepare check parameters
					var value = lib.elm.getValue(fieldElm);
					var cfgRule = null;
					if (typeof cfg[ruleName] != "undefined") {
						cfgRule = cfg[ruleName];
					};
					var checkFn = true, checkReg = true;
					// check by function
					if (rules[ruleName].fn) {
						checkFn = rules[ruleName].fn(value, fieldElm, cfgRule);
					};
					// check by regExp
					if (rules[ruleName].reg) {
						checkReg = (rules[ruleName].reg.test(value));
						// checkReg = (value.toString().search(rules[ruleName].reg) >= 0);
					};
					if (!checkFn || !checkReg) {
						lib.field.setInvalid(fieldElm, ruleName);
						return false;
					}
				}
			}
		}
	};
	lib.field.setValid(fieldElm);
	return true;
};

lib.field.setValid = function (fieldElm) {
	if (lib.field.getType(fieldElm) == "radioButton") {
		var radioSet = lib.field.getRadioSet(fieldElm);
		for (var i = 0, l = radioSet.length; i < l; i++) {
			lib.cls.remove(radioSet[i], lib.field.invalidClass);
			lib.data.set(radioSet[i], "invalidRuleName", null, lib.field.name);
		}
	} else {
		lib.cls.remove(fieldElm, lib.field.invalidClass);
		lib.data.set(fieldElm, "invalidRuleName", null, lib.field.name);
	};
	return true;
};

lib.field.setInvalid = function(fieldElm, invalidRuleName) {
	
	// disabled fields should not be set as invalid
	var isDisabled = (lib.elm.getAttribute(fieldElm, "disabled") == "disabled");
	if (!isDisabled) {
		if (lib.field.getType(fieldElm) == "radioButton") {
			var radioSet = lib.field.getRadioSet(fieldElm);
			for (var i = 0, l = radioSet.length; i < l; i++) {
				lib.cls.add(radioSet[i], lib.field.invalidClass);
				lib.data.set(radioSet[i], "invalidRuleName", invalidRuleName, lib.field.name);
			}
		} else {
			lib.cls.add(fieldElm, lib.field.invalidClass);
			lib.data.set(fieldElm, "invalidRuleName", invalidRuleName, lib.field.name);
		}
	};
	return true;
};

lib.field.isValid = function (fieldElm) {
	return !lib.cls.has(fieldElm, lib.field.invalidClass);
};

lib.field.getInvalidRuleName = function (fieldElm) {
	return lib.data.get(fieldElm, "invalidRuleName", lib.field.name);
};

lib.field.setDefaultValue = function (fieldElm) {
	var defaultValue = lib.data.get(fieldElm, "defaultValue", lib.field.name);
	if (defaultValue != null) {
		lib.elm.setValue(fieldElm, defaultValue);
	};
	return true;
};

lib.field.onFocus = function (fieldElm) {
	if (lib.string.trim(lib.elm.getValue(fieldElm)) == "") {
		// the field is empty
		lib.field.setDefaultValue(fieldElm);
		if (fieldElm.select) {
			fieldElm.select();
		};
		lib.field.check(fieldElm);
	};
	return true;
};

lib.field.onChange = function (fieldElm) {
	lib.field.check(fieldElm);
	return true;
};




// helper definitions
lib.field._df                    = {};
lib.field._df.specChars          = " \!\"\#\$\%\&\'\(\)\*\+\,\-\.\/\:\;\<\=\>\?\@\[\\\]\^\_\`\{\|\}\~\xA1-\xBF\xD7\xF7";
lib.field._df.alphaLowercase     = "a-z";
lib.field._df.alphaUppercase     = "A-Z";
lib.field._df.alpha              = lib.field._df.alphaLowercase + lib.field._df.alphaUppercase;
lib.field._df.numeric            = "0-9";
lib.field._df.alphaNum           = lib.field._df.numeric + lib.field._df.alpha;
lib.field._df.alphaLowercaseISO  = lib.field._df.alphaLowercase + lib.field._df.alphaUppercase + "\xDF\xE0\xE1\xE2\xE3\xE4\xE5\xE6\xE7\xE8\xE9\xEA\xEB\xEC\xED\xEE\xEF\xF0\xF1\xF2\xF3\xF4\xF5\xF6\xF8\xF9\xFA\xFB\xFC\xFD\xFE\xFF";
lib.field._df.alphaUppercaseISO  = "\xC0\xC1\xC2\xC3\xC4\xC5\xC6\xC7\xC8\xC9\xCA\xCB\xCC\xCD\xCE\xCF\xD0\xD1\xD2\xD3\xD4\xD5\xD6\xD8\xD9\xDA\xDB\xDE";
lib.field._df.alphaISO           = lib.field._df.alphaLowercaseISO + lib.field._df.alphaUppercaseISO;
lib.field._df.alphaNumISO        = lib.field._df.numeric + lib.field._df.alphaISO;
lib.field._df.alphaLowercaseUTF  = "\u00E1\u00E9\u0115\u00ED\u00F3\u00FA\u016F\u00FD\u017E\u0161\u010D\u0159\u010F\u0165\u0148" + lib.field._df.alphaLowercase;
lib.field._df.alphaUppercaseUTF  = "\u00C1\u00C9\u0114\u00CD\u00D3\u00DA\u016E\u00DD\u0160\u017D\u010c\u0158\u010E\u0164\u0147" + lib.field._df.alphaUppercase;
lib.field._df.alphaUTF           = lib.field._df.alphaLowercaseUTF + lib.field._df.alphaUppercaseUTF;
lib.field._df.alphaNumUTF        = lib.field._df.numeric + lib.field._df.alphaUTF;

// should accept IDN domain names
lib.field._df.urlDomain          = "([^"+lib.field._df.specChars+"]+([-_.]?[^"+lib.field._df.specChars+"]){1,}[.]([a-zA-Z]){2,4})";
lib.field._df.urlIp              = "((([0-9]){1,3}\.){3}([0-9]){1,3})";
lib.field._df.urlPath            = "(/([^ \t\n\r\f/])*)*";
lib.field._df.urlGet             = "([\?]{1}[^ \t\n\r\f]*)?";
lib.field._df.urlFragment        = "([\#]{1}([;/?:@&=+$,\-_.!~*'()a-zA-Z0-9]|(%[0-9a-fA-F]{2}))*)?";




// default rules (examples)
if (typeof lib.field.defaultRules == "undefined") {
	lib.field.defaultRules = {};
};

lib.field.defaultRules.url = {
	reg: new RegExp("^((http(s)?://(("+lib.field._df.urlDomain+")|("+lib.field._df.urlIp+"))){1}(:[0-9]{2,5})?("+lib.field._df.urlPath+")("+lib.field._df.urlGet+")("+lib.field._df.urlFragment+"))?$"),
	def: "http://",
	err: "Field {label} must contain valid URL (for example \"http://www.google.com\")."
};

lib.field.defaultRules.domainUrl = {
	reg: new RegExp("^(http(s)?://"+lib.field._df.urlDomain+")?$"),
	def: "http://",
	err: "Field {label} must contain valid domain name."
};

lib.field.defaultRules.phone = {
	reg: new RegExp("^[0-9()+-/ ]{0,16}$"),
	err: "Field {label} must contain a valid phone number."
};

lib.field.defaultRules.required = {
	fn:
		function (str) {
			return (str.search(/\S/) < 0) ? false : true;
		},
	err: "Field {label} must not be empty."
};

lib.field.defaultRules.notNegative = {
	fn: function (num) {
			return (num >= 0);
		},
	err: "Field {label} must contain positive numeric value or 0."
};

lib.field.defaultRules.positive = {
	fn: function (num) {
			return (num > 0);
		},
	err: "Field {label} must contain positive numeric value."
};
