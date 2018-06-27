/**
 * API目录管理
 * @author nabaonan
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);

layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];
	var url = {
		namespace:'../resource/',
		'queryAllResource': {
			url: 'queryAllResource.do'
		},
		'getResourceTree':{//目录树页面
			url: 'getResourceTree.do'
		},
		'addResource': {//添加资源目录
            type: 'POST',
			url: 'addResource.do'
		},
        'updateResource': {//修改资源目录
            type: 'POST',
            url: 'updateResource.do'
        },
        'deleteResource': {//删除资源目录
            type: 'POST',
            url: 'deleteResource.do'
        },
        'getResourceSelect': {//查询资源目录，下拉框
            url: 'getResourceSelect.do'
        }

	}

	var result = $.extend({},baseApi, url);

	exports('resource-api', result);

});
