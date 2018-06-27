var webName = getWebName();

var requireModules = [
	'request',
	'role&authority-api',
	'toast',
	'tree-util',
	'key-bind',
	'valid-login'

];
//这里注册没有初始化注册过的 模块路径，如果是modules下有子集 的模块需要在这里注册
registeModule(window, requireModules, {
	'role&authority-api': 'api/role&authority-api'
});

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
}).use(requireModules, function(
	ajax, 
	authorityApi, 
	toast, 
	treeUtil,
	keyBind
) {

	var treeId = 'dept-tree';
	ajax.request(authorityApi.getUrl('getDeptTree'), null, function(result) {
		treeUtil.renderTree($('#' + treeId), null, result.data);
	});

	//对外开方api，供父iframe访问
	window.tree = {
		getCheckedData: function() {
			return treeUtil.getCheckedData(treeId);
		}
	}

});