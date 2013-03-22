// lib.array 1.1.11
// requires: nothing
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// Numbered array helper functions
// Don't use for asociative arrays!

var lib = lib || {};
lib.array = lib.array || {};




lib.array.name = "lib.array";
lib.array.errorMsg = {
	arrayCopyException: "Error: lib.array.copy can copy only arrays."
};
lib.array.defaultDepth = 10;




lib.array.contains = function (arr, item, exact) {
	// tests if arr contains item. By default, use == for test, if exact is true use === . Not deep testing.
	// TODO: deep testing
	var isItemFound = false;
	var i, l;
	searchForItem:
		for (i = 0, l = arr.length; i < l; i++) {
			if (exact) {
				if (arr[i] === item) {
					isItemFound = true;
					break searchForItem;
				}
			} else if (arr[i] == item) {
				isItemFound = true;
				break searchForItem;
			}
		}
	return isItemFound;
};

lib.array.isEqual = function (arr1, arr2, exact) {
	// compares two arrays. By default, use == for test, if exact is true use ===
	// not for use with associative arrays - that�s the TODO (expressed in docbook) 
	exact = exact || false;
	var result = false;
	var i, l;
	if (
		(arr1 instanceof Array) &&
		(arr2 instanceof Array) &&
		(arr1.length == arr2.length)
	) {
		result = true;
		for (i = 0, l = arr1.length; i < l; i++) {
			if ( (arr1[i] instanceof Array) && (arr2[i] instanceof Array) ) {
				result = result && lib.array.isEqual(arr1[i], arr2[i], exact);
			} else if (exact) {
				if (arr2[i] !== arr1[i]) {
					result = false;
					break;
				}
			} else {
				if (arr2[i] != arr1[i]) {
					result = false;
					break;
				}
			}
		}
	}
	return result;
};

lib.array.concat = function (arr1, arr2) {
	// joins arrays (two or more)
	var args = arguments;
	var arr = [];
	var i1, i2, l1, l2;
	for (i1 = 0, l1 = args.length; i1 < l1; i1++) {
		for (i2 = 0, l2 = args[i1].length; i2 < l2; i2++) {
			arr[arr.length] = args[i1][i2];
		}
	}
	return arr;
};

lib.array.copy = function (arr, depth) {
	// copy an array by value, if deep copying is not necessary set depth to 0
	if (typeof depth != "number") { depth = lib.array.defaultDepth; } // deep clonning is default
	var result = [];
	var i;
	if (arr instanceof Array) {
		i = arr.length;
		while (i--) {
			if ((depth > 0) && (arr[i] instanceof Array)) {
				result[i] = lib.array.copy(arr[i], depth - 1);
			} else {
				result[i] = arr[i];
			}
		}
	} else {
		throw { name: "arrayCopyException", message: lib.array.errorMsg.arrayCopyException };
	}
	return result;
};

lib.array.convert = function (obj) {
	// converts an object to array 
	var result = [];
	var i, l;
	if (typeof obj.length == "number") {
		for (i = 0, l = obj.length; i < l; i++) {
			result[i] = obj[i];
		}
	}
	return result;
};

lib.array.pop = function (arr) {
	// removes the last element of the array a and returns it
	var result = arr[arr.length-1];
	arr.length--;
	return result;
};

lib.array.push = function (arr, item) {
	// add an element to the end of an array, return the new array length
	var arg = arguments;
	var len = arr.length;
	for (var i = 1, l = arg.length; i < l; i++) {
		arr[len + i - 1] = arg[i];
	}
	return arr.length;
};

lib.array.shift = function (arr) {
	// remove and return the first element of an array
	var result = arr[0];
	for(var i = 0, l = arr.length - 1; i < l; i++ ) {
		arr[i] = arr[i + 1];
	}
	arr.length--;
	return result;
};

lib.array.unshift = function (arr, item) {
	// add an element to the beginning of an array and returns new array length
	arr.reverse();
	var a = arguments;
	for (var i = a.length - 1; i > 0; i--) {
		lib.array.push(arr, a[i]);
	}
	arr.reverse();
	return arr.length;
};

lib.array.condense = function (arr, emptyValue) {
	// takes sparse array (vector) and returns it as dense array, throwing out all items that have value emptyValue.
	// if emptyValue is not specified, removes "null" values by default
	if (typeof emptyValue == "undefined") {
		emptyValue = null;
	}
	var i, l;
	var newLength = 0;
	if (typeof arr.length != "undefined") {
		for (i = 0, l = arr.length; i < l; i++) {
			if ((typeof arr[i] != "undefined") && (arr[i] != emptyValue)) {
				arr[newLength] = arr[i];
				newLength++;
			}
		}
		arr.length = newLength;
	}
	return arr;
};

lib.array.insert = function (arr, newValue, emptyValue) {
	// inserts the newValue to array to first empty place (place that has value emptyValue)
	// if there is no such value, adds newValue to the end of the array --- RETURNS i (1)
	// if emptyValue is not specified, replaces "null" values by default -- RETURNS new arr.length
	if (typeof emptyValue == "undefined") {
		emptyValue = null;
	}
	var i, l;
	searchForHole:
		for (i = 0, l = arr.length; i < l; i++) {
			if (arr[i] == emptyValue) {
				break searchForHole;
			}
		}
	arr[i] = newValue;
	return i;
};

lib.array.sort = function (arr) {
	// fixes an ugly bug in Array.sort
	var sortFn = function (a, b) {
		var result = 0;
		if (a < b) {
			result = -1;
		} else if ( a > b ) {
			result = 1;
		}
		return result;
	};
	return arr.sort(sortFn);
};

lib.array.sortBy = function (arr, indexName) {
	// sorts multidimensional array using index "indexName" as a key for sorting
	// elements that don't have this index are put to the end of the array
	var sortFn = function (a, b) {
		var result = 0;
		if (a && b && a[indexName] && b[indexName]) {
			if (a[indexName] < b[indexName]) {
				result = -1;
			} else if ( a[indexName] > b[indexName] ) {
				result = 1;
			}
		} else if (b && b[indexName]) {
			result = -1;
		} else if ((b === null) && (a !== null)) {
			result = -1;
		}
		return result;
	};
	return arr.sort(sortFn);
};

lib.array.apply = function (arr, fn) {
	// for each array element arr[i] run fn(arr[i]) and return the array of results
	// nested arrays are replaced -> null
	var result = [];
	for (var i = 0, l = arr.length; i < l; i++) {
		result[i] = fn(arr[i]);
	}
	return result;
};


