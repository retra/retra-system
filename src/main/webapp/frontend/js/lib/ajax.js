// lib.evt 0.0.6
// requires: nothing
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// provides AJAX functionality


if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.ajax == "undefined") {
	lib.ajax = {};
};




lib.ajax.name           = "lib.ajax";
lib.ajax.defaultMethod  = "get";
lib.ajax.charset        = "utf-8";
lib.ajax.debug          = true;
lib.ajax.showResponse   = false;
lib.ajax.engine         = "standard"; // ajax engine - "standard" (Gecko), "activeX" (MSIE) or "none" (AJAX not supported) - will be autodetected
lib.ajax.maxAsyncReq    = -1;
lib.ajax.countAsyncReq  = 0;
lib.ajax.queue          = []; // store unfinished requests here

lib.ajax.readyState = {
	"uninitialized": 0,
	"loading":       1,
	"loaded":        2,
	"interactive":   3,
	"completed":     4
};

lib.ajax.errorMsg = {
	requestFailed:         "Error at {fn}: data source {url} not available",
	undefinedUrl:          "Error at {fn}: undefined data source URL {url}.",
	openRequestException:  "Exception at {fn}: {exName} - {exMsg}",
	undefinedTargetHeader: "Undefined header X-JaxTarget at {fn}: X-JaxTarget = {target}"
};




lib.ajax.handleError = function (messageId, params) {
	var message = ajax.errorMsg[messageId];
	for (par in params) {
		message = message.replace(RegExp("[{]"+par+"[}]", "g"), params[par]);
	};
	throw ({ name: "ajaxException", id: message});
	return true;
};

lib.ajax.init = function () {
	// detect if browser uses activeX (MSIE) or not (standard)
	var rq; // XmlHttpRequest
	lib.ajax.engine = "standard";
	try {
		rq = new ActiveXObject("Msxml2.XMLHTTP");
		lib.ajax.engine = "activeX";
	} catch (ex) {} // standard browsers produce exception
	return true;
};

lib.ajax.addHeader = function (rqId, headerName, headerValue) {
	var rqData = lib.ajax.getRqData(rqId);
	rqData.rq.setRequestHeader(headerName, headerValue);
	return true;
};

lib.ajax.getRqData = function (rqId) {
	if (typeof lib.ajax.queue[rqId] != null) {
		return lib.ajax.queue[rqId];
	};
	return null; // bad rqId
};

lib.ajax._createResponseHandler = function (rqId) {
	// closure - produces event handler and inserts rqId into it
	var responseHandler = function () {
		var rqData = lib.ajax.getRqData(rqId);
		var rq = rqData.rq;
		var responseAction = rqData.responseAction;
		
		if (rq.readyState == lib.ajax.readyState["completed"]) {
			if (rq.status >= 400) { // this means error code - see RFC 2616
				lib.ajax.handleError("requestFailed", {fn: "sc.ajax: rq.onreadystatechange", url: rqData.url, method: rqData.method});
				lib.ajax.queue[rqId] = null; // destroy rqData after response - prevents memory leaks
				return false; // status not OK, request failed
			};
			rqData.responseAction(rqId); // status ok, run standard handler 
			lib.ajax.queue[rqId] = null; // destroy rqData after response - prevents memory leaks
		};
		return true;
	};
	return responseHandler;
};

lib.ajax.createRequest = function (url, responseAction, method) {
	// creates request, adds it to the queue and returns requestId
	if ((typeof method == "undefined") || (method == null)) {
		method = lib.ajax.defaultMethod;
	};
	var rq; // XmlHttpRequest
	switch (lib.ajax.engine) {
		case "standard":
			rq = new XMLHttpRequest();
			break;
		case "activeX":
			rq = new ActiveXObject("Msxml2.XMLHTTP");
			break;
	};
	// add request to the queue
/*
	var i = 0;
	var rqId = 0;
	while (lib.ajax.queue[i]) {
		i++;
	};
	rqId = i;
*/
	var rqData = {
		rq:     rq,
		url:    url,
		method: method,
		responseAction: responseAction
	};
	rqData.id = lib.array.insert(lib.ajax.queue, rqData);
	lib.ajax.queue[rqData.id] = rqData;
	rq.open(method, url, true);
	
	// standard content-type header
	lib.ajax.addHeader(rqData.id, "Content-Type", "application/x-www-form-urlencoded; charset=" + lib.ajax.charset);
	rqData.rq.onreadystatechange = lib.ajax._createResponseHandler(rqData.id);
	return rqData.id;
};

lib.ajax.sendRequest = function (rqId, inputStream) {
	// requestHeaders - array of aditional request headers. Not mandatory
	// responseAction has only one argument, rqId, that is filled automatically
	inputStream = inputStream || "";
	var rqData = lib.ajax.getRqData(rqId);
	var rq = rqData.rq;
	rq.send(inputStream);
	return true;
};

lib.ajax.getText = function (rqId) {
	// returns response as text
	// may be used only in responseAction
	var rqData = lib.ajax.getRqData(rqId);
	return rqData.rq.responseText;
},

lib.ajax.getXml = function (rqId) {
	// returns response as text
	// may be used only in responseAction
	var rqData = lib.ajax.getRqData(rqId);
	return rqData.rq.responseXML;
};

lib.ajax.getJs = function(rqId) {
	// returns response text as Javascript
	// if it is function, it creates closure
	var rqData = lib.ajax.getRqData(rqId);
	var result = eval(rqData.rq.responseText);
	return result;
};




lib.ajax.init();
