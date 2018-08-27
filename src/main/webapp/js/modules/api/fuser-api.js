/**
 * 客户管理api
 */
var requireModules = [
    'base-url'
];

window.top.registeModule(window, requireModules);
layui.define('base-url', function (exports) {
    var $ = layui.jquery;
    var baseApi = layui['base-url'];

    var url = {
        namespace: '../fuser/',
        "fUserList": {
            url: "fUserList.do",
            type: "POST"
        }
    }

    var result = $.extend({}, baseApi, url);

    exports('fuser-api', result);
});