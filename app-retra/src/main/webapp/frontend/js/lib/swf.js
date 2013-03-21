// lib.swf 1.0
// requires sc, lib.evt, lib.elm
// 
// based on Unobtrusive Flash Objects (ufo) v3.20 <http://www.bobbyvandersluis.com/ufo/>
// Copyright 2005, 2006 Bobby van der Sluis
// This software is licensed under the CC-GNU LGPL <http://creativecommons.org/licenses/LGPL/2.1/>
// 

lib.swf = {
	name: "lib.swf",
	
	req: ["movie", "width", "height", "majorversion", "build"],
	opt: ["play", "loop", "menu", "quality", "scale", "salign", "wmode", "bgcolor", "base", "flashvars", "devicefont", "allowscriptaccess", "seamlesstabbing"],
	optAtt: ["id", "name", "align"],
	optExc: ["swliveconnect"],
	ximovie: "ufo.swf",
	xiwidth: "215",
	xiheight: "138",
	ua: navigator.userAgent.toLowerCase(),
	pluginType: "",
	fv: [0,0],
	foList: [],
	
	create: function (cfg, id) {
		// creates the swf movie object
		
		if (window.opera && document.embeds[id]) {
			// correct the Opera bug
			lib.swf._prepareOpera(id);
		} else {
			// default settings
			if (typeof cfg.swliveconnect == "undefined") { cfg.swliveconnect = "true" };
			
			if (!lib.swf._uaHas("w3cdom") || lib.swf._uaHas("ieMac")) {
				return false;
			};
			lib.swf.getFlashVersion();
			lib.swf.foList[id] = lib.swf._updateFO(cfg);
			lib.swf._createCSS("#" + id, "visibility:hidden;");
			lib.swf._domLoad(id);
		};
		return true;
	},
	
	get: function (id) {
		// returns the existing swf movie object
		// id - id of <object> and name of <embed>
		var swfObj = null;
		
		if ((navigator.appName.indexOf("Microsoft Internet") == -1) && document.embeds && document.embeds[id]) {
			swfObj = document.embeds[id];
		} else {
			var envelope = lib.elm.get(id);
			var objects = lib.elm.getByTag("object", envelope);
			if ((objects == null) || (objects.length < 1)) {
				var objects = lib.elm.getByTag("embed", envelope);
			};
			if ((objects == null) || (objects.length < 1)) {
				var objects = lib.elm.getByTag("div", envelope);
			};
			if ((objects != null) && (objects.length >= 1)) {
				swfObj = objects[0];
			};
		};
		return swfObj;
	},
	
	// following 7 methods work only if swliveconnect is enabled
	
	play: function (swfObj) { return swfObj.Play(); },
	
	stop: function (swfObj) { return swfObj.StopPlay(); },
	
	rewind: function (swfObj) { return swfObj.Rewind(); },
	
	getFrame: function (swfObj) { return (parseInt(swfObj.TGetProperty("/", 4)) - 1); },
	
	setFrame: function (swfObj, frameNumber) { return swfObj.GotoFrame(frameNumber); },
	
	setVar: function (swfObj, swfVarName, value) { return swfObj.SetVariable(swfVarName, value); },
	
	getVar: function (swfObj, swfVarName) { return swfObj.GetVariable(swfVarName); },
	
	hasFlashVersion: function (major, release) {
		return (lib.swf.fv[0] > major || (lib.swf.fv[0] == major && lib.swf.fv[1] >= release)) ? true : false;
	},
	
	getFlashVersion: function () {
		if (lib.swf.fv[0] != 0) return;  
		if (navigator.plugins && typeof navigator.plugins["Shockwave Flash"] == "object") {
			lib.swf.pluginType = "npapi";
			var _d = navigator.plugins["Shockwave Flash"].description;
			if (typeof _d != "undefined") {
				_d = _d.replace(/^.*\s+(\S+\s+\S+$)/, "$1");
				var _m = parseInt(_d.replace(/^(.*)\..*$/, "$1"), 10);
				var _r = /r/.test(_d) ? parseInt(_d.replace(/^.*r(.*)$/, "$1"), 10) : 0;
				lib.swf.fv = [_m, _r];
			}
		} else if (window.ActiveXObject) {
			lib.swf.pluginType = "ax";
			try { // avoid fp 6 crashes
				var _a = new ActiveXObject("ShockwaveFlash.ShockwaveFlash.7");
			} catch(e) {
				try { 
					var _a = new ActiveXObject("ShockwaveFlash.ShockwaveFlash.6");
					lib.swf.fv = [6, 0];
					_a.AllowScriptAccess = "always"; // throws if fp < 6.47 
				} catch(e) {
					if (lib.swf.fv[0] == 6) return;
				};
				try {
					var _a = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
				} catch(e) {}
			};
			if (typeof _a == "object") {
				var _d = _a.GetVariable("$version"); // bugs in fp 6.21/6.23
				if (typeof _d != "undefined") {
					_d = _d.replace(/^\S+\s+(.*)$/, "$1").split(",");
					lib.swf.fv = [parseInt(_d[0], 10), parseInt(_d[2], 10)];
				}
			}
		}
	},
	
	expressInstallCallback: function () {
		var _b = document.getElementsByTagName("body")[0];
		var _c = document.getElementById("xi-con");
		_b.removeChild(_c);
		lib.swf._createCSS("body", "height:auto; overflow:auto;");
		lib.swf._createCSS("html", "height:auto; overflow:auto;");
	},
	
	_updateFO: function (cfg) {
		if (typeof cfg.xi != "undefined" && cfg.xi == "true") {
			if (typeof cfg.ximovie == "undefined") {
				cfg.ximovie = lib.swf.ximovie;
			};
			if (typeof cfg.xiwidth == "undefined") {
				cfg.xiwidth = lib.swf.xiwidth;
			};
			if (typeof cfg.xiheight == "undefined") {
				cfg.xiheight = lib.swf.xiheight;
			}
		};
		cfg.mainCalled = false;
		return cfg;
	},

	_domLoad: function (id) {
		var _t = setInterval(function () {
			if ((document.getElementsByTagName("body")[0] != null || document.body != null) && document.getElementById(id) != null) {
				lib.swf._main(id);
				clearInterval(_t);
			}
		}, 250);
		if (typeof document.addEventListener != "undefined") {
			document.addEventListener("DOMContentLoaded", function () { lib.swf._main(id); clearInterval(_t); } , null); // Gecko, Opera 9+
		}
	},

	_main: function (id) {
		var _fo = lib.swf.foList[id];
		if (_fo.mainCalled) return;
		lib.swf.foList[id].mainCalled = true;
		document.getElementById(id).style.visibility = "hidden";
		if (lib.swf._hasRequiredParams(id)) {
			if (lib.swf.hasFlashVersion(parseInt(_fo.majorversion, 10), parseInt(_fo.build, 10))) {
				if (typeof _fo.setcontainercss != "undefined" && _fo.setcontainercss == "true") {
					lib.swf._setContainerCSS(id);
				};
				lib.swf._writeSWF(id);
			} else if (_fo.xi == "true" && lib.swf.hasFlashVersion(6, 65)) {
				lib.swf._createDialog(id);
			}
		}
		document.getElementById(id).style.visibility = "visible";
	},
	
	_createCSS: function (selector, declaration) {
		var _h = document.getElementsByTagName("head")[0]; 
		var _s = lib.swf._createElement("style");
		if (!lib.swf._uaHas("ieWin")) _s.appendChild(document.createTextNode(selector + " {" + declaration + "}")); // bugs in IE/Win
		_s.setAttribute("type", "text/css");
		_s.setAttribute("media", "screen"); 
		_h.appendChild(_s);
		if (lib.swf._uaHas("ieWin") && document.styleSheets && document.styleSheets.length > 0) {
			var _ls = document.styleSheets[document.styleSheets.length - 1];
			if (typeof _ls.addRule == "object") _ls.addRule(selector, declaration);
		}
	},
	
	_setContainerCSS: function (id) {
		var _fo = lib.swf.foList[id];
		var _w = /%/.test(_fo.width) ? "" : "px";
		var _h = /%/.test(_fo.height) ? "" : "px";
		lib.swf._createCSS("#" + id, "width:" + _fo.width + _w +"; height:" + _fo.height + _h +";");
		if (_fo.width == "100%") {
			lib.swf._createCSS("body", "margin-left:0; margin-right:0; padding-left:0; padding-right:0;");
		}
		if (_fo.height == "100%") {
			lib.swf._createCSS("html", "height:100%; overflow:hidden;");
			lib.swf._createCSS("body", "margin-top:0; margin-bottom:0; padding-top:0; padding-bottom:0; height:100%;");
		}
	},
	
	_createElement: function (el) {
		return (lib.swf._uaHas("xml") && typeof document.createElementNS != "undefined") ?  document.createElementNS("http://www.w3.org/1999/xhtml", el) : document.createElement(el);
	},
	
	_createObjParam: function (el, aName, aValue) {
		var _p = lib.swf._createElement("param");
		_p.setAttribute("name", aName);	
		_p.setAttribute("value", aValue);
		el.appendChild(_p);
	},
	
	_uaHas: function (ft) {
		var _u = lib.swf.ua;
		switch(ft) {
			case "w3cdom":
				return (typeof document.getElementById != "undefined" && typeof document.getElementsByTagName != "undefined" && (typeof document.createElement != "undefined" || typeof document.createElementNS != "undefined"));
			case "xml":
				var _m = document.getElementsByTagName("meta");
				var _l = _m.length;
				for (var i = 0; i < _l; i++) {
					if (/content-type/i.test(_m[i].getAttribute("http-equiv")) && /xml/i.test(_m[i].getAttribute("content"))) return true;
				}
				return false;
			case "ieMac":
				return /msie/.test(_u) && !/opera/.test(_u) && /mac/.test(_u);
			case "ieWin":
				return /msie/.test(_u) && !/opera/.test(_u) && /win/.test(_u);
			case "gecko":
				return /gecko/.test(_u) && !/applewebkit/.test(_u);
			case "opera":
				return /opera/.test(_u);
			case "safari":
				return /applewebkit/.test(_u);
			default:
				return false;
		}
	},
	
	_hasRequiredParams: function (id) {
		var _l = lib.swf.req.length;
		for (var i = 0; i < _l; i++) {
			if (typeof lib.swf.foList[id][lib.swf.req[i]] == "undefined") return false;
		}
		return true;
	},
	
	_writeSWF: function (id) {
		var _fo = lib.swf.foList[id];
		var _e = document.getElementById(id);
		if (lib.swf.pluginType == "npapi") {
			if (lib.swf._uaHas("gecko") || lib.swf._uaHas("xml")) {
				while(_e.hasChildNodes()) {
					_e.removeChild(_e.firstChild);
				};
				var _obj = lib.swf._createElement("object");
				_obj.setAttribute("type", "application/x-shockwave-flash");
				_obj.setAttribute("data", _fo.movie);
				_obj.setAttribute("width", _fo.width);
				_obj.setAttribute("height", _fo.height);
				var _l = lib.swf.optAtt.length;
				for (var i = 0; i < _l; i++) {
					if (typeof _fo[lib.swf.optAtt[i]] != "undefined") {
						_obj.setAttribute(lib.swf.optAtt[i], _fo[lib.swf.optAtt[i]]);
					}
				};
				var _o = lib.swf.opt.concat(lib.swf.optExc);
				var _l = _o.length;
				for (var i = 0; i < _l; i++) {
					if (typeof _fo[_o[i]] != "undefined") lib.swf._createObjParam(_obj, _o[i], _fo[_o[i]]);
				};
				_e.appendChild(_obj);
			}
			else {
				var _emb = "";
				var _o = lib.swf.opt.concat(lib.swf.optAtt).concat(lib.swf.optExc);
				var _l = _o.length;
				for (var i = 0; i < _l; i++) {
					if (typeof _fo[_o[i]] != "undefined") _emb += ' ' + _o[i] + '="' + _fo[_o[i]] + '"';
				}
				_e.innerHTML = '<embed name="'+ id +'" type="application/x-shockwave-flash" src="' + _fo.movie + '" width="' + _fo.width + '" height="' + _fo.height + '" pluginspage="http://www.macromedia.com/go/getflashplayer"' + _emb + '></embed>';
			}
		} else if (lib.swf.pluginType == "ax") {
			var _objAtt = "";
			var _l = lib.swf.optAtt.length;
			for (var i = 0; i < _l; i++) {
				if (typeof _fo[lib.swf.optAtt[i]] != "undefined") {
					_objAtt += ' ' + lib.swf.optAtt[i] + '="' + _fo[lib.swf.optAtt[i]] + '"';
				}
			};
			var _objPar = "";
			var _o = lib.swf.opt.concat(lib.swf.optExc);
			var _l = _o.length;
			for (var i = 0; i < _l; i++) {
				if (typeof _fo[_o[i]] != "undefined") {
					_objPar += '<param name="' + _o[i] + '" value="' + _fo[_o[i]] + '" />';
				}
			};
			var _p = window.location.protocol == "https:" ? "https:" : "http:";
			_e.innerHTML = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"' + _objAtt + ' width="' + _fo.width + '" height="' + _fo.height + '" codebase="' + _p + '//download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=' + _fo.majorversion + ',0,' + _fo.build + ',0"><param name="movie" value="' + _fo.movie + '" />' + _objPar + '</object>';
		}
	},
		
	_createDialog: function (id) {
		var _fo = lib.swf.foList[id];
		lib.swf._createCSS("html", "height:100%; overflow:hidden;");
		lib.swf._createCSS("body", "height:100%; overflow:hidden;");
		lib.swf._createCSS("#xi-con", "position:absolute; left:0; top:0; z-index:1000; width:100%; height:100%; background-color:#fff; filter:alpha(opacity:75); opacity:0.75;");
		lib.swf._createCSS("#xi-dia", "position:absolute; left:50%; top:50%; margin-left: -" + Math.round(parseInt(_fo.xiwidth, 10) / 2) + "px; margin-top: -" + Math.round(parseInt(_fo.xiheight, 10) / 2) + "px; width:" + _fo.xiwidth + "px; height:" + _fo.xiheight + "px;");
		var _b = document.getElementsByTagName("body")[0];
		var _c = lib.swf._createElement("div");
		_c.setAttribute("id", "xi-con");
		var _d = lib.swf._createElement("div");
		_d.setAttribute("id", "xi-dia");
		_c.appendChild(_d);
		_b.appendChild(_c);
		var _mmu = window.location;
		if (lib.swf._uaHas("xml") && lib.swf._uaHas("safari")) {
			var _mmd = document.getElementsByTagName("title")[0].firstChild.nodeValue = document.getElementsByTagName("title")[0].firstChild.nodeValue.slice(0, 47) + " - Flash Player Installation";
		}
		else {
			var _mmd = document.title = document.title.slice(0, 47) + " - Flash Player Installation";
		}
		var _mmp = lib.swf.pluginType == "ax" ? "ActiveX" : "PlugIn";
		var _uc = typeof _fo.xiurlcancel != "undefined" ? "&xiUrlCancel=" + _fo.xiurlcancel : "";
		var _uf = typeof _fo.xiurlfailed != "undefined" ? "&xiUrlFailed=" + _fo.xiurlfailed : "";
		lib.swf.foList["xi-dia"] = { movie:_fo.ximovie, width:_fo.xiwidth, height:_fo.xiheight, majorversion:"6", build:"65", flashvars:"MMredirectURL=" + _mmu + "&MMplayerType=" + _mmp + "&MMdoctitle=" + _mmd + _uc + _uf };
		lib.swf._writeSWF("xi-dia");
	},
	
	_prepareOpera: function (id) {
		// when using swliveconnect in Opera, it is necessary to put <embed> tag right
		// into the block where Flash movie should be.
		// this removes the alternative content
		var elm = lib.elm.get(id);
		for (var i = elm.childNodes.length - 1; i >=0; i--) {
			var actualNode = elm.childNodes[i];
			var deleteNode = true;
			if (actualNode.getAttribute) {
				if (actualNode.getAttribute("name") == id) {
					deleteNode = false;
				}
			};
			if (deleteNode) {
				elm.removeChild(actualNode);
			}
		};
		return true;
	},
	
	_cleanupIELeaks: function () {
		var _o = document.getElementsByTagName("object");
		var _l = _o.length
		for (var i = 0; i < _l; i++) {
			_o[i].style.display = "none";
			for (var x in _o[i]) {
				if (typeof _o[i][x] == "function") {
					_o[i][x] = null;
				}
			}
		}
	}

};

if (lib.swf._uaHas("ieWin")) {
	lib.evt.add(window, "unload", lib.swf._cleanupIELeaks);
};
