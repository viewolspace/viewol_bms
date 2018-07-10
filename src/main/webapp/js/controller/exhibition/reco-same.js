var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'exhibition-api',
    'same-reco-api',
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
    exhibitionApi,
    sameRecoApi,
    roleApi,
    toast
) {
    var $ = layui.jquery;
    var f = layui.form;
    var categoryData ;//分类相关的信息
    var param = ajax.getAllUrlParam();

    if(!$.isEmptyObject(param)) {
        formUtil.renderData($('#reco-same-form'), param);
    }

    /**
	 * 根据产品ID查询产品所属分类
     */
    ajax.request(exhibitionApi.getUrl('getExhibitionCategory'), {
        categoryId: param.categoryId	//产品类别ID
	}, function(result) {
        categoryData = result.data;
        $("#categoryNames").val(categoryData.categoryNames);
    });


    f.on('submit(reco-same-form)', function(data) {
        var datas = $.extend(true, data.field, categoryData);
        ajax.request(sameRecoApi.getUrl('addRecommentSame'), datas, function() {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
            toast.success('同类推荐成功');

        });
        return false;
    });

});