package com.viewol.exhibition.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.exhibition.vo.ExhibitionVO;
import com.viewol.exhibitor.vo.ExhibitorVO;
import com.viewol.pojo.Company;
import com.viewol.pojo.Product;
import com.viewol.service.IProductService;
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
 * 展品(产品)管理
 */
@Controller
@RequestMapping("exhibition")
public class ExhibitionController {

    @Resource
    private IProductService productService;

    @RequestMapping(value = "/exhibitionList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse exhibitionList(@RequestParam(value = "appId", defaultValue = "-1") int appId,
                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");


        PageHolder<ExhibitionVO> pageHolder = new PageHolder<>();
        ExhibitionVO vo = new ExhibitionVO();
        vo.setId(1);
        vo.setName("ABC");
        vo.setStatus(1);//下架
        vo.setIsRecommend(1);
        vo.setRecommendNum(2);
        vo.setIsSameRecommend(1);
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


    @RequestMapping(value = "/upExhibition")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "上架展品")
    @Repeat
    public BaseResponse upExhibition(int id) {
        BaseResponse rs = new BaseResponse();
        rs.setStatus(true);
        rs.setMsg("删除成功");

        return rs;
    }

    @RequestMapping(value = "/downExhibition")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "下架展品")
    @Repeat
    public BaseResponse downExhibition(int id) {
        BaseResponse rs = new BaseResponse();
        rs.setStatus(true);
        rs.setMsg("删除成功");

        return rs;
    }


    /**
     * 查询首页推荐产品
     * @return
     */
    @RequestMapping(value = "/queryRecommentProduct", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse queryRecommentProduct() {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        List<Product> productList = productService.queryRecommentProduct();
        List<ExhibitionVO> list = new ArrayList<>();

        if(null != productList && productList.size()>0){
            for(Product product : productList){
                ExhibitionVO vo = new ExhibitionVO();
                vo.setId(product.getId());
                vo.setCompanyId(product.getCompanyId());
                vo.setCategoryId(product.getCategoryId());
                vo.setStatus(product.getStatus());
                vo.setName(product.getName());
                vo.setImage(product.getImage());
                vo.setContent(product.getContent());
                vo.setPdfUrl(product.getPdfUrl());
                vo.setPdfName(product.getPdfName());
                vo.setIsRecommend(product.getIsRecommend());
                vo.setRecommendNum(product.getRecommendNum());
                vo.setcTime(product.getcTime());
                vo.setmTime(product.getmTime());

                list.add(vo);
            }
        }

        rs.setData(list);
        rs.setCount(list.size());
        return rs;
    }
}
