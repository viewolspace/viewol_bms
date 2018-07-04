var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'form',
	'form-util',
	'request',
	'category-api',
	'toast',
	'key-bind'

];

registeModule(window, requireModules, {
	'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
	form,
	formUtil,
	ajax,
    categoryApi,
	toast,
	keyBind
	) {
	var $ = layui.jquery;
	var f = layui.form;
	var data = ajax.getAllUrlParam();

    formUtil.renderData($('#category-add-form'),data);

	f.on('submit(category-add-form)', function(data) {

		ajax.request(categoryApi.getUrl('addCategory'), data.field, function() {
			toast.success('添加成功');
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			parent.tree.refresh();
		});
		return false;
	});

});