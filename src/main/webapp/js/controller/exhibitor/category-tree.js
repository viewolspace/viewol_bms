var webName = getWebName();

var requireModules = [
	'request',
	'category-api',
	'toast',
	'tree-util'
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
    categoryApi,
	toast, 
	treeUtil
) {

	var check = ajax.getFixUrlParams('check') ? true : false;
	var recheckData = ajax.getFixUrlParams('recheckData');
	var type = ajax.getFixUrlParams('type');
	var treeId = 'category-tree';

	ajax.request(categoryApi.getUrl('queryAllCategory'), {
        type: type
	}, function(result) {
		var treeId = 'category-tree';
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

	//对外开方api，供父iframe访问
	window.tree = {
		getCheckedData: function() {
			var data = treeUtil.getCheckedData(treeId);
			return data;
		},
		getAuthorityData: function() {
			var datas = this.getCheckedData();
			var categoryNames = [];
			var ids = [];
			$.each(datas, function(index, item) {
				ids.push(item.id);
                categoryNames.push(item.menuName);
			});

			return {
				ids: ids,
				categoryNames: categoryNames
			};
		}
	}

});