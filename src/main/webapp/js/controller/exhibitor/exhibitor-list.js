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
	'exhibitor-api',
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
    exhibitorApi,
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
                elem: '#exhibitor-list'
                ,height: 'full-100'
                ,url: exhibitorApi.getUrl('exhibitorList').url
				,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'name', title: '展商名称', width:200},
                    {field: 'shortName', title: '展商简称', width:100},
                    {field: 'logo', title: '展商logo', width:100},
                    {field: 'banner', title: '展商形象图片', width:100},
                    {field: 'image', title: '展商图片', width:150},
                    {field: 'place', title: '展商位置', width:150},
                    {field: 'placeSvg', title: '展商svg位置', width:150},
                    {field: 'productNum', title: '允许上传产品的数量', width:150},
                    {field: 'canApply', title: '允许申请活动', width:100, templet: function (d) {
                        if(d.canApply == 1){
                        	return '<span>允许</span>';
                        } else {
                        	return '<span>不允许</span>';
                        }
                    }},
                    {field: 'isRecommend', title: '是否推荐', width:100, templet: function (d) {
                            if(d.isRecommend == 1){
                                return '<span>推荐</span>';
                            } else {
                                return '<span>非推荐</span>';
                            }
                        }},
                    {field: 'recommendNum', title: '推荐顺序', width:120},
                    {field: 'cTime', title: '创建时间', width:160, templet: function (d) {
						return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {field: 'mTime', title: '修改时间', width:160, templet: function (d) {
                            return moment(d.mTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {fixed: 'right',width:320, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},

		add: function() {
			var index = layer.open({
				type: 2,
				title: "添加展商",
                area: ['800px', '450px'],
				offset: '5%',
				scrollbar: false,
				content: webName + '/views/exhibitor/exhibitor-add.html',
				success: function(ly, index) {
					layer.iframeAuto(index);
				}
			});
		},

		modify: function(rowdata) {
			var url = request.composeUrl(webName + '/views/exhibitor/exhibitor-update.html', rowdata);
			var index = layer.open({
				type: 2,
				title: "修改展商",
                area: ['800px', '450px'],
                offset: '5%',
				scrollbar: false,
				content: url,
				success: function(ly, index) {
					layer.iframeAuto(index);
				}
			});
		},

        view: function(rowdata) {
            var url = request.composeUrl(webName + '/views/exhibitor/user-view.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "查看展商",
                area: ['800px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
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