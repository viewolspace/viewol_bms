/**
 * 业务员管理api
 */
var requireModules = [
    'base-url'
];

window.top.registeModule(window, requireModules);
layui.define('base-url', function (exports) {
    var $ = layui.jquery;
    var baseApi = layui['base-url'];

    var url = {
        namespace: '../buser/',
        "bUserList": {
            url: "bUserList.do",
            type: "POST"
        }
    }

    var result = $.extend({}, baseApi, url);

    exports('buser-api', result);
});