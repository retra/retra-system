// lib.timeout 1.4.2
// requires: lib.array, lib.evt
//
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// manages timeouts

var lib = lib || {};
lib.timeout = lib.timeout || {};




lib.timeout.name = "lib.timeout";
lib.timeout._data = [];




lib.timeout.create = function (delay, fn, cfg) {
	// creates timeout and prepares it to work
	if (typeof cfg == "undefined") { cfg = {}; }
	var timerData = {
		fn: fn,
		cfg: cfg,
		delay: delay
	};
	var id = lib.array.insert(lib.timeout._data, timerData);
	timerData.id = id;
	return id;
};

lib.timeout.run = function (id) {
	// starts timeout with given id
	var timeout, result = false;
	if (lib.timeout._data && lib.timeout._data[id]) {
		timeout = lib.timeout._data[id];
		timeout.timeout = setTimeout("lib.timeout._trigger("+timeout.id+")", timeout.delay);
		result = true;
	}
	return result;
};

lib.timeout._trigger = function (id) {
	lib.timeout._data[id].fn(lib.timeout._data[id].cfg);
	lib.timeout._data[id] = null;
	return true;
};

lib.timeout.destroy = function (id) {
	// stops and removes the timeout with given id
	var timerData = lib.timeout._data[id];
	clearTimeout(id);
	timerData = null;
	return true;
};

lib.timeout.flush = function () {
	// destroys all timeouts. Prevents memory leaks
	for (var i = 0, l = lib.timeout._data.length; i < l; i++) {
		lib.timeout.destroy(i);
	}
	return true;
};




lib.evt.add(window, "unload", lib.timeout.flush);
