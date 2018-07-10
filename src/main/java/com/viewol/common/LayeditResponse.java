package com.viewol.common;

import java.util.Map;

public class LayeditResponse {
    private int code;//0表示成功，1失败
    private String msg;//提示消息
    private Map<String, String> data;//图片信息

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
