var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'form',
	'form-util',
	'request',
	'ad-api',
	'toast',
	'key-bind',
    'laydate',
	'upload',
    'date-util',
	'valid-login'

];

registeModule(window, requireModules, {
	'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
	form,
	formUtil,
	ajax,
	adApi,
	toast,
	keyBind,
    laydate,
    upload,
    dateUtil
	) {
	var $ = layui.jquery;
	var f = layui.form;
	var data = ajax.getAllUrlParam();

    data.beginDate=moment(new Date(parseInt(data.beginDate))).format("YYYY-MM-DD hh:mm:ss");
    data.endDate=moment(new Date(parseInt(data.endDate))).format("YYYY-MM-DD hh:mm:ss");

    formUtil.renderData($('#ad-view-form'),data);

    $('#avatar_show_id').attr('src', data.adImage);

});