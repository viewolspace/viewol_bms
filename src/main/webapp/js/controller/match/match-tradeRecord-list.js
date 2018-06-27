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
                elem: '#tradeRecord-list'
                ,height: 'full-100'
                ,url: matchApi.getUrl('tradeRecordList').url
				,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[
                    {type:'numbers'},
                    {field: 'matchId', title: '比赛ID', width:80},
                    {field: 'matchName', title: '比赛名称', width:150},
                    {field: 'initFund', title: '初始资金', width:100},
                    {field: 'yongj', title: '佣金', width:80},
                    {field: 'yinhs', title: '印花税', width:80},
                    {field: 'openTime', title: '开始时间', width:160, templet: function (d) {
						return moment(d.openTime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {field: 'closeTime', title: '结束时间', width:160, templet: function (d) {
						return moment(d.closeTime).format("YYYY-MM-DD HH:mm:ss");
					}},
                    {field: 'des', title: '比赛介绍', width:200},
                    {fixed: 'right',width:MyController.toolbarWidth, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},

		refresh: function() {
            mainTable.reload();
		},

		bindEvent: function() {
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