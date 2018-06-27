
/**
 * 云账号管理api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../cloud_user/',
		"getAll": {//查询列表
			url: "cloudUserList.do"
		},
        "addCloudAuth": {//添加云账号
            type: 'POST',
            url: "addCloudAuth.do"
        },
        "getCloudAccountSelect": {//初始化云账户下拉框
            url: "getCloudAccountSelect.do"
        }

	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('clouduser-api', result);
});