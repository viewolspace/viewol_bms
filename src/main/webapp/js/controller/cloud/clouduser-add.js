var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'form',
	'form-util',
	'request',
    'clouduser-api',
	'toast',
	'key-bind',
    'laydate',
	'upload',
	'valid-login'

];

registeModule(window, requireModules, {
	'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
	form,
	formUtil,
	ajax,
    clouduserApi,
	toast,
	keyBind,
    laydate,
    upload
	) {
	var $ = layui.jquery;
	var f = layui.form;
	var data = ajax.getAllUrlParam();

	f.on('submit(clouduser-add-form)', function(data) {

		ajax.request(clouduserApi.getUrl('addCloudAuth'), data.field, function() {
			toast.success('添加云账号成功');
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			parent.list.refresh();
		});
		return false;
	});

});