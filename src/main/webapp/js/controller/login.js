var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});


var requireModules = [
    'form',
    'layer',
    'login',
    'login-api',
    'key-bind',
    'request'
];

registeModule(window, requireModules);

layui.use(requireModules, function (form,
                                    layer,
                                    login,
                                    loginApi,
                                    keyBind,
                                    ajax) {
    var $ = layui.jquery;
    var f = layui.form;

    // 验证
    f.verify({
        account: function (value) {
            if (value == "") {
                return "请输入用户名";
            }
        },

        password: function (value) {
            if (value == "") {
                return "请输入密码";
            }
        },

        code: function (value) {
            if (value == "") {
                return "请输入验证码";
            }
        }
    });

    //初始化验证码赋值
    $('#valid-img').attr('src', loginApi.getUrl('getValidImg').url + '?type=1&_=' + Math.random());
    $('#valid-img').click(function () {
        $(this).attr('src', loginApi.getUrl('getValidImg').url + '?type=1&_=' + Math.random());
    });

    // $('#regist_code').attr('src', loginApi.getUrl('getValidImg').url + '?type=2&_=' + Math.random());
    // $('#regist_code').click(function () {
    //     $(this).attr('src', loginApi.getUrl('getValidImg').url + '?type=2&_=' + Math.random());
    // });

    //登录，注册面板切换
    // $('.quickFlip').quickFlip();


    //光标自动聚焦
    $('input:first').focus();

    keyBind.bindKey({
        13: function () {
            $(".btn-submit").trigger("click");
        }
    });

    // 登录监听
    f.on('submit(login)', function (data) {
        var user = data.field;
        login.login(user, function () {
            $('#valid-img').trigger('click');//登录失败，刷新验证码
        });
        return false;
    });

    //
    // f.on('submit(register)', function (data) {
    //     var user = data.field;
    //     login.register(user, function () {
    //         $('#regist_code').trigger('click');//注册失败，刷新验证码
    //     });
    //     return false;
    // });
})