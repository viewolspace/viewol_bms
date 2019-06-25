package com.viewol.exhibitor.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.common.UploadResponse;
import com.viewol.exhibitor.response.ExhibitorCategoryResponse;
import com.viewol.exhibitor.vo.ExhibitorCategoryVO;
import com.viewol.exhibitor.vo.ExhibitorVO;
import com.viewol.pojo.Category;
import com.viewol.pojo.Company;
import com.viewol.pojo.Recommend;
import com.viewol.pojo.query.CompanyQuery;
import com.viewol.service.ICompanyService;
import com.viewol.service.IRecommendService;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.pojo.SysUser;
import com.viewol.sys.pojo.SysUserRole;
import com.viewol.sys.service.SysUserRoleService;
import com.viewol.sys.service.SysUserService;
import com.viewol.sys.utils.Constants;
import com.youguu.core.pojo.Response;
import com.youguu.core.util.HttpUtil;
import com.youguu.core.util.MD5;
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

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 展商管理
 */
@Controller
@RequestMapping("exhibitor")
public class ExhibitorController {

    @Resource
    private ICompanyService companyService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserRoleService sysUserRoleService;

    public static final int ROLE_ID = 8;//展商角色ID
    /**
     * 查询展商列表
     * @param name 展商名称
     * @param categoryId 展商类别
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/exhibitorList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse exhibitorList(@RequestParam(value = "name", defaultValue = "") String name,
                                          @RequestParam(value = "categoryId", defaultValue = "") String categoryId,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        CompanyQuery query = new CompanyQuery();
        query.setPageIndex(page);
        query.setPageSize(limit);
        query.setName(name);
        if(null != categoryId && !"".equals(categoryId) && !"-1".equals(categoryId)){
            query.setCategoryId(categoryId);
        }

        PageHolder<Company> pageHolder = companyService.queryCompany(query);
        List<ExhibitorVO> list = new ArrayList<>();

        if(null != pageHolder && pageHolder.getList().size()>0){
            for(Company company : pageHolder.getList()){
                ExhibitorVO vo = new ExhibitorVO();
                vo.setId(company.getId());
                vo.setName(company.getName());
                vo.setShortName(company.getShortName());
                vo.setLogo(company.getLogoView());
                vo.setBanner(company.getBannerView());
                vo.setImage(company.getImageView());
                vo.setPlace(company.getPlace());
                vo.setPlaceSvg(company.getPlaceSvg());
                vo.setProductNum(company.getProductNum());
                vo.setCanApply(company.getCanApply());
                vo.setIsRecommend(company.getIsRecommend());
                vo.setRecommendNum(company.getRecommendNum());
                vo.setcTime(company.getcTime());
                vo.setmTime(company.getmTime());
                vo.setTopNum(company.getTopNum());
                vo.setAward(company.getAward());
                list.add(vo);
            }
        }

        if (null != pageHolder) {
            rs.setData(list);
            rs.setCount(pageHolder.getTotalCount());
        }
        return rs;
    }


    /**
     * 添加展商
     * @param name 展商名称
     * @param shortName 展商简称
     * @param place 展商位置
     * @param placeSvg 展商svg位置
     * @param canApply 申请活动
     * @param isRecommend 是否推荐
     * @param recommendNum 推荐顺序
     * @param productNum 展品数量
     * @param ids 分类ID集合
     * @return
     */
    @RequestMapping(value = "/addExhibitor", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITOR, desc = "添加展商")
    @Repeat
    public BaseResponse addExhibitor(@RequestParam(value = "name", defaultValue = "") String name,
                                     @RequestParam(value = "shortName", defaultValue = "") String shortName,
                                     @RequestParam(value = "place", defaultValue = "") String place,
                                     @RequestParam(value = "placeSvg", defaultValue = "") String placeSvg,
                                     @RequestParam(value = "canApply", defaultValue = "0") int canApply,
                                     @RequestParam(value = "isRecommend", defaultValue = "0") int isRecommend,
                                     @RequestParam(value = "recommendNum", defaultValue = "0") int recommendNum,
                                     @RequestParam(value = "productNum", defaultValue = "5") int productNum,
                                     @RequestParam(value = "ids[]") String[] ids,
                                     @RequestParam(value = "categoryNames[]") String[] categoryNames,
                                     @RequestParam(value = "award", defaultValue = "0") int award) {

        BaseResponse rs = new BaseResponse();
        Company company = new Company();
        company.setName(name);
        company.setShortName(shortName);
        company.setPlace(place);
        company.setPlaceSvg(placeSvg);
        company.setCanApply(canApply);
        company.setIsRecommend(isRecommend);
        company.setRecommendNum(recommendNum);
        company.setProductNum(productNum);
        company.setcTime(new Date());
        company.setmTime(new Date());
        company.setAward(award);
        List<String> categoryIdList = Arrays.asList(ids);
        int expoId = TokenManager.getExpoId();//展会ID
        int result = companyService.addCompany(expoId, company, categoryIdList);

        if(result>0){
            String userName = genRandomUserName();
            String password = "123456";//密码固定

            SysUser sysUser = new SysUser();
            sysUser.setUserName(userName);
            sysUser.setRealName(shortName);
            sysUser.setPswd(new MD5().getMD5ofStr(password).toLowerCase());
            sysUser.setUserStatus(1);
            sysUser.setCompanyId(result);
            sysUser.setCreateTime(new Date());
            int userId = sysUserService.saveSysUser(sysUser);

            if(userId>0) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUid(userId);
                userRole.setRid(ROLE_ID);
                userRole.setCreateTime(new Date());
                sysUserRoleService.saveSysUserRole(userRole);
            }
            rs.setStatus(true);
            rs.setMsg("保存成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("保存失败");
        }
        return rs;
    }

    public String genRandomUserName() {
        int maxNum = 36;
        int i;
        int count = 0;
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < 8) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();

    }

    @RequestMapping(value = "/deleteExhibitor")
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITOR, desc = "删除展商")
    @Repeat
    public BaseResponse deleteExhibitor(@RequestParam(value = "id", defaultValue = "-1") int id) {
        BaseResponse rs = new BaseResponse();

        int result = companyService.delCompany(id);

        if(result>0){
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }

        return rs;
    }

    /**
     * 添加展商
     * @param id 展商id
     * @param name 展商名称
     * @param shortName 展商简称
     * @param place 展商位置
     * @param placeSvg 展商svg位置
     * @param canApply 申请活动
     * @param isRecommend 是否推荐
     * @param recommendNum 推荐顺序
     * @param productNum 展品数量
     * @param ids 分类ID集合
     * @return
     */
    @RequestMapping(value = "/updateExhibitor", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITOR, desc = "修改展商")
    @Repeat
    public BaseResponse updateExhibitor(@RequestParam(value = "id", defaultValue = "-1") int id,
                                        @RequestParam(value = "name", defaultValue = "") String name,
                                        @RequestParam(value = "shortName", defaultValue = "") String shortName,
                                        @RequestParam(value = "place", defaultValue = "") String place,
                                        @RequestParam(value = "placeSvg", defaultValue = "") String placeSvg,
                                        @RequestParam(value = "canApply", defaultValue = "0") int canApply,
                                        @RequestParam(value = "isRecommend", defaultValue = "0") int isRecommend,
                                        @RequestParam(value = "recommendNum", defaultValue = "0") int recommendNum,
                                        @RequestParam(value = "productNum", defaultValue = "5") int productNum,
                                        @RequestParam(value = "ids[]") String[] ids,
                                        @RequestParam(value = "categoryNames[]") String[] categoryNames,
                                        @RequestParam(value = "award", defaultValue = "0") int award) {

        BaseResponse rs = new BaseResponse();
        Company company = companyService.getCompany(id);
        company.setName(name);
        company.setShortName(shortName);
        company.setPlace(place);
        company.setPlaceSvg(placeSvg);
        company.setCanApply(canApply);
        company.setIsRecommend(isRecommend);
        company.setRecommendNum(recommendNum);
        company.setProductNum(productNum);
        company.setAward(award);
        company.setmTime(new Date());
        List<String> categoryIdList = Arrays.asList(ids);

        int result = companyService.updateCompany(company, categoryIdList);

        if(result>0){
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }
        return rs;
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getExhibitorCategory", method = RequestMethod.GET)
    @ResponseBody
    public ExhibitorCategoryResponse getExhibitorCategory(@RequestParam(value = "id", defaultValue = "-1") int id) {
        ExhibitorCategoryResponse rs = new ExhibitorCategoryResponse();
        List<Category> list = companyService.getCompanyCategory(id);
        if(null != list && list.size()>0){
            List<String> idsList = new ArrayList<>();
            List<String> namesList = new ArrayList<>();
            for(Category category : list){
                idsList.add(category.getId());
                namesList.add(category.getName());
            }

            ExhibitorCategoryVO vo = new ExhibitorCategoryVO();
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
     * 推荐展商到首页
     * @param id 展商ID
     * @param recommendNum 推荐顺序
     * @return
     */
    @RequestMapping(value = "/addRecommentHome", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITOR, desc = "推荐展商到首页")
    @Repeat
    public BaseResponse addRecommentHome(@RequestParam(value = "id", defaultValue = "-1") int id,
                                         @RequestParam(value = "recommendNum", defaultValue = "-1") int recommendNum) {

        int result = companyService.addRecomment(id, recommendNum);

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

    /**
     * 取消推荐
     * @param id 展商ID
     * @return
     */
    @RequestMapping(value = "/delRecommentHome", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITOR, desc = "取消展商首页推荐")
    @Repeat
    public BaseResponse delRecommentHome(@RequestParam(value = "id", defaultValue = "-1") int id) {

        int result = companyService.delRecomment(id);

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
     * 查询首页推荐展商列表
     * @return
     */
    @RequestMapping(value = "/queryRecommentCompany", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse queryRecommentCompany() {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        int expoId = TokenManager.getExpoId();//展会ID
        List<Company> companyList = companyService.queryRecommentCompany(expoId);
        List<ExhibitorVO> list = new ArrayList<>();

        if(null != companyList && companyList.size()>0){
            for(Company company : companyList){
                ExhibitorVO vo = new ExhibitorVO();
                vo.setId(company.getId());
                vo.setName(company.getName());
                vo.setShortName(company.getShortName());
                vo.setLogo(company.getLogoView());
                vo.setBanner(company.getBannerView());
                vo.setImage(company.getImageView());
                vo.setPlace(company.getPlace());
                vo.setPlaceSvg(company.getPlaceSvg());
                vo.setProductNum(company.getProductNum());
                vo.setCanApply(company.getCanApply());
                vo.setIsRecommend(company.getIsRecommend());
                vo.setRecommendNum(company.getRecommendNum());
                vo.setcTime(company.getcTime());
                vo.setmTime(company.getmTime());

                list.add(vo);
            }
        }

        rs.setData(list);
        rs.setCount(list.size());
        return rs;
    }

    /**
     * 展商置顶
     * @param id 展商ID
     * @param num 置顶顺序
     * @return
     */
    @RequestMapping(value = "/addTop", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITOR, desc = "展商置顶")
    @Repeat
    public BaseResponse addTop(@RequestParam(value = "id", defaultValue = "-1") int id,
                                         @RequestParam(value = "num", defaultValue = "-1") int num) {

        int result = companyService.addTop(id, num);

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
     * 取消展商置顶
     * @param id 展商ID
     * @return
     */
    @RequestMapping(value = "/delTop", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITOR, desc = "取消展商置顶")
    @Repeat
    public BaseResponse delTop(@RequestParam(value = "id", defaultValue = "-1") int id) {

        int result = companyService.delTop(id);

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
     * 查询置顶展商列表
     * @return
     */
    @RequestMapping(value = "/queryTopCompany", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse queryTopCompany() {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        int expoId = TokenManager.getExpoId();//展会ID
        List<Company> companyList = companyService.queryTopCompany(expoId);
        List<ExhibitorVO> list = new ArrayList<>();

        if(null != companyList && companyList.size()>0){
            for(Company company : companyList){
                ExhibitorVO vo = new ExhibitorVO();
                vo.setId(company.getId());
                vo.setName(company.getName());
                vo.setShortName(company.getShortName());
                vo.setLogo(company.getLogoView());
                vo.setBanner(company.getBannerView());
                vo.setImage(company.getImageView());
                vo.setPlace(company.getPlace());
                vo.setPlaceSvg(company.getPlaceSvg());
                vo.setProductNum(company.getProductNum());
                vo.setCanApply(company.getCanApply());
                vo.setIsRecommend(company.getIsRecommend());
                vo.setRecommendNum(company.getRecommendNum());
                vo.setcTime(company.getcTime());
                vo.setmTime(company.getmTime());
                vo.setTopNum(company.getTopNum());

                list.add(vo);
            }
        }

        rs.setData(list);
        rs.setCount(list.size());
        return rs;
    }
}
