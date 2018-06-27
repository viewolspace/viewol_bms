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
    'resource-api',
	'toast',
	'key-bind',
	'valid-login'
];

//这里注册没有初始化注册过的 模块路径，如果是modules下有子集 的模块需要在这里注册
registeModule(window, requireModules);

layui.use(requireModules, function(
	form,
	formUtil,
	layer,
	ajax,
	resourceApi,
	toast,
	keyBind
) {
	var $ = layui.jquery;
	var param = ajax.getAllUrlParam();
	var chooseFather = {
		id:0,//默认值是0，如果没有选择父亲
		levels:0//对于新增没有父权限，level是1，最高级，由于提交时候会自增1所以这里就是设置0
	};//选择的father，如果选择了权限树，则要赋值，如果没选择不赋值
	
	if(!$.isEmptyObject(param)) {
		param.type = param.type.split(',');
		chooseFather.id = param.parentId;
		var levels = parseInt(param.levels);
		chooseFather.levels = --levels;//因为下面需要自增，如果想保持原来的不变，这里需要自减一下
		formUtil.renderData($('#resource-form'), param);
	}

	var f = layui.form;

	//监听提交
	f.on('submit(resource-form)', function(data) {

		if(chooseFather){
			data.field.parentId = chooseFather.id;
			var levels = parseInt(chooseFather.levels);
			data.field.authValue = param.authValue;
			data.field.levels = ++levels;//如果选择了父节点则级别加一，没选择的话，保持不变
		}
		
		ajax.request(resourceApi.getUrl('addResource'), data.field, function() {
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index); //再执行关闭   
			parent.authorityList.refreshTable();
			toast.success('添加成功');
		});
		return false;

	});

	$('#choose-father').click(function() {
		ajax.composeUrl()
		layer.open({
			type: 2,
			anim: 3,
			title: "选择上级目录",
			area:['50%','80%'],
			content: webName + '/views/api/resource-tree.html',
			btn: ['确定', '取消'],
			yes: function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var datas = iframeWin.tree.getCheckedData();
				if(datas[0]){
					$('#input-father').val(datas[0].name);
					chooseFather = datas[0];
				}
				layer.close(index);
			}

		});
	});


});