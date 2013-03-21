// lib.pulldn 0.1.0
// requires: lib.evt, lib.Box, lib.dhtml, lib.mouse, lib.interval
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// lib.pulldn needs the lib.mouse linked and running (!)

var lib = lib || {};
lib.pulldn = lib.pulldn || {};
lib.pulldn.opener = lib.pulldn.opener || {};
lib.pulldn.pulldnBox = lib.pulldn.pulldnBox || {};




lib.pulldn.name = "lib.pulldn";
lib.pulldn.opener.openClassName = "libPulldnOpen";
lib.pulldn.pulldnBox.openClassName = "libPulldnOpen";
lib.pulldn.timeout = 1000; // after this time [ms] the window check if it should be closed or not
lib.pulldn.chains = [];


lib.pulldn.init = function () {
	
	return true;
};

lib.pulldn.add = function (elm, contentElm, chainId) {
	
	// update lib.pulldn.chains array
	chainId = chainId || null;
	if ((chainId) && (lib.pulldn.chains[chainId])) {
		lib.array.insert(lib.pulldn.chains[chainId], elm);
	} else if ((chainId) && (!lib.pulldn.chains[chainId])) {
		lib.pulldn.chains[chainId] = [];
		lib.pulldn.chains[chainId][0] = elm;
	} else {
		chainId = lib.array.insert(lib.pulldn.chains, [ elm ]);
	}
	lib.data.set(elm, "chainId", chainId, lib.pulldn.name);
	
	lib.elm.envelopeChildren(elm, lib.pulldn.opener.template);

	var contentBox = lib.pulldn.PulldnBox.newInstance(contentElm, elm); 
	
	lib.data.set(elm, "contentBox", contentBox, lib.pulldn.name);
	lib.data.set(contentBox, "elm", elm, lib.pulldn.name);
	lib.data.set(elm, "openClassName", lib.pulldn.opener.openClassName, lib.pulldn.name );
	
	lib.evt.add(elm, "mouseover", lib.pulldn.open);
	
	lib.pulldn.close(elm);

	var repeaterCfg = {
		openerElm: elm,
		contentBox: contentBox
	};
	var repeaterId = lib.interval.create(lib.pulldn.timeout, lib.pulldn.checkClose, repeaterCfg);
	lib.data.set(elm, "repeaterId", repeaterId, lib.pulldn.name);
	repeaterCfg.repeaterId = repeaterId;

	return chainId;
};

lib.pulldn._setOpen = function (elm) {
	
	var i, l;
	var chainId = lib.data.get(elm, "chainId", lib.pulldn.name);
	
	for (i = 0, l = lib.pulldn.chains[chainId].length ; i < l; i++ ) {
		lib.pulldn._setClosed(lib.pulldn.chains[chainId][i]);
	}

	var openerOpenClassName = lib.data.get(elm, "openClassName", lib.pulldn.name );
	lib.cls.add(elm, openerOpenClassName);
	
	var contentBox = lib.data.get(elm, "contentBox", lib.pulldn.name );
	contentBox.open();
	
	return true;
};

lib.pulldn._setClosed = function (elm) {
	var openerOpenClassName = lib.data.get(elm, "openClassName", lib.pulldn.name );
	lib.cls.remove(elm, openerOpenClassName);
	
	var contentBox = lib.data.get(elm, "contentBox", lib.pulldn.name );
	contentBox.close();

	return true;
};

lib.pulldn.open = function (elm, e, evId) {
	lib.pulldn._setOpen(elm);
	
	var repeaterId = lib.data.get(elm, "repeaterId", lib.pulldn.name);
	lib.interval.run(repeaterId);

	return true;
};

lib.pulldn.close = function (elm) {
	lib.pulldn._setClosed(elm);

	return true;
};

lib.pulldn.checkClose = function (repeaterCfg) {
	// this function checks if pulldn shall remain open or if it shall be closed
	var closePullDn = true;
	if (
		lib.dhtml.isOver(repeaterCfg.openerElm, [lib.mouse.x, lib.mouse.y]) ||
		lib.dhtml.isOver(repeaterCfg.contentBox.envelope, [lib.mouse.x, lib.mouse.y])
	) {
		closePullDn = false;
	}
	if (closePullDn) {
		lib.pulldn.close(repeaterCfg.openerElm);
	}
	return true;
};

lib.pulldn.opener.template = {
	tagName: "div",
	children: [
		{tagName: "hr"},
		{contentNode: 0},
		{tagName: "hr"}
	]
};




lib.pulldn.PulldnBox = lib.Box.extend({

	opener: null,
	
	envelopeTemplate : {
		tagName: "div",
	
		style: {
			color: "#606060",
			border:"1px solid silver",
			backgroundColor: "snow",
			filter:"alpha(opacity=85)",
			opacity:"0.85",
			padding: "3px"
		},
		children: [
			{ contentNode: 0 }
		]
	},
	
	init: function (contentElm, openerElm) {
		openerElm = openerElm || document.body;
		
		lib.Class.init.apply(this, arguments);
		
		this.content = contentElm;
		this.envelope = lib.elm.create(lib.pulldn.PulldnBox.envelopeTemplate, [contentElm]);

		// this.envelope.style.visibility = "hidden";
		openerElm.appendChild(this.envelope);
		lib.dhtml.setAbsolute(this.envelope);
		lib.dhtml.fixSize(this.envelope);
		
		// register the instance
		lib.array.insert(lib.Box.boxNodes, this);
		
		this.opener = openerElm;
		return true;
	},
	
	open: function () {
		this.show(); 
	},
	
	close: function () {
		this.hide();
	}

});
