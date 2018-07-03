package com.viewol.recommend.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.recommend.vo.RecommendVO;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.utils.Constants;
import com.youguu.core.util.PageHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 同类推荐管理(展商，展品)
 */
@Controller
@RequestMapping("sameRecommend")
public class SameRecommendController {

    /**
     * 同类推荐
     * @param type  1 展商  2 产品
     * @param categoryId  分类id
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/recommendList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse recommendList(@RequestParam(value = "type", defaultValue = "0") int type,
                                          @RequestParam(value = "categoryId", defaultValue = "") String categoryId,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        PageHolder<RecommendVO> pageHolder = new PageHolder<>();
        RecommendVO vo = new RecommendVO();
        vo.setId(1);
        vo.setType(1);
        vo.setThirdId(1);
        vo.setName("Z5");
        vo.setImage("");
        vo.setCategoryId("L12");
        vo.setcTime(new Date());
        vo.setmTime(new Date());
        pageHolder.add(vo);
        pageHolder.setTotalCount(1);

        if (null != pageHolder) {
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }


    @RequestMapping(value = "/cancelRecommend")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "取消同类推荐")
    @Repeat
    public BaseResponse cancelExhibitor(@RequestParam(value = "id", defaultValue = "") int id) {
        BaseResponse rs = new BaseResponse();
        rs.setStatus(true);
        rs.setMsg("删除成功");

        return rs;
    }
}
