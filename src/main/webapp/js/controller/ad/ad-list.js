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
	'ad-api',
	'table-util',
	'btns',
	'authority',
	'toast',
    'table',
    'clouduser-api',
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
	adApi,
	tableUtil,
	btns,
	authority,
	toast,
    table,
    clouduserApi
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

            //初始化应用名称下拉框
            request.request(
                clouduserApi.getUrl('getCloudAccountSelect'), null, function(result) {
                    formUtil.renderSelects('#appId', result.data, true);
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
                elem: '#ad-list'
                ,height: 'full-100'
                ,url: adApi.getUrl('getAll').url
				,method: 'post'
                ,page: true
                ,limits:[10,50,100,200]
                ,cols: [[
                    {type:'numbers'},
                    {field: 'id', title: '广告ID', width:80},
                    {field: 'title', title: '广告标题', width:150},
                    {field: 'forwardUrl', title: '跳转链接', width:300},
                    {field: 'adImage', title: '图片', width:150, templet: function (d) {
                            return "<a href='"+d.adImage+"' target='_blank'><img src='"+d.adImage+"' /></a>";
                    }},
                    // {field: 'contentType', title: '内容类型', width:100, templet: function (d) {
                    //     if(d.contentType==2501){
                    //         return "图片";
                    //     } else if(d.contentType==2502){
                    //         return "文字";
                    //     }
                    // }},
                    {field: 'beginDate', title: '开始时间', width:160, templet: function (d) {
						return moment(d.beginDate).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {field: 'endDate', title: '结束时间', width:160, templet: function (d) {
						return moment(d.endDate).format("YYYY-MM-DD HH:mm:ss");
					}},
                    {field: 'rank', title: '排序', width:80},
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
				content: webName + '/views/ad/ad-add.html',
				success: function(ly, index) {
					// layer.iframeAuto(index);
				}
			});
            layer.full(index);
		},

		modify: function(rowdata) {
			var url = request.composeUrl(webName + '/views/ad/ad-update.html', rowdata);
			var index = layer.open({
				type: 2,
				title: "修改广告",
                // area: ['800px', '450px'],
                offset: '5%',
				scrollbar: false,
				content: url,
				success: function(ly, index) {
					// layer.iframeAuto(index);
				}
			});
            layer.full(index);
		},

        view: function(rowdata) {
            var url = request.composeUrl(webName + '/views/ad/ad-view.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "查看广告",
                // area: ['800px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
            });
            layer.full(index);
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

                request.request(adApi.getUrl('deleteAd'), {
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