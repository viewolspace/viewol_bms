var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'element',
	'form',
	'layer',
	'request',
	'role&authority-api',
	'table-util',
	'authority',
	'btns',
	'toast',
    'table',
	'valid-login'
];

registeModule(window, requireModules, {
	'role&authority-api': 'api/role&authority-api'
});

//参数有顺序
layui.use(requireModules, function(
	element,
	form,
	layer,
	ajax,
	authorityApi,
	tableUtil,
	authority,
	btns,
	toast,
    table

) {

	var $ = layui.jquery;
	var $table = table;
    var mainTable;
	var controller = {
		init: function() {
			var navId = ajax.getFixUrlParams("navId");

			var totalBtns = authority.getNavBtns(navId);
			var btnObjs = btns.getBtns(totalBtns);
			controller.pageBtns = btns.getPageBtns(btnObjs);
			controller.rowBtns = btns.getRowBtns(btnObjs);
			controller.rowIconBtns = btns.getIconBtns(controller.rowBtns);
			
			$('#page-btns').html(btns.renderBtns(controller.pageBtns));
            btns.renderLayuiTableBtns(controller.rowIconBtns, $("#barDemo"));

            mainTable = controller.renderTable();
			controller.bindEvent();
		},

		renderTable: function() {
            return $table.render({
                elem: '#role-list'
                ,height: 'full-100'
                ,url: authorityApi.getUrl('getRoleList').url
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'id', title: '角色ID', width:100},
                    {field: 'name', title: '角色名称', width:150},
                    {field: 'code', title: '角色编码', width:150},
                    {field: 'remark', title: '角色描述', width:250},
                    {fixed: 'right',width:180, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},

		refresh: function() {
            mainTable.reload();
		},

		add: function() {
			var index = layer.open({
				type: 2,
				title: "添加角色",
				area: ['50%','80%'],
				offset: '10%',
				scrollbar: false,
				content: webName + '/views/role&authority/role-add.html'
			});
		},

		rowEdit: function(rowdata) {
			var url = ajax.composeUrl(webName + '/views/role&authority/role-update.html', rowdata);
			var index = layer.open({
				type: 2,
				title: "修改权限",
				area: ['50%','80%'],
				offset: '10%',
				scrollbar: false,
				content: url
			});
		},

        rowView: function(rowdata) {
            var url = ajax.composeUrl(webName + '/views/role&authority/role-view.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "查看权限",
                area: ['260px', '400px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }

            });
        },

		rowDelete: function(rowdata) {
			layer.confirm('确认删除数据?', {
				icon: 3,
				title: '提示',
				closeBtn: 0
			}, function(index) {
				layer.load(0, {
					shade: 0.5
				});
				layer.close(index);

				ajax.request(authorityApi.getUrl('deleteRole'), {
					id: rowdata.id
				}, function() {
					layer.closeAll('loading');
					toast.success('成功删除！');
					controller.refresh();
				},true,function(){
					layer.closeAll('loading');
				});
			});
		},

        relateCloud: function(rowdata) {
            var url = ajax.composeUrl(webName + '/views/role&authority/role-relate-app.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "关联应用账号",
                area: ['520px', '400px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

		bindEvent: function() {

            $table.on('tool(test)', function(obj){
                var data = obj.data;
                if(obj.event === 'row-view'){
					controller.rowView(data);
                } else if(obj.event === 'row-edit'){//编辑
					controller.rowEdit(data);
                } else if(obj.event === 'row-delete'){//删除
					controller.rowDelete(data);
                } else if(obj.event === 'row-cloud'){//关联账户
                    controller.relateCloud(data);
                }
            });


			//点击刷新
			$('body').on('click', '.refresh', controller.refresh);
			//点击添加
			$('body').on('click', '.add', controller.add);
		}
	};

	controller.init();
	
	//开方外部访问api
	window.list = {
		refresh: function() {
			controller.refresh();
		}
	}
	
});