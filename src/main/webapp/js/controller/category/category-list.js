var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'layer',
	'request',
	'category-api',
	'tree-table',
	'toast',
	'authority',
	'btns',
	'key-bind',
    'form',
    'form-util'


];
//这里注册没有初始化注册过的 模块路径，如果是modules下有子集 的模块需要在这里注册
registeModule(window, requireModules, {
	'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
	layer,
	ajax,
    categoryApi,
	treeTable,
	toast,
	authority,
	btns,
	keyBind,
    form,
    formUtil
) {
    var $ = layui.jquery;
    var f = layui.form;
    var formData;

    var controller = {
		init: function() {
            var navId = ajax.getFixUrlParams("navId");

            var totalBtns = authority.getNavBtns(navId);
            var btnObjs = btns.getBtns(totalBtns);
            controller.pageBtns = btns.getPageBtns(btnObjs);
            controller.switchPageBtns = btns.getSwitchPageBtns(btnObjs);
            $('#page-btns').html(btns.renderBtns(controller.pageBtns)+btns.renderSwitchBtns(controller.switchPageBtns));

            controller.bindEvent();
            var setting = {
                data: {
                    key: {
                        title:"t"
                    },
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    beforeClick: beforeClick,
                    onClick: onClick
                }
            };

            $(document).ready(function(){
                ajax.request(categoryApi.getUrl('categoryTreeList'), null, function(result) {
                    var zNodes = result.data;
                    var zTree = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                    zTree.expandAll(true);
                });
            });

            function beforeClick(treeId, treeNode, clickFlag) {
                return (treeNode.click != false);
            }

            function onClick(event, treeId, treeNode, clickFlag) {
                var parentTId = treeNode.parentTId;
                var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
                var parentNode = treeObj.getNodeByTId(parentTId);

                var selectId = treeNode.id;
                var selectName = treeNode.name;
                var type = treeNode.type;

                if(parentNode==null){
                    var parentId = 0;
                    var parentName = '根节点';
                } else {
                    var parentId = parentNode.id;
                    var parentName = parentNode.name;
                }


                formData = {
                    "id": selectId,
                    "name": selectName,
                    "pid": parentId,
                    "pName": parentName,
                    "type": type
                }

                formUtil.renderData($('#category-update-form'),formData);
            }


        },

        addLevel: function () {
		    if(formData == undefined){
                toast.success('请先选择节点');
		        return;
            }
            formData.id = '';
            formData.name = '';
            var url = ajax.composeUrl(webName + '/views/category/category-add.html', formData);
            var index = layer.open({
                type: 2,
                title: "添加同级节点",
                area: ['600px', '400px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
                }
            });
        },

        addChild: function () {
            if(formData == undefined){
                toast.success('请先选择节点');
                return;
            }

            formData.pid = formData.id;
            formData.pName = formData.name;
            formData.id = '';
            formData.name = '';

            var url = ajax.composeUrl(webName + '/views/category/category-add.html', formData);
            var index = layer.open({
                type: 2,
                title: "添加子节点",
                area: ['600px', '400px'],
                offset: '5%',
                scrollbar: false,
                content: url,
                success: function(ly, index) {
                    layer.iframeAuto(index);
                }
            });
        },

        delete: function() {
            layer.confirm('确认删除数据?', {
                icon: 3,
                title: '提示',
                closeBtn: 0
            }, function(index) {
                layer.load(0, {
                    shade: 0.5
                });
                layer.close(index);
                ajax.request(categoryApi.getUrl('deleteCategory'), {
                    id: $("#id").val()
                }, function() {
                    layer.closeAll('loading');
                    toast.success('成功删除！');
                    controller.refresh();
                },true,function(){
                    layer.closeAll('loading');
                });
            });
        },

        bindEvent: function() {
            //点击添加同级节点
            $('body').on('click', '.add_level', controller.addLevel);

            //点击添加子节点
            $('body').on('click', '.add_child', controller.addChild);

            $(document).on('click','#delete',function(){
                controller.delete();
            });
        },

        refresh: function() {
            window.location.reload();
        }
	};

	controller.init();

    window.tree = {
        refresh: controller.refresh
    }

    f.on('submit(category-update-form)', function(data) {
        ajax.request(categoryApi.getUrl('updateCategory'), data.field, function() {
            toast.success('修改成功');
            controller.refresh();
        });
        return false;
    });
});
