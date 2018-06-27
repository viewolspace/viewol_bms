
/**
 * 比赛管理api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../match/',
		"getAll": {//查询比赛列表
			url: "matchList.do"
		},
		"createMatch": {//创建比赛
			type: 'POST',
			url: "createMatch.do"
		},
        "updateMatch": {//修改比赛
            type: 'POST',
            url: "updateMatch.do"
        },
        "makeInveiteCode": {//生成比赛邀请码
            type: 'POST',
            url: "makeInveiteCode.do"
        },
		"getMatchSelect":{//查询比赛下拉框
			url: "getMatchSelect.do"
		},
        "tradeRecordList":{//查询交易记录
            url: "tradeRecordList.do"
        }


	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('match-api', result);
});