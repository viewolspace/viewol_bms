
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
		"selectRealAccount": {//查询实盘单元列表，下拉框使用
			url: "selectRealAccount.do"
		},
        "listDataDic": {//查询数据字典，通用方法
            url: "listDataDic.do"
        },
		"listDataDicFlow":{//查询出入金流水类型，子单元流水类型
			url: "listDataDicFlow.do"
		}
	}

	var result = $.extend({}, baseApi, url);
	exports('select-api', result);
});