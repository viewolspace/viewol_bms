package com.youguu.match.controller;

import com.youguu.common.GridBaseResponse;
import com.youguu.core.util.ClassCast;
import com.youguu.core.util.PageHolder;
import com.youguu.match.vo.RankVO;
import com.youguu.member.client.service.MemberRpcService;
import com.youguu.member.common.pojo.CloudUserBind;
import com.youguu.shiro.token.TokenManager;
import com.youguu.simtrade.rpcservice.client.TradeRpcService;
import com.youguu.simtrade.rpcservice.client.TradeRpcServiceFactory;
import com.youguu.simtrade.rpcservice.common.Constants;
import com.youguu.simtrade.rpcservice.common.pojo.Rank;
import com.youguu.simtrade.rpcservice.common.pojo.RankNew;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("rank")
public class RankController {

    private static TradeRpcService rankService = TradeRpcServiceFactory.getTradeRpcService();
    @Resource
    private MemberRpcService asyncMemberRpcService;

    @RequestMapping(value = "/total", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse totalList(@RequestParam(value = "matchId", defaultValue = "-1") int matchId,
                                      @RequestParam(value = "thirdId", defaultValue = "") String thirdId,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        CloudUserBind cloudUserBind = asyncMemberRpcService.findCloudUserBind(thirdId, TokenManager.getAppId());
        int userId = 0;
        if(null != cloudUserBind){
            userId = cloudUserBind.getYouguuId();
        }
        PageHolder<Rank> pageHolder =  rankService.listRankByType(matchId, Constants.RANK_TYPE_TOTAL, userId, page, limit);

        List<Integer> uidList = new ArrayList<>();
        if(null != pageHolder && null != pageHolder.getList()){
            for(Rank rank : pageHolder.getList()){
                uidList.add(rank.getUserId());
            }
        }

        Map<Integer, String> map = this.getThirdIdMap(uidList);

        if(null != pageHolder && null != pageHolder.getList()){
            List<RankVO> voList = new ArrayList<>();
            for(Rank rank : pageHolder.getList()){
                RankVO vo = ClassCast.cast(rank, RankVO.class);
                vo.setThirdId(map.get(rank.getUserId()));
                voList.add(vo);
            }

            rs.setData(voList);
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    private Map<Integer, String> getThirdIdMap(List<Integer> uidList){
        List<CloudUserBind> bindList = asyncMemberRpcService.queryCloudUserBindByids(uidList);
        Map<Integer, String> map = new HashMap<>();
        if(null != bindList && bindList.size()>0){
            for(CloudUserBind bind : bindList){
                map.put(bind.getYouguuId(), bind.getThirdId());
            }
        }

        return map;
    }


    @RequestMapping(value = "/month", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse monthList(@RequestParam(value = "matchId", defaultValue = "-1") int matchId,
                                      @RequestParam(value = "thirdId", defaultValue = "") String thirdId,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        CloudUserBind cloudUserBind = asyncMemberRpcService.findCloudUserBind(thirdId, TokenManager.getAppId());
        int userId = 0;
        if(null != cloudUserBind){
            userId = cloudUserBind.getYouguuId();
        }
        PageHolder<Rank> pageHolder =  rankService.listRankByType(matchId, Constants.RANK_TYPE_MONTH, userId, page, limit);

        List<Integer> uidList = new ArrayList<>();
        if(null != pageHolder && null != pageHolder.getList()){
            for(Rank rank : pageHolder.getList()){
                uidList.add(rank.getUserId());
            }
        }

        Map<Integer, String> map = this.getThirdIdMap(uidList);

        if(null != pageHolder && null != pageHolder.getList()){
            List<RankVO> voList = new ArrayList<>();
            for(Rank rank : pageHolder.getList()){
                RankVO vo = ClassCast.cast(rank, RankVO.class);
                vo.setThirdId(map.get(rank.getUserId()));
                voList.add(vo);
            }

            rs.setData(voList);
            rs.setCount(pageHolder.getTotalCount());
        }
        return rs;
    }


    @RequestMapping(value = "/week", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse weekList(@RequestParam(value = "matchId", defaultValue = "-1") int matchId,
                                     @RequestParam(value = "thirdId", defaultValue = "") String thirdId,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        CloudUserBind cloudUserBind = asyncMemberRpcService.findCloudUserBind(thirdId, TokenManager.getAppId());
        int userId = 0;
        if(null != cloudUserBind){
            userId = cloudUserBind.getYouguuId();
        }
        PageHolder<Rank> pageHolder =  rankService.listRankByType(matchId, Constants.RANK_TYPE_WEEK, userId, page, limit);

        List<Integer> uidList = new ArrayList<>();
        if(null != pageHolder && null != pageHolder.getList()){
            for(Rank rank : pageHolder.getList()){
                uidList.add(rank.getUserId());
            }
        }

        Map<Integer, String> map = this.getThirdIdMap(uidList);

        if(null != pageHolder && null != pageHolder.getList()){
            List<RankVO> voList = new ArrayList<>();
            for(Rank rank : pageHolder.getList()){
                RankVO vo = ClassCast.cast(rank, RankVO.class);
                vo.setThirdId(map.get(rank.getUserId()));
                voList.add(vo);
            }

            rs.setData(voList);
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }
}
