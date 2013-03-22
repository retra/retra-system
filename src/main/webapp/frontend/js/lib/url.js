// lib.url 0.3.0
// requires: lib.array
//
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// works with url

lib.url = {
	name: "lib.url",
	
	getParameters: function (url) {
		// returns URL parameters as array of pairs {name, value}
		var result = null;
		var parts = url.split("?");
		if (parts.length == 2) {
			result = new Array();
			var resultStr = parts[1].split("#")[0];
			var resultArray = resultStr.split("&");
			for (var i = 0, l = resultArray.length; i < l; i++) {
				var pair = resultArray[i].split("=");
				if (pair.length == 1) { pair[1] = ""; };
				lib.array.push(result, {name: pair[0], value: pair[1]});
			}
		};
		return result;
	},
	
	getParameter: function (url, paramName) {
		var pairs = lib.url.getParameters(url);
		for (var i = 0, l = pairs.length; i < l; i++) {
			if (pairs[i].name == paramName) {
				return pairs[i].value;
			}
		};
		return null;
	}
};
