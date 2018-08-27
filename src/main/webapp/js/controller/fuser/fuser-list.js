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
	'fuser-api',
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
    fuserApi,
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

            mainTable = MyController.renderTable();
			MyController.bindEvent();
		},
		getQueryCondition: function() {
			var condition = formUtil.composeData($("#condition"));
			return condition;
		},
		renderTable: function() {
            return $table.render({
                elem: '#fuser-list'
                ,height: 'full-100'
                ,url: fuserApi.getUrl('fUserList').url
                ,method: 'post'
                ,page: true
                ,limits:[10,50,100,200]
                ,cols: [[
                    {type:'numbers'},
                    {field: 'headImgUrl', title: '客户头像', width:100, templet: function (d) {
                    	if(d.headImgUrl){
                            return "<img src='"+d.headImgUrl+"' />";
						}else {
                    		return "";
						}
                    }},
                    {field: 'userId', title: '客户ID', width:100},
                    {field: 'userName', title: '客户姓名', width:100},
                    {field: 'phone', title: '手机号', width:130},
                    {field: 'company', title: '公司名称', width:250},
                    {field: 'position', title: '职位', width:100},
                    {field: 'email', title: '邮箱', width:200},
                    {field: 'age', title: '年龄', width:100},
                    {field: 'companyId', title: '邀请展商ID', width:100},
                    {field: 'companyName', title: '邀请展商名称', width:250},
                    {field: 'cTime', title: '注册时间', width:160, templet: function (d) {
                        return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                    }}
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
		}
	};

	MyController.init();
});