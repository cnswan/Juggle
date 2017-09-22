package com.cnswan.juggle.module.http;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangxin 2016/10/28.
 * <p>
 * Description :
 * NOTE:感觉API的获取有点问题....
 */

public class HttpUtils {
    private static final String API_ANDROIDNEWS = "http://gank.io/api/data/Android/";
    private static final String API_PICTURE = "http://gank.io/api/data/福利/";
    private static final String API_MOVIEW = "https://api.douban.com/";
    private static final String API_TOP = "http://v.juhe.cn/";
    private static final String API_CHAT = "http://op.juhe.cn/";
    private static final String API_HISTORY = "http://history.lifetime.photo:81/";

    private final static String API_MOVIEW_DETAILE = "https://api.douban.com";


    private static Api AndroidNewClient;
    private static Api PictureClient;
    private static Api MovieClient;
    private static Api TopNewsClient;
    private static Api ChatClient;
    private static Api HistoryClient;
    private static Api MovieDetailClient;

    private static HttpUtils sHttpUtils;  //用一个单利吧...


    private Context context;


    private HttpUtils() {
    }

    //单利模式,提供http请求的同意入口;
    public static HttpUtils getInstance() {
        if (sHttpUtils == null) {
            synchronized (HttpUtils.class) {
                if (sHttpUtils == null) {
                    sHttpUtils = new HttpUtils();
                }
            }
        }
        return sHttpUtils;
    }

    public Api getAndroidNewClient() {
        if (AndroidNewClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_ANDROIDNEWS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            AndroidNewClient = retrofit.create(Api.class);
        }

        return AndroidNewClient;
    }

    public Api getPictureClient() {
        if (PictureClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_PICTURE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            PictureClient = retrofit.create(Api.class);
        }
        return PictureClient;
    }

    public Api getMovieClient() {
        if (MovieClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_MOVIEW)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            MovieClient = retrofit.create(Api.class);
        }
        return MovieClient;
    }


    public Api getTopNewClient() {
        if (TopNewsClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_TOP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            TopNewsClient = retrofit.create(Api.class);
        }
        return TopNewsClient;
    }


    public Api getChatClient() {
        if (ChatClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_CHAT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            ChatClient = retrofit.create(Api.class);
        }
        return ChatClient;
    }


    public Api getHistoryClient() {
        if (HistoryClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_HISTORY)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            HistoryClient = retrofit.create(Api.class);
        }
        return HistoryClient;
    }

    public Api getMovieDetailClient() {
        if (MovieDetailClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_MOVIEW_DETAILE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            MovieDetailClient = retrofit.create(Api.class);
        }
        return MovieDetailClient;
    }


    public void setContext(Context context) {
        this.context = context;
    }
}
