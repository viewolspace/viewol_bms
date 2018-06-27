
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
		namespace: '../template/',
		"getAll": {//查询API模板列表
			url: "templateList.do"
		} ,
        "addTemplate": {//添加API模板
            type: 'POST',
            url: "addTemplate.do"
        } ,
        "updateTemplate": {//修改API模板
            type: 'POST',
            url: "updateTemplate.do"
        },
        "getTemplate": {//查询API模板
            url: "getTemplate.do"
        },
        "deleteTemplate": {//删除API模板
            url: "deleteTemplate.do"
        },
        "getTemplateSelect": {//加载模板下拉选择框
            url: "getTemplateSelect.do"
        }

	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('api-template-api', result);
});