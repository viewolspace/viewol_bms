package com.viewol.recommend.controller;

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
 * 同类推荐管理(展商，展品)
 */
@Controller
@RequestMapping("sameRecommend")
public class SameRecommendController {

    /**
     * 同类推荐的展商列表
     * @param appId
     * @param page
     * @param limit
     * @return
     */
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


    @RequestMapping(value = "/updateExhibitorRank", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "修改展商顺序")
    @Repeat
    public BaseResponse updateExhibitorRank(@RequestParam(value = "title", defaultValue = "") String title,
                                            @RequestParam(value = "beginDate", defaultValue = "") String beginDate,
                                            @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                            @RequestParam(value = "rank", defaultValue = "1") int rank,
                                            @RequestParam(value = "forwardUrl", defaultValue = "") String forwardUrl,
                                            @RequestParam(value = "avatar", defaultValue = "") String adImage) {

        BaseResponse rs = new BaseResponse();


        return rs;
    }

    @RequestMapping(value = "/cancelExhibitor")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "取消同类展商推荐")
    @Repeat
    public BaseResponse cancelExhibitor(int id) {
        BaseResponse rs = new BaseResponse();
        rs.setStatus(true);
        rs.setMsg("删除成功");

        return rs;
    }

    /**
     * 同类推荐的展品列表
     * @param appId
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/exhibitionList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse exhibitionList(@RequestParam(value = "appId", defaultValue = "-1") int appId,
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


    @RequestMapping(value = "/updateExhibitionRank", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "修改展品顺序")
    @Repeat
    public BaseResponse updateExhibitionRank(@RequestParam(value = "title", defaultValue = "") String title,
                                             @RequestParam(value = "beginDate", defaultValue = "") String beginDate,
                                             @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                             @RequestParam(value = "rank", defaultValue = "1") int rank,
                                             @RequestParam(value = "forwardUrl", defaultValue = "") String forwardUrl,
                                             @RequestParam(value = "avatar", defaultValue = "") String adImage) {

        BaseResponse rs = new BaseResponse();


        return rs;
    }

    @RequestMapping(value = "/cancelExhibition")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "取消同类展品推荐")
    @Repeat
    public BaseResponse cancelExhibition(int id) {
        BaseResponse rs = new BaseResponse();
        rs.setStatus(true);
        rs.setMsg("删除成功");

        return rs;
    }
}
