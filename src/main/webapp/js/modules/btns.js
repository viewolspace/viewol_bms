/**
 * 用来管理页面中的按钮（包括列表中的）
 */

layui.define(function(exports) {

    var baseBtnClass = 'layui-btn ';
    var btnConfig = {
        'edit': {
            className: baseBtnClass + 'layui-btn-warm edit',
            icon: '&#xe642;',
            name: '编辑'
        },
        'delete': {
            className: baseBtnClass + 'layui-btn-danger delete',
            icon: '&#xe640;',
            name: '删除'
        },
        'add': {
            className: baseBtnClass + 'add',
            icon: '&#xe654;',
            name: '添加'
        },
        'add_level': {
            className: baseBtnClass + 'add_level',
            icon: '&#xe654;',
            name: '添加同级节点'
        },
        'add_child': {
            className: baseBtnClass + 'add_child',
            icon: '&#xe654;',
            name: '添加子节点'
        },
        'refresh': {
            className: baseBtnClass + 'layui-btn-normal refresh',
            icon: '&#x1002;',
            name: '刷新'
        },
        'export': {
            className: baseBtnClass + 'layui-btn-normal export',
            icon: '&#xe61e;',
            name: '导出'
        },
        'copy': {
            className: baseBtnClass + 'layui-btn-normal copy',
            icon: '&#xe630;',
            name: '复制'
        },
        'audit': {
            className: baseBtnClass + 'layui-btn audit',
            icon: '&#xe605;',
            name: '提审'
        },
        'row-edit': {
            className: baseBtnClass + 'layui-btn-small layui-btn-warm row-edit',
            icon: '&#xe642;',
            name: '编辑'
        },
        'row-view': {
            className: baseBtnClass + 'layui-btn-small layui-btn-normal row-view',
            icon: '&#xe615;',
            name: '查看'
        },
        'row-config': {
            className: baseBtnClass + 'layui-btn-small layui-btn-primary row-config',
            icon: '&#xe620;',
            name: '配置'
        },
        'row-reset-pwd': {
            className: baseBtnClass + 'layui-btn-small layui-btn-primary row-reset-pwd',
            icon: '&#x1002;',
            name: '重置密码'
        },
        'row-switch': {
            className: 'row-switch',
            prop: 'enable', //对应提交时候的name值
            name: '启用|禁用' //对应显示的字
        },
        'row-audit': {
            className: baseBtnClass + 'layui-btn-small layui-btn-danger row-audit',
            icon: '&#xe605;',
            name: '审核'
        },
        'row-desc': {
            className: baseBtnClass + 'layui-btn-small layui-btn-normal row-desc',
            icon: '&#xe60b;',
            name: '描述'
        },
        'row-delete': {
            className: baseBtnClass + 'layui-btn-small layui-btn-danger row-delete',
            icon: '&#x1006;',
            name: '删除'
        },
        'row-stop': {
			className: baseBtnClass + 'layui-btn-small layui-btn-danger row-stop',
            icon: '&#x1007;',
            name: '停用'
		},
        'row-modify': {
			className: baseBtnClass + 'layui-btn-small layui-btn-warm row-modify',
            icon: '&#xe642;',
            name: '修改'
		},
        'row-edit-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '编辑',
            event: 'row-edit'
        },
        'row-delete-layui': {
            className: baseBtnClass + 'layui-btn-danger layui-btn-xs',
            icon: '&#x1006;',
            name: '删除',
            event: 'row-delete'
        },
        'row-view-layui': {
            className: baseBtnClass + 'layui-btn-primary layui-btn-xs',
            icon: '&#xe615;',
            name: '查看',
            event: 'row-view'
        },
        'row-reco-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '推荐',
            event: 'row-reco'
        },
        'row-review-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe605;',
            name: '审核',
            event: 'row-review'
        },
        'row-cancel-reco-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '取消推荐',
            event: 'row-cancel-reco'
        },
        'row-freeze-layui':{
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe615;',
            name: '冻结',
            event: 'row-freeze'
        },
        'row-unfreeze-layui':{
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe615;',
            name: '恢复',
            event: 'row-unfreeze'
        },
        'row-home-reco-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '首页推荐',
            event: 'row-home-reco'
        },
        'row-same-reco-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '同类推荐',
            event: 'row-same-reco'
        },
        'row-cancel-home-reco-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '取消首页推荐',
            event: 'row-cancel-home-reco'
        },
        'row-cancel-same-reco-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '取消同类推荐',
            event: 'row-cancel-same-reco'
        },
        'row-up-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '上架',
            event: 'row-up'
        },
        'row-down-layui': {
            className: baseBtnClass + 'layui-btn-xs',
            icon: '&#xe642;',
            name: '下架',
            event: 'row-down'
        },
    };

    var btns = {
        bindEvent: function() {
            /*	$('body').on('click', '.add,edit', function() {
            		console.log('点击了按钮');
            		var href = 'views/user/test/test.html'
            		console.log($(window.top.document).find('.layui-nav-tree a[href-url="' + href + '"]')[0]);

            		$(window.top.document).find('.layui-nav-tree a[href-url="' + href + '"]').parents('dl').prev('a').trigger('click');
            		$(window.top.document).find('.layui-nav-tree a[href-url="' + href + '"]').trigger('click');
            		//				$('a[href-url="'+href+'"]').trigger('click');
            	});*/
        },

        /**
         * 根据接口返回 的字符串数组，丰富按钮对象并返回
         * @param {Object} btnNameArr 类似于['edit','row-edit']
         */
        getBtns: function(btnNameArr) {
            var result = [];
            if(btnNameArr){
            	$.each(btnNameArr, function(index, item) {
                    var btn = btnConfig[item.btnKey];

                    if (btn) {
                        result.push($.extend(true, item, btn));
                    }
                });
            }
            return result;
        },

        /**
         * 传入总的btns对象，筛选页面按钮,不包含列表按钮
         * @param {Object} btns
         */
        getPageBtns: function(btnObjs) {
            return $.grep(btnObjs, function(item, index) {
                return !btns.isRowBtn(item.btnKey) && !btns.isSwitchPageBtn(item.btnKey);
            });
        },
        getSwitchPageBtns: function(btnObjs) {
            return $.grep(btnObjs, function(item, index) {
                return btns.isSwitchPageBtn(item.btnKey);
            });
        },
        /**
         * 筛选表格中显示的按钮
         * @param {Object} btns
         */
        getRowBtns: function(btnObjs) {
            return $.grep(btnObjs, function(item, index) {
                return btns.isRowBtn(item.btnKey);
            });
        },

        /**
         * 筛选表格普通按钮
         * @param {Object} btns
         */
        getIconBtns: function(btnObjs) {
            return $.grep(btnObjs, function(item, index) {
                return !btns.isSwitchBtn(item.btnKey); //现在的逻辑是只有两种按钮，一个是开关一个是普通按钮，所以筛选普通按钮是非开关按钮
            });
        },

        /**
         * 筛选开关按钮
         * @param {Object} btnObjs
         */
        getSwitchBtns: function(btnObjs) {
            return $.grep(btnObjs, function(item, index) {
                return btns.isSwitchBtn(item.btnKey);
            });
        },

        isRowBtn: function(btnKey) {
            return /^row-/.test(btnKey);
        },

        isSwitchPageBtn: function(btnKey) {
            return /^switch/.test(btnKey);
        },

        isSwitchBtn: function(btnKey) {
            return btnKey.indexOf('-switch') > 0;
        },

        /**
         * 只渲染普通按钮
         * @param {Object} btns
         */
        renderBtns: function(btnObjs) {
            var results = '';
            $.each(btnObjs, function(index, item) {
                var btn = btns.renderBtn(item.className, item.name, item.icon);
                if (btn) {
                    results += btn;
                }
            });
            if(results.length==0){
                $("#page-btns").attr("style","display:none;");//隐藏div
            }
            return results;
        },

        renderLayuiTableBtns: function(btnObjs, $btnToolBar) {
            var results = '';
            $.each(btnObjs, function(index, item) {
                var btn = btns.renderLayuiBtn(item.className, item.name, item.icon, item.event);
                if (btn) {
                    results += btn;
                }
            });

            $btnToolBar.html(results);
        },

        renderSwitchBtns: function(btnObjs) {
            var results = '';
            $.each(btnObjs, function(index, item) {
                var btn = btns.renderSwitch(item.className, item.name, item.checked);
                if (btn) {
                    results += btn;
                }
            });
            return results;
        },

        /**
         *
         * @param {Object} name 指定的name值
         * @param {Object} text 开关显示名字  启用|停用
         * @param {Boolean} isChecked 默认开还是关
         * @param {Boolean} isDisable 是否禁用
         */
        renderSwitch: function(name, text, isChecked, isDisable) {
            var checkStr = '';
            if (isChecked) {
                checkStr = 'checked';
            }
            var disabled = '';
            if (isDisable) {
                disabled = 'disabled'
            }
            return '<div class="layui-form" style="display:inline-block;margin-right:10px;">' +
                '<input type="checkbox" ' + checkStr + ' ' + disabled + ' lay-filter="' + name + '" name="' + name + '" lay-skin="switch" lay-text="' + text + '">' +
                '</div>';
        },

        renderBtn: function(className, btnName, btnIcon, isDisable, otherOpt) {
            if (otherOpt) {
                btnName = otherOpt.btnName || btnName; //自定义名字
                className = otherOpt.className || className; //自定义样式
            }

            if (isDisable) {
                className += ' layui-btn-disabled';
                return '<button class="' + className + '" disabled="disabled"><i class="layui-icon">' + btnIcon + '</i> ' + btnName + '</button>';
            } else {
                return '<button class="' + className + '" ><i class="layui-icon">' + btnIcon + '</i> ' + btnName + '</button>';
            }

        },

        renderLayuiBtn: function(className, btnName, btnIcon, event, isDisable, otherOpt) {
            if (otherOpt) {
                btnName = otherOpt.btnName || btnName; //自定义名字
                className = otherOpt.className || className; //自定义样式
            }

            if(event=='row-home-reco'){
                var freezeBtns = "";
                freezeBtns+='{{#  if(d.isRecommend == 0){ }}';
                freezeBtns+='<a class="' + className + '" lay-event="'+event+'">' + btnName + '</a>';
                freezeBtns+='{{#  } }}';
                return freezeBtns;
            }else if(event == 'row-cancel-home-reco'){
                var freezeBtns = "";
                freezeBtns+='{{#  if(d.isRecommend == 1){ }}';
                freezeBtns+='<a class="' + className + '" lay-event="'+event+'">' + btnName + '</a>';
                freezeBtns+='{{#  } }}';
                return freezeBtns;
            }

            if(event=='row-up'){
                var freezeBtns = "";
                freezeBtns+='{{#  if(d.status == 1){ }}';
                freezeBtns+='<a class="' + className + '" lay-event="'+event+'">' + btnName + '</a>';
                freezeBtns+='{{#  } }}';
                return freezeBtns;
            }else if(event == 'row-down'){
                var freezeBtns = "";
                freezeBtns+='{{#  if(d.status == 0){ }}';
                freezeBtns+='<a class="' + className + '" lay-event="'+event+'">' + btnName + '</a>';
                freezeBtns+='{{#  } }}';
                return freezeBtns;
            }

            if (isDisable) {
                className += ' layui-btn-disabled';
                return '<a class="' + className + '" lay-event="'+event+'">' + btnName + '</a>';
            } else {
                return '<a class="' + className + '" lay-event="'+event+'">' + btnName + '</a>';
            }

        }

    }

    exports('btns', btns);

});
