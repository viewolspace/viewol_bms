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
	'schedule-api',
	'table-util',
	'btns',
    'authority',
	'toast',
    'table',
    'laydate',

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
    scheduleApi,
	tableUtil,
	btns,
    authority,
	toast,
    table,
    laydate
) {

	var $ = layui.jquery;
    var $table = table;
    var mainTable;

    laydate.render({
        elem: '#createTime',
        range: true
    });

	var MyController = {
		init: function() {
			var navId = request.getFixUrlParams("navId");

			var totalBtns = authority.getNavBtns(navId);
			var btnObjs = btns.getBtns(totalBtns);
			MyController.pageBtns = btns.getPageBtns(btnObjs);
			MyController.switchPageBtns = btns.getSwitchPageBtns(btnObjs);

			MyController.rowBtns = btns.getRowBtns(btnObjs);
			MyController.rowSwitchBtns = btns.getSwitchBtns(MyController.rowBtns);
			MyController.rowIconBtns = btns.getIconBtns(MyController.rowBtns);

			$('#page-btns').html(btns.renderBtns(MyController.pageBtns)+btns.renderSwitchBtns(MyController.switchPageBtns));
            btns.renderLayuiTableBtns(MyController.rowIconBtns, $("#barDemo"));

            mainTable = MyController.renderTable();
			MyController.bindEvent();
		},
		getQueryCondition: function() {
			var condition = formUtil.composeData($("#condition"));
			return condition;
		},
		renderTable: function() {
            return $table.render({
                elem: '#recommend-schedule-list'
                ,height: 'full-100'
                ,url: scheduleApi.getUrl('recoScheduleList').url
				,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'scheduleId', title: '日程ID', width:100},
                    {field: 'title', title: '主题', width:400},
                    {field: 'companyName', title: '主办方', width:100},
                    {field: 'type', title: '推荐类型', width:100, templet: function (d) {
                        if(d.type == 1){
                        	return '<span>置顶活动</span>';
                        } else {
                        	return '<span>推荐活动</span>';
                        }
                    }},
                    {field: 'sTime', title: '推荐位开始时间', width:160, templet: function (d) {
						return moment(d.sTime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {field: 'eTime', title: '推荐位结束时间', width:160, templet: function (d) {
                            return moment(d.eTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {field: 'startTime', title: '活动开始时间', width:160, templet: function (d) {
                            return moment(d.startTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {field: 'endTime', title: '活动结束时间', width:160, templet: function (d) {
                            return moment(d.endTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {field: 'cTime', title: '创建时间', width:160, templet: function (d) {
                            return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {fixed: 'right',width:180, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},

		add: function() {
			var index = layer.open({
				type: 2,
				title: "添加用户",
				area: '80%',
				offset: '10%',
				scrollbar: false,
				content: webName + '/views/user/user-add.html',
				success: function(ly, index) {
					layer.iframeAuto(index);
				}
			});
		},

		modify: function(rowdata) {
			var url = request.composeUrl(webName + '/views/user/user-update.html', rowdata);
			var index = layer.open({
				type: 2,
				title: "修改用户",
				area: '80%',
				offset: '10%',
				scrollbar: false,
				content: url,
				success: function(ly, index) {
					layer.iframeAuto(index);
				}
			});
		},

        view: function(rowdata) {
            var url = request.composeUrl(webName + '/views/user/user-view.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "查看用户",
                area: '60%',
                offset: '10%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
                }
            });
        },

        relateCloud: function(rowdata) {
            var url = request.composeUrl(webName + '/views/user/user-relate-cloud.html', rowdata);
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

				request.request(userApi.getUrl('deleteUser'), {
					id: rowdata.id
				}, function() {
					layer.closeAll('loading');
					toast.success('成功删除！');
					MyController.refresh();
				},true,function(){
					layer.closeAll('loading');
				});
			});
		},

		refresh: function() {
            mainTable.reload();
		},

		bindEvent: function() {
            $table.on('tool(test)', function(obj){
                var data = obj.data;
                if(obj.event === 'row-view'){
                    MyController.view(data);
                } else if(obj.event === 'row-edit'){//编辑
                    MyController.modify(data);
                } else if(obj.event === 'row-delete'){//删除
                    MyController.delete(data);
                } else if(obj.event === 'row-cloud'){//关联账户
                    MyController.relateCloud(data);
                }

            });

			//点击查询按钮
			$('#search-btn').on('click', function() {
                mainTable.reload({
                    where: MyController.getQueryCondition()
                });
			});

            //点击刷新
            $('body').on('click', '.refresh', MyController.refresh);
			//点击添加
			$('body').on('click', '.add', MyController.add);

		}
	};

	window.list = {
		refresh: MyController.refresh
	}

	MyController.init();

});