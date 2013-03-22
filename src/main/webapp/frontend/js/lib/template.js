// lib.template 1.0.0
// requires: lib.elm
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// templating engine can be used to unify HTML code generation from server and client
// The template looks like this: <span><a href="{0}" title="{1}" {2}>{3}</a></span>
// and the corresponding data: ["http://myurl.com", "My Webpage", "Goto my webpage"]
// The template variables may be string identifiers, too: <input name="{name}" value="{value}" />
// and the corresponding data {name: "myField", value: "myField value"}

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.template == "undefined") {
	lib.template = {};
};




lib.template.name = "lib.template";




lib.template.getHtml = function (template, data) {
	// returns the HTML code made from template and templating data
	var result = template;
	for (var i in data) {
		result = result.replace(eval("/\{["+i+"]{1}\}/g"), data[i]);
	};
	result = result.replace(/\{([^}]{1,})\}/g, "");
	return result;
};

lib.template.createNode = function (template, data) {
	// returns the DOM node created from template and data.
	// It returns only the first element created from the template
	var tmpNode = lib.elm.create({tagName: "div"});
	tmpNode.innerHTML = lib.template.getHtml(template, data);
	if (tmpNode.firstChild) {
		return tmpNode.firstChild;
	};
	return null;
};
