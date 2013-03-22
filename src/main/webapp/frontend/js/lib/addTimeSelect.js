// lib.addTimeSelect 0.1.0
// requires: elm.js, array.js, string.js, data.js, dhtml.js, vector.js, evt.js, cls.js, object.js, Class.js, Box.js, Popbox.js, TimeSelect.js
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

var lib = lib || {};
lib.addTimeSelect = lib.addTimeSelect || {};




lib.addTimeSelect.name = "lib.addTimeSelect";




lib.addTimeSelect.addByElm = function (elm) {
	// adds Time Select feuture to element elm
	lib.TimeSelect.newInstance(elm);
	
	return true;
};

lib.addTimeSelect.addById = function (elmId) {
	// adds Time Select feature to element by its id
	lib.TimeSelect.newInstance(lib.elm.get(elmId));
	return true;
};

lib.addTimeSelect.addByClass = function (elmClassName) {
	// adds Time Select feature to elements by class name
	
	var elements = lib.elm.getByClass(elmClassName);
	var i, l;
	for (i = 0, l = elements.length; i < l; i++ ) {
		lib.TimeSelect.newInstance(elements[i]);
	}	
	return true;
};
