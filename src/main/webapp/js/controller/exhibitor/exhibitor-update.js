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
    var categoryData ;//分类相关的信息
    var param = ajax.getAllUrlParam();

    if(!$.isEmptyObject(param)) {
        formUtil.renderData($('#exhibitor-update-form'), param);
    }

    /**
	 * 根据展商ID查询展商所属分类
     */
    ajax.request(exhibitorApi.getUrl('getExhibitorCategory'), {
    	id: param.id	//展商ID
	}, function(result) {
        categoryData = result.data;
        $("#categoryNames").val(categoryData.categoryNames);
    });

    $('#choose-category').click(function() {
        var ids = categoryData?categoryData.ids:'';

        var url = ajax.composeUrl(webName + '/views/exhibitor/category-tree.html', {
            check: true,
            recheckData: ids,	//前端回显数据
            type: 1			//分类的类型
        });

        layer.open({
            type: 2,
            title: "选择分类",
            content:url ,
            area:['50%','80%'],
            btn: ['确定了', '取消了'],
            yes: function(index, layero) {
                var iframeWin = window[layero.find('iframe')[0]['name']];
                categoryData = iframeWin.tree.getAuthorityData();
                layer.close(index);
                $("#categoryNames").val(categoryData.categoryNames);
            }
        });
    });


    f.on('submit(exhibitor-update-form)', function(data) {
        var datas = $.extend(true, data.field, categoryData);
        ajax.request(exhibitorApi.getUrl('updateExhibitor'), datas, function() {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
            toast.success('修改成功');

        });
        return false;
    });

});