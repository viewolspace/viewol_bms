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
        format: 'yyyy-MM-dd HH:mm:ss',
        value: moment(new Date()).format("YYYY-MM-DD hh:mm:ss")
    });

    laydate.render({
        elem: '#endTime',
        format: 'yyyy-MM-dd HH:mm:ss',
        value: moment(new Date()).add(7, 'days').format("YYYY-MM-DD hh:mm:ss")
    });

    laydate.render({
        elem: '#openTime',
        format: 'yyyy-MM-dd HH:mm:ss',
        value: moment(new Date()).format("YYYY-MM-DD hh:mm:ss")
    });

    laydate.render({
        elem: '#closeTime',
        format: 'yyyy-MM-dd HH:mm:ss',
        value: moment(new Date()).add(7, 'days').format("YYYY-MM-DD hh:mm:ss")
    });


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

    f.on('submit(match-add-form)', function(data) {
		ajax.request(matchApi.getUrl('createMatch'), data.field, function() {
			toast.success('创建比赛成功');
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			parent.list.refresh();
		});
		return false;
	});

});