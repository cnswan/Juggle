package com.cnswan.juggle.bean.topnews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 */

public class TopNewsResult implements Serializable {
    private String stat;

    private List<TopNewsItem> data = null;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<TopNewsItem> getData() {
        return data;
    }

    public void setData(List<TopNewsItem> data) {
        this.data = data;
    }
}
