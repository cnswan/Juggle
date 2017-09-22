package com.cnswan.juggle.bean.topnews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 */

public class TopNewsBean implements Serializable{
    private String reason;

    private TopNewsResult result;

    private Integer errorCode;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TopNewsResult getResult() {
        return result;
    }

    public void setResult(TopNewsResult result) {
        this.result = result;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }


}
