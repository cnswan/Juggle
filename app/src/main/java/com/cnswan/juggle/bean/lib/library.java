package com.cnswan.juggle.bean.lib;

import com.cnswan.juggle.bean.BaseBean;

/**
 * Created by 00013259 on 2017/9/30.
 */

public class Library extends BaseBean {

    private String name;

    public Library(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}