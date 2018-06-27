var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'form',
	'form-util',
	'layer',
	'request',
	'role&authority-api',
    'clouduser-api',
	'toast',
	'key-bind',
	'valid-login'
];

registeModule(window, requireModules);

layui.use(requireModules, function(
	form, 
	formUtil,
	layer, 
	ajax,
	roleApi,
    clouduserApi,
	toast,
	keyBind
) {
	var $ = layui.jquery;
	var param = ajax.getAllUrlParam();

    ajax.request(clouduserApi.getUrl('getCloudAccountSelect'), null, function(result) {
        formUtil.renderSelects('#appId', result.data);
        if(!$.isEmptyObject(param)){
            formUtil.renderData($('#relate-cloud-form'),param);
        }
        f.render();
    });

	if(!$.isEmptyObject(param)) {
		formUtil.renderData($('#relate-cloud-form'), param);
	}

	var f = layui.form;

    f.on('submit(relate-cloud-form)', function(data) {
        ajax.request(roleApi.getUrl('relateApp'), data.field, function() {
            toast.success('关联成功');
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();

        });
        return false;
    });

});