// lib.log 1.1.1
// requires: lib.elm, lib.cls
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.log == "undefined") {
	lib.log = {};
};




lib.log.name = "lib.log";
lib.log.enabled =      true;  // if true, logging is on
lib.log.initialized =  false; // if true, log is prepared to work
lib.log.nodeId =       "messages";
lib.log.node =         null;
lib.log.messagesList = null;




lib.log.init = function () {
	lib.log.node = lib.elm.get(lib.log.nodeId);
	if (lib.log.node == null) {
		lib.log.node = lib.log.create();
	};
	lib.log.messagesList = lib.elm.create({tagName: "ul"});
	lib.log.node.appendChild(lib.log.messagesList);
	lib.log.initialized = true; // init succeded
	return true;
};

lib.log.create = function () {
	// creates a new log and places it at the start of the body.
	var node = lib.elm.create({tagName: "div", id: lib.log.nodeId});
	var body = lib.elm.getByTag("body", document)[0];
	body.insertBefore(node, body.firstChild);
	return node;
};

lib.log.write = function (msgText, msgType) {
	var i;
	if ((typeof msgText == "undefined") || (msgText == null) || (typeof msgText.toString != "function")) {
		msgText = "";
	};
	msgText = msgText.toString();
	if (lib.log.enabled) {
		var li = lib.elm.create({tagName: "li"});
		if ((typeof msgType != "undefined") && (msgType != null) && (msgType != "")) {
			lib.cls.add(li, msgType);
		};
		var msgLines = msgText.split("\n");
		for (var i = 0, l = msgLines.length; i < l; i++) {
			var msg = document.createTextNode(msgLines[i]);
			var br = document.createElement("br")
			li.appendChild(msg);
			li.appendChild(br);
		};
		lib.log.messagesList.insertBefore(li, lib.log.messagesList.firstChild);
	};
	return true;
};

lib.log.clear = function () {
	while (messagesList.firstChild) {
		messagesList.removeChild(messagesList.firstChild);
	};
	return true;
};
