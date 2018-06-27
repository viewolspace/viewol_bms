package com.youguu.match.controller;

import com.alibaba.fastjson.JSONObject;
import com.youguu.common.BaseResponse;
import com.youguu.common.GridBaseResponse;
import com.youguu.core.util.PageHolder;
import com.youguu.match.response.InviteBaseResponse;
import com.youguu.member.client.service.MemberRpcService;
import com.youguu.member.common.pojo.CloudUserBind;
import com.youguu.mncg.match.pojo.MatchQuery;
import com.youguu.mncg.match.pojo.MncgMatch;
import com.youguu.mncg.match.service.MncgMatchService;
import com.youguu.mncg.match.service.MncgMatchV2Service;
import com.youguu.shiro.token.TokenManager;
import com.youguu.simtrade.rpcservice.client.TradeRpcServiceFactory;
import com.youguu.simtrade.rpcservice.common.pojo.Conclude;
import com.youguu.sys.interceptor.Repeat;
import com.youguu.sys.log.annotation.MethodLog;
import com.youguu.sys.response.RoleComboResponse;
import com.youguu.sys.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("match")
public class MatchController {

    @Resource
    private MncgMatchV2Service mncgMatchV2Service;
    @Resource
    private MncgMatchService mncgMatchService;
    @Resource
    private MemberRpcService asyncMemberRpcService;

    @RequestMapping(value = "/matchList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse matchList(@RequestParam(value = "appId", defaultValue = "0") int appId,
                                      @RequestParam(value = "matchId", defaultValue = "0") int matchId,
                                      @RequestParam(value = "likeMatchName", defaultValue = "") String likeMatchName,
                                      @RequestParam(value = "type", defaultValue = "") String type,
                                      @RequestParam(value = "currentDate", defaultValue = "") String currentDate,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        if(TokenManager.getAppId() > 0){
            appId = TokenManager.getAppId();
        }

        MatchQuery query = new MatchQuery();
        query.setMatchId(matchId);
        query.setLikeMatchName(likeMatchName);
        query.setType(type);
        query.setCurrentDate(currentDate);
        PageHolder<MncgMatch> pageHolder = mncgMatchV2Service.queryMncgMatchListByAppid(appId, page, limit, query);
        if(null != pageHolder.getList()){
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    /**
     * 创建比赛
     * @param matchName 比赛名称
     * @param initFund 初始资金
     * @param yongj 佣金
     * @param yinhs 印花税
     * @param openTime 开赛时间
     * @param closeTime 结束时间
     * @param des 比赛描述
     * @return
     */
    @RequestMapping(value = "/createMatch", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.MATCH, desc = "创建比赛")
    @Repeat
    public BaseResponse createMatch(@RequestParam(value = "matchName", defaultValue = "") String matchName,
                                    @RequestParam(value = "initFund", defaultValue = "100000") int initFund,
                                    @RequestParam(value = "yongj", defaultValue = "0.0008") String yongj,
                                    @RequestParam(value = "yinhs", defaultValue = "0.001") String yinhs,
                                    @RequestParam(value = "openTime", defaultValue = "") String openTime,
                                    @RequestParam(value = "closeTime", defaultValue = "") String closeTime,
                                    @RequestParam(value = "needapply", defaultValue = "-1") int needapply,
                                    @RequestParam(value = "beginTime", defaultValue = "") String beginTime,
                                    @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                    @RequestParam(value = "des", defaultValue = "") String des,
                                    @RequestParam(value = "detailUrl", defaultValue = "") String detailUrl,
                                    @RequestParam(value = "avatar", defaultValue = "") String matchImage,
                                    @RequestParam(value = "inviteCode", defaultValue = "") String inviteCode) {

        BaseResponse rs = new BaseResponse();

        MncgMatch match = new MncgMatch();
        match.setMatchName(matchName);
        match.setInitFund(initFund);
        match.setYongj(new BigDecimal(yongj));
        match.setYinhs(new BigDecimal(yinhs));
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            match.setOpenTime(dft.parse(openTime));
            match.setCloseTime(dft.parse(closeTime));

            match.setBeginTime(dft.parse(beginTime));
            match.setEndTime(dft.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        match.setWeight(1);
        match.setType(974);
        match.setNeedapply(0);//不需要报名
        match.setNeedcheck(0);//不需要审核
        match.setAuditFlag(1);//默认审核通过
        match.setDes(des);
        match.setDetailUrl(detailUrl);
        if(needapply>=1){
            match.setNeedapply(1);
        }
        match.setLogoImage(matchImage);

        //是否需要邀请码
        if(needapply == 2){
            match.setInviteCode(inviteCode);
        }

        //是否白名单报名
        if(needapply == 3){
            match.setWhiteList(1);
        } else {
            match.setWhiteList(0);
        }


        int appId = TokenManager.getAppId();
        boolean result = mncgMatchV2Service.addMncgMatch(appId, match);
        if(result){
            rs.setStatus(true);
            rs.setMsg("ok");
        } else {
            rs.setStatus(false);
            rs.setMsg("创建比赛失败");
        }
        return rs;
    }

    /**
     * 修改比赛
     * @param matchId
     * @param matchName
     * @param initFund
     * @param yongj
     * @param yinhs
     * @param openTime
     * @param closeTime
     * @param needapply
     * @param beginTime
     * @param endTime
     * @param des
     * @param detailUrl
     * @param matchImage
     * @param inviteCode
     * @param whiteList 0 不是白名单 1 需要白名单
     * @return
     */
    @RequestMapping(value = "/updateMatch", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.MATCH, desc = "修改比赛")
    @Repeat
    public BaseResponse updateMatch(@RequestParam(value = "matchId", defaultValue = "-1") int matchId,
                                    @RequestParam(value = "matchName", defaultValue = "") String matchName,
                                    @RequestParam(value = "initFund", defaultValue = "100000") int initFund,
                                    @RequestParam(value = "yongj", defaultValue = "0.0008") String yongj,
                                    @RequestParam(value = "yinhs", defaultValue = "0.001") String yinhs,
                                    @RequestParam(value = "openTime", defaultValue = "") String openTime,
                                    @RequestParam(value = "closeTime", defaultValue = "") String closeTime,
                                    @RequestParam(value = "needapply", defaultValue = "1") int needapply,
                                    @RequestParam(value = "beginTime", defaultValue = "") String beginTime,
                                    @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                    @RequestParam(value = "des", defaultValue = "") String des,
                                    @RequestParam(value = "detailUrl", defaultValue = "") String detailUrl,
                                    @RequestParam(value = "avatar", defaultValue = "") String matchImage,
                                    @RequestParam(value = "inviteCode", defaultValue = "") String inviteCode) {

        BaseResponse rs = new BaseResponse();

        MncgMatch match = new MncgMatch();
        match.setMatchId(matchId);
        match.setMatchName(matchName);
        match.setInitFund(initFund);
        match.setYongj(new BigDecimal(yongj));
        match.setYinhs(new BigDecimal(yinhs));
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            match.setOpenTime(dft.parse(openTime));
            match.setCloseTime(dft.parse(closeTime));

            match.setBeginTime(dft.parse(beginTime));
            match.setEndTime(dft.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        match.setWeight(1);
        match.setType(974);
        match.setNeedapply(0);//不需要报名
        match.setNeedcheck(0);//不需要审核
        match.setAuditFlag(1);//默认审核通过
        match.setDes(des);
        match.setDetailUrl(detailUrl);

        if(needapply>=1){
            match.setNeedapply(1);
        }
        match.setLogoImage(matchImage);

        //是否需要邀请码
        if(needapply == 2){
            match.setInviteCode(inviteCode);
        }

        //是否白名单报名
        if(needapply == 3){
            match.setWhiteList(1);
        } else {
            match.setWhiteList(0);
        }

        boolean result = mncgMatchV2Service.updateMncgMatch(match);
        if(result){
            rs.setStatus(true);
            rs.setMsg("ok");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改比赛失败");
        }
        return rs;
    }

    @RequestMapping(value = "/makeInveiteCode", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.MATCH, desc = "生成邀请码")
    @Repeat
    public InviteBaseResponse makeInveiteCode(@RequestParam(value = "matchId", defaultValue = "-1") int matchId){
        InviteBaseResponse rs = new InviteBaseResponse();

        try {
            String userName = TokenManager.getUserName();
            String inviteCode = mncgMatchService.makeInviteCode(userName);
            rs.setStatus(true);
            rs.setMsg("ok");
            rs.setInviteCode(inviteCode);
        } catch (Exception e){
            rs.setStatus(false);
            rs.setMsg("生成邀请码失败");
        }

        return rs;
    }


    @RequestMapping(value = "/getMatchSelect")
    @ResponseBody
    public RoleComboResponse getMatchSelect() {

        RoleComboResponse rs = new RoleComboResponse();
        int appId = TokenManager.getAppId();
        PageHolder<MncgMatch> pageHolder = mncgMatchV2Service.queryMncgMatchListByAppid(appId, 1, 10000, new MatchQuery());

        List<MncgMatch> matchList = pageHolder.getList();
        if(null == matchList){
            rs.setStatus(false);
            rs.setMsg("加载比赛下拉框异常");
            return rs;
        }

        List<JSONObject> matchSelect = new ArrayList<>();
        for(MncgMatch match : matchList){
            JSONObject option = new JSONObject();
            option.put("key", match.getMatchId());
            option.put("value", match.getMatchName());
            matchSelect.add(option);
        }

        rs.setStatus(true);
        rs.setMsg("ok");
        rs.setData(matchSelect);
        return rs;
    }


    /**
     * 查询交易记录
     * @param matchId
     * @param thirdId
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/tradeRecordList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse tradeRecordList(@RequestParam(value = "matchId", defaultValue = "0") int matchId,
                                            @RequestParam(value = "thirdId", defaultValue = "0") String thirdId,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        int appId = TokenManager.getAppId();
        CloudUserBind cloudUserBind = asyncMemberRpcService.findCloudUserBind(thirdId, appId);
        int userId = cloudUserBind.getYouguuId();
        List<Conclude>  list = TradeRpcServiceFactory.getTradeRpcService().getConcludeList(userId, matchId, 1, 20, false);

        if(null != list){
            rs.setData(list);
//            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

}
