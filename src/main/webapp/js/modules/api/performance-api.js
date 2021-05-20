/**
 * 展演区展演申请
 */
var requireModules = [
    'base-url'
];

window.top.registeModule(window, requireModules);
layui.define('base-url', function (exports) {
    var $ = layui.jquery;
    var baseApi = layui['base-url'];

    var url = {
        namespace: '../performance/',
        "getPerformance": {
            type: 'POST',
            url: "getPerformance.do"
        },
        "addPerformance": {
            type: 'POST',
            url: "addPerformance.do"
        },
        "performanceList": {
            type: 'POST',
            url: "performanceList.do"
        },
        "updatePerformance": {
            type: 'POST',
            url: "updatePerformance.do"
        },
        "deletePerformance": {
            url: "deletePerformance.do"
        }
    };

    var result = $.extend({}, baseApi, url);

    exports('performance-api', result);
});