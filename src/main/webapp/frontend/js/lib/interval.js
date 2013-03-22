// lib.interval 1.4.1
// requires: lib.array, lib.evt
//
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// timer manages timeouts and intervals

var lib = lib || {};
lib.interval = lib.interval || {};




lib.interval.name = "lib.interval";
lib.interval._data = [];




lib.interval.create = function (delay, fn, cfg) {
	// creates repeater and prepares it to work
	if (typeof cfg == "undefined") { cfg = {}; }
	var timerData = {
		fn: fn,
		cfg: cfg,
		delay: delay,
		iterations: 0,
		started: false
	};
	var id = lib.array.insert(lib.interval._data, timerData);
	timerData.id = id;
	return id;
};

lib.interval.run = function (id) {
	// starts repeater with given id
	var timerData, result = false;
	if (lib.interval._data && lib.interval._data[id]) {
		timerData = lib.interval._data[id];
		if (!timerData.started) {
			timerData.repeater = setInterval("lib.interval._trigger("+timerData.id+")", timerData.delay);
			timerData.started = true;
			result = true;
		}
	}
	return result;
};

lib.interval._trigger = function (id) {
	var timerData = lib.interval._data[id];
	timerData.fn(timerData.cfg);
	return true;
};

lib.interval.stop = function (id) {
	// stops repeater with given id
	var timerData = lib.interval._data[id];
	if (timerData) {
		if (timerData.repeater) {
			clearInterval(timerData.repeater);
		}
		timerData.started = false;
	}
	return true;
};

lib.interval.destroy = function (id) {
	// stops and removes the repeater with given id
	var timerData = lib.interval._data[id];
	lib.interval.stop(id);
	timerData = null;
	return true;
};

lib.interval.flush = function () {
	// destroys all repeaters. Prevents memory leaks
	for (var i = 0, l = lib.interval._data.length; i < l; i++) {
		lib.interval.destroy(i);
	}
	return true;
};




lib.evt.add(window, "unload", lib.interval.flush);
