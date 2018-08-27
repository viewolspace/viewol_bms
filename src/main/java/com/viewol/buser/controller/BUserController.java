package com.viewol.buser.controller;

import com.viewol.buser.vo.BUserVO;
import com.viewol.common.GridBaseResponse;
import com.viewol.pojo.BUser;
import com.viewol.pojo.query.BUserQuery;
import com.viewol.service.IBUserService;
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
 * 业务员查询
 */
@Controller
@RequestMapping("buser")
public class BUserController {

    @Resource
    private IBUserService bUserService;

    /**
     * 查询业务员列表
     * @param userName 业务员姓名
     * @param phone 手机号
     * @param companyId 公司ID
     * @param status 状态
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/bUserList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse bUserList(@RequestParam(value = "userName", defaultValue = "") String userName,
                                      @RequestParam(value = "phone", defaultValue = "") String phone,
                                      @RequestParam(value = "companyId", defaultValue = "0") int companyId,
                                      @RequestParam(value = "status", defaultValue = "0") int status,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        BUserQuery query = new BUserQuery();
        query.setPageIndex(page);
        query.setPageSize(limit);
        query.setCompanyId(companyId);
        query.setPhone(phone);
        if(status!=999){
            query.setStatus(status);
        }

        query.setUserName(userName);

        PageHolder<BUser> pageHolder = bUserService.queryBUser(query);
        List<BUserVO> list = new ArrayList<>();

        if (null != pageHolder && pageHolder.getList().size() > 0) {
            for (BUser bUser : pageHolder.getList()) {
                BUserVO vo = new BUserVO();
                vo.setUserId(bUser.getUserId());
                vo.setUserName(bUser.getUserName());
                vo.setPhone(bUser.getPhone());
                vo.setCompanyId(bUser.getCompanyId());
                vo.setStatus(bUser.getStatus());
                vo.setPosition(bUser.getPosition());
                vo.setHeadImgUrl(bUser.getHeadImgUrl());
                vo.setCompanyName("");
                vo.setcTime(bUser.getcTime());
                vo.setmTime(bUser.getmTime());
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
