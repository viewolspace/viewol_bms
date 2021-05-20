var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: true
});

var requireModules = [
    'element',
    'form',
    'layer',
    'request',
    'form-util',
    'performance-api',
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
layui.use(requireModules, function (
    element,
    form,
    layer,
    request,
    formUtil,
    performanceApi,
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
        init: function () {
            var navId = request.getFixUrlParams("navId");

            var totalBtns = authority.getNavBtns(navId);
            var btnObjs = btns.getBtns(totalBtns);
            MyController.pageBtns = btns.getPageBtns(btnObjs);
            MyController.switchPageBtns = btns.getSwitchPageBtns(btnObjs);

            MyController.rowBtns = btns.getRowBtns(btnObjs);
            MyController.rowSwitchBtns = btns.getSwitchBtns(MyController.rowBtns);
            MyController.rowIconBtns = btns.getIconBtns(MyController.rowBtns);

            // $('#page-btns').html(btns.renderBtns(MyController.pageBtns) + btns.renderSwitchBtns(MyController.switchPageBtns));
            btns.renderLayuiTableBtns(MyController.rowIconBtns, $("#barDemo"));

            mainTable = MyController.renderTable();
            MyController.bindEvent();
        },
        getQueryCondition: function() {
            var condition = formUtil.composeData($("#condition"));
            return condition;
        },
        renderTable: function () {
            return $table.render({
                elem: '#performance-list'
                , height: 'full-100'
                , url: performanceApi.getUrl('performanceList').url
                , method: 'post'
                , page: true //开启分页
                , limits: [10, 50, 100, 200]
                , cols: [[
                    {type: 'numbers'},
                    {field: 'companyId', title: '展商ID', width: 100},
                    {field: 'companyName', title: '展商名称', width: 200},
                    {field: 'phone', title: '手机号', width: 200},
                    {field: 'email', title: '邮箱', width: 200},
                    {field: 'openId', title: '微信号', width: 200},
                    {field: 'performanceCategory', title: '展演类别', width: 200},
                    {field: 'performanceProduct', title: '展演产品', width: 200},
                    {field: 'feature', title: '产品优势及特性', width: 200},
                    {field: 'area', title: '所需面积（平米）', width: 200},
                    {field: 'needHelp', title: '需辅助服务及功能需求', width: 200},
                    {field: 'performanceTime', title: '展演时段', width: 200},
                    {field: 'adPositon', title: '广告位置', width: 200},
                    {field: 'adMethod', title: '广告方式', width: 200},
                    {
                        field: 'updateTime', title: '修改时间', width: 160, templet: function (d) {
                            return moment(d.updateTime).format("YYYY-MM-DD HH:mm:ss");
                        }
                    },
                    {
                        field: 'createTime', title: '创建时间', width: 160, templet: function (d) {
                            return moment(d.createTime).format("YYYY-MM-DD HH:mm:ss");
                        }
                    },
                    {fixed: 'right', width: 100, align: 'center', toolbar: '#barDemo'}
                ]]
            });
        },

        add: function () {
            var index = layer.open({
                type: 2,
                title: "展演区展演申请录入",
                area: ['1000px', '600px'],
                offset: '5%',
                scrollbar: false,
                content: webName + '/views/performance/performance-add.html',
                success: function (ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

        modify: function (rowdata) {
            var url = request.composeUrl(webName + '/views/performance/performance-update.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "展演区展演申请修改",
                area: ['1000px', '600px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function (ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

        view: function(rowdata) {
            var url = request.composeUrl(webName + '/views/performance/performance-update.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "展演区展演申请详情",
                area: ['1000px', '600px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
                }
            });
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

                request.request(performanceApi.getUrl('deletePerformance'), {
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

        refresh: function () {
            mainTable.reload();
        },

        bindEvent: function () {
            $table.on('tool(test)', function (obj) {
                var data = obj.data;
                if (obj.event === 'row-view') {//编辑
                    MyController.view(data);
                } else if (obj.event === 'row-delete') {//删除
                    MyController.delete(data);
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