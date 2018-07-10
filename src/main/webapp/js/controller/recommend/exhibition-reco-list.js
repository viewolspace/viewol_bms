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
	'exhibition-api',
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
    exhibitionApi,
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

		renderTable: function() {
            return $table.render({
                elem: '#exhibitor-list'
                ,height: 'full-100'
                ,url: exhibitionApi.getUrl('queryRecommentProduct').url
				,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'name', title: '产品名称', width:200},
                    {field: 'companyId', title: '展商id', width:100},
                    {field: 'categoryId', title: '分类id', width:100},
                    {field: 'status', title: '状态', width:100, templet: function (d) {
                            if(d.status == 1){
                                return '<span>下架</span>';
                            } else {
                                return '<span>上架</span>';
                            }
                        }},
                    {field: 'image', title: '产品图片', width:100},
                    {field: 'content', title: '产品介绍', width:150},
                    {field: 'pdfUrl', title: '产品说明书', width:150},
                    {field: 'pdfName', title: '说明书的名字', width:150},
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
                    {fixed: 'right',width:80, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},


		delete: function(rowdata) {
			layer.confirm('确认取消首页推荐吗?', {
				icon: 3,
				title: '提示',
				closeBtn: 0
			}, function(index) {
				layer.load(0, {
					shade: 0.5
				});
				layer.close(index);

				request.request(exhibitionApi.getUrl('delRecommentHome'), {
					id: rowdata.id
				}, function() {
					layer.closeAll('loading');
					toast.success('取消推荐成功！');
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
                if(obj.event === 'row-delete'){//删除推荐
                    MyController.delete(data);
                }
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