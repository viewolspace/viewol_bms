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
	'rank-api',
    'match-api',
	'table-util',
	'btns',
	'authority',
	'toast',
    'table',
	'laydate',
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
	rankApi,
	matchApi,
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
                        MyController.toolbarWidth += 17;//一个汉字20px
                    }
                });
            }

            //初始化比赛下拉框
             request.request(
                matchApi.getUrl('getMatchSelect'), null, function(result) {
                    formUtil.renderSelects('#matchId', result.data, true);
                    form.render('select');
                },
                false
            );

            mainTable = MyController.renderTable();
			MyController.bindEvent();
		},
		getQueryCondition: function() {
			var condition = formUtil.composeData($("#condition"));
			return condition;
		},
		renderTable: function() {
            return $table.render({
                elem: '#rank-list'
                ,height: 'full-100'
                ,url: rankApi.getUrl('queryWeek').url
				,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[
                    {field: 'rank', title: '排名', width:80},
                    {field: 'thirdId', title: '客户号', width:150},
                    {field: 'userId', title: '优顾用户ID', width:150},
                    {field: 'matchId', title: '比赛ID', width:100},
                    {field: 'profit', title: '盈亏', width:150},
                    {field: 'profitRate', title: '盈利率', width:100},
                    {field: 'sucRate', title: '成功率', width:100},
                    {field: 'rise', title: '排名变动', width:100},
                    {fixed: 'right',width:MyController.toolbarWidth, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},

        view: function(rowdata) {
		    var toUid = rowdata.userId;
            var url = request.composeUrl("http://m.youguu.com/mobile/simtrade-tiancheng/html/otherMatchTradeHome.html?matchId=8183&toUid="+toUid);
            var index = layer.open({
                type: 2,
                title: "个人主页",
                area: ['270px', '480px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
            });
            layer.full(index);
        },

		refresh: function() {
            mainTable.reload();
		},

		bindEvent: function() {
            $table.on('tool(test)', function(obj){
                var data = obj.data;
                if(obj.event === 'row-view'){
                    MyController.view(data);
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

	window.list = {
		refresh: MyController.refresh
	}

	MyController.init();

});