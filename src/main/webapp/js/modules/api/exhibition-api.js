
/**
 * 展品管理api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../exhibition/',
		"exhibitionList": {
			url: "exhibitionList.do"
		} ,
        "upExhibition": {
            type: 'POST',
            url: "upExhibition.do"
        } ,
        "downExhibition": {
            type: 'POST',
            url: "downExhibition.do"
        }
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('exhibition-api', result);
});