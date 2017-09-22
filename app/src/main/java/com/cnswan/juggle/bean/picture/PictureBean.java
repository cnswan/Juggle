package com.cnswan.juggle.bean.picture;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxin on 2016/10/28.
 * <p>
 * Description :
 */

public class PictureBean implements Serializable {
    //总体返回的就一个json对象,分为error,表明是否有错误;
    private boolean error;

    //第二个对象就是一个ResultBean对象,是一个列表,我们需要的url就在这个list中;
    private List<ResultBean> results;

    public static class ResultBean implements Serializable {

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images; //没有这个image吧...

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




        public ResultBean() {
        }

        //进行反序列化
        protected ResultBean(Parcel in) {
            this._id = in.readString();
            this.createdAt = in.readString();
            this.desc = in.readString();
            this.publishedAt = in.readString();
            this.source = in.readString();
            this.type = in.readString();
            this.url = in.readString();
            this.used = in.readByte() != 0;
            this.who = in.readString();
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
        return "PictureBean{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
