
/**
 * 日程管理api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../schedule/',
		"scheduleList": {
			url: "scheduleList.do"
		} ,
        "recommendSchedule": {//推荐日程
            type: 'POST',
            url: "recommendSchedule.do"
        } ,
        "reviewSchedule": {//审核日程，默认已审核
            type: 'POST',
            url: "reviewSchedule.do"
        },
        "recoScheduleList": {//已推荐日程列表
            type: 'POST',
            url: "recoScheduleList.do"
        } ,
        "unRecommendSchedule": {//取消推荐日程
            url: "unRecommendSchedule.do"
        }
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('schedule-api', result);
});