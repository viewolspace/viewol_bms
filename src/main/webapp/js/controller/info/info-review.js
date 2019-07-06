var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'form',
	'form-util',
	'request',
	'info-api',
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
	infoApi,
	roleApi,
	toast
	) {
	var $ = layui.jquery;
	var f = layui.form;
	var param = ajax.getAllUrlParam();

	formUtil.renderData($('#info-review-form'), param);


	f.on('submit(info-review-form)', function(data) {
		ajax.request(infoApi.getUrl('updateStatus'), data.field, function() {
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			parent.list.refresh();
			toast.success('保存成功');
		});
		return false;
	});

});