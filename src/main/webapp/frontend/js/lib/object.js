// lib.object 1.2.3
// requires: nothing
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// helper methods for Object type

var lib = lib || {};
lib.object = lib.object || {};




lib.object.name = "lib.object";
lib.object.defaultDepth = "10";




lib.object.isArray = function (obj) {
	return obj instanceof Array;
};

lib.object.isRegExp = function (obj) {
	// returns true if the object is a regular expression
	// return (obj.constructor && (obj.constructor == RegExp.prototype.constructor));
	return obj instanceof RegExp;
};

lib.object.cloneRegExp = function (reg) {
	// returns a new regExp object identical to reg
	var mods = "";
	if (reg.global)     { mods = "g"; }
	if (reg.ignoreCase) { mods += "i"; }
	var result = new RegExp(reg.source, mods);
	return result;
};

lib.object.isEqual = function (obj1, obj2, exact, depth) {
	// detects if two objects are equal
	// exact = true - checks if all references match exactly objects
	exact = exact || false;
	var prop;
	if (typeof depth != "number") {
		depth = lib.object.defaultDepth;
	}
	var noDiffFound = true;
	
	if ((typeof obj1 != "object") || (typeof obj2 != "object") || (obj1 === null) || (obj2 === null)) {
		// compare simple data types
		if (exact) {
			noDiffFound = (obj1 === obj2);
		} else {
			noDiffFound = (obj1 == obj2);
		}
	} else {
		// compare objects
		compareNumberOfProps:
			for (prop in obj2) {
				if (typeof obj1[prop] == "undefined") {
					// obj1 has different properties than obj2
					noDiffFound = false;
					break compareNumberOfProps;
				}
			}
		// end compareNumberOfProps
		if (noDiffFound) {
			compareObjects:
				for (prop in obj1) {
					if (typeof obj2[prop] == "undefined") {
						// obj2 has different properties
						noDiffFound = false;
						break compareObjects;
					}
					if (exact) {
						// if compare is exact, properties including references have to match exactly
						if (obj1[prop] !== obj2[prop]) {
							noDiffFound = false;
							break compareObjects;
						}
					} else {
						// if compare is not exact, only values have to match
						if ((typeof obj1[prop] != "object") || (depth < 1)) {
							// simple types may be compared directly. If you are too deep, compare even objects directly
							if (obj1[prop] != obj2[prop]) {
								noDiffFound = false;
								break compareObjects;
							}
						} else {
							// object have to be compared by value if it's not too deep
							if (!lib.object.isEqual(obj1[prop], obj2[prop], false, depth - 1)) {
								noDiffFound = false;
								break compareObjects;
							}
						}
					}
				}
			// end compareObjects
		}
	}
	return noDiffFound;
};

lib.object.clone = function (obj, depth) {
	// returns copy of the source object obj. 
	// If deep is set to true (default), all references are cloned, too. 
	// If deep is set to false, references are left intact.
	if (typeof depth != "number") {
		depth = lib.object.defaultDepth; // deep clonning is default
	}
	var result;
	if (typeof obj == "object") {
		result = {};
		for (var prop in obj) {
			if ((typeof obj[prop] != "object") || (depth < 1)) {
				// simple types are copied by assignment as well as too deep references
				result[prop] = obj[prop];
			} else {
				// referenced objects are cloned
				result[prop] = lib.object.clone(obj[prop], depth - 1);
			}
		}
	} else {
		result = obj;
	}
	return result;
};
