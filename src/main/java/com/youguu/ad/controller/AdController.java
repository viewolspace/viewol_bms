package com.youguu.ad.controller;

import com.youguu.ad.response.UploadResponse;
import com.youguu.asteroid.ad.pojo.AdWall;
import com.youguu.asteroid.rpc.client.AsteroidRPCClientFactory;
import com.youguu.asteroid.rpc.client.ad.IAdWallRPCService;
import com.youguu.common.BaseResponse;
import com.youguu.common.GridBaseResponse;
import com.youguu.core.pojo.Response;
import com.youguu.core.util.HttpUtil;
import com.youguu.core.util.PageHolder;
import com.youguu.core.zookeeper.pro.ZkPropertiesHelper;
import com.youguu.shiro.token.TokenManager;
import com.youguu.sys.interceptor.Repeat;
import com.youguu.sys.log.annotation.MethodLog;
import com.youguu.sys.utils.Constants;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

@Controller
@RequestMapping("ad")
public class AdController {

    private IAdWallRPCService adWallRPCService = AsteroidRPCClientFactory.getAdWallRPCService();

    @RequestMapping(value = "/adList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse adList(@RequestParam(value = "appId", defaultValue = "-1") int appId,
                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        if (TokenManager.getAppId() > 0) {
            appId = TokenManager.getAppId();
        }

        PageHolder<AdWall> pageHolder = adWallRPCService.queryAdWallList(appId, new HashMap<>(), page, limit);
        if (null != pageHolder) {
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    /**
     * 添加广告
     *
     * @param title
     * @param beginDate
     * @param endDate
     * @param rank
     * @param forwardUrl
     * @param adImage
     * @return
     */
    @RequestMapping(value = "/addAd", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "添加广告")
    @Repeat
    public BaseResponse addAd(@RequestParam(value = "title", defaultValue = "") String title,
                              @RequestParam(value = "beginDate", defaultValue = "") String beginDate,
                              @RequestParam(value = "endDate", defaultValue = "") String endDate,
                              @RequestParam(value = "rank", defaultValue = "1") int rank,
                              @RequestParam(value = "forwardUrl", defaultValue = "") String forwardUrl,
//                              @RequestParam(value = "content", defaultValue = "") String content,
                              @RequestParam(value = "avatar", defaultValue = "") String adImage) {

        BaseResponse rs = new BaseResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        AdWall ad = new AdWall();
        ad.setTitle(title);
        ad.setForwardUrl(forwardUrl);
        try {
            ad.setBeginDate(sdf.parse(beginDate));
            ad.setEndDate(sdf.parse(endDate));
        } catch (ParseException e1) {
        }
        ad.setAdImage(adImage);
        ad.setPositionType("2424");
        ad.setContentType("2502");
        ad.setRank(rank);
        ad.setAdvertisers(String.valueOf(TokenManager.getAppId()));//广告主填写Appid
        ad.setCreateTime(new Date());

        int addResult = adWallRPCService.addAdWall(ad);
        if (addResult > 0) {
            rs.setStatus(true);
            rs.setMsg("添加成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("添加失败");
        }

        return rs;
    }

    /**
     * 上传广告图
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "上传广告图片")
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

    @RequestMapping(value = "/deleteAd")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "删除广告")
    @Repeat
    public BaseResponse deleteAd(int id) {
        BaseResponse rs = new BaseResponse();
        int result = adWallRPCService.deleteAdWall(id);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }

        return rs;
    }

    /**
     * 修改广告
     *
     * @param id
     * @param title
     * @param beginDate
     * @param endDate
     * @param rank
     * @param forwardUrl
     * @param adImage
     * @return
     */
    @RequestMapping(value = "/updateAd", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "修改广告")
    @Repeat
    public BaseResponse updateAd(@RequestParam(value = "id", defaultValue = "-1") int id,
                                 @RequestParam(value = "title", defaultValue = "") String title,
                                 @RequestParam(value = "beginDate", defaultValue = "") String beginDate,
                                 @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                 @RequestParam(value = "rank", defaultValue = "1") int rank,
                                 @RequestParam(value = "forwardUrl", defaultValue = "") String forwardUrl,
//                                 @RequestParam(value = "content", defaultValue = "") String content,
                                 @RequestParam(value = "avatar", defaultValue = "") String adImage) {

        BaseResponse rs = new BaseResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        AdWall ad = adWallRPCService.getAdWall(id);
        ad.setTitle(title);
        ad.setForwardUrl(forwardUrl);
        try {
            ad.setBeginDate(sdf.parse(beginDate));
            ad.setEndDate(sdf.parse(endDate));
        } catch (ParseException e1) {
        }
        ad.setAdImage(adImage);
        ad.setPositionType("2424");
        ad.setContentType("2502");
//        ad.setContent(content);
        ad.setRank(rank);

        int result = adWallRPCService.updateAdWall(ad);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }

        return rs;
    }
}
