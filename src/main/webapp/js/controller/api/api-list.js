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
	'api-api',
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
	apiApi,
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

            //计算行按钮toolbar宽度
            if(MyController.rowIconBtns){
                MyController.toolbarWidth = 40;
                $.each(MyController.rowIconBtns, function(index, item) {
                    for(var i=0 ;i<item.name.length;i++){
                        MyController.toolbarWidth += 18;//一个汉字20px
                    }
                });
            }

            mainTable = MyController.renderTable();
			MyController.bindEvent();
		},
		getQueryCondition: function() {
			var condition = formUtil.composeData($("#condition"));
			return condition;
		},
		renderTable: function() {
            return $table.render({
                elem: '#api-list'
                ,height: 'full-100'
                ,url: apiApi.getUrl('getAll').url
				,method: 'post'
                ,page: true
                ,limits:[10,50,100,200]
                ,cols: [[
                    {type:'numbers'},
                    {field: 'projectId', title: '归属项目', width:100},
                    {field: 'serviceId', title: '归属Action', width:100},
                    {field: 'apiName', title: '接口名称', width:150},
                    {field: 'apiUrl', title: '接口地址', width:150},
                    {field: 'requestMethod', title: '请求方式', width:100},
                    {field: 'apiStatus', title: '是否可用', width:100},
                    {field: 'apiDeveloper', title: '开发者', width:100},
                    {field: 'updateTime', title: '修改时间', width:160, templet: function (d) {
						return moment(d.updateTime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {field: 'createTime', title: '创建时间', width:160, templet: function (d) {
						return moment(d.createTime).format("YYYY-MM-DD HH:mm:ss");
					}},
                    {fixed: 'right',width:MyController.toolbarWidth, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},

		add: function() {
			var index = layer.open({
				type: 2,
                // maxmin: true,
                title: "添加广告",
                // area: ['800px', '450px'],
                offset: '5%',
				scrollbar: false,
				content: webName + '/views/api/api-add.html',
				success: function(ly, index) {
					// layer.iframeAuto(index);
				}
			});
            layer.full(index);
		},

		modify: function(rowdata) {
			var url = request.composeUrl(webName + '/views/match/match-update.html', rowdata);
			var index = layer.open({
				type: 2,
				title: "修改比赛",
                area: ['800px', '450px'],
                offset: '5%',
				scrollbar: false,
				content: url,
				success: function(ly, index) {
					layer.iframeAuto(index);
				}
			});
		},

        viewImage: function () {
		    alert("adddddddddddd");
        },

		refresh: function() {
            mainTable.reload();
		},

		bindEvent: function() {
            $table.on('tool(test)', function(obj){
                var data = obj.data;
                if(obj.event === 'row-view'){
                    // controller.
                } else if(obj.event === 'row-edit'){//编辑
                    MyController.modify(data);
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