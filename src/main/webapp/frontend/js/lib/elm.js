// lib.elm 1.1.7
// requires: lib.array, lib.string, lib.cls
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// handles DOM elements, encapsulates common DOM operations

var lib = lib || {};
lib.elm = lib.elm || {};



lib.elm.name = "lib.elm";
lib.elm.xHtmlNameSpace = "http://www.w3.org/1999/xhtml";




lib.elm.get = function (obj) {
	// HtmlElement lib.elm.get(HtmlElement|String)
	// Returns HTML element that belongs to object obj.
	var result;
	if (typeof obj == "undefined") {
		result = null;
	} else if (typeof obj == "string") {
		// if obj is String, returns object with id obj
		return document.getElementById(obj);
	}
	return obj; // if obj is HtmlElement, returns it unchanged
};

lib.elm.getByTag = function (tagName, parentElm) {
	// returns array of elements with given tagName
	parentElm = parentElm || null;
	parentElm = (parentElm) ? lib.elm.get(parentElm) : document;
	var result = null;
	if (parentElm.all && (tagName == "*")) { // MSIE hack
		result = parentElm.all;
	}
	if (parentElm.getElementsByTagName) { // standard way
		result = parentElm.getElementsByTagName(tagName);
	}
	return result;
};

lib.elm.getByClass = function (className, parentElm, tagName) {
	// not very effective. If you can, use only when parentElement and tagName is specified
	// thanx http://www.dustindiaz.com/getelementsbyclass for inspiration
	parentElm = parentElm || null;
	parentElm = (parentElm) ? lib.elm.get(parentElm) : document;
	var result = [];
	tagName = (tagName) ? tagName : "*";
	var allElm = lib.elm.getByTag(tagName, parentElm);
	var c = 0;
	for (var i = 0, l = allElm.length; i < l; i++ ) {
		if (lib.cls.has(allElm[i], className)) {
			result[c++] = allElm[i];
		}
	}
	return result;
};

lib.elm.hasAttributes = function (elm, attributes) {
	// tests if the element has all attributes specified by associative array
	// don't use href as search attribute - MSIE changes it
	elm = elm || null;
	elm = lib.elm.get(elm);
	var result = true;
	var attr, elmAttr;
	testAttributes:
		for (attr in attributes) {
			if (!((attributes[attr] === true) && (elm[attr]))) {
				if (attr == "className") {
					if (!lib.cls.has(elm, attributes[attr])) {
						result = false;
						break testAttributes;
					}
				} else {
					elmAttr = lib.elm.getAttribute(elm, attr);
					if (attr == "tagName") {
						elmAttr = elmAttr.toLowerCase();
					}
					if (elmAttr != attributes[attr]) {
						result = false;
						break testAttributes;
					}
				}
			}
		}
	return result;
};

lib.elm.find = function (attributes, parentElm) {
	// finds all elements with attributes specified by associative array
	// don't use href as search attribute - MSIE changes it
	// beware, this is very slow!
	parentElm = parentElm || null;
	parentElm = (parentElm) ? lib.elm.get(parentElm) : document;
	var result = [];
	var tagName = "*";
	if (typeof attributes.tagName == "string") {
		 tagName = attributes.tagName;
	}
	var allElm = lib.elm.getByTag(tagName, parentElm);
	var i, l;
	if (allElm !== null) {
		findElm:
			for (i = 0, l = allElm.length; i < l; i++) {
				if (lib.elm.hasAttributes(allElm[i], attributes)) {
					lib.array.push(result, allElm[i]);
				}
			}
	}
	return result;
};

lib.elm.findFirst = function (attributes, parentElm) {
	// finds the first element that has all attributes specified by associative array
	parentElm = parentElm || null;
	parentElm = (parentElm) ? lib.elm.get(parentElm) : document;
	var result = null;
	var tagName = "*";
	if (typeof attributes.tagName == "string") {
		 tagName = attributes.tagName;
	}
	var allElm = lib.elm.getByTag(tagName, parentElm);
	var i, l;
	if (allElm !== null) {
		findElm:
			for (i = 0, l = allElm.length; i < l; i++) {
				if (lib.elm.hasAttributes(allElm[i], attributes)) {
					result = allElm[i];
					break findElm; // element found!
				}
			}
	}
	return result;
};

lib.elm.getParent = function (elm) {
	// returns parent element. If there is no parent element it returns null
	elm = elm || null;
	elm = lib.elm.get(elm);
	if (typeof elm.parentNode == "undefined") {
		return null;
	} else {
		return elm.parentNode;
	}
};

lib.elm.getAncestorByTag = function (elm, tagName) {
	// returns ancestor elemenet (by tagName), if nothing has been found, returns null
	elm = elm || null;
	elm = lib.elm.get(elm);
	tagName = tagName.toLowerCase();
	var actualTagName = "";
	do {
		elm = lib.elm.getParent(elm);
		if (elm === null) { return null; } // parent not found
		actualTagName = elm.tagName.toLowerCase();
	} while ((actualTagName != tagName) && (actualTagName != "html"));
	if (actualTagName == tagName) {
		return elm; // element found
	} else {
		return null; // no element found
	}
};

lib.elm.getChildrenByTag = function (tagName, elm) {
	// returns array of children, if nothing has been found, returns null
	elm = elm || null;
	elm = lib.elm.get(elm);
	tagName = tagName.toLowerCase();
	var result = null;
	var actualNode;
	if (elm.firstChild) {
		actualNode = elm.firstChild;
		do {
			if (actualNode.tagName && (actualNode.tagName.toLowerCase() == tagName)) {
				if (result === null) {
					result = [];
				}
				result[result.length] = actualNode;
			}
			actualNode = actualNode.nextSibling;
		} while (actualNode !== null);
	}
	return result;
};

lib.elm.getValue = function (elm) {
	// returns the value of the element
	elm = elm || null;
	elm = lib.elm.get(elm);
	var result = "";
	var radioSet;
	var i, l;
	if (elm.type && (elm.type == "checkbox")) {
		result = elm.checked ? elm.value : "";
	} else if (elm.type && (elm.type == "radio")) {
		// radiobutton is special
		radioSet = elm.form[elm.name]; // if there is no form, throw an exception
		findChecked:
			for (i = 0, l = radioSet.length; i < l; i++) {
				if (radioSet[i].checked) {
					result = radioSet[i].value;
					break findChecked;
				}
			}
	} else if ((typeof elm.value != "undefined") && (typeof elm.value !== null)) {
		result = elm.value;
	} else if (elm.tagName.toLowerCase() == "select") {
		if (typeof (elm.selectedIndex != "undefined") && (typeof elm.options != "undefined")) {
			if (elm.selectedIndex >= 0) {
				result = elm.options[elm.selectedIndex].value; // if select is multiple, returns only first value
			}
		}
	} else { result = null; }
	return result;
};

lib.elm.setValue = function (elm, newValue) {
	// sets the element value.
	elm = elm || null;
	elm = lib.elm.get(elm);
	var i, l;
	if (elm.options && (elm.options.length > 0)) { // element is SELECT
		for (i = 0, l = elm.options.length; i < l; i++) {
			if (elm.options[i].value == newValue) {
				elm.options[i].selected = true;
				elm.options[i].setAttribute("selected", "selected");
				// throws exception in MSIE
				// if (elm.focus) { elm.focus(); } // focus the field if possible
				return true;
			} else {
				elm.options[i].selected = false;
				elm.options[i].setAttribute("selected", "");
			}
		}
		return false;
	} else if (typeof elm.value != "undefined") {
		elm.value = newValue;
		// throws exception in MSIE
		// if (elm.focus) { elm.focus(); } // focus the field if possible
		// if (elm.select) { elm.select(); } // select the content if possible
		return true;
	}
	return false;
};

lib.elm.getAttribute = function (elm, attrName) {
	// gets value of attribute attrName of element elm
	elm = elm || null;
	elm = lib.elm.get(elm);
	var result = elm.getAttribute(attrName);
	if (result === null) {
		if ((attrName == "class") && (typeof elm.getAttribute("class") == "undefined")) {
			result = elm.getAttribute("className");
		} else if ((attrName == "for") && (elm.getAttribute("for") === null)) {
			result = elm.getAttribute("htmlFor");
		} else if (typeof elm[attrName] != "undefined") {
			result = elm[attrName];
		}
	}
	return result;
};

lib.elm.getStyle = function(elm, styleProp) {
	// tries to obtain the CSS property value of element elm
	// TODO: add support for composite attributes like "background" or "padding"
	elm = elm || null;
	elm = lib.elm.get(elm);
	var value = elm.style[lib.string.camelize(styleProp)];
	var css;
	if (!value) {
		if (document.defaultView && document.defaultView.getComputedStyle) {
			css = document.defaultView.getComputedStyle(elm, null);
			value = css ? css.getPropertyValue(styleProp) : null;
		} else if (elm.currentStyle) {
			value = elm.currentStyle[lib.string.camelize(styleProp)];
		}
	}
	if (window.opera) {
		if (lib.array.contains(["left", "top", "right", "bottom"], styleProp)) {
			if (lib.elm.getStyle(elm, "position") == "static") {
				value = "auto";
			}
		}
	}
	return value == "auto" ? null : value;
};

lib.elm.applyStyle = function (elm, styleCfg) {
	// apply the style defined in JSON array on element
	elm = elm || null;
	elm = lib.elm.get(elm);
	for (var i in styleCfg) {
		if (typeof styleCfg[i] == "string") {
			elm.style[i] = styleCfg[i];
		}
	}
	return true;
};

lib.elm.create = function (config, contentNodes) {
	// creates element tree defined by config object.
	contentNodes = contentNodes || [];
	config = config || {};
	var result = null;
	var i1, i2, i3, l2, l3;
	var child = null;
	var newElement = null;
	
	// config = { contentNode: x }
	var childConfig;
	var isChildContentNode;
	var contentNodesItem;
	
	if (typeof config.contentNode == "number") {
		if (contentNodes[config.contentNode]) {
			result = contentNodes[config.contentNode];
		} else {
			result = null;
		}
	} else { // config = { tagName: ... }
		config.innerText   = config.innerText || null;
		config.tagName     = config.tagName || null;
		if (config.innerText) {
			// create text node
			result = document.createTextNode(config.innerText);
		}
		if (config.tagName) {
			// create standard HTML element
			if (document.createElementNS) { // Gecko, standard
				newElement = document.createElementNS(lib.elm.xHtmlNameSpace, config.tagName);
			} else {
				newElement = document.createElement(config.tagName);
			}
			if (result !== null) {
				newElement.appendChild(result);
			}
			for (i1 in config) {
				switch (i1) {
					case "tagName": break; // already used
					case "innerText": break; // already used
					case "contentNode": break; // already used
					case "children": // create childNodes
						for (i2 = 0, l2 = config.children.length; i2 < l2; i2++) {
							childConfig = config.children[i2];
							
							isChildContentNode = (typeof childConfig.contentNode == "number") && (typeof contentNodes[childConfig.contentNode] == "object");
							
							if (isChildContentNode) {
								contentNodesItem = contentNodes[childConfig.contentNode];
								
								if (contentNodesItem instanceof Array) {
									// this means the actual contentNode is not 1 HTML element but an array of them
									for (i3 = 0, l3 = contentNodesItem.length; i3 < l3; i3++) {
										newElement.appendChild(contentNodesItem[i3]);
									}
								} else if (contentNodesItem !== null) {
									// this means the contentNode is single HTML element
									newElement.appendChild(contentNodesItem);
								}
							} else {
								child = lib.elm.create(childConfig, contentNodes); // child == null or array or html element
								if (child !== null) {
									newElement.appendChild(child);
								}
							}
						}
						break;
					case "style": // style definitions
						lib.elm.applyStyle(newElement, config[i1]);
						break;
					case "className":
						newElement.className = config[i1];
						break;
					case "htmlFor":
						newElement.setAttribute("for", config[i1]);
						break;
					default: // add standard attribute
						newElement.setAttribute(i1, config[i1]);
						break;
				}
			}
			if (newElement) {
				result = newElement;
			}
		}
	}
	return result;
};

lib.elm.envelope = function (elm, envelopeConfig) {
	// envelopes element elm by newly created element
	elm = elm || null;
	elm = lib.elm.get(elm);
	var envelope = lib.elm.create(envelopeConfig);
	if (elm.parentNode) {
		elm.parentNode.insertBefore(envelope, elm);
	}
	envelope.appendChild(elm);
	return envelope;
};

lib.elm.envelopeChildren = function (elm, envelopeConfig) {
	// envelopes obj childNodes by newly created element
	elm = elm || null;
	elm = lib.elm.get(elm);
	var envelope = lib.elm.create(envelopeConfig, [ lib.array.convert(elm.childNodes) ] );
	elm.appendChild(envelope);
	return envelope;
};
