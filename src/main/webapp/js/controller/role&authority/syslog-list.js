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
    'select-api',
	'role&authority-api',
	'table-util',
	'authority',
	'btns',
	'toast',
    'table',
	'laydate',
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
    formUtil,
    selectApi,
	authorityApi,
	tableUtil,
	authority,
	btns,
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

	var controller = {
		init: function() {
			var navId = ajax.getFixUrlParams("navId");

			var totalBtns = authority.getNavBtns(navId);
			var btnObjs = btns.getBtns(totalBtns);
			controller.pageBtns = btns.getPageBtns(btnObjs);
			controller.rowBtns = btns.getRowBtns(btnObjs);
			controller.rowIconBtns = btns.getIconBtns(controller.rowBtns);

            mainTable = controller.renderTable();
			controller.bindEvent();
		},

        getQueryCondition: function() {
            var condition = formUtil.composeData($("#condition"));
            return condition;
        },

		renderTable: function() {
            return $table.render({
                elem: '#syslog-list'
                ,height: 'full-100'
                ,url: authorityApi.getUrl('logList').url
				,method: 'post'
                ,page: true //开启分页
				,limit:50
                ,limits:[50,100,200]
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'ipAddress', title: 'IP', width:150},
                    {field: 'operId', title: '操作人ID', width:100},
                    {field: 'userName', title: '用户名', width:90},
                    {field: 'moduleName', title: '模块名称', width:100},
                    {field: 'methodName', title: '方法名称', width:150},
                    {field: 'methodDesc', title: '方法描述', width:150},
                    {field: 'createtime', title: '记录时间', width:160, templet: function(d){
                        return moment(d.createtime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {field: 'operContent', title: '入参', width:500}
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
                    where: controller.getQueryCondition()
                });
            });
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