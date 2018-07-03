
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
		namespace: '../category/',
		"categoryList": {
			url: "categoryList.do"
		} ,
        "uploadImg": {//上传图片
            type: 'POST',
            url: "uploadImg.do"
        } ,
        "addCategory": {
            type: 'POST',
            url: "addCategory.do"
        },
        "updateCategory": {
            type: 'POST',
            url: "updateCategory.do"
        } ,
        "deleteCategory": {
            url: "deleteCategory.do"
        }
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('category-api', result);
});