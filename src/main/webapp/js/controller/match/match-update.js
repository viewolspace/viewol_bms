var webName = getWebName();

layui.config({
    base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
    'form',
    'form-util',
    'request',
    'match-api',
    'toast',
    'key-bind',
    'laydate',
    'upload',
    'ad-api',
    'valid-login'

];

registeModule(window, requireModules, {
    'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
    form,
    formUtil,
    ajax,
    matchApi,
    toast,
    keyBind,
    laydate,
    upload,
    adApi
) {
    var $ = layui.jquery;
    var f = layui.form;

    var data = ajax.getAllUrlParam();

    laydate.render({
        elem: '#beginTime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    laydate.render({
        elem: '#endTime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    laydate.render({
        elem: '#openTime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    laydate.render({
        elem: '#closeTime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });


    data.beginTime=moment(new Date(parseInt(data.beginTime))).format("YYYY-MM-DD hh:mm:ss");
    data.endTime=moment(new Date(parseInt(data.endTime))).format("YYYY-MM-DD hh:mm:ss");

    data.openTime=moment(new Date(parseInt(data.openTime))).format("YYYY-MM-DD hh:mm:ss");
    data.closeTime=moment(new Date(parseInt(data.closeTime))).format("YYYY-MM-DD hh:mm:ss");

    formUtil.renderData($('#match-update-form'),data);

    if(data.inviteCode != undefined && data.inviteCode!="") {
        $("#needapply").val(2);
        $("#inviteInput").show();
    } else if(data.whiteList==1){
        $("#needapply").val(3);
    } else {
        $("#needapply").val(1);
    }
    f.render();//赋值重新渲染

    $('#avatar_show_id').attr('src', data.logoImage);
    $('#avatar').val(data.logoImage);

    /**
     * 普通图片上传
     */
    var uploadInst = upload.render({
        elem: '#btnUploadImg_avatar'
        ,url: adApi.getUrl('uploadImg').url
        ,ext: 'jpg|png|gif|bmp'
        ,type: 'image'
        ,before: function(obj){
            //预读本地文件
        }
        ,done: function(res){
            if(res.status == false){
                return layer.msg('上传失败');
            }

            //上传成功
            $('#avatar_show_id').attr('src', res.imageUrl);
            $('#avatar').val(res.imageUrl);
        }
        ,error: function(){
            return layer.msg('数据请求异常');
        }
    });

    $('#inviteBtn').on('click', function() {
        ajax.request(matchApi.getUrl('makeInveiteCode'), null, function(result) {
            $("#inviteCode").val(result.inviteCode);
        });
    });

    f.on('select(needapply)', function(data){
        if(data.value == 2){
            $("#inviteInput").show();
        } else {
            $("#inviteInput").hide();
        }
    });

    f.on('submit(match-update-form)', function(data) {
        ajax.request(matchApi.getUrl('updateMatch'), data.field, function() {
            toast.success('修改比赛成功');
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.list.refresh();
        });
        return false;
    });

});