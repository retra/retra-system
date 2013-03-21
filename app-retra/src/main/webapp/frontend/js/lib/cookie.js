// lib.cookie 1.0.0
// requires: nothing
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// cookie manipulations
// written by FCZBKK - http://www.fczbkk.com

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.cookie == "undefined") {
	lib.cookie = {};
};




lib.cookie.name = "lib.cookie";




lib.cookie.set = function (name, value, days, domain, path) {
	// sets cookie
	var date; // Date
	var expires; // String
	value = value + "; ";
	if ((typeof days != "undefined") && (days != null)) {
		date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		expires = "expires=" + date.toGMTString() + "; ";
	} else {
		expires = "";
	};
	if ((typeof domain != "undefined") && (domain != null) && (domain.indexOf(".") != -1)) {
		domain = "domain=" + domain + "; ";
	} else {
		domain = "";
	};
	if (typeof path != "undefined") {
		path = "path=" + path;
	} else {
		path = "path=/";
	};
	document.cookie = name + "=" + value + expires + domain + path;
	return true;
};

lib.cookie.get = function (name) {
	// gets cookie value by name
	var i; // Int iterator
	var nameEQ = name + "="; // String
	var c; // String
	var ca = document.cookie.split(";"); // Array of Strings
	for (i = 0, l = ca.length; i < l; i++) {
		c = ca[i];
		while (c.charAt(0) == " ") {
			c = c.substring(1, c.length);
		};
		if (c.indexOf(nameEQ) == 0) {
			return c.substring(nameEQ.length, c.length);
		};
	};
	return null;
};

lib.cookie.remove = function (name) {
	// unsets the cookie
	lib.cookie.set(name, "", -1);
	return true;
};
