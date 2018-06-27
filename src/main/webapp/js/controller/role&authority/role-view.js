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
    var param = ajax.getAllUrlParam();
    //已有权限
    var authorityData = {
        ids: param.permissions.split(",")//已有权限
    };
	var check = true;
	var recheckData = authorityData?authorityData.ids:'';
    recheckData = recheckData+"";//object转string

	var groupId = param.id?param.id:'';//角色ID
	var treeId = 'authority-tree';

	ajax.request(authorityApi.getUrl('getAuthorityTree'), {
		groupId: groupId
	}, function(result) {
		var treeId = 'authority-tree';
		if(!$.isEmptyObject(recheckData)) {
			recheckData = recheckData.split(",");
			$.each(result.data, function(index, item) {
				delete item.checked;
				if($.inArray('' + item.id, recheckData) != -1) {
					item.checked = true;
				}
			});
		}
		treeUtil.renderTree($('#' + treeId), {
			data:{
				key: {
					name: 'menuName'
				},
				simpleData: {
					pIdKey: 'parentId'
				}
			}

		}, result.data, check);
	});
});