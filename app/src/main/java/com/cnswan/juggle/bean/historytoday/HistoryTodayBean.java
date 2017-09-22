package com.cnswan.juggle.bean.historytoday;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxin on 2017/3/27 0027.
 * <p>
 * Description :
 */

public class HistoryTodayBean implements Serializable{

    private List<ResultBean> res = null;

    private Integer month;

    private Integer day;

    public List<ResultBean> getRes() {
        return res;
    }

    public void setRes(List<ResultBean> res) {
        this.res = res;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
