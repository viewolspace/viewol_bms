var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'exhibitor-api',
    'role&authority-api',
    'toast'

];

registeModule(window, requireModules, {
    'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
    form,
    formUtil,
    ajax,
    exhibitorApi,
    roleApi,
    toast
) {
    var $ = layui.jquery;
    var f = layui.form;
    var param = ajax.getAllUrlParam();

    if(!$.isEmptyObject(param)) {
        formUtil.renderData($('#exhibitor-top-form'), param);
    }

    f.on('submit(exhibitor-top-form)', function(data) {
        ajax.request(exhibitorApi.getUrl('addTop'), data.field, function() {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
            toast.success('置顶成功');

        });
        return false;
    });

});