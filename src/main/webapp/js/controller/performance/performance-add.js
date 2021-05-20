var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: true
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'toast',
    'key-bind',
    'performance-api',
    'upload',
    'layer',
    'table'

];

registeModule(window, requireModules, {
    'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function (form,
                                    formUtil,
                                    ajax,
                                    toast,
                                    keyBind,
                                    performanceApi,
                                    upload,
                                    layer,
                                    table) {
    var $ = layui.jquery;
    var f = layui.form;

    setAdPosition("馆外大屏");

    form.on('select(adPositon)', function (data) {
        var value = data.value;  //select选中的值
        setAdPosition(value);
    })

    function setAdPosition(value) {
        $("#adPositon-adMethod-div").empty();
        var postionHtml = "";
        if (value == "馆外大屏") {
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="视频直播" title="视频直播">';
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="滚动播出" title="滚动播出">';
        } else if (value == "馆内大屏") {
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="视频直播" title="视频直播">';
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="滚动播出" title="滚动播出">';
        } else if (value == "官媒") {
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="视频直播" title="视频直播">';
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="视频回放" title="视频回放">';
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="图片直播" title="图片直播">';
        } else if (value == "自媒体") {
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="视频直播" title="视频直播">';
            postionHtml +='<input type="checkbox" name="adMethod" lay-skin="primary" value="媒体推送" title="媒体推送">';
        }
        $("#adPositon-adMethod-div").html(postionHtml);
        layui.form.render();
    }

    f.on('submit(performance-form)', function (data) {
        //获取checkbox[name='foodId']的值，获取所有选中的复选框，并将其值放入数组中
        var arr = new Array();
        $("input:checkbox[name='adMethod']:checked").each(function(i){
            arr[i] = $(this).val();
        });
        data.field.adMethod = arr.join(",");//将数组合并成字符串

        ajax.request(performanceApi.getUrl('addPerformance'), data.field, function () {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
            toast.success('保存成功');
        });
        return false;
    });

});