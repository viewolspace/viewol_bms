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

        renderTable: function() {
            return $table.render({
                elem: '#exhibition-list'
                ,height: 'full-100'
                ,url: exhibitionApi.getUrl('exhibitionList').url
                ,method: 'post'
                ,page: true //开启分页
                ,limits:[10,50,100,200]
                ,cols: [[ //表头
                    {type:'numbers'},
                    {field: 'id', title: '展品ID', width:100},
                    {field: 'name', title: '产品名称', width:150},
                    {field: 'status', title: '状态', width:100, templet: function (d) {
                            if(d.status == 1){
                                return '<span>下架</span>';
                            } else {
                                return '<span>上架</span>';
                            }
                        }},
                    {field: 'isRecommend', title: '推荐到首页', width:100, templet: function (d) {
                            if(d.isRecommend == 1){
                                return '<span>是</span>';
                            } else {
                                return '<span>否</span>';
                            }
                        }},
                    {field: 'isSameRecommend', title: '推荐到同类', width:100, templet: function (d) {
                            if(d.isSameRecommend == 1){
                                return '<span>是</span>';
                            } else {
                                return '<span>否</span>';
                            }
                        }},
                    {field: 'categoryId', title: '分类id', width:100},
                    {field: 'image', title: '产品图片', width:150},
                    {field: 'content', title: '产品介绍', width:120},
                    {field: 'pdfUrl', title: '产品说明书', width:120},
                    {field: 'pdfName', title: '说明书的名字', width:120},
                    {field: 'mTime', title: '修改时间', width:160, templet: function (d) {
                            return moment(d.mTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {field: 'cTime', title: '录入时间', width:160, templet: function (d) {
                            return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {fixed: 'right',width:260, align:'center', toolbar: '#barDemo'}
                ]]
            });
        },

        add: function() {
            var index = layer.open({
                type: 2,
                title: "添加展品",
                area: ['800px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: webName + '/views/exhibition/exhibition-add.html',
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

        modify: function(rowdata) {
            var url = request.composeUrl(webName + '/views/exhibition/exhibition-update.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "修改展品",
                area: ['800px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

        view: function(rowdata) {
            var url = request.composeUrl(webName + '/views/exhibition/exhibition-view.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "查看展品",
                area: '60%',
                offset: '10%',
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

        refresh: function() {
            mainTable.reload();
        },

        bindEvent: function() {
            $table.on('tool(test)', function(obj){
                var data = obj.data;
                alert(obj.event);
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