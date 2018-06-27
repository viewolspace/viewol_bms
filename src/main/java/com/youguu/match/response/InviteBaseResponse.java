package com.youguu.match.response;

import com.youguu.common.BaseResponse;

public class InviteBaseResponse extends BaseResponse {
    private String inviteCode;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
