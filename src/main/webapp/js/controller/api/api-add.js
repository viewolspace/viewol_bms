var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'api-api',
    'api-template-api',
    'resource-api',
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
                                    apiApi,
                                    apiTemplateApi,
                                    resourceApi,
                                    toast,
                                    keyBind,
                                    laydate,
                                    upload) {
    var $ = layui.jquery;
    var f = layui.form;
    var data = ajax.getAllUrlParam();

    //初始化模板下拉框
    ajax.request(apiTemplateApi.getUrl('getTemplateSelect'), null, function(result) {
        formUtil.renderSelects('#templateId', result.data);
        f.render();
    },false);


    //初始项目下拉框
    ajax.request(resourceApi.getUrl('getResourceSelect'), null, function(result) {
        formUtil.renderSelects('#templateId', result.data);
        f.render();
    },false);

    //初始化md编辑器
    var apiEditor = editormd("api-editormd", {
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

    //模板选择事件监听
    form.on('select(templateId)', function(data){
        layer.confirm('是否切换模板？', {
            btn: ['确定', '取消'],
            btn2: function(index, layero){
                layer.close(index);
            }
        }, function(index, layero){
            var templateId = data.value;

            ajax.request(apiTemplateApi.getUrl('getTemplate'), {
                id: templateId
            }, function(result) {
                apiEditor.setMarkdown(result.data.contentText);
            }, false);

            layer.close(index);
        });
    });

    f.on('submit(api-add-form)', function (data) {
        ajax.request(adApi.getUrl('addAd'), data.field, function () {
            toast.success('添加API成功');
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
        });
        return false;
    });

});