
/**
 * 用户管理api
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);
layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];

	var url = {
		namespace: '../user/',
		"getAll": {//查询系统用户列表
			url: "userlist.do"
		},
		"addSysUser": {//新增系统用户
			type: 'POST',
			url: "addUser.do"
		},
        "addUserGroup": {//设置用户分组(关联账户)
            type: 'POST',
            url: "addUserGroup.do"
        },
		"updateSysUser": {//修改系统用户
			type: 'POST',
			url: "updateUser.do"
		},
		"deleteUser": {//删除系统用户
			url: "deleteUser.do"
		},
		"updatePwd": {//修改密码
			type: 'POST',
			url: "updatePwd.do"
		},
		"onlineUserList": {//查询在线用户
			url: "onlineUserList.do"
		},
		"getOnlineUser": {//根据sessionId查询在线用户
			url: "getOnlineUser.do"
		},
		"changeSessionStatus": {//剔除用户
			url: "changeSessionStatus.do"
		}
	}
	//下面这种避免不同api相同key取值相同的问题
	var result = $.extend({}, baseApi, url);

	exports('user-api', result);
});