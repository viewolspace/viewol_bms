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
    scheduleApi,
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
                        MyController.toolbarWidth += 17;//一个汉字20px
                    }
                });
            }

            mainTable = MyController.renderTable();
			MyController.bindEvent();
		},
		getQueryCondition: function() {
			var condition = formUtil.composeData($("#condition"));
			condition.type = 0;//只查询协会日程
            condition.companyId=-1;
			return condition;
		},
		renderTable: function() {
            return $table.render({
                elem: '#schedule-list'
                ,height: 'full-100'
                ,url: scheduleApi.getUrl('scheduleList').url
                ,method: 'post'
                ,page: true
                ,limits:[10,50,100,200]
                ,cols: [[
                    {type:'numbers'},
                    {field: 'companyId', title: '主办方的id', width:100},
                    {field: 'type', title: '发布人', width:100, templet: function (d) {
                            if(d.type == 1){
                                return '<span>展商</span>';
                            } else {
                                return '<span>主办方</span>';
                            }
                        }},
                    {field: 'companyName', title: '主办方的名称', width:150},
                    {field: 'id', title: '小程序码', width:100, templet: function (d) {
                        request.request(scheduleApi.getUrl('getScheduleMaErCode'), {
                            "width":100,
                            "id": d.id
                        }, function(result) {
                            if(result.status == true){
                                var base64Str = result.ercode;
                                return "<img src='"+"data:image/png;base64,"+base64Str+"'/>"
                            }
                        });
                    }},
                    {field: 'title', title: '主题', width:200},
                    {field: 'status', title: '状态', width:100, templet: function (d) {
                            if(d.status == 1){
                                return '<span>审核通过</span>';
                            } else if(d.status == 0) {
                                return '<span>待审</span>';
                            } else {
                                return '<span>打回</span>';
                            }
                        }},
                    {field: 'sTime', title: '开始时间', width:160, templet: function (d) {
                            return moment(d.sTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {field: 'eTime', title: '结束时间', width:160, templet: function (d) {
                            return moment(d.eTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {field: 'place', title: '活动地点', width:300},
                    {field: 'content', title: '活动内容', width:300},
                    {field: 'cTime', title: '创建时间', width:160, templet: function (d) {
                            return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {fixed: 'right',width:MyController.toolbarWidth, align:'center', toolbar: '#barDemo'}
                ]]
            });
		},

        add: function() {
            var index = layer.open({
                type: 2,
                title: "发布日程",
                area: ['900px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: webName + '/views/schedule/schedule-add.html',
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

        modify: function(rowdata) {
            var url = request.composeUrl(webName + '/views/schedule/schedule-update.html?scheduleId='+rowdata.id);

            layer.open({
                type: 2,
                title: "修改日程",
                area: ['900px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {

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

                request.request(scheduleApi.getUrl('deleteSchedule'), {
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

        recommend: function(rowdata) {
            rowdata.content='';
			var url = request.composeUrl(webName + '/views/schedule/schedule-recc.html', rowdata);
			var index = layer.open({
				type: 2,
				title: "日程推荐",
                area: ['800px', '450px'],
                offset: '5%',
				scrollbar: false,
				content: url,
				success: function(ly, index) {
					// layer.iframeAuto(index);
				}
			});
		},

        review: function(rowdata) {
            rowdata.content='';
            var url = request.composeUrl(webName + '/views/schedule/schedule-review.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "日程审核",
                area: ['500px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

        viewDetail: function(rowdata) {
            var scheduleId = rowdata.id;//活动ID
            var url = request.composeUrl(webName + '/views/schedule/schedule-user-list.html?scheduleId='+scheduleId);
            var index = layer.open({
                type: 2,
                title: "报名列表",
                area: ['800px', '450px'],
                offset: '5%',
                scrollbar: true,
                content: url,
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
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
                } else if(obj.event === 'row-reco'){//推荐
                    MyController.recommend(data);
                } else if(obj.event === 'row-review'){//审核
                    MyController.review(data);
                } else if(obj.event === 'row-view-detail'){//查看报名数据
                    MyController.viewDetail(data);
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