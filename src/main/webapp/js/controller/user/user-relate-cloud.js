var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'form',
	'form-util',
	'request',
	'user-api',
    'clouduser-api',
	'role&authority-api',
	'toast',
	'key-bind',
	'valid-login'

];

registeModule(window, requireModules, {
	'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
	form,
	formUtil,
	ajax,
	userApi,
    clouduserApi,
	roleApi,
	toast,
	keyBind
	) {
	var $ = layui.jquery;
	var f = layui.form;
	
	var param = ajax.getAllUrlParam();

	ajax.request(clouduserApi.getUrl('getCloudAccountSelect'), null, function(result) {
		formUtil.renderSelects('#appId', result.data);
		if(!$.isEmptyObject(param)){
			formUtil.renderData($('#relate-cloud-form'),param);
        }
		f.render();
	});
	

	f.on('submit(relate-cloud-form)', function(data) {
        ajax.request(userApi.getUrl('addUserGroup'), data.field, function() {
			toast.success('关联成功');
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			parent.list.refresh();
			
		});
		return false;
	});

});