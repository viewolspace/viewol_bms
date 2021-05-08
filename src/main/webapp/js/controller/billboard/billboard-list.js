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
    'billboard-api',
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
layui.use(requireModules, function (
    element,
    form,
    layer,
    request,
    formUtil,
    billboardApi,
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
        init: function () {
            var navId = request.getFixUrlParams("navId");

            var totalBtns = authority.getNavBtns(navId);
            var btnObjs = btns.getBtns(totalBtns);
            MyController.pageBtns = btns.getPageBtns(btnObjs);
            MyController.switchPageBtns = btns.getSwitchPageBtns(btnObjs);

            MyController.rowBtns = btns.getRowBtns(btnObjs);
            MyController.rowSwitchBtns = btns.getSwitchBtns(MyController.rowBtns);
            MyController.rowIconBtns = btns.getIconBtns(MyController.rowBtns);


            mainTable = MyController.renderTable();
            MyController.bindEvent();
        },
        renderTable: function () {
            return $table.render({
                elem: '#billboard-list'
                , height: 'full-100'
                , url: billboardApi.getUrl('billboardList').url
                , method: 'post'
                , page: true //开启分页
                , limits: [10, 50, 100, 200]
                , cols: [[ //表头
                    {type: 'numbers'},
                    {field: 'companyId', title: '展商ID', width: 100},
                    {field: 'companyName', title: '展商名称', width: 200},
                    {field: 'showRoom', title: '展厅', width: 200},
                    {field: 'itemName', title: '项目名称', width: 200},
                    {field: 'num', title: '序号', width: 100},
                    {field: 'size', title: '尺寸', width: 200},
                    {field: 'price', title: '价格', width: 200},
                    {field: 'phone', title: '联系电话', width: 150},
                    {field: 'userName', title: '联系人', width: 100},
                    {
                        field: 'cTime', title: '申请时间', width: 160, templet: function (d) {
                            return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                        }
                    },
                ]]
            });
        },

        refresh: function () {
            mainTable.reload();
        },

        bindEvent: function () {
            $table.on('tool(test)', function (obj) {

            });

            //点击查询按钮
            $('#search-btn').on('click', function () {
                mainTable.reload();
            });
        }
    };

    window.list = {
        refresh: MyController.refresh
    }

    MyController.init();

});