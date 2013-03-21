// lib.Class 1.4.0
// requires: lib.object
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// Inheritance support
// 
// reserved properties:
//     _parent - reference to parent class. May not be used in Class definition.
// 
// reserved methods:
//     extend - is used to extend the class. May not be used in Class definition.
//     newInstance - is used to create object instance. May not be used in Class definition.
//     init - constructor. Is used automatically when creating the instance

var lib = lib || {};
lib.Class = lib.Class || {};




lib.Class.extend = function (newFeatures) {
	// create a new class (the child)
	var newClassObj = lib.object.clone(this);
	// insert own properties and methods, overriding inherited ones
	for (var prop in newFeatures) {
		newClassObj[prop] = newFeatures[prop];
	}
	return newClassObj;
};

lib.Class.newInstance = function () {
	var newInstanceObj = {};
	var prop;
	// insert inherited properties and methods
	for (prop in this) {
		if ((prop != "extend") && (prop != "newInstance")) {
			if (typeof this[prop] == "function") {
				// add method
				newInstanceObj[prop] = this[prop];
			} else {
				// add property
				if ( (this[prop] instanceof Object) || (this[prop] instanceof Array) ) {
					// assume the object is a JSON data object literal
					newInstanceObj[prop] = lib.object.clone(this[prop]);
				} else {
					// object is primitive type or general reference (except Object and Array)
					newInstanceObj[prop] = this[prop];
				}
				// the value of the instance property is not copied - it must be set in the constructor
				// if you need the static properties, you have to acces them Class.prop = ...
			}
		}
	}
	if (typeof newInstanceObj.init == "function") {
		newInstanceObj.init.apply(newInstanceObj, arguments);
	}
	return newInstanceObj;
};

lib.Class.init = function () {
	// default constructor
	return true;
};
