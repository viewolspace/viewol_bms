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
	var date = new Date();
    data.lastAccess = moment(date.setTime(data.lastAccess)).format("YYYY-MM-DD HH:mm:ss");
    data.startTime = moment(date.setTime(data.startTime)).format("YYYY-MM-DD HH:mm:ss");
    data.lastLoginTime = moment(date.setTime(data.lastLoginTime)).format("YYYY-MM-DD HH:mm:ss");

	formUtil.renderData($('#online-user-form'),data);
	f.render();
});