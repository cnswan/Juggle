package com.cnswan.juggle.bean.historytoday;

import java.io.Serializable;

/**
 * Created by zhangxin on 2017/3/27 0027.
 * <p>
 * Description :
 */

public class ResultBean implements Serializable {

    private String id;

    private java.util.List<ResultItem> lists = null;

    private String name;  //分为三种:大记事/出生/死亡;对应的id分别为 1 2 3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.util.List<ResultItem> getLists() {
        return lists;
    }

    public void setLists(java.util.List<ResultItem> lists) {
        this.lists = lists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
