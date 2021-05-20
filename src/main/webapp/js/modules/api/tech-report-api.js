/**
 * 技术报告会申请
 */
var requireModules = [
    'base-url'
];

window.top.registeModule(window, requireModules);
layui.define('base-url', function (exports) {
    var $ = layui.jquery;
    var baseApi = layui['base-url'];

    var url = {
        namespace: '../techReport/',
        "techReportList": {
            type: 'POST',
            url: "techReportList.do"
        },
        "getTechReport": {
            type: 'POST',
            url: "getTechReport.do"
        },
        "addTechReport": {
            type: 'POST',
            url: "addTechReport.do"
        },
        "updateTechReport": {
            type: 'POST',
            url: "updateTechReport.do"
        },
        "deleteTechReport": {
            url: "deleteTechReport.do"
        }

    };

    var result = $.extend({}, baseApi, url);

    exports('tech-report-api', result);
});