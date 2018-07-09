
/**
 * 动态查询下拉框API
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../dictionary/',
        "listDataDic": {//查询分类下拉框
            url: "listDataDic.do"
        }
	}

	var result = $.extend({}, baseApi, url);
	exports('select-api', result);
});