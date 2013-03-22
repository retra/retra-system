// lib.cls 1.0.0
// requires: nothing
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// element class manipulations
// written by FCZBKK - http://www.fczbkk.com

var lib = lib || {};
lib.cls = lib.cls || {};



lib.cls.name = "lib.cls";




lib.cls.get = function (elm) {
	// Array lib.cls.get(HtmlElement elm)
	// Returns all classes of the element elm as Array of Strings
	var cls = ""; // String
	if (elm && (typeof elm.className != "undefined")) {
		cls = elm.className.replace(/\s+/g, " ");
		if (cls === "") {
			return [];
		}
		return cls.split(" ");
	}
	return null;
};

lib.cls.has = function (elm, cls) {
	// Boolean lib.cls.has(HtmlElement elm, String cls)
	// Returns true if element elm contains the class cls
	
	var actCls, i, l;
	if ((typeof cls == "string") && (actCls = lib.cls.get(elm))) {
		for (i = 0, l = actCls.length; i < l; i++) {
			if (actCls[i] == cls) {
				return true;
			}
		}
	}
	return false;
};

lib.cls.add = function (elm, cls) {
	// Boolean lib.cls.add(HtmlElement elm, String cls)
	// Adds class cls to element elm if it is not present
	var actCls = lib.cls.get(elm);
	if (actCls !== null) {
		if (!lib.cls.has(elm, cls)) {
			elm.className += (actCls.length > 0) ? " " + cls : cls;
		}
	} else {
		elm.className = cls;
	}
	return true;
};

lib.cls.remove = function (elm, cls) {
	// Boolean lib.cls.remove(HtmlElement elm, String cls)
	// Removes class cls from element elm
	var actCls = lib.cls.get(elm); // String
	var tempCls = ""; // String
	var i, l;
	if ((typeof cls == "string") && (actCls !== null)) {
		for (i = 0, l = actCls.length; i < l; i++) {
			if (actCls[i] != cls) {
				if (tempCls !== "") {
					tempCls += " ";
				}
				tempCls += actCls[i];
			}
			elm.className = tempCls;
		}
		return true;
	}
	return false;
};

lib.cls.replace = function (elm, oldCls, newCls) {
	// Boolean lib.cls.replace(HtmlElement elm, String oldCls, String newCls)
	// Replaces class oldCls by class newCls. If there is no oldCls adds newCls.
	var actCls = lib.cls.get(elm); // String
	var tempCls = ""; // String
	var i, l;
	if ((typeof oldCls == "string") && (typeof newCls == "string") && (actCls !== null)) {
		if (lib.cls.has(elm, newCls)) {
			lib.cls.remove(elm, oldCls);
		} else if (lib.cls.has(elm, oldCls)) {
			for (i = 0, l = actCls.length; i < l; i++) {
				if (tempCls !== "") {
					tempCls += " ";
				}
				tempCls += (actCls[i] == oldCls) ? newCls : actCls[i];
			}
			elm.className = tempCls;
		} else {
			lib.cls.add(elm, newCls);
		}
		return true;
	}
	return false;
};
