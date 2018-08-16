package com.viewol.recommend.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.pojo.Recommend;
import com.viewol.pojo.query.RecommendQuery;
import com.viewol.recommend.vo.RecommendVO;
import com.viewol.service.IRecommendService;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.utils.Constants;
import com.youguu.core.util.PageHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同类推荐管理(展商，展品)
 */
@Controller
@RequestMapping("sameRecommend")
public class SameRecommendController {

    @Resource
    private IRecommendService recommendService;

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
    public GridBaseResponse recommendList(@RequestParam(value = "type", defaultValue = "-1") String type,
                                          @RequestParam(value = "categoryId", defaultValue = "") String categoryId,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        RecommendQuery query = new RecommendQuery();
        if("-1".equals(type)){
            query.setType(0);//查询全部
        } else if("0001".equals(type)){
            query.setType(1);//查询展商
        } else if("0002".equals(type)){
            query.setType(2);//查询产品
        }

        if(null != categoryId && !"".equals(categoryId)&& !"-1".equals(categoryId)){
            query.setCategoryId(categoryId);
        }
        query.setPageIndex(page);
        query.setPageSize(limit);
        PageHolder<Recommend> pageHolder = recommendService.queryRecommend(query);

        if(null != pageHolder && pageHolder.getList().size()>0){
            List<RecommendVO> voList = new ArrayList<>();
            for(Recommend recommend : pageHolder.getList()){
                RecommendVO vo = new RecommendVO();
                vo.setId(recommend.getId());
                vo.setType(recommend.getType());
                vo.setThirdId(recommend.getThirdId());
                vo.setName(recommend.getName());
                vo.setImage(recommend.getImage());
                vo.setCategoryId(recommend.getCategoryId());
                vo.setcTime(recommend.getcTime());
                vo.setmTime(recommend.getmTime());

                voList.add(vo);

            }

            rs.setData(voList);
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    /**
     * 同类推荐，推荐展商（产品）到分类下
     * @param id 展商（产品）ID
     * @param type 1-展商;2-产品
     * @return
     */
    @RequestMapping(value = "/addRecommentSame", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.RECOMMEND, desc = "同类推荐")
    @Repeat
    public BaseResponse addRecommentSame(@RequestParam(value = "id", defaultValue = "-1") int id,
                                         @RequestParam(value = "type", defaultValue = "-1") int type) {

        int result = recommendService.addRecommend(type, id);

        BaseResponse rs = new BaseResponse();
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("同类推荐成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("同类推荐失败");
        }
        return rs;
    }

    @RequestMapping(value = "/cancelRecommend")
    @ResponseBody
    @MethodLog(module = Constants.RECOMMEND, desc = "取消同类推荐")
    @Repeat
    public BaseResponse cancelExhibitor(@RequestParam(value = "id", defaultValue = "") int id) {
        BaseResponse rs = new BaseResponse();

        int result = recommendService.delRecommend(id);

        if(result>0){
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }

        return rs;
    }
}
