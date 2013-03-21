// lib.browser 1.3.0
// requires: nothing
//
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// browser capabilities detection
// WARNING: init runs imediately after script is loaded (you need not to call it).
// may NOT use any JS library, because it is initialized before page load

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.browser == "undefined") {
	lib.browser = {};
};

if (typeof lib.browser.data == "undefined") {
	lib.browser.data = {};
};




lib.browser.name = "lib.browser";

lib.browser.gecko =     false;
lib.browser.opera =     false;
lib.browser.ie =        false;
lib.browser.ie5 =       false;
lib.browser.ie55 =      false;
lib.browser.ie6 =       false;
lib.browser.safari =    false;
lib.browser.iCab =      false;
lib.browser.konqueror = false;
lib.browser.netscape =  false;

lib.browser.agent =    "an unknown browser";
lib.browser.version =  "an unknown version";
lib.browser.os =       "an unknown OS";




lib.browser.init = function() {
	var browserData = lib.browser._getBrowserData();
	if (browserData != null) {
		lib.browser._setBrowserInfo(browserData);
	};
	var osData = lib.browser._getOsData();
	if (osData != null) {
		lib.browser._setOsInfo(osData);
	};
	return true;
};

lib.browser._getBrowserData = function () {
	for (var i = 0, l = lib.browser.data.agent.length; i < l; i++) {
		var dataString = lib.browser.data.agent[i].string;
		var dataProp = lib.browser.data.agent[i].prop;
		if (dataString) {
			if (dataString.indexOf(lib.browser.data.agent[i].subString) != -1) {
				return lib.browser.data.agent[i];
			}
		} else if (dataProp) {
			return lib.browser.data.agent[i];
		}
	};
	return null; // not found
};

lib.browser._setBrowserInfo = function (browserData) {
	// detect version
	var versionSearchString = browserData.versionSearch || browserData.identity;
	var dataString = navigator.userAgent;
	var index = dataString.indexOf(versionSearchString);
	if (index < 0) {
		dataString = navigator.appVersion;
		index = dataString.indexOf(versionSearchString);
	};
	if (index > 0) {
		lib.browser.version = parseFloat(dataString.substring(index + versionSearchString.length + 1));
	};
	// detect browser
	lib.browser.agent = browserData.identity;
	switch (browserData.identity) {
		case "Safari": lib.browser.safari = true; break;
		case "Opera": lib.browser.opera = true; break;
		case "Firefox": lib.browser.gecko = true; break;
		case "Netscape":
			if (lib.browser.version >= 6) {
				lib.browser.gecko = true;
			} else {
				lib.browser.netscape = true;
			};
			break;
		case "Explorer":
			lib.browser.ie = true;
			if (lib.browser.version == 5) {
				lib.browser.ie5 = true;
			} else if (lib.browser.version == 5.5) {
				lib.browser.ie55 = true;
			} else if (lib.browser.version == 6) {
				lib.browser.ie6 = true;
			};
	};
	return true;
};

lib.browser._getOsData = function () {
	for (var i = 0, l = lib.browser.data.os.length; i < l; i++) {
		var dataString = lib.browser.data.os[i].string;
		if (dataString) {
			if (dataString.indexOf(lib.browser.data.os[i].subString) != -1) {
				return lib.browser.data.os[i];
			}
		}
	};
	return null; // not found
};

lib.browser._setOsInfo = function (osData) {
	lib.browser.os = osData.identity;
};




// browser and os data

lib.browser.data.agent = [
	{
		string: navigator.vendor,
		subString: "Apple",
		identity: "Safari"
	},
	{
		prop: window.opera,
		identity: "Opera"
	},
	{
		string: navigator.vendor,
		subString: "iCab",
		identity: "iCab"
	},
	{
		string: navigator.vendor,
		subString: "KDE",
		identity: "Konqueror"
	},
	{
		string: navigator.userAgent,
		subString: "Firefox",
		identity: "Firefox"
	},
	{	// for newer Netscapes (6+)
		string: navigator.userAgent,
		subString: "Netscape",
		identity: "Netscape"
	},
	{
		string: navigator.userAgent,
		subString: "MSIE",
		identity: "Explorer",
		versionSearch: "MSIE"
	},
	{
		string: navigator.userAgent,
		subString: "Gecko",
		identity: "Mozilla",
		versionSearch: "rv"
	},
	{ 	// for older Netscapes (4-)
		string: navigator.userAgent,
		subString: "Mozilla",
		identity: "Netscape",
		versionSearch: "Mozilla"
	}
];

lib.browser.data.os = [
	{
		string: navigator.platform,
		subString: "Win",
		identity: "Windows"
	},
	{
		string: navigator.platform,
		subString: "Mac",
		identity: "Mac"
	},
	{
		string: navigator.platform,
		subString: "Linux",
		identity: "Linux"
	}
];



lib.browser.init(); // init immediately
