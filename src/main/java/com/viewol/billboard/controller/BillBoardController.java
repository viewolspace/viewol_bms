package com.viewol.billboard.controller;

import com.viewol.common.GridBaseResponse;
import com.viewol.pojo.AdMedia;
import com.viewol.pojo.query.AdMediaQuery;
import com.viewol.service.IAdMediaService;
import com.youguu.core.util.PageHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("billboard")
public class BillBoardController {
    @Resource
    private IAdMediaService adMediaService;

    @RequestMapping(value = "/billboardList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse billboardList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        AdMediaQuery adMediaQuery = new AdMediaQuery();
        adMediaQuery.setPageIndex(page);
        adMediaQuery.setPageSize(limit);

        PageHolder<AdMedia> pageHolder = adMediaService.queryAdMedia(adMediaQuery);

        rs.setData(pageHolder.getList());
        rs.setCount(pageHolder.getTotalCount());

        return rs;
    }

}
