package com.cnswan.juggle.bean.historytoday;

import java.io.Serializable;

/**
 * Created by zhangxin on 2017/3/27 0027.
 * <p>
 * Description :
 */

public class ResultItem implements Serializable {
    private String year;

    private String title;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "" + year + ":" + title;
    }
}
