
/**
 * 同类推荐管理api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../sameRecommend/',
		"recommendList": {//同类推荐列表
            type: 'POST',
			url: "recommendList.do"
		} ,
        "cancelRecommend": {//取消推荐
            url: "cancelRecommend.do"
        },
        "addRecommentSame": {//同类推荐(展商)
            type: 'POST',
            url: "addRecommentSame.do"
        }
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('same-reco-api', result);
});