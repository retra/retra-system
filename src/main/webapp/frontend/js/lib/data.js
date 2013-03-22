// lib.data 1.0.4
// requires: lib.array
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// allows to set and retrieve data from DOM elements as well as other object

var lib = lib || {};
lib.data = lib.data || {};




lib.data.name = "lib.data";
lib.data.attribute = "_jsData"; // name of DOM element attribute containing all data from sc framework
lib.data._flushData = []; // this is needed for flushing data on unload (to prevent memory leaks)




lib.data.set = function (elm, fieldName, data, nameSpace) {
	if (typeof nameSpace == "undefined") {
		nameSpace = lib.data.name;
	}
	if (typeof elm[lib.data.attribute] == "undefined") {
		elm[lib.data.attribute] = {};
		lib.array.push(lib.data._flushData, {elm: elm});
	}
	if (typeof elm[lib.data.attribute][nameSpace] == "undefined") {
		elm[lib.data.attribute][nameSpace] = {};
	}
	elm[lib.data.attribute][nameSpace][fieldName] = data;
	return true;
};

lib.data.get = function (elm, fieldName, nameSpace) {
	if (typeof nameSpace == "undefined") {
		nameSpace = lib.data.name;
	}
	var data = null;
	if (
		(typeof elm[lib.data.attribute] != "undefined") &&
		(typeof elm[lib.data.attribute][nameSpace] != "undefined") &&
		(typeof elm[lib.data.attribute][nameSpace][fieldName] != "undefined")
	) {
		data = elm[lib.data.attribute][nameSpace][fieldName];
	}
	return data;
};

lib.data.getFieldNames = function (elm, nameSpace) {
	var result = [];
	var fieldName;
	if (
		(typeof elm[lib.data.attribute] != "undefined") &&
		(typeof elm[lib.data.attribute][nameSpace] != "undefined")
	) {
		for (fieldName in elm[lib.data.attribute][nameSpace]) {
			if (
				(typeof elm[lib.data.attribute][nameSpace].hasOwnProperty != "undefined") &&
				(elm[lib.data.attribute][nameSpace].hasOwnProperty(fieldName) )
			) {
				result[result.length] = fieldName;
			}
		}
	}
	return result;
};

lib.data.flush = function () {
	// this shall be called on unload to prevent memory leaks in MSIE
	// this is called in lib.evt on unload by default
	var i, l;
	for (i = 0, l = lib.data._flushData.length; i < l; i++) {
		if (lib.data._flushData[i].elm) {
			lib.data._flushData[i].elm[lib.data.attribute] = null; // remove the reference
		}
	}
	return true;
};
