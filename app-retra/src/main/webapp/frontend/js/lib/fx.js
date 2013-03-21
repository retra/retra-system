// lib.fx 0.1.0
// requires: lib.elm, lib.evt, lib.interval, lib.vector
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// efects with DOM objects
//
// WARNING
// in MSIE, all positioned elements MUST have layout 
// (see http://www.satzansatz.de/cssd/onhavinglayout.html)
// or the absolute positioning will fail

var lib = lib || {};
lib.fx = lib.fx || {};




lib.fx.name = "lib.fx";
lib.fx.tweenEvtName = "aftertween";




lib.fx.tween = function ( obj, fn, delay, userCfg, fromPercent, toPercent, stepSizePercent, afterTweenFn ) {
	// runs fn on object obj
	fromPercent = fromPercent || 0;
	if (typeof toPercent != "number") { 
		toPercent = 100; 
	}
	if (typeof stepSizePercent != "number") {
		stepSizePercent = 1;
	} else if (stepSizePercent === 0) {
		throw "step size may not be 0";
	}
	afterTweenFn = afterTweenFn || null; 
	var cfg = {
		userCfg:         userCfg,
		obj:             obj,
		fn:              fn,
		delay:           delay,
		fromPercent:     fromPercent,
		toPercent:       toPercent,
		progressPercent: fromPercent,
		stepSizePercent: stepSizePercent,
		afterTweenFn:    afterTweenFn
	};
	var tweenId = lib.interval.create(delay, lib.fx._tweenCallback, cfg);
	cfg.tweenId = tweenId;
	
	lib.evt.addType(obj, lib.fx.tweenEvtName);
	
	lib.interval.run(tweenId);
	return tweenId;
};

lib.fx._tweenCallback = function ( cfg ) {
	// TODO: prepare multi fn running (fn could be an array of functions !)
	if (cfg.progressPercent >= cfg.toPercent) {
		cfg.progressPercent = cfg.toPercent;
	}
	
	cfg.fn(cfg.obj, cfg.progressPercent, cfg.userCfg, cfg); // calling fx function
	
	if (cfg.progressPercent == cfg.toPercent) { // end tween (this was the last step)
		lib.fx.stopTween(cfg.tweenId);
		if (cfg.afterTweenFn) {
			cfg.afterTweenFn(cfg.obj, cfg.toPercent, cfg);
		}
		lib.evt.run(cfg.obj, lib.fx.tweenEvtName);
	} else { // continue
		cfg.progressPercent = cfg.progressPercent + cfg.stepSizePercent;
	}
	return true;
};

lib.fx.stopTween = function ( tweenId ) {
	return lib.interval.destroy( tweenId );
};

lib.fx.resizeIt = function (elm, progressPercent, userCfg, cfg) {
	// resizes elm from start size to target size 
	// (defined by array userCfg.startSize and array userCfg.targetSize)
	var contrastWidth, contrastHeigth, contrastWidthPerPercent, contrastHeightPerPercent;
	var newSize = [];
	
	if (progressPercent < 100) {
		contrastWidth = userCfg.targetSize[0] - userCfg.startSize[0];
		contrastHeigth = userCfg.targetSize[1] - userCfg.startSize[1];
		
		contrastWidthPerPercent = contrastWidth / 100;
		contrastHeightPerPercent = contrastHeigth / 100;
		
		newSize[0] = userCfg.startSize[0] + (contrastWidthPerPercent * progressPercent); 
		newSize[1] = userCfg.startSize[1] + (contrastHeightPerPercent * progressPercent);
		newSize[0] = Math.round(newSize[0]);
		newSize[1] = Math.round(newSize[1]);
	} else {
		newSize = userCfg.targetSize;
	}
		lib.dhtml.setSize(elm, newSize); // seting element size

	return true;
};

lib.fx.moveIt = function (elm, progressPercent, userCfg, cfg) {
	// moves elm from start position to target position
	// (defined by array userCfg.startPosition and array userCfg.targetPosition)
	var contrastX, contrastY, contrastXPerPercent, contrastYPerPercent;
	var newPos = [];
	
	if (progressPercent < 100) {
		contrastX = userCfg.targetPos[0] - userCfg.startPos[0];
		contrastY = userCfg.targetPos[1] - userCfg.startPos[1];
		
		contrastXPerPercent = contrastX / 100;
		contrastYPerPercent = contrastY / 100;
		
		newPos[0] = userCfg.startPos[0] + (contrastXPerPercent * progressPercent); 
		newPos[1] = userCfg.startPos[1] + (contrastYPerPercent * progressPercent);
		newPos[0] = Math.round(newPos[0]);
		newPos[1] = Math.round(newPos[1]);
	} else {
		newPos = userCfg.targetPos;
	}
	lib.dhtml.setPos(elm, newPos); // seting element position

	return true;
};

lib.fx.colourIt = function (elm, progressPercent, userCfg, cfg) {
	// changes value of userCfg.cssProperty from value userCfg.ctartColor to value userCfg.targetColor
	var contrastR, contrastG, contrastB, contrastRPerPercent, contrastGPerPercent, contrastBPerPercent;
	var newColor = [];

	if (progressPercent < 100) {
		contrastR = userCfg.targetColor[0] - userCfg.startColor[0];
		contrastG = userCfg.targetColor[1] - userCfg.startColor[1];
		contrastB = userCfg.targetColor[2] - userCfg.startColor[2];
		
		contrastRPerPercent = contrastR / 100;
		contrastGPerPercent = contrastG / 100;
		contrastBPerPercent = contrastB / 100;
		
		newColor[0] = userCfg.startColor[0] + (contrastRPerPercent * progressPercent); 
		newColor[1] = userCfg.startColor[1] + (contrastGPerPercent * progressPercent);
		newColor[2] = userCfg.startColor[2] + (contrastBPerPercent * progressPercent);
		
		newColor[0] = Math.round(newColor[0]);
		newColor[1] = Math.round(newColor[1]);
		newColor[2] = Math.round(newColor[2]);
	} else {
		newColor = userCfg.targetColor;
	}
	
	var newProperty = {};
	newProperty[userCfg.cssProperty] = "rgb(" + newColor[0] + ", " + newColor[1] + ", " + newColor[2] + ")"; 
	lib.elm.applyStyle(elm, newProperty); // seting cssProperty color of element

	return true;
};

lib.fx.transparentIt = function (elm, progressPercent, userCfg, cfg) {
	// changes transparency from value userCfg.startTransparency to value userCfg.targetTransparency
	var contrast, contrastPerPercent, newTransparency;

	if (progressPercent < 100) {

		contrast = userCfg.targetTransparency - userCfg.startTransparency;
		contrastPerPercent = contrast / 100;
		newTransparency  = userCfg.startTransparency + (contrastPerPercent * progressPercent);
		newTransparency = Math.round(newTransparency);

	} else {
		newTransparency = userCfg.targetTransparency;
	}
		
		// applying style for MSIE		
		var cssIe = "filter";
		var newIeTransparency = {};
		newIeTransparency[cssIe] = "alpha(opacity=" + newTransparency + ")"; 
		lib.elm.applyStyle(elm,newIeTransparency);
		
		// applying style for other browsers
		var cssOther = "opacity";
		var newOtherTransparency = {};
		var newDividendTransparencyValue = newTransparency / 100;
		newOtherTransparency[cssOther] = newDividendTransparencyValue ;
		lib.elm.applyStyle(elm, newOtherTransparency);

	return true;
};
