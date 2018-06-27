var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'form',
	'form-util',
	'layer',
	'request',
	'role&authority-api',
	'toast',
	'key-bind',
	'valid-login'
];

registeModule(window, requireModules);

layui.use(requireModules, function(
	form, 
	formUtil,
	layer, 
	ajax,
	roleApi,
	toast,
	keyBind
) {
	var $ = layui.jquery;
	var param = ajax.getAllUrlParam();
	if(!$.isEmptyObject(param)) {
		formUtil.renderData($('#role-form'), param);
	}

	var f = layui.form;

	//权限相关的信息
	var authorityData = {
		ids: param.permissions.split(",")//已有权限
	};

	//监听提交
	f.on('submit(role-form)', function(data) {
		var datas = $.extend(true, data.field,{authValue:param.authValue}, authorityData);
		ajax.request(roleApi.getUrl('updateRole'),datas,function(){
			
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index); //再执行关闭   
			parent.list.refresh();
			toast.success('更新成功');
		});
		return false;
	});
	
	$('button[type="reset"]').click(function() {
		authorityData = undefined;
	});

	$('#choose-authority').click(function() {
		var ids = authorityData?authorityData.ids:'';
		
		var url = ajax.composeUrl(webName + '/views/role&authority/authority-tree.html', {
			check: true,
			recheckData: ids,//前端回显数据
			groupId:param.id?param.id:''//用户组id也就是角色id
		});
		
		layer.open({
			type: 2,
			title: "选择权限",
			content:url ,
			area:['50%','80%'],
			btn: ['确定了', '取消了'],
			yes: function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				authorityData = iframeWin.tree.getAuthorityData();
				layer.close(index);
			}

		});
	});
	


});