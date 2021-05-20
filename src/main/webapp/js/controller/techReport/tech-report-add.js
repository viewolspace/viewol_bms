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
    'tech-report-api',
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
                                    techReportApi,
                                    upload,
                                    layer,
                                    table) {
    var $ = layui.jquery;
    var f = layui.form;

    //初始值
    setForumTime("中国国际消防大型石油化工储罐灭火及救援技术高峰论坛(W201室)");

    form.on('select(forumTitle)', function (data) {
        var value = data.value;  //select选中的值
        setForumTime(value);

    })

    function setForumTime(value){
        //清空赋值
        $("#forumTime").empty();
        if (value == "中国国际消防大型石油化工储罐灭火及救援技术高峰论坛(W201室)") {
            $('#forumTime').append(new Option("12日下午", "12日下午"));
        } else if (value == "中国智慧消防生态建设高峰论坛(W201室)") {
            $('#forumTime').append(new Option("13日上午", "13日上午"));
            $('#forumTime').append(new Option("13日下午", "13日下午"));
        } else if (value == "中国灭火救援技术论坛(W201室)") {
            $('#forumTime').append(new Option("14日上午", "14日上午"));
        } else if (value == "中国消防装备创新论坛(W201室)") {
            $('#forumTime').append(new Option("14日下午", "14日下午"));
        } else if (value == "中国消防创新成果技术交流高峰论坛暨创新评比颁奖典礼(南登录大厅)") {
            $('#forumTime').append(new Option("12日下午", "12日下午"));
            $('#forumTime').append(new Option("13日上午", "13日上午"));
            $('#forumTime').append(new Option("13日下午", "13日下午"));
        } else if (value == "其它会议及企业产品技术发布会(W105室)") {
            $('#forumTime').append(new Option("12日下午", "12日下午"));
            $('#forumTime').append(new Option("13日上午", "13日上午"));
            $('#forumTime').append(new Option("13日下午", "13日下午"));
            $('#forumTime').append(new Option("14日上午", "14日上午"));
            $('#forumTime').append(new Option("14日下午", "14日下午"));
        }

        layui.form.render("select");
    }

    f.on('submit(tech-form)', function (data) {

        ajax.request(techReportApi.getUrl('addTechReport'), data.field, function () {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
            toast.success('保存成功');
        });
        return false;
    });

});