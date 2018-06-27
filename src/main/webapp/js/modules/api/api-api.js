
/**
 * 广告管理api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../api/',
		"getAll": {//查询API列表
			url: "apiList.do"
		} ,
        "addApi": {//添加API
            type: 'POST',
            url: "addApi.do"
        } ,
        "updateApi": {//修改API
            type: 'POST',
            url: "updateApi.do"
        },
        "deleteApi": {//删除API
            url: "deleteApi.do"
        },
        "getApi": {//查询API
            url: "getApi.do"
        }
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('api-api', result);
});