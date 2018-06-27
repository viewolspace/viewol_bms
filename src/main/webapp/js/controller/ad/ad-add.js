var webName = getWebName();

layui.config({
	base: webName + '/js/modules/',
    version: 2018011001
});

var requireModules = [
	'form',
	'form-util',
	'request',
	'ad-api',
	'toast',
	'key-bind',
    'laydate',
	'upload',
	'valid-login'

];

registeModule(window, requireModules, {
	'role&authority-api': 'api/role&authority-api'
});

layui.use(requireModules, function(
	form,
	formUtil,
	ajax,
	adApi,
	toast,
	keyBind,
    laydate,
    upload
	) {
	var $ = layui.jquery;
	var f = layui.form;
	var data = ajax.getAllUrlParam();

    laydate.render({
        elem: '#beginDate',
        format: 'yyyy-MM-dd HH:mm:ss',
        value: moment(new Date()).format("YYYY-MM-DD hh:mm:ss")
    });

    laydate.render({
        elem: '#endDate',
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


	f.on('submit(ad-add-form)', function(data) {

		ajax.request(adApi.getUrl('addAd'), data.field, function() {
			toast.success('添加广告成功');
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			parent.list.refresh();
		});
		return false;
	});

});