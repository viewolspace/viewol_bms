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
    'info-api',
    'table-util',
    'btns',
    'authority',
    'toast',
    'table',
    'select-api',
    'laydate'
];

registeModule(window, requireModules, {
    'good-api': 'api/good-api'
});

//参数有顺序
layui.use(requireModules, function (
    element,
    form,
    layer,
    request,
    formUtil,
    infoApi,
    tableUtil,
    btns,
    authority,
    toast,
    table,
    selectApi,
    laydate
) {

    var $ = layui.jquery;
    var $table = table;
    var mainTable;

    //日期范围
    laydate.render({
        elem: '#test6',
        type: 'date',
        range: true,
        format: 'yyyy-MM-dd'
    });

    var MyController = {
        init: function () {
            var navId = request.getFixUrlParams("navId");

            var totalBtns = authority.getNavBtns(navId);
            var btnObjs = btns.getBtns(totalBtns);
            MyController.pageBtns = btns.getPageBtns(btnObjs);
            MyController.switchPageBtns = btns.getSwitchPageBtns(btnObjs);

            MyController.rowBtns = btns.getRowBtns(btnObjs);
            MyController.rowSwitchBtns = btns.getSwitchBtns(MyController.rowBtns);
            MyController.rowIconBtns = btns.getIconBtns(MyController.rowBtns);

            $('#page-btns').html(btns.renderBtns(MyController.pageBtns) + btns.renderSwitchBtns(MyController.switchPageBtns));
            btns.renderLayuiTableBtns(MyController.rowIconBtns, $("#barDemo"));

            mainTable = MyController.renderTable();
            MyController.bindEvent();
        },
        getQueryCondition: function () {
            var condition = formUtil.composeData($("#condition"));
            return condition;
        },
        renderTable: function () {
            return $table.render({
                elem: '#info-list'
                , height: 'full-100'
                , url: infoApi.getUrl('infoList').url
                , method: 'post'
                , page: true //开启分页
                , limits: [10, 50, 100, 200]
                , cols: [[ //表头
                    {type: 'numbers'},
                    {field: 'id', title: '资讯ID', width: 100},
                    {field: 'title', title: '标题', width: 200},
                    {
                        field: 'pubTime', title: '发布时间', width: 160, templet: function (d) {
                            return moment(d.pubTime).format("YYYY-MM-DD HH:mm:ss");
                        }
                    },
                    {
                        field: 'picUrl', title: '显示图片', width: 100, templet: function (d) {
                            return "<a href='" + d.picUrl + "' target='_blank'><img src='" + d.picUrl + "' /></a>";
                        }
                    },
                    {
                        field: 'status', title: '状态', width: 100, templet: function (d) {
                            if (d.status == 0) {
                                return '<span>待审</span>';
                            } else if (d.status == 1) {
                                return '<span>发布</span>';
                            } else {
                                return '<span>打回</span>';
                            }
                        }
                    },
                    {
                        field: 'classify', title: '展会', width: 100, templet: function (d) {
                            if (d.classify == 1) {
                                return '<span>安防展</span>';
                            } else if (d.classify == 2) {
                                return '<span>消防展</span>';
                            } else {
                                return '<span>其他</span>';
                            }
                        }
                    },
                    {field: 'companyId', title: '展商ID', width: 120},
                    {
                        field: 'createTime', title: '创建时间', width: 160, templet: function (d) {
                            return moment(d.createTime).format("YYYY-MM-DD HH:mm:ss");
                        }
                    },
                    {fixed: 'right', width: 200, align: 'center', toolbar: '#barDemo'}
                ]]
            });
        },

        add: function () {
            var card = 'card'; // 选项卡对象
            var title = "添加资讯"; // 导航栏text
            var src = webName + '/views/info/info-add.html'; // 导航栏跳转URL
            var id = new Date().getTime(); // ID

            parent.layui.element.tabAdd(card, {
                title: '<span>' + title + '</span>',
                content: '<iframe src="' + src + '" frameborder="0" ></iframe>',
                id: id
            });
            parent.layui.element.tabChange(card, id);
        },


        modify: function (rowdata) {
            var card = 'card'; // 选项卡对象
            var title = "修改资讯"; // 导航栏text
            var url = request.composeUrl(webName + '/views/info/info-update.html', rowdata);
            var id = new Date().getTime(); // ID

            parent.layui.element.tabAdd(card, {
                title: '<span>' + title + '</span>',
                content: '<iframe src="' + url + '" frameborder="0" ></iframe>',
                id: id
            });
            parent.layui.element.tabChange(card, id);
        },

        view: function (rowdata) {
            var card = 'card'; // 选项卡对象
            var title = "查看资讯"; // 导航栏text
            var url = request.composeUrl(webName + '/views/info/info-view.html', rowdata);
            var id = new Date().getTime();

            parent.layui.element.tabAdd(card, {
                title: '<span>' + title + '</span>',
                content: '<iframe src="' + url + '" frameborder="0" ></iframe>',
                id: id
            });
            parent.layui.element.tabChange(card, id);
        },

        delete: function (rowdata) {
            layer.confirm('确认删除数据?', {
                icon: 3,
                title: '提示',
                closeBtn: 0
            }, function (index) {
                layer.load(0, {
                    shade: 0.5
                });
                layer.close(index);

                request.request(infoApi.getUrl('deleteInfo'), {
                    id: rowdata.id
                }, function () {
                    layer.closeAll('loading');
                    toast.success('成功删除！');
                    MyController.refresh();
                }, true, function () {
                    layer.closeAll('loading');
                });
            });
        },

        //审核
        review: function (rowdata) {
            var url = request.composeUrl(webName + '/views/info/info-review.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "资讯审核",
                area: ['600px', '400px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function (ly, index) {
                    layer.iframeAuto(index);
                }
            });
        },

        refresh: function () {
            mainTable.reload();
        },

        bindEvent: function () {
            $table.on('tool(test)', function (obj) {
                var data = obj.data;
                if (obj.event === 'row-view') {//查看
                    MyController.view(data);
                } else if (obj.event === 'row-edit') {//编辑
                    MyController.modify(data);
                } else if (obj.event === 'row-delete') {//删除
                    MyController.delete(data);
                } else if (obj.event === 'row-review') {//审核
                    MyController.review(data);
                }

            });

            //点击查询按钮
            $('#search-btn').on('click', function () {
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