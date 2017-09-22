package com.cnswan.juggle.bean.movie;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxin on 2016/11/25.
 * <p>
 * Description :用来接收豆瓣API返回的电影bean对象,这个bean嵌套有点深啊...
 */

public class HotMovieBean  implements Serializable {

    private int count;
    private int start;
    private int total;
    private String title;
    private List<SubjectsBean> subjects;  //其实最重要的是这一个;

    public int getCount() {
        return count;
    }
    public int getStart() {
        return start;
    }
    public int getTotal() {
        return total;
    }
    public String getTitle() {
        return title;
    }
    public List<SubjectsBean> getSubjects() {
        return subjects;
    }




}