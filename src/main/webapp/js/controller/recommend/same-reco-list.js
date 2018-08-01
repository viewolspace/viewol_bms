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
	'same-reco-api',
    'select-api',
	'table-util',
	'btns',
	'authority',
	'toast',
    'table'

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
    sameRecoApi,
    selectApi,
	tableUtil,
	btns,
	authority,
	toast,
    table
) {

	var $ = layui.jquery;
    var $table = table;
    var form = layui.form;
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

            //初始化分类下拉列表
            // request.request(
            //     selectApi.getUrl('listDataDic'),{
            //         parentId: '0001'
            //     }, function(result) {
            //         formUtil.renderSelects('#categoryId', result.data, true);
            //         form.render('select');
            //     },
            //     false
            // );

            mainTable = MyController.renderTable();
			MyController.bindEvent();
		},
		getQueryCondition: function() {
			var condition = formUtil.composeData($("#condition"));
			return condition;
		},
		renderTable: function() {
            return $table.render({
                elem: '#recommend-list'
                ,height: 'full-100'
                ,url: sameRecoApi.getUrl('recommendList').url
				,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'type', title: '类型', width:100, templet: function (d) {
                            if(d.type == 1){
                                return '<span>展商</span>';
                            } else {
                                return '<span>产品</span>';
                            }
                        }},
                    {field: 'thirdId', title: '展商(产品)ID', width:120},
                    {field: 'name', title: '展商(产品)名称', width:200},
                    {field: 'categoryId', title: '分类', width:150},
                    {field: 'mTime', title: '修改时间', width:160, templet: function (d) {
                            return moment(d.mTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {field: 'cTime', title: '创建时间', width:160, templet: function (d) {
						return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {fixed: 'right',width:80, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},

		delete: function(rowdata) {
			layer.confirm('确认删除该条同类推荐吗?', {
				icon: 3,
				title: '提示',
				closeBtn: 0
			}, function(index) {
				layer.load(0, {
					shade: 0.5
				});
				layer.close(index);

				request.request(sameRecoApi.getUrl('cancelRecommend'), {
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
                if(obj.event === 'row-delete'){//删除
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
		}
	};

    form.on('select(myselect)', function(data){
    	var type = data.value;
        request.request(
            selectApi.getUrl('listDataDic'),{
                parentId: type
            }, function(result) {
                formUtil.renderSelects('#categoryId', result.data, true);
                form.render('select');
            },
            false
        );
    });

	window.list = {
		refresh: MyController.refresh
	}

	MyController.init();

});