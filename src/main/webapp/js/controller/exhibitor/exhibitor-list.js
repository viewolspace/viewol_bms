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
	'same-reco-api',
	'table-util',
	'btns',
	'authority',
	'toast',
    'table',
    'select-api'

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
    sameRecoApi,
	tableUtil,
	btns,
	authority,
	toast,
    table,
    selectApi
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

            request.request(
                selectApi.getUrl('listDataDic'),{
                    parentId: '0001'
                }, function(result) {
                    formUtil.renderSelects('#categoryId', result.data, true);
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
                elem: '#exhibitor-list'
                ,height: 'full-100'
                ,url: exhibitorApi.getUrl('exhibitorList').url
				,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'id', title: '展商ID', width:100},
                    {field: 'name', title: '展商名称', width:200},
                    {field: 'shortName', title: '展商简称', width:100},
                    {field: 'logo', title: '展商logo', width:100, templet: function (d) {
                            return "<a href='"+d.logo+"' target='_blank'><img src='"+d.logo+"' /></a>";
                        }},
                    {field: 'banner', title: '展商形象图片', width:100, templet: function (d) {
                            return "<a href='"+d.banner+"' target='_blank'><img src='"+d.banner+"' /></a>";
                        }},
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
                    {field: 'topNum', title: '置顶顺序', width:120},
                    {field: 'award', title: '是否获奖', width:120 ,templet: function (d) {
                            if(d.award == 1){
                                return '<span>获奖</span>';
                            } else {
                                return '<span>未获奖</span>';
                            }
                        }},
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
            var url = request.composeUrl(webName + '/views/exhibitor/exhibitor-view.html', rowdata);
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

		//首页推荐
        homeReco: function (rowdata) {
            var url = request.composeUrl(webName + '/views/exhibitor/reco-home.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "推荐展商到首页",
                area: ['400px', '200px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
                }
            });
        },

		//同类推荐
        sameReco: function (rowdata) {
            var url = request.composeUrl(webName + '/views/exhibitor/reco-same.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "推荐展商到同类",
                area: ['400px', '200px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
                }
            });
        },

        //取消首页推荐
        cancelHomeReco: function (rowdata) {
            layer.confirm('确认取消首页推荐吗?', {
                icon: 3,
                title: '提示',
                closeBtn: 0
            }, function(index) {
                layer.load(0, {
                    shade: 0.5
                });
                layer.close(index);

                request.request(exhibitorApi.getUrl('delRecommentHome'), {
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

        //取消同类推荐
        cancelSameReco: function () {

        },

        //置顶
        homeTop: function (rowdata) {
            var url = request.composeUrl(webName + '/views/exhibitor/top-home.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "展商置顶",
                area: ['400px', '200px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
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
                } else if(obj.event === 'row-home-reco'){//首页推荐
                    MyController.homeReco(data);
                } else if(obj.event === 'row-same-reco'){//同类推荐
                    MyController.sameReco(data);
                } else if(obj.event === 'row-cancel-home-reco'){//取消首页推荐
                    MyController.cancelHomeReco(data);
                } else if(obj.event === 'row-cancel-same-reco'){//取消同类推荐
                    MyController.cancelSameReco(data);
                } else if(obj.event === 'row-top'){//置顶展商
                    MyController.homeTop(data);
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