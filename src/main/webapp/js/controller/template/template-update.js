var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'api-template-api',
    'toast',
    'key-bind',
    'laydate',
    'upload',
    'valid-login'

];

registeModule(window, requireModules, {
    'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function (form,
                                    formUtil,
                                    ajax,
                                    apiTemplateApi,
                                    toast,
                                    keyBind,
                                    laydate,
                                    upload) {
    var $ = layui.jquery;
    var f = layui.form;
    var param = ajax.getAllUrlParam();

    ajax.request(apiTemplateApi.getUrl('getTemplate'), {
        id: param.id
    }, function(result) {
        formUtil.renderData($('#template-update-form'),result.data);
    }, false);




    //初始化md编辑器
    var apiEditor = editormd("template-editormd", {
        width: "90%",
        height: 720,
        path: '../../frame/editor.md/lib/',
        onchange: function () {
            $("#output").html("onchange : this.id =>" + this.id + ", markdown =>" + this.getValue());
            console.log("onchange =>", this, this.id, this.settings, this.state);
        },
        toolbarIcons: function () {
            return ["undo", "redo", "|",
                "bold", "del", "italic", "quote", "|",
                "h1", "h2", "h3", "h4", "h5", "h6", "|",
                "list-ul", "list-ol", "hr", "|",
                "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "emoji", "html-entities", "pagebreak", "|",
                "goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
                "help", "info"]
        },
    });

    f.on('submit(template-update-form)', function (data) {
        ajax.request(apiTemplateApi.getUrl('updateTemplate'), data.field, function () {
            toast.success('修改模板成功');
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
        });
        return false;
    });

});