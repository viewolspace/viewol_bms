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
	roleApi,
	toast,
	keyBind
	) {
	var $ = layui.jquery;
	var f = layui.form;
	
	var data = ajax.getAllUrlParam();
	
	ajax.request(roleApi.getUrl('getRolesSelect'), null, function(result) {
		formUtil.renderSelects('#roleId', result.data);
		if(!$.isEmptyObject(data)){
			$('#username').attr('disabled',true);
			formUtil.renderData($('#sys-user-form'),data);
		}
		f.render();
	});
	

	f.on('submit(sys-user-form)', function(data) {

		ajax.request(userApi.getUrl('addSysUser'), data.field, function() {
			toast.success('添加用户成功');
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index); //再执行关闭  
			parent.list.refresh();//刷新列表
			toast.success('保存成功');
			
		});
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});

});