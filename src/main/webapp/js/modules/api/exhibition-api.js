/**
 * 展品管理api
 */
var requireModules = [
    'base-url'
];

window.top.registeModule(window, requireModules);
layui.define('base-url', function (exports) {
    var $ = layui.jquery;
    var baseApi = layui['base-url'];

    var url = {
        namespace: '../exhibition/',
        "exhibitionList": {
            url: "exhibitionList.do"
        },
        "upExhibition": {
            type: 'POST',
            url: "upExhibition.do"
        },
        "downExhibition": {
            type: 'POST',
            url: "downExhibition.do"
        },
        "queryRecommentProduct": {//查询首页推荐产品
            type: 'POST',
            url: "queryRecommentProduct.do"
        },
        "addRecommentHome": {
            type: 'POST',
            url: "addRecommentHome.do"
        },
        "delRecommentHome": {
            type: 'POST',
            url: "delRecommentHome.do"
        },
        "getExhibitionCategory": {
            url: "getExhibitionCategory.do"
        },
        "addTop": {
            type: 'POST',
            url: "addTop.do"
        },
        "delTop": {
            type: 'POST',
            url: "delTop.do"
        },
        "queryTopProduct": {
            type: 'POST',
            url: "queryTopProduct.do"
        },
        "getExhibition": {
            type: 'POST',
            url: "getExhibition.do"
        },
        "productIdeaList": {
            type: 'POST',
            url: "productIdeaList.do"
        },
        "updateProductIdeaStatus": {
            type: 'POST',
            url: "updateProductIdeaStatus.do"
        },
        "getProductIdea": {
            type: 'POST',
            url: "getProductIdea.do"
        }
    };

    //下面这种避免不同api相同key取值相同的问题
    var result = $.extend({}, baseApi, url);

    exports('exhibition-api', result);
});