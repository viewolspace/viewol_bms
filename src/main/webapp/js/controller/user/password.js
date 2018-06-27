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
	var redirect =  layui.redirect;
	
	f.on('submit(password-form)', function(data) {

		ajax.request(userApi.getUrl('updatePwd'), data.field, function() {
			toast.success('密码修改成功');
			$('#password-form')[0].reset();
		});
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});

});