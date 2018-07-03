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
	'valid-login'


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
	keyBind
) {

	var controller = {

		init: function() {
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

            var zNodes =[
                { id:1, pId:0, name:"父节点1", open:true},
                { id:11, pId:1, name:"父节点11"},
                { id:111, pId:11, name:"叶子节点111"},
                { id:112, pId:11, name:"叶子节点112"},
                { id:113, pId:11, name:"叶子节点113"},
                { id:114, pId:11, name:"叶子节点114"},
                { id:12, pId:1, name:"父节点12"},
                { id:121, pId:12, name:"叶子节点121"},
                { id:122, pId:12, name:"叶子节点122"},
                { id:123, pId:12, name:"叶子节点123"},
                { id:124, pId:12, name:"叶子节点124"},
                { id:13, pId:1, name:"父节点13", isParent:true},
                { id:2, pId:0, name:"父节点2"},
                { id:21, pId:2, name:"父节点21", open:true},
                { id:211, pId:21, name:"叶子节点211"},
                { id:212, pId:21, name:"叶子节点212"},
                { id:213, pId:21, name:"叶子节点213"},
                { id:214, pId:21, name:"叶子节点214"},
                { id:22, pId:2, name:"父节点22"},
                { id:221, pId:22, name:"叶子节点221"},
                { id:222, pId:22, name:"叶子节点222"},
                { id:223, pId:22, name:"叶子节点223"},
                { id:224, pId:22, name:"叶子节点224"},
                { id:23, pId:2, name:"父节点23"},
                { id:231, pId:23, name:"叶子节点231"},
                { id:232, pId:23, name:"叶子节点232"},
                { id:233, pId:23, name:"叶子节点233"},
                { id:234, pId:23, name:"叶子节点234"},
                { id:3, pId:0, name:"父节点3", isParent:true}
            ];

            $(document).ready(function(){
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
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
                var parentId = parentNode.id;
                var parentName = parentNode.name;

                var formData = {
                    "id": selectId,
                    "name": selectName,
                    "pid": parentId,
                    "pName": parentName
                }

                alert(JSON.stringify(formData));
            }
        }


	};

	controller.init();
});
