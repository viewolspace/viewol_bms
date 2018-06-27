var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'match-api',
    'toast',
    'key-bind',
    'laydate',
    'upload',
    'ad-api',
    'valid-login'

];

registeModule(window, requireModules, {
    'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
    form,
    formUtil,
    ajax,
    matchApi,
    toast,
    keyBind,
    laydate,
    upload,
    adApi
) {
    var $ = layui.jquery;
    var f = layui.form;

    var data = ajax.getAllUrlParam();


    data.beginTime=moment(new Date(parseInt(data.beginTime))).format("YYYY-MM-DD hh:mm:ss");
    data.endTime=moment(new Date(parseInt(data.endTime))).format("YYYY-MM-DD hh:mm:ss");

    data.openTime=moment(new Date(parseInt(data.openTime))).format("YYYY-MM-DD hh:mm:ss");
    data.closeTime=moment(new Date(parseInt(data.closeTime))).format("YYYY-MM-DD hh:mm:ss");

    formUtil.renderData($('#match-view-form'),data);

    $('#avatar_show_id').attr('src', data.logoImage);

});