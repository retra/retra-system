// lib.tree 1.1.0
// requires: lib.cls, lib.data, lib.elm
// 
// protected by creative commons deed
// under the following conditions: Attribution, Share Alike
// http://creativecommons.org/licenses/by-sa/2.5/
// 
// takes a bunch of unordered lists and prepares them to be used as a tree

if (typeof lib == "undefined") {
	var lib = {};
};

if (typeof lib.tree == "undefined") {
	lib.tree = {};
};

if (typeof lib.tree.branch == "undefined") {
	lib.tree.branch = {};
};





lib.tree.name    = "lib.tree";
lib.tree.cls     = "tree";
lib.tree.rootCls = "treeRoot";




lib.tree.initRoot = function (ulElm) {
	// make ulElm the root of the tree
	lib.cls.add(ulElm, lib.tree.rootCls);
	return lib.tree.init(ulElm);
};

lib.tree.init = function (ulElm) {
	var initOk = lib.data.get(ulElm, "initOk", lib.tree.name);
	if (!initOk) {
		lib.cls.add(ulElm, lib.tree.cls);
		var branches = lib.elm.getChildrenByTag("li", ulElm); // get collection of all branches
		for (var i = 0, l = branches.length; i < l; i++) { // now we iterate only over real tree branches
			lib.tree.branch.init(branches[i]); // init each branch..
		};
		lib.data.set(ulElm, "branches", branches, lib.tree.name);
		lib.data.set(ulElm, "initOk", true, lib.tree.name); // prevent tree to be initialized twice
	};
	return true;
};

lib.tree.isTree = function (elm) {
	return lib.cls.has(elm, lib.tree.cls);
};

lib.tree.isTreeRoot = function (elm) {
	return lib.cls.has(elm, lib.tree.rootCls);
};

lib.tree.getTreeRoot = function (elm) {
	while (!lib.tree.isTreeRoot(elm)) {
		if (elm.parentNode) {
			elm = elm.parentNode;
		} else {
			return null; // tree root not found
		}
	};
	return elm;
};

lib.tree.getParentBranch = function (treeElm) {
	return lib.data.get(ulNodes[i], "parentBranch", lib.tree.name);
};

lib.tree.getBranches = function (treeElm) {
	return lib.data.get(treeElm, "branches", lib.tree.name);
};




lib.tree.branch.name = "lib.tree.branch";
	
lib.tree.branch.init = function (liElm) {
	// make liElm branch
	var ulNodes = lib.elm.getByTag("ul", liElm);
	findChildTree:
		for (var i = 0, l = ulNodes.length; i < l; i++) {
			if (lib.tree.isTree(ulNodes[i])) { // init first ul that is assigned as the tree
				lib.tree.init(ulNodes[i]);
				lib.data.set(liElm, "childTree", ulNodes[i], lib.tree.branch.name);
				lib.data.set(ulNodes[i], "parentBranch", liElm, lib.tree.name);
				break findChildTree;
			}
		};
	return true;
};

lib.tree.branch.getTree = function (branchElm) {
	return branchElm.parentNode;
};

lib.tree.branch.getChildTree = function (branchElm) {
	return lib.data.get(branchElm, "childTree", lib.tree.branch.name);
};

