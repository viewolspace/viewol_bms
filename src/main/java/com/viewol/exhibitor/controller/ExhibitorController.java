package com.viewol.exhibitor.controller;

import com.viewol.category.vo.CategoryVO;
import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.common.UploadResponse;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.utils.Constants;
import com.youguu.core.pojo.Response;
import com.youguu.core.util.HttpUtil;
import com.youguu.core.util.PageHolder;
import com.youguu.core.zookeeper.pro.ZkPropertiesHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * 展商管理
 */
@Controller
@RequestMapping("category")
public class ExhibitorController {

    @RequestMapping(value = "/exhibitorList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse exhibitorList(@RequestParam(value = "appId", defaultValue = "-1") int appId,
                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        if (TokenManager.getAppId() > 0) {
            appId = TokenManager.getAppId();
        }

        PageHolder<CategoryVO> pageHolder = null;
        if (null != pageHolder) {
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }


    @RequestMapping(value = "/addExhibitor", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "添加类别")
    @Repeat
    public BaseResponse addExhibitor(@RequestParam(value = "title", defaultValue = "") String title,
                              @RequestParam(value = "beginDate", defaultValue = "") String beginDate,
                              @RequestParam(value = "endDate", defaultValue = "") String endDate,
                              @RequestParam(value = "rank", defaultValue = "1") int rank,
                              @RequestParam(value = "forwardUrl", defaultValue = "") String forwardUrl,
                              @RequestParam(value = "avatar", defaultValue = "") String adImage) {

        BaseResponse rs = new BaseResponse();


        return rs;
    }

    /**
     * 批量增加展商，从excel导入
     * @return
     */
    @RequestMapping(value = "/batchAddExhibitor", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "添加类别")
    @Repeat
    public BaseResponse batchAddExhibitor(@RequestParam(value = "title", defaultValue = "") String title,
                                     @RequestParam(value = "beginDate", defaultValue = "") String beginDate,
                                     @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                     @RequestParam(value = "rank", defaultValue = "1") int rank,
                                     @RequestParam(value = "forwardUrl", defaultValue = "") String forwardUrl,
                                     @RequestParam(value = "avatar", defaultValue = "") String adImage) {

        BaseResponse rs = new BaseResponse();


        return rs;
    }

    @RequestMapping(value = "/deleteExhibitor")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "删除类别")
    @Repeat
    public BaseResponse deleteExhibitor(int id) {
        BaseResponse rs = new BaseResponse();
        rs.setStatus(true);
        rs.setMsg("删除成功");

        return rs;
    }

    @RequestMapping(value = "/updateExhibitor", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "删除类别")
    @Repeat
    public BaseResponse updateExhibitor(@RequestParam(value = "id", defaultValue = "-1") int id,
                                 @RequestParam(value = "title", defaultValue = "") String title,
                                 @RequestParam(value = "beginDate", defaultValue = "") String beginDate,
                                 @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                 @RequestParam(value = "rank", defaultValue = "1") int rank,
                                 @RequestParam(value = "forwardUrl", defaultValue = "") String forwardUrl,
                                 @RequestParam(value = "avatar", defaultValue = "") String adImage) {

        BaseResponse rs = new BaseResponse();

        rs.setStatus(true);
        rs.setMsg("修改成功");

        return rs;
    }

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "上传类别图片")
    @Repeat
    public UploadResponse uploadImg(@RequestParam(value = "file", required = false) MultipartFile file) {

        UploadResponse rs = new UploadResponse();

        if (null != file) {
            String myFileName = file.getOriginalFilename();// 文件原名称
            SimpleDateFormat dft = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = dft.format(new Date()) + Integer.toHexString(new Random().nextInt()) + "." + myFileName.substring(myFileName.lastIndexOf(".") + 1);

            WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
            ServletContext servletContext = webApplicationContext.getServletContext();
            // 得到文件绝对路径
            String contexPath = servletContext.getRealPath("/");

            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            String sqlPath = "upload/images/" + yyyyMMdd.format(new Date()) + "/";

            File fileDir = new File(contexPath + sqlPath);
            if (!fileDir.exists()) { //如果不存在 则创建
                fileDir.mkdirs();
            }
            String path = contexPath + sqlPath + fileName;
            File localFile = new File(path);
            try {
                file.transferTo(localFile);

                rs.setStatus(true);
                rs.setMsg("上传成功");
                Properties p = ZkPropertiesHelper.getCacheAndWatchProperties("config.properties", true);
                String url_prefix = p.getProperty("ad.images");
                String httpUrl = url_prefix + File.separator + sqlPath + fileName;
                rs.setImageUrl(httpUrl);

                //检查图片是否同步完，同步完成再回显
                for (int i = 0; i < 5; i++) {
                    Response<String> response = HttpUtil.sendGet(httpUrl, null, "UTF-8");
                    if ("0000".equals(response.getCode())) {
                        break;
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }

                }
            } catch (IllegalStateException e) {
                rs.setStatus(false);
                rs.setMsg("服务器异常");
            } catch (IOException e) {
                rs.setStatus(false);
                rs.setMsg("服务器异常");
            }
        } else {
            rs.setStatus(false);
            rs.setMsg("文件为空");
        }

        return rs;
    }
}
