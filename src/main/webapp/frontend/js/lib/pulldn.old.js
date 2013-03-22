// lib.pulldn 3.0
// requires sc, lib.gui, lib.evt, lib.elm, lib.dhtml, lib.cls, lib.interval
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// Creates the pulldown.
// Each pulldown has a chainId. If the pulldown is open, all other pulldowns with the same chainId are closed instantly.
// lib.pulldn.add creates 2 event handlers on target element: onpulldnopen and onpulldnclose.
// 
// WARNING - in function lib.pulldn.create(elm, ...) elm.style.position will be set to "relative", so avoid any external positioning of this element

lib.pulldn = {
	name: "lib.pulldn",
	timeout: 1000, // number of miliseconds - interval between checks if pulldown may be closed
	
	opener: {
		open: {
			className: "scGuiPullDnOpen",
			zIndex: 110
		},
		closed: {
			className: "scGuiPullDnClosed",
			zIndex: 10
		}
	},
	
	pullDn: {
		elm: {
			tagName: "div",
			className: "scGuiPullDn"
		},
		
		envelope: {
			elm: {
				tagName: "span",
				style: {
					display:  "block",
					position: "absolute",
					top: "0",
					left: "0"
				}
			}
		},
		open:   { style: { display: "block" } },
		closed: { style: { display: "none"  } }
	},
	
	chains: {}, // array of chains, chainId is a key
	
	create: function (elm, clsPullDn, clsOpen, clsClosed, chainId) {
		// create pulldown for elm
		// WARNING - elm.style.position will be set to "relative"
		if (typeof chainId == "undefined") { chainId = null; };
		if ((typeof clsPullDn == "undefined") || (clsPullDn == null)) { clsPullDn = lib.pulldn.pullDn.className; }
		if ((typeof clsOpen == "undefined") || (clsOpen == null)) { clsOpen = lib.pulldn.opener.open.className; }
		if ((typeof clsClosed == "undefined") || (clsClosed == null)) { clsClosed = lib.pulldn.opener.closed.className; }
		
		var env = lib.elm.create(lib.pulldn.pullDn.envelope.elm);
		elm.appendChild(env);
		lib.data.set(elm, "pullDnEnv", env, lib.pulldn.name);
		
		var pullDn = lib.elm.create(lib.pulldn.pullDn.elm);
		env.appendChild(pullDn);
		lib.data.set(elm, "pullDn", pullDn, lib.pulldn.name);
		
		lib.data.set(pullDn, "pullDnOpener", elm, lib.pulldn.name);
		lib.data.set(elm, "clsPullDn", clsPullDn, lib.pulldn.name);
		lib.cls.add(pullDn, clsPullDn);
		lib.data.set(elm, "clsOpen", clsOpen, lib.pulldn.name);
		lib.data.set(elm, "clsClosed", clsClosed, lib.pulldn.name);
		
		elm.onpulldnopen = function () {};
		elm.onpulldnclose = function () {};
		
		lib.data.set(elm, "chainId", chainId, lib.pulldn.name);
		if (chainId) {
			if (typeof lib.pulldn.chains[chainId] == "undefined") {
				lib.pulldn.chains[chainId] = [];
			};
			lib.array.push(lib.pulldn.chains[chainId], elm);
		};
		
		lib.pulldn.close(elm);
		
		var repeaterCfg = {
			pullDnOpener: elm,
			pullDn: pullDn
		};
		var repeaterId = lib.interval.create(lib.pulldn.timeout, lib.pulldn.check, repeaterCfg);
		lib.data.set(elm, "repeaterId", repeaterId, lib.pulldn.name);
		repeaterCfg.repeaterId = repeaterId;
		
		return pullDn;
	},
	
	_setPosition: function (elm) {
		// positions the pulldown block to the bottom left corner of the elm
		var pullDn = lib.data.get(elm, "pullDn", lib.pulldn.name);
		var env = pullDn.parentNode;
		
		var pos = lib.dhtml.getPos(elm);
		var size = lib.dhtml.getSize(elm);
		lib.dhtml.setPos(env, [pos[0], pos[1] + size[1]]);
		
		return true;
	},
	
	open: function (elm) {
		// open pulldn and call onpulldnopen
		
		lib.pulldn._setPosition(elm);
		
		var chainId = lib.data.get(elm, "chainId", lib.pulldn.name);
		if (chainId != null) {
			lib.pulldn.closeChain(chainId);
		};
		var pullDn = lib.data.get(elm, "pullDn", lib.pulldn.name);
		var clsOpen = lib.data.get(elm, "clsOpen", lib.pulldn.name);
		var clsClosed = lib.data.get(elm, "clsClosed", lib.pulldn.name);
		
		if (clsClosed != null) { lib.cls.remove(elm, clsClosed); }
		if (clsOpen != null) { lib.cls.add(elm, clsOpen); }
		elm.style.zIndex = lib.pulldn.opener.open.zIndex;
		pullDn.style.display = lib.pulldn.pullDn.open.style.display;
		
		elm.onpulldnopen(lib.evt.create("pulldnopen"));
		
		var repeaterId = lib.data.get(elm, "repeaterId", lib.pulldn.name);
		lib.interval.run(repeaterId);
		return true;
	},
	
	check: function (repeaterCfg) {
		// this function checks if pulldn shall remain open or if it shall be closed
		
		var closePullDn = true;
		if (lib.dhtml.isOver(repeaterCfg.pullDnOpener, [lib.mouse.x, lib.mouse.y])) {
			closePullDn = false;
		} else if (lib.dhtml.isOver(repeaterCfg.pullDn, [lib.mouse.x, lib.mouse.y])) {
			closePullDn = false;
		};
		if (closePullDn) {
			lib.pulldn.close(repeaterCfg.pullDnOpener);
		};
		return true;
	},
	
	close: function (elm) {
		// close the pulldown and call onpulldnclose
		lib.pulldn._setPosition(elm);
		
		var pullDn = lib.data.get(elm, "pullDn", lib.pulldn.name);
		var clsOpen = lib.data.get(elm, "clsOpen", lib.pulldn.name);
		var clsClosed = lib.data.get(elm, "clsClosed", lib.pulldn.name);
		
		if (clsOpen != null) { lib.cls.remove(elm, clsOpen); }
		if (clsClosed != null) { lib.cls.add(elm, clsClosed); }
		elm.style.zIndex = lib.pulldn.opener.closed.zIndex;
		pullDn.style.display = lib.pulldn.pullDn.closed.style.display;
		
		var repeaterId = lib.data.get(elm, "repeaterId", lib.pulldn.name);
		if (repeaterId != null) {
			lib.interval.stop(repeaterId);
			elm.onpulldnclose(lib.evt.create("pulldnclose"));
		};
		return true;
	},
	
	closeChain: function (chainId) {
		for (var i = 0, l = lib.pulldn.chains[chainId].length; i < l; i++) {
			lib.pulldn.close(lib.pulldn.chains[chainId][i]);
		};
		return true;
	}
};
