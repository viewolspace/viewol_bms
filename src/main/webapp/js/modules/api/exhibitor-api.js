
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
		namespace: '../exhibitor/',
		"exhibitorList": {
			url: "exhibitorList.do"
		} ,
        "addExhibitor": {
            type: 'POST',
            url: "addExhibitor.do"
        },
        "updateExhibitor": {
            type: 'POST',
            url: "updateExhibitor.do"
        } ,
        "deleteExhibitor": {
            url: "deleteExhibitor.do"
        },
        "getExhibitorCategory": {//查询展商所属分类
            url: "getExhibitorCategory.do"
        }

	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('exhibitor-api', result);
});