/**
 * 这里配置权限的信息，包括获取权限，等
 */
var webName = getWebName();

layui.config({
	base: webName + '/js/modules/' //这个路径以页面引入的位置进行计算
});

var requireModules = [
	'request',
	'login',
	'role&authority-api'
];

registeModule(window,requireModules);

layui.define(requireModules,function(exports) {
	var ajax = layui.request,
	login = layui.login,
	authority = layui.authority,
	roleApi = layui['role&authority-api'];

	var authority = {

		getNavs: function() {//获取左侧导航信息
			var user = login.getLoginInfo();
			return ajax.request(roleApi.getUrl('getUserNavs'),user,function(result){

			});
		},

		getNavBtns: function(navId) {
			var user = login.getLoginInfo();
			var userId = user.id;
			var btns ;
			ajax.request(roleApi.getUrl('getNavBtns'),{
				userId: userId,
				id: navId
			},function(result){
				btns = result.data;
			},false);
			return btns;
		}

	};

	exports('authority',authority);

});
