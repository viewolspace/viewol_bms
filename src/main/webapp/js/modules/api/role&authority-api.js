/**
 * 角色权限api
 * @author nabaonan
 */
var requireModules =[
	'base-url'
];

window.top.registeModule(window,requireModules);

layui.define('base-url', function(exports) {
	var $ = layui.jquery;
	var baseApi = layui['base-url'];
	var url = {
		namespace:'role&authority/',
		'getUserNavs': {//获取用户左侧导航   ---左侧导航
			url:'../../permission/queryMenuPermission.do'
			//url:'user-navs.json'
		},
		'getNavBtns':{//点击导航获取右侧的按钮
			url:'../../permission/queryBtnPermission.do'
			//url:'nav-btns.json'
		},
		'getAllUsers': {//获取系统用户    --------系统用户列表
			url: 'sys-user-list.json'
		},
		'getAllAutorityList': {//加载权限列表树    ----权限列表
			url: '../../permission/queryAllPermission.do'
		},
		'getAuthorityTree':{//这里可以和上边配成同一个接口，但是感觉配成同一个不好，因为这里是纯tree，上面那个有其他字段，最好分开   ---配置权限的树型结构
			url: '../../permission/queryAllPermission.do'
		},
		'getDeptTree': {//获取部门的树形结构
			url: 'dept-tree.json'
		},
		'getRoleList': {//角色列表
			url: '../../role/rolelist.do'
		},
		'getRolesSelect': {//获取角色下拉框
			url: '../../role/getRolesSelect.do'
		},
		'updateSysUser':{//更新系统用户
			url:'../true.json'
		},
		'addAuthority':{//添加权限
			type: 'POST',
			url:'../../permission/addSysPermission.do'
		},
		'updateAuthority':{//修改权限
			type: 'POST',
			url:'../../permission/updateSysPermission.do'
		},
		'deleteAuthority':{	//删除权限
			type: 'POST',
			url: '../../permission/deleteSysPermission.do'
		},
		'addRole':{//添加角色
			type:'POST',
			url:'../../role/addRole.do'
		},
		'updateRole':{//更新角色
			type:'POST',
			url:'../../role/updateRole.do'
		},
		'deleteRole':{//删除角色
			type:'POST',
			url:'../../role/deleteRole.do'
		},
		'enableUser':{//启用系统用户
			url:'../false.json'
		},
		'resetPwd':{//重置密码
			url:'../true.json'
		},
		'getSignRepList':{
			url:'../key-value.json'
		},
		'logList':{
			url:'../../sysLog/logList.do'
		},
        'relateApp':{//关联应用
            type:'POST',
            url:'../../role/relateApp.do'
        }


	}

	var result = $.extend({},baseApi, url);

	exports('role&authority-api', result);

});
