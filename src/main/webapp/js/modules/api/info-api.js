
/**
 * 资讯管理api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../info/',
		"infoList": {//查询列表
			url: "infoList.do"
		} ,
        "addInfo": {//添加
            type: 'POST',
            url: "addInfo.do"
        },
        "updateStatus": {//审核
            type: 'POST',
            url: "updateStatus.do"
        } ,
        "getInfo": {//查询资讯信息
            url: "getInfo.do"
        },
        "updateInfo": {//修改
            type: 'POST',
            url: "updateInfo.do"
        },
        "deleteInfo": {//删除
            url: "deleteInfo.do"
        },
        "uploadImg": {//上传资讯图片
            type: 'POST',
            url: "uploadImg.do"
        }
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('info-api', result);
});