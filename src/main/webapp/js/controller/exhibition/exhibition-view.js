var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'toast',
    'key-bind',
    'layedit',
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
                                    layedit,
                                    exhibitionApi,
                                    upload,
                                    layer) {
    var $ = layui.jquery;
    var layedit = layui.layedit;
    var f = layui.form;
    var categoryData ;//分类相关的信息

    var param = ajax.getAllUrlParam();
    if(!$.isEmptyObject(param)) {
        formUtil.renderData($('#exhibition-view-form'), param);
    }
    $('#imageAvatarId').attr('src', param.image);
    $('#imageAvatar').val(param.image);

    $('#regImageAvatarId').attr('src', param.regImage);
    $('#regImageAvatar').val(param.regImage);

    /**
     * 根据产品ID查询产品所属分类
     */
    ajax.request(exhibitionApi.getUrl('getExhibitionCategory'), {
        categoryId: param.categoryId	//展品所属类别ID
    }, function(result) {
        categoryData = result.data;
        $("#categoryName").val(categoryData.categoryNames);
    });


    ajax.request(exhibitionApi.getUrl('getExhibition'), {
        id: param.id	//展品所属类别ID
    }, function(result) {
        if(result.status == true){
            $("#content").val(result.data.content);
            var index = layedit.build('content');//产品介绍富文本编辑初始化
        }
    });
});