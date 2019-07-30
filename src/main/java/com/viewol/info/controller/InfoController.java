package com.viewol.info.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.common.UploadResponse;
import com.viewol.info.response.InfoResponse;
import com.viewol.info.vo.InfoVO;
import com.viewol.pojo.Info;
import com.viewol.pojo.query.InfoQuery;
import com.viewol.service.IInfoService;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.utils.Constants;
import com.youguu.core.util.PageHolder;
import com.youguu.core.util.PropertiesUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * 资讯管理
 */
@Controller
@RequestMapping("info")
public class InfoController {

    @Resource
    private IInfoService infoService;

    @RequestMapping(value = "/infoList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse infoList(@RequestParam(value = "title", defaultValue = "") String title,
                                     @RequestParam(value = "publicTime", defaultValue = "") String publicTime,
                                     @RequestParam(value = "classify", defaultValue = "1") Integer classify,
                                     @RequestParam(value = "companyId", defaultValue = "") Integer companyId,
                                     @RequestParam(value = "status", defaultValue = "") Integer status,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        try {
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            InfoQuery infoQuery = new InfoQuery();
            infoQuery.setTitle(title);
            if (!StringUtils.isEmpty(publicTime)) {
                infoQuery.setStartTime(dft.parse(publicTime.split(" - ")[0]));
                infoQuery.setEndTime(dft.parse(publicTime.split(" - ")[1]));
            }

            infoQuery.setClassify(classify);
            infoQuery.setCompanyId(companyId);
            if (null != status && status != 99) {
                infoQuery.setStatus(status);
            }

            infoQuery.setPageIndex(page);
            infoQuery.setPageSize(limit);

            PageHolder<Info> pageHolder = infoService.queryInfo(infoQuery);
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());

            return rs;
        } catch (Exception e) {
            rs.setCode(-1);
            rs.setMsg("系统异常");
            return rs;
        }

    }

    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    @ResponseBody
    public InfoResponse getInfo(@RequestParam(value = "id", defaultValue = "") int id) {
        InfoResponse rs = new InfoResponse();
        Info info = infoService.getInfo(id);

        if (null != info) {
            InfoVO vo = new InfoVO();
            vo.setId(info.getId());
            vo.setTitle(info.getTitle());
            vo.setSummary(info.getSummary());
            vo.setPubTime(info.getPubTime());
            vo.setPicUrl(info.getPicUrl());
            vo.setContentUrl(info.getContentUrl());
            vo.setCreateTime(info.getCreateTime());
            vo.setMd5Str(info.getMd5Str());
            vo.setStatus(info.getStatus());
            vo.setmTime(info.getmTime());
            vo.setContent(info.getContent());
            vo.setClassify(info.getClassify());
            vo.setCompanyId(info.getCompanyId());

            rs.setStatus(true);
            rs.setMsg("ok");
            rs.setData(vo);
        } else {
            rs.setStatus(false);
            rs.setMsg("无此资讯");
        }
        return rs;
    }

    @RequestMapping(value = "/addInfo", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "添加资讯")
    @Repeat
    public BaseResponse addInfo(@RequestParam(value = "title", defaultValue = "") String title,
                                @RequestParam(value = "summary", defaultValue = "") String summary,
                                @RequestParam(value = "imageAvatar", defaultValue = "") String imageAvatar,
                                @RequestParam(value = "content", defaultValue = "") String content) {

        BaseResponse rs = new BaseResponse();
        Info info = new Info();
        info.setTitle(title);
        info.setSummary(summary);
        info.setPubTime(new Date());
        info.setPicUrl(imageAvatar);
        info.setCreateTime(new Date());
        info.setStatus(0);//-1 打回  0 待审 1 发布
        info.setContent(content);
        info.setClassify(TokenManager.getExpoId());
        info.setCompanyId(-1);

        int result = infoService.save(TokenManager.getExpoId(), info);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("保存成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("保存失败");
        }

        return rs;
    }

    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "修改展品")
    @Repeat
    public BaseResponse updateInfo(@RequestParam(value = "id", defaultValue = "") int id,
                                   @RequestParam(value = "title", defaultValue = "") String title,
                                   @RequestParam(value = "summary", defaultValue = "") String summary,
                                   @RequestParam(value = "imageAvatar", defaultValue = "") String imageAvatar,
                                   @RequestParam(value = "content", defaultValue = "") String content) {

        BaseResponse rs = new BaseResponse();
        Info info = infoService.getInfo(id);
        info.setTitle(title);
        info.setSummary(summary);
        info.setPicUrl(imageAvatar);
        info.setContent(content);
        info.setmTime(new Date());
        int result = infoService.updateInfo(info);

        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }

        return rs;
    }

    @RequestMapping(value = "/deleteInfo")
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "删除展品")
    @Repeat
    public BaseResponse deleteInfo(@RequestParam(value = "id", defaultValue = "") int id) {
        BaseResponse rs = new BaseResponse();

        int result = infoService.deleteInfo(id);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }
        return rs;
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "修改资讯状态")
    @Repeat
    public BaseResponse updateStatus(@RequestParam(value = "id", defaultValue = "") int id,
                                     @RequestParam(value = "status", defaultValue = "") int status) {

        BaseResponse rs = new BaseResponse();

        int result = infoService.updateStatus(id, status);

        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }

        return rs;
    }

    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    @Repeat
    public UploadResponse uploadImg(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        UploadResponse rs = new UploadResponse();

        if (null != file) {
            String myFileName = file.getOriginalFilename();// 文件原名称
            SimpleDateFormat dft = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = dft.format(new Date()) + Integer.toHexString(new Random().nextInt()) + "." + myFileName.substring(myFileName.lastIndexOf(".") + 1);

            Properties properties = PropertiesUtil.getProperties("properties/config.properties");
            String path = properties.getProperty("img.path");
            String imageUrl = properties.getProperty("imageUrl");

            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            String midPath = yyyyMMdd.format(new Date());
            File fileDir = new File(path + midPath);
            if (!fileDir.exists()) { //如果不存在 则创建
                fileDir.mkdirs();
            }
            path = path + midPath + File.separator + fileName;
            File localFile = new File(path);
            try {
                file.transferTo(localFile);

                rs.setStatus(true);
                rs.setMsg("上传成功");
                String httpUrl = imageUrl + File.separator + midPath + File.separator + fileName;
                rs.setImageUrl(httpUrl);

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
