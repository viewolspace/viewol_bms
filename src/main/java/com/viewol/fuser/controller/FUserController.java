package com.viewol.fuser.controller;

import com.viewol.common.GridBaseResponse;
import com.viewol.fuser.vo.FUserVO;
import com.viewol.pojo.FUser;
import com.viewol.pojo.query.FUserQuery;
import com.viewol.service.IFUserService;
import com.youguu.core.util.PageHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户查询
 */
@Controller
@RequestMapping("fuser")
public class FUserController {

    @Resource
    private IFUserService fUserService;

    /**
     * 查询客户列表
     * @param userName 客户姓名
     * @param phone 客户电话
     * @param company 客户公司
     * @param email 客户邮箱
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/fUserList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse fUserList(@RequestParam(value = "userName", defaultValue = "") String userName,
                                      @RequestParam(value = "phone", defaultValue = "") String phone,
                                      @RequestParam(value = "company", defaultValue = "") String company,
                                      @RequestParam(value = "email", defaultValue = "") String email,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        FUserQuery query = new FUserQuery();
        query.setPageIndex(page);
        query.setPageSize(limit);

        if(!"".equals(company)){
            query.setCompany(company);
        }

        if(!"".equals(phone)){
            query.setPhone(phone);
        }

        if(!"".equals(email)){
            query.setEmail(email);
        }

        if(!"".equals(userName)){
            query.setUserName(userName);
        }


        PageHolder<FUser> pageHolder = fUserService.querFUser(query);
        List<FUserVO> list = new ArrayList<>();

        if (null != pageHolder && pageHolder.getList().size() > 0) {
            for (FUser fUser : pageHolder.getList()) {
                FUserVO vo = new FUserVO();
                vo.setUserId(fUser.getUserId());
                vo.setUserName(fUser.getUserName());
                vo.setPhone(fUser.getPhone());
                vo.setCompany(fUser.getCompany());
                vo.setPosition(fUser.getPosition());
                vo.setEmail(fUser.getEmail());
                vo.setAge(fUser.getAge());
                vo.setcTime(fUser.getcTime());
                vo.setmTime(fUser.getmTime());
                vo.setCompanyId(fUser.getCompanyId());
                vo.setCompanyName("");
                vo.setHeadImgUrl(fUser.getHeadImgUrl());

                list.add(vo);
            }
        }

        if (null != pageHolder) {
            rs.setData(list);
            rs.setCount(pageHolder.getTotalCount());
        }
        return rs;
    }

}
