package com.cnswan.juggle.bean.chat;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 */

public class ChatBean {
    private String reason;
    private TalkResult result;
    private Integer error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TalkResult getResult() {
        return result;
    }

    public void setResult(TalkResult result) {
        this.result = result;
    }

    public Integer getErrorCode() {
        return error_code;
    }

    public void setErrorCode(Integer errorCode) {
        this.error_code = errorCode;
    }


    public static class TalkResult {
        private Integer code;
        private String text;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
