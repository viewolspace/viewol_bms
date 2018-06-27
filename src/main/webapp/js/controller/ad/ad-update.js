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
    'date-util',
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
    upload,
    dateUtil
	) {
	var $ = layui.jquery;
	var f = layui.form;
	var data = ajax.getAllUrlParam();


    laydate.render({
        elem: '#beginDate',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    laydate.render({
        elem: '#endDate',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    data.beginDate=moment(new Date(parseInt(data.beginDate))).format("YYYY-MM-DD hh:mm:ss");
    data.endDate=moment(new Date(parseInt(data.endDate))).format("YYYY-MM-DD hh:mm:ss");

    formUtil.renderData($('#ad-update-form'),data);

    $('#avatar_show_id').attr('src', data.adImage);
    $('#avatar').val(data.adImage);

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


	f.on('submit(ad-update-form)', function(data) {

		ajax.request(adApi.getUrl('updateAd'), data.field, function() {
			toast.success('修改成功');
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			parent.list.refresh();
		});
		return false;
	});

});