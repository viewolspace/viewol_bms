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
layui.use(requireModules, function (
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

            //计算行按钮toolbar宽度
            if (MyController.rowIconBtns) {
                MyController.toolbarWidth = 40;
                $.each(MyController.rowIconBtns, function (index, item) {
                    for (var i = 0; i < item.name.length; i++) {
                        MyController.toolbarWidth += 18;//一个汉字20px
                    }
                });
            }

            request.request(
                selectApi.getUrl('listDataDic'), {
                    parentId: '0002'
                }, function (result) {
                    formUtil.renderSelects('#categoryId', result.data, true);
                    form.render('select');
                },
                false
            );

            mainTable = MyController.renderTable();
            MyController.bindEvent();
        },

        getQueryCondition: function () {
            var condition = formUtil.composeData($("#condition"));
            return condition;
        },

        renderTable: function () {
            return $table.render({
                elem: '#exhibition-list'
                , height: 'full-100'
                , url: exhibitionApi.getUrl('productIdeaList').url
                , method: 'post'
                , page: true //开启分页
                , limits: [10, 50, 100, 200]
                , cols: [[ //表头
                    {field: 'productId', title: '展品ID', width: 100},
                    {field: 'productName', title: '展品名称', width: 100},
                    // {field: 'companyId', title: '展商ID', width: 100},
                    {field: 'companyName', title: '展商名称', width: 100},
                    {field: 'companyPlace', title: '展位号', width: 100},
                    {field: 'liaisonMan', title: '联系人', width: 100},
                    {field: 'phone', title: '手机', width: 100},
                    {field: 'landLine', title: '座机', width: 100},
                    {field: 'website', title: '网站', width: 100},
                    {field: 'email', title: '邮箱', width: 100},
                    {field: 'categoryId', title: '产品类别', width: 100},
                    {
                        field: 'logo', title: '产品商标', width: 150, templet: function (d) {
                            return "<a href='" + d.logo + "' target='_blank'><img src='" + d.logo + "' /></a>";
                        }
                    },
                    {field: 'des', title: '产品概况', width: 100},
                    {field: 'quota', title: '关键技术指标', width: 100},
                    {field: 'ideaPoint', title: '产品创新点', width: 100},
                    {field: 'extend', title: '国内外市场推广情况', width: 100},
                    {field: 'productPic', title: '展品图片', width: 100},
                    {field: 'comLogo', title: '公司logo', width: 100},
                    // {field: 'ext', title: '相关证书(证书打包上传)', width: 100},
                    {field: 'model', title: '展品ID', width: 100},
                    {
                        field: 'status', title: '状态', width: 100, templet: function (d) {
                            if (d.status == 0) {
                                return '<span>等待评选</span>';
                            } else if (d.status == 1) {
                                return '<span>评选通过</span>';
                            } else if (d.status == -1) {
                                return '<span>评选失败</span>';
                            } else {
                                return '<span>未知</span>';
                            }
                        }
                    },
                    {
                        field: 'mTime', title: '修改时间', width: 160, templet: function (d) {
                            return moment(d.mTime).format("YYYY-MM-DD HH:mm:ss");
                        }
                    },
                    {
                        field: 'cTime', title: '录入时间', width: 160, templet: function (d) {
                            return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                        }
                    },

                    {fixed: 'right', width: 150, align: 'center', toolbar: '#barDemo'}
                ]]
            });
        },

        view: function (rowdata) {
            var url = request.composeUrl(webName + '/views/exhibition/exhibition-idea-view.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "查看创新产品",
                area: ['900px', '450px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function (ly, index) {
                    // layer.iframeAuto(index);
                }
            });
        },

        //产品评选审核
        review: function (rowdata) {
            var url = request.composeUrl(webName + '/views/exhibition/exhibition-idea-update.html', rowdata);
            var index = layer.open({
                type: 2,
                title: "产品评选",
                area: ['400px', '200px'],
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
                if (obj.event === 'row-view') {//查看评选产品详情
                    MyController.view(data);
                } else if (obj.event === 'row-review') {//评选
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
        }
    };

    window.list = {
        refresh: MyController.refresh
    };

    MyController.init();

});