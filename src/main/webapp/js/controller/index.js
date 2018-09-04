var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'layer',
    'element',
    'util',
    'authority',
    'login',
    'laytpl',
    'request',
    'key-bind',
    'valid-login',
    'toast'
]
window.top.registeModule(window, requireModules,'');

layui.use(requireModules, function(layer,element,util,authority,login,laytpl,ajax,keyBind,validLogin,toast) {

    var $ = layui.jquery;

    var side = $('.my-side');
    var body = $('.my-body');
    var footer = $('.my-footer');

    // 监听导航栏收缩
    $('body').on('click', '.btn-nav', function(e) {
        if (localStorage.log == 0) {
            $(this).find('.layui-icon').html('&#xe603;');
            navShow(50);
        } else {
            $(this).find('.layui-icon').html('&#xe602;');
            navHide(50);
        }

    });

    //顶部导航显示登录人真实姓名
    var loginObj = JSON.parse(sessionStorage.getItem("login"));
    //清理浏览器缓存后，loginObj为null
    if(loginObj==null){
        login.backToLogin();
    }

    $("#displayName").text(loginObj.name);

    var navsTpl = navs.innerHTML;
    var navData = authority.getNavs().success(function(result) {
        laytpl(navsTpl).render(result.data, function(html) {
            //动态渲染左侧导航
            navview.innerHTML = html;
            element.init();
        });
    });


    $('#logout').click(function() {
        login.backToLogin();
    });

    // 导航栏收缩
    function navHide(t, st) {
        var time = t ? t : 50;
        st ? localStorage.log = 1 : localStorage.log = 0;
        side.animate({
            'left': -200
        }, time);
        body.animate({
            'left': 0
        }, time);
        footer.animate({
            'left': 0
        }, time);
    }

    // 导航栏展开
    function navShow(t, st) {
        var time = t ? t : 50;
        st ? localStorage.log = 0 : localStorage.log = 1;
        side.animate({
            'left': 0
        }, time);
        body.animate({
            'left': 200
        }, time);
        footer.animate({
            'left': 200
        }, time);
    }

    // 监听导航(side)点击切换页面
    element.on('nav(side)', function(elem) {
        // 添加tab方法
        addTab(element, elem);
    });

    // 监听顶部左侧导航
    element.on('nav(side-left)', function(elem) {
        // 添加tab方法
        addTab(element, elem);

    });

    // 监听顶部右侧导航
    element.on('nav(side-right)', function(elem) {
        // 修改skin
        if ($(this).parent().attr('data-skin')) {
            localStorage.skin = $(this).parent().attr('data-skin');
            skin();
        } else {
            // 添加tab方法
            addTab(element, elem);
        }

    });


    $('#clearCache').on('click', function() {
        toast.smile("如有系统升级，可通过快捷键Ctrl+Shift+Delete清除浏览器缓存，只需清理缓存的图片和文件。");
    });

    if (window.sessionStorage.getItem("locksys") == "true") {
        lockPage();
    }

    //ALT+L锁屏
    $(document).on('keydown', function(e) {
        if(e.keyCode == 76 && e.altKey) {
            window.sessionStorage.setItem("locksys", true);
            lockPage();
        }
    });

    //点击锁屏按钮锁屏
    $('#lock').on('click', function() {
        window.sessionStorage.setItem("locksys", true);
        lockPage();
    });

    //锁屏
    function lockPage() {
        //自定页
        layer.open({
            title: false,
            type: 1,
            closeBtn: 0,
            anim: 6,
            content: $('#lock-temp').html(),
            shade: [0.9, '#393D49'],
            success: function(layero, lockIndex) {
                var $lockBox = $('div#lock-box');
                var loginUser = login.getLoginInfo();
                $lockBox.find('div#lockUserName').html(loginUser.name);

                //绑定解锁按钮的点击事件
                layero.find('button#unlock').on('click', function() {
                    var userName = loginUser.userName;
                    var pwd = $lockBox.find('input[name=lockPwd]').val();
                    if(pwd == '' || pwd.length == 0) {
                        layer.msg('请输入密码', {
                            icon: 2,
                            time: 1000
                        });
                        return;
                    }
                    unlock(userName, pwd);
                });

                //解锁操作方法
                var unlock = function(userName, pwd) {

                    var isSuccess = login.unlock(userName, pwd);
                    if(isSuccess){
                        //关闭锁屏层
                        window.sessionStorage.setItem("locksys", false);
                        layer.close(lockIndex);
                    }
                };
            }
        });
    };

    // 添加TAB选项卡
    function addTab(element, elem) {
        console.log(elem);
        var card = 'card'; // 选项卡对象
        var title = elem.html(); // 导航栏text
        var src = elem.attr('href-url'); // 导航栏跳转URL
        var id = new Date().getTime(); // ID
        var navId = elem.attr('data-id');
        var flag = getTitleId(card, title); // 是否有该选项卡存在
        // 大于0就是有该选项卡了
        if (flag > 0) {
            id = flag;
        } else {
            if (src) {
                //新增
                var src = ajax.composeUrl(src, {
                    navId: navId
                });

                layer.load(0, {
                    shade: 0.5
                });

                element.tabAdd(card, {
                    title: '<span>' + title + '</span>',
                    content: '<iframe src="' + src + '" frameborder="0" onload="layer.closeAll(\'loading\');" ></iframe>',
                    id: id
                });

                // 关闭弹窗
                //				layer.closeAll('loading');
            }
        }
        // 切换相应的ID tab
        element.tabChange(card, id);
        // 提示信息
        //		layer.msg(title);
    }

    // 根据导航栏text获取lay-id
    function getTitleId(card, title) {
        var id = -1;
        $(document).find(".layui-tab[lay-filter=" + card + "] ul li").each(function() {
            if (title === $(this).find('span').html()) {
                id = $(this).attr('lay-id');
            }
        });
        return id;
    }

    // 工具
    function _util() {
        var bar = $('.layui-fixbar');
        // 分辨率小于1024  使用内部工具组件
        if ($(window).width() < 1024) {
            util.fixbar({
                bar1: '&#xe602;',
                css: {
                    left: 10,
                    bottom: 54
                },
                click: function(type) {
                    if (type === 'bar1') {
                        //iframe层
                        layer.open({
                            type: 1, // 类型
                            title: false, // 标题
                            offset: 'l', // 定位 左边
                            closeBtn: 0, // 关闭按钮
                            anim: 3, // 动画3
                            shadeClose: true, // 点击遮罩关闭
                            shade: 0.8, // 半透明
                            area: ['150px', '100%'], // 区域
                            skin: 'my-mobile', // 样式
                            content: $('body .my-side').html() // 内容
                        });
                    }
                    element.init();
                }
            });
            bar.removeClass('layui-hide');
            bar.addClass('layui-show');
        } else {
            bar.removeClass('layui-show');
            bar.addClass('layui-hide');
        }
    }

    // 皮肤
    function skin() {
        var skin = localStorage.skin ? localStorage.skin : 0;
        var layout = $('.layui-layout-admin');
        layout.removeClass('skin-0');
        layout.removeClass('skin-1');
        layout.removeClass('skin-2');
        layout.addClass('skin-' + skin);
    }

    // 自适应
    $(window).on('resize', function() {
        if ($(this).width() > 1024) {
            if (localStorage.log == 0) {

                navShow();
            }
        } else {
            if (localStorage.log == 1) {

                navHide();
            }
        }
        init();
    });


    // 监听控制content高度
    function init() {
        // 起始判断收缩还是展开
        if (localStorage.log == 0) {
            $('.btn-nav .layui-icon').html('&#xe602;');
            navHide(100);
        } else {
            $('.btn-nav .layui-icon').html('&#xe603;');
            navShow(1);
        }
        // 工具
        _util();
        // skin
        skin();
        // 选项卡高度
        cardTitleHeight = $(document).find(".layui-tab[lay-filter='card'] ul.layui-tab-title").height();
        // 需要减去的高度
        height = $(window).height() - $('.layui-header').height() - cardTitleHeight - $('.layui-footer').height();
        // 设置高度
        $(document).find(".layui-tab[lay-filter='card'] div.layui-tab-content").height(height - 2);
    }

    var user = login.getLoginInfo();
    if (user) {
        $("#iframe").attr('src', 'views/welcome/welcome.html');
    } else {
        $("#iframe").attr('src', 'demo/welcome.html');
    }

    // 初始化
    init();
    window.elementParent = element;
});
