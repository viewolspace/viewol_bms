
/**
 * 排行榜api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../rank/',
		"queryTotal": {//查询总榜
            type: 'POST',
			url: "total.do"
		},
        "queryMonth": {//查询月榜
            type: 'POST',
            url: "month.do"
        },
        "queryWeek": {//查询周榜
            type: 'POST',
            url: "week.do"
        },
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('rank-api', result);
});