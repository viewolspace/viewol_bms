var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'schedule-api',
    'role&authority-api',
    'toast',
    'laydate',
    'layedit'

];

registeModule(window, requireModules, {
    'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function (form,
                                    formUtil,
                                    ajax,
                                    scheduleApi,
                                    roleApi,
                                    toast,
                                    laydate,
                                    layedit) {
    var $ = layui.jquery;
    var f = layui.form;

    var data = ajax.getAllUrlParam();

    laydate.render({
        elem: '#sTime',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    laydate.render({
        elem: '#eTime',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    ajax.request(scheduleApi.getUrl('getSchedule'), {
        "id": data.scheduleId
    }, function (result) {
        data = result.data;
        data.sTime=moment(new Date(parseInt(data.sTime))).format("YYYY-MM-DD HH:mm:ss");
        data.eTime=moment(new Date(parseInt(data.eTime))).format("YYYY-MM-DD HH:mm:ss");

        formUtil.renderData($('#schedule-update-form'),data);

        ajax.request(scheduleApi.getUrl('getScheduleMaErCode'), {
            "id": data.id,
            "width": 100
        }, function (result) {
            $('#imageAvatarId').attr('src', "data:image/png;base64,"+result.ercode);
        }, false, function (result) {

        });
    }, false, function (result) {

    });






    layedit.set({
        uploadImage: {
            url: scheduleApi.getUrl('uploadContentImage').url,
            type: 'POST'
        }
    });

    // var index = layedit.build('content');//活动内容富文本编辑初始化

    f.on('submit(schedule-update-form)', function (data) {
        ajax.request(scheduleApi.getUrl('updateSchedule'), data.field, function () {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
            toast.success('保存成功');

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

});