package com.cnswan.juggle.bean.technews;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxin on 2016/10/28 0028.
 * <p>
 * Description :
 * 用来获取android 新闻的bean对象;
 */

public class AndroidNewsBean implements Serializable {

    private boolean error;
    /**
     * _id : 5832662b421aa929b0f34e99
     * createdAt : 2016-11-21T11:12:43.567Z
     * desc :  深入Android渲染机制
     * publishedAt : 2016-11-24T11:40:53.615Z
     * source : web
     * type : Android
     * url : http://blog.csdn.net/ccj659/article/details/53219288
     * used : true
     * who : Chauncey
     */

    private List<ResultBean> results;

    public static class ResultBean implements Serializable {

        private String _id;
        private String createdAt;
        private String desc;        //重要的还是这个字段,简短的描述;
        private String publishedAt;     //发布时间
        private String source;      //来源.
        private String type;    //android
        private String url;   //详情页面
        private boolean used; //一般都是true吧,我们这里不使用这个字段.
        private String who;  //作者
        private List<String> images;  //文章内部的一些图片资源,你到底想怎么渲染呢???

        public String get_id() {
            return _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public String getSource() {
            return source;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public boolean isUsed() {
            return used;
        }

        public String getWho() {
            return who;
        }

        @Override
        public String toString() {
            return "ResultsBean{" +
                    "who='" + who + '\'' +
                    ", used=" + used +
                    ", url='" + url + '\'' +
                    ", type='" + type + '\'' +
                    ", source='" + source + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", _id='" + _id + '\'' +
                    '}';
        }

        public List<String> getImages() {
            return images;
        }
    }

    public boolean isError() {
        return error;
    }

    public List<ResultBean> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "GankIoDataBean{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
