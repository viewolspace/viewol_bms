
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
		namespace: '../ad/',
		"getAll": {//查询广告列表
			url: "adList.do"
		} ,
        "uploadImg": {//上传图片
            type: 'POST',
            url: "uploadImg.do"
        } ,
        "addAd": {//添加广告
            type: 'POST',
            url: "addAd.do"
        },
        "updateAd": {//修改广告
            type: 'POST',
            url: "updateAd.do"
        } ,
        "deleteAd": {//删除广告
            url: "deleteAd.do"
        }
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('ad-api', result);
});