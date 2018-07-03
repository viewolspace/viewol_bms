
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
        "uploadImg": {//上传图片
            type: 'POST',
            url: "uploadImg.do"
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
        "batchAddExhibitor": {
            type: 'POST',
            url: "batchAddExhibitor.do"
        }

	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('exhibitor-api', result);
});