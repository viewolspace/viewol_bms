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
    'exhibition-api',
    'upload',
    'layer'

];

registeModule(window, requireModules, {
    'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function (form,
                                    formUtil,
                                    ajax,
                                    toast,
                                    keyBind,
                                    exhibitionApi,
                                    upload,
                                    layer) {
    var $ = layui.jquery;
    var f = layui.form;
    var productIdeaData;//创新产品信息

    var param = ajax.getAllUrlParam();
    ajax.request(exhibitionApi.getUrl('getProductIdea'), {
        id: param.productId	//产品ID
    }, function (result) {
        productIdeaData = result.data;
        if (!$.isEmptyObject(productIdeaData)) {
            formUtil.renderData($('#exhibition-idea-add-form'), productIdeaData);

            if(productIdeaData.categoryId == '其它'){
                $("#otherCategoryDiv").show();
            } else {
                $("#otherCategoryDiv").hide();
                $("#otherCategoryDiv").val("");
            }

            $('#promisePicAvatarId').attr('src', productIdeaData.promisePic);
            $('#promisePic').val(productIdeaData.promisePic);

            $('#productPicAvatarId').attr('src', productIdeaData.productPic);
            $('#productPic').val(productIdeaData.productPic);

            $('#comLogoAvatarId').attr('src', productIdeaData.comLogo);
            $('#comLogo').val(productIdeaData.comLogo);
        }
    });

    $('#downloadBtn').click(function () {
        var url = $('#ext').val();
        window.location.href = url;
    });

    //提交form表单
    f.on('submit(exhibition-idea-add-form)', function (data) {
        ajax.request(exhibitionApi.getUrl('updateProductIdeaStatus'), data.field, function () {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
            toast.success('修改成功');

        });
        return false;
    });

});