// lib.string 1.1.5
// requires: lib.array
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

var lib = lib || {};
lib.string = lib.string || {};
lib.string.buffer = lib.string.buffer || {};
lib.string.json = lib.string.json || {};




lib.string.name = "lib.string";
lib.string.nbsp = String.fromCharCode(160); // nonbreaking space
lib.string.lineBreak = "\n\r";




lib.string.getValue = function (str) {
	// evaluates the JSON string str
	var result = null;
	try {
		eval("result = " + str.valueOf()); // this evals JSON data, pay attention to security
	} catch (ex) {
		result = null;
	}
	return result;
};

lib.string.ltrim = function (str) {
	// remove leading whitespace
	return str.replace(/^\s*(.*)/, "$1");
};

lib.string.rtrim = function (str) {
	// remove trailing whitespace
	return str.replace(/\s*$/, "");
};

lib.string.trim = function (str) {
	// remove leading and trailing whitespace
	return lib.string.ltrim(lib.string.rtrim(str));
};

lib.string.isEmpty = function (str) {
	// test if the string is empty or containing only whitespace
	return (str.search(/\S/) < 0) ? true : false;
};

lib.string.isEmptyHtml = function (str) {
	// test if string is empty HTML.
	// String is emptyHTML if it contains only whitespace characters 
	// by fczbkk
	var blankCharacters = [" ", "&nbsp;", "&#032;"];
	for (var i = 0, l = blankCharacters.length; i < l; i++) {
		str = str.replace(eval("/" + blankCharacters[i] + "/gi"), ""); // TODO - this is not secure, fix this
	}
	return (lib.string.isEmpty(str));
};

lib.string.removeHtmlTags = function (str) {
	// removes all HTML tags from string, leaves HTML entities intact
	// by Sam Stephenson, http://prototype.conio.net/
	return str.replace(/<\/?[^>]+>/gi, '');
};

lib.string.escapeHtml = function (str) {
	// escapes all "forbiden" HTML characters (<>&")
	// by Sam Stephenson, http://prototype.conio.net/
	var div = document.createElement('div');
	var text = document.createTextNode(str);
	div.appendChild(text);
	return div.innerHTML.replace(/["]/gi,'&quot;');
};

lib.string.htmlToText = function (str) {
	// removes all HTML tags and entities from string
	// by Sam Stephenson, http://prototype.conio.net/
	var div = document.createElement("div");
	div.innerHTML = lib.string.removeHtmlTags(str); // remove entities this way
	return div.childNodes[0].nodeValue;
};

lib.string.camelize = function (str) {
	// transforms CSS-like identifier ("My-property-id") to the proper javascript identifier ("myPropertyId")
	var result = "";
	var strList = str.split('-');
	for (var i = 0, l = strList.length; i < l; i++) {
		result += strList[i].charAt(0).toUpperCase() + strList[i].substring(1);
	}
	if (str.indexOf('-') === 0) {
		result = result.charAt(0).toUpperCase() + result.substring(1);
	} else {
		result = result.charAt(0).toLowerCase() + result.substring(1);
	}
	return result;
};

lib.string.split2 = function (str, delimiter) {
	// split string into 2 substrings by delimiter
	var result = [];
	var index = str.indexOf(delimiter);
	if (index < 0) {
		result[0] = str;
	} else {
		result[0] = str.substring(0, index);
		result[1] = str.substring(index + 1);
	}
	return result;
};




lib.string.buffer.create = function () {
	// creates and returns a new string buffer
	return [];
};

lib.string.buffer.add = function (buffer, str) {
	// adds a string to the string buffer
	lib.array.push(buffer, str);
	return buffer;
};

lib.string.buffer.get = function (buffer) {
	// retrieves merged string from string buffer
	return buffer.join("");
};




lib.string.json.encode = function (obj) {
	// gets JSON string of given object
	// copyright: (c)2005 JSON.org
	// license: http://www.crockford.com/JSON/license.html
	var arr = [];
	
	function addStr(arr, str) {
		// emit a string
		arr[arr.length] = str;
		return arr;
	}
	function convertVal(obj) {
		// convert a value
		var c, i, l, v, ol;
		
		switch (typeof obj) {
		case 'object':
			if (obj) {
				if (obj instanceof Array) {
					arr = addStr(arr, '[');
					l = arr.length;
					for (i = 0, ol = obj.length; i < ol; i += 1) {
						v = obj[i];
						if (typeof v != 'undefined' &&
								typeof v != 'function') {
							if (l < arr.length) {
								arr = addStr(arr, ',');
							}
							convertVal(v);
						}
					}
					arr = addStr(arr, ']');
					return true;
				} else if (typeof obj.valueOf == 'function') {
					arr = addStr(arr, '{');
					l = arr.length;
					for (i in obj) {
						v = obj[i];
						if (typeof v != 'undefined' &&
								typeof v != 'function' &&
								(!v || typeof v != 'object' ||
									typeof v.valueOf == 'function')) {
							if (l < arr.length) {
								arr =addStr(arr, ',');
							}
							convertVal(i);
							arr = addStr(arr, ':');
							convertVal(v);
						}
					}
					arr = addStr(arr, '}');
					return arr;
				}
			}
			arr = addStr(arr, 'null');
			return true;
		case 'number':
			arr = addStr(arr, isFinite(obj) ? +obj : 'null');
			return true;
		case 'string':
			l = obj.length;
			arr = addStr(arr, '"');
			for (i = 0; i < l; i += 1) {
				c = obj.charAt(i);
				if (c >= ' ') {
					if (c == '\\' || c == '"') {
						arr = addStr(arr, '\\');
					}
					arr = addStr(arr, c);
				} else {
					switch (c) {
					case '\b':
						arr = addStr(arr, '\\b');
						break;
					case '\f':
						arr = addStr(arr, '\\f');
						break;
					case '\n':
						arr = addStr(arr, '\\n');
						break;
					case '\r':
						arr = addStr(arr, '\\r');
						break;
					case '\t':
						arr = addStr(arr, '\\t');
						break;
					default:
						c = c.charCodeAt();
						arr = addStr(arr, '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16));
					}
				}
			}
			arr = addStr(arr, '"');
			return true;
		case 'boolean':
			arr = addStr(arr, String(obj));
			return true;
		default:
			arr = addStr(arr, 'null');
			return true;
		}
	}
	convertVal(obj);
	return arr.join('');
};

lib.string.json.decode = function (str) {
	// deserializes the JSON string
	return lib.string.getValue(str);
};
