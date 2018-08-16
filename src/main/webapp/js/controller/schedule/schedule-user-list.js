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
    'schedule-api',
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
    scheduleApi,
    tableUtil,
    btns,
    authority,
    toast,
    table
) {

    var $ = layui.jquery;
    var $table = table;
    var mainTable;
    var scheduleId;

    var MyController = {
        init: function() {
            scheduleId = request.getFixUrlParams("scheduleId");
            mainTable = MyController.renderTable();
        },
        renderTable: function() {
            return $table.render({
                elem: '#schedule-user-list'
                ,height: '400px'
                ,url: scheduleApi.getUrl('scheduleUserList').url
                ,where:{
                    "scheduleId": scheduleId
                }
                ,method: 'post'
                ,page: true
                ,limits:[20,50,100,200]
                ,cols: [[
                    {type:'numbers'},
                    {field: 'userId', title: '用户ID', width:100},
                    {field: 'userName', title: '姓名', width:100},
                    {field: 'phone', title: '手机', width:130},
                    {field: 'company', title: '公司', width:200},
                    {field: 'position', title: '职位', width:100},
                    {field: 'email', title: '邮箱', width:200},
                    {field: 'age', title: '年龄', width:100},
                    {field: 'reminderFlag', title: '是否提醒', width:100, templet: function (d) {
                            if(d.reminderFlag == 1){
                                return '<span>已通知</span>';
                            } else {
                                return '<span>未通知</span>';
                            }
                        }},
                    {field: 'reminderTime', title: '提醒时间', width:160, templet: function (d) {
                            return moment(d.reminderTime).format("YYYY-MM-DD HH:mm:ss");
                        }},
                    {field: 'cTime', title: '报名时间', width:160, templet: function (d) {
                            return moment(d.cTime).format("YYYY-MM-DD HH:mm:ss");
                        }}
                ]]
            });
        }
    };

    MyController.init();

});