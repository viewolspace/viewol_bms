/**
 * 登录api
 */
var requireModules =[
	'base-url'
];
window.top.registeModule(window,requireModules);

layui.define(requireModules, function(exports) {
	
	var $ = layui.jquery;
	var baseApi = layui['base-url'];
	var url = {
		namespace:'../login/',
		'validLogin': {
			url: 'validLogin.do'
		},
		'login': {
			type: 'POST',
			url: 'submitLogin.do'
		},
		'logout': {
			url: 'logout.do'
		},
		'getValidImg': {
			url:'getValidImg.do'
		},
		"unlock":{
            type: 'POST',
            url: 'unlock.do'
		},
        'register': {
            type: 'POST',
            url: 'register.do'//注册并登录
        }
	}
	
	var result = $.extend({},baseApi, url);

	exports('login-api', result);

});