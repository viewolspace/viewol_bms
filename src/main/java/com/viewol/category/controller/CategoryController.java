package com.viewol.category.controller;

import com.viewol.category.response.CategoryResponse;
import com.viewol.category.response.CategoryTreeResponse;
import com.viewol.category.vo.CategoryTreeVO;
import com.viewol.category.vo.CategoryVO;
import com.viewol.common.BaseResponse;
import com.viewol.pojo.Category;
import com.viewol.service.ICategoryService;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.pojo.SysPermission;
import com.viewol.sys.response.AllPermissionResponse;
import com.viewol.sys.utils.Constants;
import com.viewol.sys.vo.PermissionVO;
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
 * 展商，展品分类
 */
@Controller
@RequestMapping("category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "添加类别")
    @Repeat
    public BaseResponse addCategory(@RequestParam(value = "num", defaultValue = "0") int num,
                                    @RequestParam(value = "type", defaultValue = "0") int type,
                                    @RequestParam(value = "name", defaultValue = "") String name,
                                    @RequestParam(value = "pid", defaultValue = "") String pid) {

        BaseResponse rs = new BaseResponse();
        Category category = new Category();
        category.setName(name);
        category.setParentId(pid);
        category.setNum(num);
        category.setType(type);
        category.setcTime(new Date());

        String id = categoryService.addCategory(category);
        if(null == id || "".equals(id)){
            rs.setStatus(false);
            rs.setMsg("添加失败");
        } else {
            rs.setStatus(true);
            rs.setMsg("添加成功");
        }

        return rs;
    }

    @RequestMapping(value = "/deleteCategory")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "删除类别")
    @Repeat
    public BaseResponse deleteCategory(@RequestParam(value = "id", defaultValue = "") String id) {
        BaseResponse rs = new BaseResponse();

        int result = categoryService.delCategory(id);
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }

        return rs;
    }

    @RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "修改类别")
    @Repeat
    public BaseResponse updateCategory(@RequestParam(value = "id", defaultValue = "") String id,
                                       @RequestParam(value = "name", defaultValue = "") String name) {

        BaseResponse rs = new BaseResponse();
        Category category = categoryService.getCategory(id);
        category.setName(name);
        int result = categoryService.updateCategory(category);

        if(result>0){
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }


        return rs;
    }

    @RequestMapping(value = "/categoryTreeList", method = RequestMethod.GET)
    @ResponseBody
    public CategoryResponse categoryTreeList(@RequestParam(value = "type", defaultValue = "1") int type) {

        CategoryResponse rs = new CategoryResponse();
        rs.setStatus(true);
        rs.setMsg("ok");

        CategoryVO vo1 = new CategoryVO();
        vo1.setId("0001");
        vo1.setName("展商分类");
        vo1.setpId("0");
        vo1.setType(1);
        vo1.setNum(1);
        vo1.setcTime(new Date());

        CategoryVO vo2 = new CategoryVO();
        vo2.setId("0002");
        vo2.setName("产品分类");
        vo2.setpId("0");
        vo2.setType(2);
        vo2.setNum(2);
        vo2.setcTime(new Date());

        List<CategoryVO> list = new ArrayList<>();
        list.add(vo1);
        list.add(vo2);

        List<Category> companyList = categoryService.listAll("0001");
        List<Category> productList = categoryService.listAll("0002");

        if(null!=companyList && companyList.size()>0){
            for(Category category : companyList){
                CategoryVO vo = new CategoryVO();
                vo.setId(category.getId());
                vo.setName(category.getName());
                vo.setpId(category.getParentId());
                vo.setType(category.getType());
                vo.setcTime(category.getcTime());
                list.add(vo);
            }
        }

        if(null!=productList && productList.size()>0){
            for(Category category : productList){
                CategoryVO vo = new CategoryVO();
                vo.setId(category.getId());
                vo.setName(category.getName());
                vo.setpId(category.getParentId());
                vo.setType(category.getType());
                vo.setcTime(category.getcTime());
                list.add(vo);
            }
        }

        rs.setData(list);
        return rs;
    }

    @RequestMapping(value = "/queryAllCategory")
    @ResponseBody
    public CategoryTreeResponse queryAllCategory(@RequestParam(value = "type", defaultValue = "0") int type) {
        CategoryTreeResponse rs = new CategoryTreeResponse();
        rs.setStatus(true);
        rs.setMsg("ok");

        List<Category> list = categoryService.listByParent("0001");

        if(list!=null && list.size()>0){
            List<CategoryTreeVO> volist = new ArrayList<>();
            for(Category category : list){
                CategoryTreeVO vo = new CategoryTreeVO();
                vo.setChecked(false);
                vo.setId(category.getId());
                vo.setMenuName(category.getName());
                vo.setParentId(category.getParentId());
                vo.setType(category.getType());
                volist.add(vo);
            }
            rs.setData(volist);
        }
        return rs;
    }

    @RequestMapping(value = "/listByParent")
    @ResponseBody
    public CategoryTreeResponse listByParent(@RequestParam(value = "categoryId", defaultValue = "0") String categoryId) {
        CategoryTreeResponse rs = new CategoryTreeResponse();
        rs.setStatus(true);
        rs.setMsg("ok");

        List<Category> list = categoryService.listByParent(categoryId);

        if(list!=null && list.size()>0){
            List<CategoryTreeVO> volist = new ArrayList<>();
            for(Category category : list){
                CategoryTreeVO vo = new CategoryTreeVO();
                vo.setChecked(false);
                vo.setId(category.getId());
                vo.setMenuName(category.getName());
                vo.setParentId(category.getParentId());
                vo.setType(category.getType());
                volist.add(vo);
            }
            rs.setData(volist);
        }
        return rs;
    }
}
