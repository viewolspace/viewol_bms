package com.viewol.data.controller;

import com.viewol.data.response.SelectListResponse;
import com.viewol.data.vo.Option;
import com.viewol.pojo.Category;
import com.viewol.service.ICategoryService;
import com.viewol.shiro.token.TokenManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/12/15.
 */
@Controller
@RequestMapping("dictionary")
public class DictionaryController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 加载分类下拉框数据
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/listDataDic")
    @ResponseBody
    public SelectListResponse listDataDic(@RequestParam(value = "parentId", defaultValue = "0") String parentId) {
        SelectListResponse rs = new SelectListResponse();
        rs.setStatus(true);
        rs.setMsg("ok");

        Integer expoId = TokenManager.getExpoId();
        String pid = parentId;
        if (null != expoId && expoId == 1) {
            //安防展
            if ("0001".equals(parentId)) {//查询展商分类
                pid = "0001";
            } else {//查询展品分类
                pid = "0002";
            }
        } else {
            //消防展
            if ("0001".equals(parentId)) {//查询展商分类
                pid = "0003";
            } else {//查询展品分类
                pid = "0004";
            }
        }
        List<Category> list = categoryService.listByParent(pid);
        if (list != null && list.size() > 0) {
            List<Option> optionList = new ArrayList<>();
            for (Category category : list) {
                Option option = new Option();
                option.setKey(category.getId());
                option.setValue(category.getName());
                optionList.add(option);
            }
            rs.setData(optionList);
        }
        return rs;
    }

}
