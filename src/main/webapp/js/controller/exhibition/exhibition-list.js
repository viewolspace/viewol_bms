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
    exhibitionApi,
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

            //计算行按钮toolbar宽度
            if(MyController.rowIconBtns){
                MyController.toolbarWidth = 40;
                $.each(MyController.rowIconBtns, function(index, item) {
                    for(var i=0 ;i<item.name.length;i++){
                        MyController.toolbarWidth += 18;//一个汉字20px
                    }
                });
            }

            request.request(
                selectApi.getUrl('listDataDic'),{
                    parentId: '0002'
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
                elem: '#exhibition-list'
                ,height: 'full-100'
                ,url: exhibitionApi.getUrl('exhibitionList').url
                ,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[ //表头
                    {field: 'id', title: '展品ID', width:100},
                    {field: 'name', title: '产品名称', width:100},
                    {field: 'status', title: '状态', width:100, templet: function (d) {
                        if(d.status == 1){
                            return '<span>下架</span>';
                        } else {
                            return '<span>上架</span>';
                        }
                    }},
                    {field: 'isRecommend', title: '是否推荐', width:100, templet: function (d) {
                            if(d.isRecommend == 1){
                                return '<span>推荐</span>';
                            } else {
                                return '<span>非推荐</span>';
                            }
                        }},
                    {field: 'recommendNum', title: '推荐顺序', width:100},
                    {field: 'categoryId', title: '分类id', width:100},
                    {field: 'image', title: '产品图片', width:150, templet: function (d) {
                        return "<a href='"+d.image+"' target='_blank'><img src='"+d.image+"' /></a>";
                    }},
                    {field: 'pdfName', title: '说明书的名字', width:120},
                    {field: 'pdfUrl', title: '说明书下载地址', width:300},
                    {field: 'mTime', title: '修改时间', width:160, templet: function (d) {
                        return moment(d.mTime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {field: 'cTime', title: '录入时间', width:160, templet: function (d) {
                        return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                    }},
                    {fixed: 'right',width:300, align:'center', toolbar: '#barDemo'}
                ]]
            });
        },

        view: function(rowdata) {
            var url = request.composeUrl(webName + '/views/exhibition/exhibition-view.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "查看产品",
                area: ['900px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

        //首页推荐
        homeReco: function (rowdata) {
            var url = request.composeUrl(webName + '/views/exhibition/reco-home.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "推荐产品到首页",
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

        //同类推荐
        sameReco: function (rowdata) {
            var url = request.composeUrl(webName + '/views/exhibition/reco-same.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "推荐产品到同类",
                area: ['400px', '200px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
                }
            });
        },

        up: function(rowdata) {
            layer.confirm('确认上架该产品吗?', {
                icon: 3,
                title: '提示',
                closeBtn: 0
            }, function(index) {
                layer.load(0, {
                    shade: 0.5
                });
                layer.close(index);

                request.request(exhibitionApi.getUrl('upExhibition'), {
                    id: rowdata.id
                }, function() {
                    layer.closeAll('loading');
                    toast.success('上架成功！');
                    MyController.refresh();
                },true,function(){
                    layer.closeAll('loading');
                });
            });
        },

        down: function(rowdata) {
            layer.confirm('确认下架该产品吗?', {
                icon: 3,
                title: '提示',
                closeBtn: 0
            }, function(index) {
                layer.load(0, {
                    shade: 0.5
                });
                layer.close(index);

                request.request(exhibitionApi.getUrl('downExhibition'), {
                    id: rowdata.id
                }, function() {
                    layer.closeAll('loading');
                    toast.success('下架成功！');
                    MyController.refresh();
                },true,function(){
                    layer.closeAll('loading');
                });
            });
        },

        //产品置顶
        homeTop: function (rowdata) {
            var url = request.composeUrl(webName + '/views/exhibition/top-home.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "产品置顶",
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
                } else if(obj.event === 'row-home-reco'){//首页推荐
                    MyController.homeReco(data);
                } else if(obj.event === 'row-cancel-home-reco'){//取消首页推荐
                    MyController.cancelHomeReco(data);
                } else if(obj.event === 'row-same-reco'){//同类推荐
                    MyController.sameReco(data);
                } else if(obj.event === 'row-up'){//上架
                    MyController.up(data);
                } else if(obj.event === 'row-down'){//下架
                    MyController.down(data);
                } else if(obj.event === 'row-top'){//产品置顶
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
        }
    };

    window.list = {
        refresh: MyController.refresh
    }

    MyController.init();

});