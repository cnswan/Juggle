package com.cnswan.juggle.bean.movie;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxin on on 2016/11/25.
 * <p>
 * Description :
 */

public class SubjectsBean implements Serializable {
    /**
     * rating : {"max":10,"average":6.9,"stars":"35","min":0}
     * genres : ["剧情","喜剧"]
     * title : 我不是潘金莲
     * casts : [{"alt":"https://movie.douban.com/celebrity/1050059/","avatars":{"small":"https://img3.doubanio
     * .com/img/celebrity/small/1691.jpg","large":"https://img3.doubanio.com/img/celebrity/large/1691.jpg",
     * "medium":"https://img3.doubanio.com/img/celebrity/medium/1691.jpg"},"name":"范冰冰","id":"1050059"},
     * {"alt":"https://movie.douban.com/celebrity/1274274/","avatars":{"small":"https://img3.doubanio
     * .com/img/celebrity/small/39703.jpg","large":"https://img3.doubanio.com/img/celebrity/large/39703.jpg",
     * "medium":"https://img3.doubanio.com/img/celebrity/medium/39703.jpg"},"name":"郭涛","id":"1274274"},
     * {"alt":"https://movie.douban.com/celebrity/1324043/","avatars":{"small":"https://img3.doubanio
     * .com/img/celebrity/small/58870.jpg","large":"https://img3.doubanio.com/img/celebrity/large/58870.jpg",
     * "medium":"https://img3.doubanio.com/img/celebrity/medium/58870.jpg"},"name":"大鹏","id":"1324043"}]
     * （多少人评分）collect_count : 56325
     * （原名）original_title : 我不是潘金莲
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1274255/","avatars":{"small":"https://img1.doubanio
     * .com/img/celebrity/small/45667.jpg","large":"https://img1.doubanio.com/img/celebrity/large/45667.jpg",
     * "medium":"https://img1.doubanio.com/img/celebrity/medium/45667.jpg"},"name":"冯小刚","id":"1274255"}]
     * year : 2016
     * images : {"small":"https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p2378133884.jpg",
     * "large":"https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2378133884.jpg","medium":"https://img3
     * .doubanio.com/view/movie_poster_cover/spst/public/p2378133884.jpg"}
     * （更多信息）alt : https://movie.douban.com/subject/26630781/
     * id : 26630781
     */
    private RatingBean rating;
    private List<String> genres; //电影类型
    private String title;           //电影名字
    private List<PersonBean> directors; //导演
    private int collect_count;        //收藏次数;
    private String original_title;      //电影原名
    private String subtype;             //类型:电影...
    private String year;                //上映时间:2017年
    private ImagesBean images;          //封面:有小中大三个图片
    private String alt;                 //有一个图片是啥
    private String id;                  //id;
    private List<PersonBean> casts;        //演员

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PersonBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<PersonBean> directors) {
        this.directors = directors;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PersonBean> getCasts() {
        return casts;
    }

    public void setCasts(List<PersonBean> casts) {
        this.casts = casts;
    }

    @Override
    public String toString() {
        return "SubjectsBean{" +
                "directors=" + directors +
                ", casts=" + casts +
                ", genres=" + genres +
                ", id='" + id + '\'' +
                ", alt='" + alt + '\'' +
                ", images=" + images +
                ", year='" + year + '\'' +
                ", subtype='" + subtype + '\'' +
                ", original_title='" + original_title + '\'' +
                ", collect_count=" + collect_count +
                ", title='" + title + '\'' +
                ", rating=" + rating +
                '}';
    }
}
