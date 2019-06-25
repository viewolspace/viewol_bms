package com.viewol.exhibition.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.exhibition.response.ExhibitionCategoryResponse;
import com.viewol.exhibition.response.ExhibitionResponse;
import com.viewol.exhibition.vo.ExhibitionCategoryVO;
import com.viewol.exhibition.vo.ExhibitionVO;
import com.viewol.exhibitor.vo.ExhibitorVO;
import com.viewol.pojo.Category;
import com.viewol.pojo.Company;
import com.viewol.pojo.Product;
import com.viewol.pojo.query.ProductQuery;
import com.viewol.service.ICategoryService;
import com.viewol.service.IProductService;
import com.viewol.shiro.token.TokenManager;
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
    @Resource
    private ICategoryService categoryService;


    @RequestMapping(value = "/exhibitionList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse exhibitionList(@RequestParam(value = "categoryId", defaultValue ="") String categoryId,
                                           @RequestParam(value = "companyId", defaultValue = "") Integer companyId,
                                           @RequestParam(value = "name", defaultValue = "") String name,
                                           @RequestParam(value = "status", defaultValue = "") Integer status,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        ProductQuery productQuery = new ProductQuery();
        if(null != categoryId && !"".equals(categoryId) && !"-1".equals(categoryId)){
            productQuery.setCategoryId(categoryId);
        }
        if(null != name && !"".equals(name)){
            productQuery.setName(name);
        }
        productQuery.setCompanyId(companyId);

        if(null != status && status!=999){
            productQuery.setStatus(status);
        }

        productQuery.setPageIndex(page);
        productQuery.setPageSize(limit);

        PageHolder<Product> pageHolder = productService.queryProduct(productQuery);
        List<ExhibitionVO> voList = new ArrayList<>();

        if (null != pageHolder && null != pageHolder.getList() && pageHolder.getList().size() > 0) {
            for (Product product : pageHolder.getList()) {
                ExhibitionVO vo = new ExhibitionVO();
                vo.setId(product.getId());
                vo.setName(product.getName());
                vo.setCompanyId(product.getCompanyId());
                vo.setCategoryId(product.getCategoryId());
                vo.setStatus(product.getStatus());
                vo.setPdfName(product.getPdfName());
                vo.setPdfUrl(product.getPdfUrlView());
                vo.setImage(product.getImageView());
                vo.setcTime(product.getcTime());
                vo.setmTime(product.getmTime());

                vo.setIsRecommend(product.getIsRecommend());
                vo.setRecommendNum(product.getRecommendNum());

                voList.add(vo);
            }

            rs.setData(voList);
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }


    @RequestMapping(value = "/upExhibition")
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "上架展品")
    @Repeat
    public BaseResponse upExhibition(int id) {
        BaseResponse rs = new BaseResponse();
        int result = productService.updateStatus(id, Product.STATUS_ON);

        if(result>0){
            rs.setStatus(true);
            rs.setMsg("上架成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("上架失败");
        }

        return rs;
    }

    @RequestMapping(value = "/downExhibition")
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "下架展品")
    @Repeat
    public BaseResponse downExhibition(int id) {
        BaseResponse rs = new BaseResponse();
        int result = productService.updateStatus(id, Product.STATUS_OFF);

        if(result>0){
            rs.setStatus(true);
            rs.setMsg("下架成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("下架失败");
        }

        return rs;
    }

    @RequestMapping(value = "/addRecommentHome", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "推荐产品到首页")
    @Repeat
    public BaseResponse addRecommentHome(@RequestParam(value = "id", defaultValue = "-1") int id,
                                         @RequestParam(value = "recommendNum", defaultValue = "-1") int recommendNum) {

        int result = productService.addRecomment(id, recommendNum);

        BaseResponse rs = new BaseResponse();
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("推荐成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("推荐失败");
        }
        return rs;
    }

    @RequestMapping(value = "/delRecommentHome", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "取消产品首页推荐")
    @Repeat
    public BaseResponse delRecommentHome(@RequestParam(value = "id", defaultValue = "-1") int id) {

        int result = productService.delRecomment(id);

        BaseResponse rs = new BaseResponse();
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("取消推荐成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("取消推荐失败");
        }
        return rs;
    }

    /**
     * 查询首页推荐产品
     *
     * @return
     */
    @RequestMapping(value = "/queryRecommentProduct", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse queryRecommentProduct() {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        int expoId = TokenManager.getExpoId();//展会ID
        List<Product> productList = productService.queryRecommentProduct(expoId);
        List<ExhibitionVO> list = new ArrayList<>();

        if (null != productList && productList.size() > 0) {
            for (Product product : productList) {
                ExhibitionVO vo = new ExhibitionVO();
                vo.setId(product.getId());
                vo.setCompanyId(product.getCompanyId());
                vo.setCategoryId(product.getCategoryId());
                vo.setStatus(product.getStatus());
                vo.setName(product.getName());
                vo.setImage(product.getImageView());
                vo.setContent(product.getContentView());
                vo.setPdfUrl(product.getPdfUrlView());
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

    /**
     * 查询产品所属分类
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/getExhibitionCategory", method = RequestMethod.GET)
    @ResponseBody
    public ExhibitionCategoryResponse getExhibitionCategory(@RequestParam(value = "categoryId", defaultValue = "") String categoryId) {
        ExhibitionCategoryResponse rs = new ExhibitionCategoryResponse();
        Category category = categoryService.getCategory(categoryId);
        if(null != category){
            List<String> idsList = new ArrayList<>();
            List<String> namesList = new ArrayList<>();

            idsList.add(category.getId());
            namesList.add(category.getName());

            ExhibitionCategoryVO vo = new ExhibitionCategoryVO();
            vo.setIds(idsList.toArray(new String[idsList.size()]));
            vo.setCategoryNames((namesList.toArray(new String[namesList.size()])));

            rs.setStatus(true);
            rs.setMsg("ok");
            rs.setData(vo);
        } else {
            rs.setStatus(false);
            rs.setMsg("无数据");
        }

        return rs;
    }

    /**
     * 置顶产品
     * @param id
     * @param num
     * @return
     */
    @RequestMapping(value = "/addTop", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "置顶产品")
    @Repeat
    public BaseResponse addTop(@RequestParam(value = "id", defaultValue = "-1") int id,
                                         @RequestParam(value = "num", defaultValue = "-1") int num) {

        int result = productService.addTop(id, num);

        BaseResponse rs = new BaseResponse();
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("置顶成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("置顶失败");
        }
        return rs;
    }

    /**
     * 取消置顶产品
     * @param id 产品ID
     * @return
     */
    @RequestMapping(value = "/delTop", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "取消置顶产品")
    @Repeat
    public BaseResponse delTop(@RequestParam(value = "id", defaultValue = "-1") int id) {

        int result = productService.delTop(id);

        BaseResponse rs = new BaseResponse();
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("取消置顶成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("取消置顶失败");
        }
        return rs;
    }

    /**
     * 查询置顶产品
     *
     * @return
     */
    @RequestMapping(value = "/queryTopProduct", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse queryTopProduct() {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        int expoId = TokenManager.getExpoId();//展会ID
        List<Product> productList = productService.queryTopProduct(expoId);
        List<ExhibitionVO> list = new ArrayList<>();

        if (null != productList && productList.size() > 0) {
            for (Product product : productList) {
                ExhibitionVO vo = new ExhibitionVO();
                vo.setId(product.getId());
                vo.setCompanyId(product.getCompanyId());
                vo.setCategoryId(product.getCategoryId());
                vo.setStatus(product.getStatus());
                vo.setName(product.getName());
                vo.setImage(product.getImageView());
                vo.setContent(product.getContentView());
                vo.setPdfUrl(product.getPdfUrlView());
                vo.setPdfName(product.getPdfName());
                vo.setIsRecommend(product.getIsRecommend());
                vo.setRecommendNum(product.getRecommendNum());
                vo.setcTime(product.getcTime());
                vo.setmTime(product.getmTime());
                vo.setTopNum(product.getTopNum());
                list.add(vo);
            }
        }

        rs.setData(list);
        rs.setCount(list.size());
        return rs;
    }

    /**
     * 根据产品ID查询产品
     * @param id
     * @return
     */
    @RequestMapping(value = "/getExhibition", method = RequestMethod.POST)
    @ResponseBody
    public ExhibitionResponse getExhibition(@RequestParam(value = "id", defaultValue = "") int id) {
        ExhibitionResponse rs = new ExhibitionResponse();
        Product product = productService.getProduct(id);

        if(null != product){
            ExhibitionVO vo = new ExhibitionVO();
            vo.setId(product.getId());
            vo.setName(product.getName());
            vo.setCompanyId(product.getCompanyId());
            vo.setCategoryId(product.getCategoryId());
            vo.setStatus(product.getStatus());
            vo.setPdfName(product.getPdfName());
            vo.setPdfUrl(product.getPdfUrl());
            vo.setImage(product.getImageView());
            vo.setRegImage(product.getReImgView());
            vo.setContent(product.getContentView());
            vo.setcTime(product.getcTime());
            vo.setmTime(product.getmTime());

            rs.setStatus(true);
            rs.setMsg("ok");
            rs.setData(vo);
        } else {
            rs.setStatus(false);
            rs.setMsg("无此产品");
        }
        return rs;
    }
}
