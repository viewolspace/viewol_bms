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
	'form-util',
	'user-api',
	'table-util',
	'btns',
	'authority',
	'toast',
    'table',
	'valid-login'

];

registeModule(window, requireModules, {
	'good-api': 'api/good-api'
});

//参数有顺序
layui.use(requireModules, function(
	element,
	form,
	layer,
	request,
	formUtil,
	userApi,
	tableUtil,
	btns,
	authority,
	toast,
    table
) {

	var $ = layui.jquery;
    var $table = table;
    var mainTable;
	var MyController = {
		init: function() {
			var navId = request.getFixUrlParams("navId");

			var totalBtns = authority.getNavBtns(navId);
			var btnObjs = btns.getBtns(totalBtns);
			MyController.pageBtns = btns.getPageBtns(btnObjs);

			MyController.rowBtns = btns.getRowBtns(btnObjs);
			MyController.rowSwitchBtns = btns.getSwitchBtns(MyController.rowBtns);
			MyController.rowIconBtns = btns.getIconBtns(MyController.rowBtns);

			$('#page-btns').html(btns.renderBtns(MyController.pageBtns));
            btns.renderLayuiTableBtns(MyController.rowIconBtns, $("#barDemo"));

            mainTable = MyController.renderTable();
			MyController.bindEvent();
		},
		renderTable: function() {
            return $table.render({
                elem: '#online-list'
                ,height: 'full-100'
                ,url: userApi.getUrl('onlineUserList').url
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'sessionId', title: 'SessionId', width:300},
                    {field: 'realName', title: '真实姓名', width:100},
                    {field: 'userName', title: '登录账号', width:140},
                    {field: 'phone', title: '手机号', width:120},
                    {field: 'sessionStatus', title: '状态', width:80, templet: function (d) {
                        if(d.sessionStatus == true){
                        	return '<span>在线</span>';
                        } else {
                        	return '<span>离线</span>';
						}
                    }},
                    {field: 'lastAccess', title: '最后活动时间', width:160, templet: function (d) {
						return moment(d.lastAccess).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {field: 'startTime', title: '创建时间', width:160, templet: function (d) {
						return moment(d.startTime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {fixed: 'right',width:180, align:'center', toolbar: '#barDemo'}
                ]]
            });

		},

        refresh: function() {
            mainTable.reload();
        },

		view: function(rowdata) {
			var url = request.composeUrl(webName + '/views/user/online-user-view.html', rowdata);
			layer.open({
				type: 2,
				title: "查询在线用户详情",
				area: ['550px', '400px'],
				//offset: '10%',
				content: url,
				success: function(ly, index) {
					// layer.iframeAuto(index);
				}
			});
		},

		delete: function(rowdata) {
			layer.confirm('确认删除数据?', {
				icon: 3,
				title: '提示',
				closeBtn: 0
			}, function(index) {
				layer.load(0, {
					shade: 0.5
				});
				layer.close(index);

				request.request(userApi.getUrl('changeSessionStatus'), {
					sessionId: rowdata.sessionId
				}, function() {
					layer.closeAll('loading');
					toast.success('成功删除！');
					MyController.refresh();
				},true,function(){
					layer.closeAll('loading');
				});
			});
		},

		bindEvent: function() {
            $table.on('tool(test)', function(obj){
                var data = obj.data;
                if(obj.event === 'row-view'){
                    MyController.view(data);
                } else if(obj.event === 'row-delete'){//删除
                    MyController.delete(data);
                }
            });
		}
	};

	MyController.init();

});