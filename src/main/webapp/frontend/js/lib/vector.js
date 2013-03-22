// lib.vector 1.0.2
// requires: nothing
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

var lib = lib || {};
lib.vector = lib.vector || {};



lib.vector.name = "lib.vector";




lib.vector.adjustDimension = function () {
	// modifies all vectors (arrays) entered as arguments to have the equal dimension
	var dimension = 0;
	var i, l, vector;
	for (i = 0, l = arguments.length; i < l; i++) {
		if (arguments[i].length > dimension) {
			dimension = arguments[i].length;
		}
	}
	for (i = 0, l = arguments.length; i < l; i++) {
		if (arguments[i].length < dimension) {
			vector = arguments[i];
			for (i = 0, l = dimension; i < l; i++) {
				if ((typeof vector[i] == "undefined") || (typeof vector[i] === null)) {
					vector[i] = 0;
				}
			}
		}
	}
	return true;
};

lib.vector.copy = function (vector) {
	// returns the copy of the vector
	var result = [];
	for (var i = 0, l = vector.length; i < l; i++) {
		result[i] = vector[i];
	}
	return result;
};

lib.vector.equals = function (vectorA, vectorB) {
	// returns true if vectorA equals vectorB
	lib.vector.adjustDimension(vectorA, vectorB);
	for (var i = 0, l = vectorA.length; i < l; i++) {
		if (vectorA[i] != vectorB[i]) {
			return false;
		}
	}
	return true;
};

lib.vector.add = function (vectorA, vectorB) {
	// adds vector A to vector B, returns the final vector
	lib.vector.adjustDimension(vectorA, vectorB);
	var result = [];
	for (var i = 0, l = vectorA.length; i < l; i++) {
		result[i] = vectorA[i] + vectorB[i];
	}
	return result;
};

lib.vector.subtract = function (vectorA, vectorB) {
	// subtracts vector B from vector A, returns the final vector
	lib.vector.adjustDimension(vectorA, vectorB);
	var result = [];
	for (var i = 0, l = vectorA.length; i < l; i++) {
		result[i] = vectorA[i] - vectorB[i];
	}
	return result;
};

lib.vector.getDirection = function (startPoint, endPoint) {
	// returns the direction vector
	return lib.vector.subtract(endPoint, startPoint);
};

lib.vector.norm = function (vector) {
	// returns the size ("norm") of the vector
	var result = 0;
	for (var i = 0, l = vector.length; i < l; i++) {
		result += vector[i] * vector[i];
	}
	result = Math.sqrt(result);
	return result;
};

lib.vector.sgn = function (vector) {
	// returns the signum vector - it tells what quadrant the vector belongs to.
	var result = [];
	for (var i = 0, l = vector.length; i < l; i++) {
		if (vector[i] > 0) {
			result[i] = 1;
		} else if (vector[i] < 0 ) {
			result[i] = -1;
		} else {
			result[i] = 0;
		}
	}
	return result;
};
