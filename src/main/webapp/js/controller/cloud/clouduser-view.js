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
	var data = ajax.getAllUrlParam();

    data.createTime=moment(new Date(parseInt(data.createTime))).format("YYYY-MM-DD hh:mm:ss");

    formUtil.renderData($('#clouduser-view-form'),data);

});