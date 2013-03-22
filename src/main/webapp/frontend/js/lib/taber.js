// lib.taber 0.0.1
// requires: elm.js, array.js, cls.js, data.js, evt.js
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// creates tabs from definition lists




if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.taber == "undefined") {
	lib.taber = {};
};




lib.taber.name = "lib.taber";
lib.taber.cls = "taber";                            // taber class
lib.taber.clsTab = "tab";                           // tab - apply to dt and dd
lib.taber.clsActive = "tabActive";                  // activated tab
lib.taber.clsInactive = "tabInactive";              // deactivated tab
lib.taber.clsDefaultActive = "tabDefaultActive";    // deactivated tab
lib.taber.reorder = false;                          // put all dt's before all dd's

lib.taber.ddZIndex = 100;                           // basic taber Z index
lib.taber.dtActiveZIndex = lib.taber.ddZIndex + 1;
lib.taber.dtInactiveZIndex = lib.taber.ddZIndex - 1;

lib.taber.ddFrameWidth = 3;                         // change this number, if border of dd is different

lib.elm.dtEnvelopeTemplate = {
	tagName: "div",
	style:{border: "solid 1px red"},
	children: [
		{contentNode: 0}
	]
};

lib.elm.ddEnvelopeTemplate = {
	contentNode: 0
};

lib.elm.ddActiveStyle = {
	border: "solid 1px blue",
	display: "block",
	marginLeft: "0px",
	position: "relative" // needed for proper zIndex activity
};

lib.elm.ddInactiveStyle = {
	display: "none"
};




lib.taber.init = function (dl) {
	dl = lib.elm.get(dl);
	dl.style.position = "relative";
	if ( (dl && dl.tagName && dl.tagName.toLowerCase() == "dl") ) { // dl is valid definition list
		var dts = [];
		var currentNode = dl.firstChild;
		if (currentNode) {
			var actualDt = null;
			do {
				if (currentNode.tagName) { // Node is tag, go on
					if (currentNode.tagName.toLowerCase() == "dt") {
						// definition term found, process it
						var dt = currentNode;
						lib.taber._initDt(currentNode);
						actualDt = dt;
						lib.array.push(dts, dt);
						
					} else if (currentNode.tagName.toLowerCase() == "dd") {
						// definition data found
						var dd = currentNode;
						lib.taber._initDd(dd);
						lib.taber._attach(actualDt, dd);
					}
				}
				currentNode = currentNode.nextSibling;
			} while (currentNode);
		};
		
		if (lib.taber.reorder) {
			// reorder the elements so dt's are before all dd's
			for (var i = dts.length - 1 ; i >= 0; i--) {
				dl.insertBefore(dts[i], dl.firstChild);
			}
		};
		
		lib.data.set(dl, "dts", dts, lib.taber.name);
		
		lib.taber._initTabs(dl);
		
		var defaultActiveDtElm = lib.data.get(dl, "defaultActiveDtElm", lib.taber.name);
		
		if (defaultActiveDtElm) {
			lib.taber._setActive(defaultActiveDtElm);
		} else {
			lib.taber._setActive(dts[0]);
		};
	};
	
	return dl;
};

lib.taber._initDt = function (dtElm) {
	// add decorator spans
	lib.elm.applyStyle(dtElm, lib.elm.dtStyle);
	lib.taber._envelope(dtElm, lib.elm.dtEnvelopeTemplate);
	lib.taber._setInactive(dtElm);
	
	lib.evt.add(dtElm, "click", lib.taber.activateTab); 	// seting event
	// lib.data.set(dtElm, "handlerId", handlerId, lib.taber.name); 			//
	
	var dl = dtElm.parentNode;
	if (lib.cls.has(dtElm, lib.taber.clsDefaultActive)) {
		lib.data.set(dl, "defaultActiveDtElm", dtElm, lib.taber.name);
	};
	lib.cls.add(dtElm, lib.taber.clsTab);
	return true;
};

lib.taber._initDd = function (ddElm) {
	// add decorator spans
	lib.taber._envelope(ddElm, lib.elm.ddEnvelopeTemplate);
	lib.taber._setInactive(ddElm);
	lib.cls.add(ddElm, lib.taber.clsTab);
	
	ddElm.style.zIndex = lib.taber.ddZIndex;
	return true;
};

lib.taber._initTabs = function (dl) {
	
	var dts = lib.data.get(dl, "dts", lib.taber.name);
	
	var dlPosition = lib.dhtml.getPos(dl);
	var dtX = dlPosition[0];
	var dtMaxHeight = 0;
	
	// set horizontal position of the tabs
	for (var i = 0, l = dts.length; i < l; i++) {
		lib.dhtml.setAbsolute(dts[i]);
		var dtSize = lib.dhtml.getSize(dts[i]);
		var dtHeight = dtSize[1];
		var dtPosition = [dtX, dlPosition[1]];
		
		lib.dhtml.setPos(dts[i], dtPosition);
		
		dtX = dtX + dtSize[0];
		
		if (dtSize[1] > dtMaxHeight) {
			dtMaxHeight = dtSize[1];
		};
	};
	
	dlPosition[1] = dlPosition[1] + dtMaxHeight;
	dl.style.top = dtMaxHeight + "px";
	dl.style.marginBottom = dtMaxHeight + "px";
	
	// set vertical position of the tabs
	for (var i = 0, l = dts.length; i < l; i++) {
		var dtSize = lib.dhtml.getSize(dts[i]);
		var dtPos = lib.dhtml.getPos(dts[i]);
		var dtHeight = dtSize[1];
		var dtY = dlPosition[1] - dtHeight + lib.taber.ddFrameWidth;
		var dtPosition = [dtPos[0], dtY];
		
		lib.dhtml.setPos(dts[i], dtPosition);
	};
};

lib.taber._setActive = function (elm) {
	var dt;
	if (elm.tagName.toLowerCase() == "dd") {
		dt = lib.data.get(elm, "dt", lib.taber.name);
	} else {
		dt = elm;
	};
	
	if (dt) {
		// first deactivate the active tab
		var dl = dt.parentNode;
		var activeDtElm = lib.data.get(dl, "activeDtElm", lib.taber.name);
		if (activeDtElm) {
			lib.taber._setInactive(activeDtElm);
		};
		lib.data.set(dl, "activeDtElm", dt, lib.taber.name);
		// now activate the clicked tab
		lib.cls.remove(dt, lib.taber.clsInactive);
		lib.cls.add(dt, lib.taber.clsActive);
		// ... and his dd's
		var dds = lib.data.get(dt, "dds", lib.taber.name);
		if (dds) { 
			for (var i = 0, l = dds.length; i < l; i++) {
				var dd = dds[i];
				lib.cls.remove(dd, lib.taber.clsInactive);
				lib.cls.add(dd, lib.taber.clsActive);
				lib.elm.applyStyle(dd, lib.elm.ddActiveStyle);
			}
		};
		
		// set Z-index of active dt
		dt.style.zIndex = lib.taber.dtActiveZIndex;
	} else {
		// elm is not dt
		lib.cls.remove(elm, lib.taber.clsInactive);
		lib.cls.add(elm, lib.taber.clsActive);
		if (elm.tagName.toLowerCase() == "dd") {
			lib.elm.applyStyle(elm, lib.elm.ddActiveStyle);
		}
	};
	return true;
};

lib.taber._setInactive = function (elm) {
	var dt;
	if (elm.tagName.toLowerCase() == "dd") {
		dt = lib.data.get(elm, "dt", lib.taber.name);
	} else {
		dt = elm;
	};
	
	if (dt) {
		var dds = lib.data.get(dt, "dds", lib.taber.name);
		lib.cls.remove(dt, lib.taber.clsActive);
		lib.cls.add(dt, lib.taber.clsInactive);
		
		if (dds) { 
			for (var i = 0, l = dds.length; i < l; i++) {
				var dd = dds[i];
				lib.cls.remove(dd, lib.taber.clsActive);
				lib.cls.add(dd, lib.taber.clsInactive);
				lib.elm.applyStyle(dd, lib.elm.ddInactiveStyle);
			}
		};
		// set Z index of inactive dt
		dt.style.zIndex = lib.taber.dtInactiveZIndex;
	} else {
		lib.cls.remove(elm, lib.taber.clsActive);
		lib.cls.add(elm, lib.taber.clsInactive);
		if (elm.tagName.toLowerCase() == "dd") {
			lib.elm.applyStyle(elm, lib.elm.ddInactiveStyle);
		}
	};
	return true;
};

lib.taber._envelope = function (elm, template) {
	var childrenElm = lib.elm.envelopeChildren(elm, {tagName: "div"});
	var envelopeElm = lib.elm.create(template, [childrenElm]);
	elm.appendChild(envelopeElm);
	return envelopeElm;
};

lib.taber._attach = function (dt, dd) {
	var dds = lib.data.get(dt, "dds", lib.taber.name);
	if (dds == null) {
		dds = [];
	};
	lib.array.push(dds, dd);
	lib.data.set(dt, "dds", dds, lib.taber.name);
	
	lib.data.set(dd, "dt", dt, lib.taber.name);
	
	return true;
}

lib.taber.getTab = function (elm) {
	// if elm belongs to a tab, returns reference to that tab. Otherwise returns null
};

lib.taber.activateTab = function (tabElm) {
	lib.taber._setActive(tabElm);
	return true;
};

lib.taber.deactivateTab = function (tabElm) {
	
};

lib.taber.getActiveTabNumber = function (dlElm) {
	var activeTabNumber = 0;
	var activeDtElm = lib.data.get(dlElm, "activeDtElm", lib.taber.name);
	var dts = lib.data.get(dlElm, "dts", lib.taber.name);
	
	findActiveTabNumber:
		for(var i = 0, l = dts.length; i < l; i++){
			if (dts[i] == activeDtElm) {
				activeTabNumber = i;
				break findActiveTabNumber;
			}
		};
	return activeTabNumber;
};

lib.taber.isFirstTabActive = function (dlElm) {
	var isFirst = false;
	if (lib.taber.getActiveTabNumber(dlElm) == 0) {
		isFirst = true;
	};
	return isFirst;
};

lib.taber.isLastTabActive = function (dlElm) {
	var isLast = false;
	if (lib.taber.getActiveTabNumber(dlElm) == lib.taber.getTabsNumber(dlElm)) {
		isLast = true;
	};
	return isLast;
};

lib.taber.getTabsNumber = function (dlElm) {
	return lib.data.get(dlElm, "dts", lib.taber.name).length;
};

lib.taber.createBtn = function (template, action, dlElm) {
	var buttonElm = lib.elm.create(template);
	lib.data.set(buttonElm, "dlElm", dlElm, lib.taber.name);
	
	switch (action) {
		case "next":
			lib.evt.add(buttonElm, "click", lib.taber.activateNextTab);
			break;
		case "prev":
			lib.evt.add(buttonElm, "click", lib.taber.activatePrevTab);
			break;
	};
	
	return buttonElm;
};

lib.taber.getTaber = function (elm) {
	var taberElm = null;
	
	if ((elm.tagName.toLowerCase() == "dl") && (lib.data.get(dl, "dts", lib.taber.name))) {
		taberElm = elm;
	} else {
		taberElm = lib.data.get(elm, "dlElm", lib.taber.name);
		
		if (!taberElm) {
			var currentElm = elm;
			findTaber:
				do {
					var dl = lib.elm.getAncestorByTag(elm, "dl");
					if (dl != null) {
						var dts = lib.data.get(dl, "dts", lib.taber.name);
						if (dts) {
							taberElm = dl;
							break findTaber;
						}
					};
					currentElm = dl;
				} while (currentElm != null);
		}
	};
	return taberElm;
};

lib.taber.activateNextTab = function (elm, e) {
	var dl = lib.taber.getTaber(elm);
	if (dl) {
		var dts = lib.data.get(dl, "dts", lib.taber.name);
		var indexOfActiveDtElm = lib.taber.getActiveTabNumber(dl);
		
		var lastIndexOfArray = lib.taber.getTabsNumber(dl) - 1;
		
		if (indexOfActiveDtElm != lastIndexOfArray) {
			var nextActiveDtId = indexOfActiveDtElm + 1;
			if (nextActiveDtId <= lastIndexOfArray) {
				var nextActiveDt = dts[nextActiveDtId];
				lib.taber._setActive(nextActiveDt);
			};
	//		lib.taber._refreshNextAndPrevButt(dl);
		} else {
	//		lib.taber._refreshNextAndPrevButt(dl);
		};
	};
	if (e) {
		lib.evt.cancel(e);
	};
	return true;
};

lib.taber.activatePrevTab = function (elm, e) {
	var dl = lib.taber.getTaber(elm);
	if (dl) {
		var dts = lib.data.get(dl, "dts", lib.taber.name);
		var indexOfActiveDtElm = lib.taber.getActiveTabNumber(dl);
		
		if (indexOfActiveDtElm != 0) {
			var prevActiveDtId = indexOfActiveDtElm - 1;
			if (prevActiveDtId >= 0) {
				var prevActiveDt = dts[prevActiveDtId];
				lib.taber._setActive(prevActiveDt);
			};
	//		lib.taber._refreshNextAndPrevButt(dl);
		} else {
	//		lib.taber._refreshNextAndPrevButt(dl);
		};
	};
	if (e) {
		lib.evt.cancel(e);
	};
	return true;
};
