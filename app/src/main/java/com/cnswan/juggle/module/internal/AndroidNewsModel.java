package com.cnswan.juggle.module.internal;

import android.util.Log;

import com.cnswan.juggle.aapp.MApplicationLike;
import com.cnswan.juggle.bean.technews.AndroidNewsBean;
import com.cnswan.juggle.module.http.HttpUtils;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.utils.ACache;
import com.cnswan.juggle.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;


public class AndroidNewsModel {
    private int page = 1;      //当前页面
    private int per_page = 20;  //每页加载20张吧,不提供对外设置的接口了;
    private ACache mCache;

    public AndroidNewsModel() {
        MApplicationLike.get
        mCache = ACache.get(App.getInstance());
    }

    public void setData(String id, int page, int per_page) {
        this.page = page;
        this.per_page = per_page;
    }

    public void getNews(int page, final RequestImpl request) {
        Subscription subscription = HttpUtils.getInstance().getAndroidNewClient()
                .getAndroidNews(page, per_page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AndroidNewsBean>() {
                    @Override
                    public void onCompleted() {
                        request.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("###", e.getMessage());
                        request.loadFailed();
                    }

                    @Override
                    public void onNext(AndroidNewsBean androidNewsBean) {
                        System.out.println("----------------");
                        System.out.println(androidNewsBean);
                        System.out.println("----------------");
                        mCache.put(Constants.TECH_NEWS_ALL, androidNewsBean);
                        //执行到这一步是在UI线程;
                        request.loadSuccess(androidNewsBean);
                    }
                });
        request.addSubscription(subscription);
    }

    /***
     * 从缓存中获取新闻,这是在没有网络的情况下执行的,其实也很无奈,毕竟后台不是咱自己写的,所以只能用这种逻辑来处理了;
     */
    public void getNewsFromCache( final RequestImpl request) {
        Log.e("###", "执行getNewsFromCache");
/*        HttpUtils.getInstance().getAndroidNewClient()
                .getAndroidNews(page, per_page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AndroidNewsBean>() {
                    @Override
                    public void onCompleted() {
                        request.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("###", e.getMessage());
                        request.loadFailed();
                    }

                    @Override
                    public void onNext(AndroidNewsBean androidNewsBean) {
                        System.out.println("----------------");
                        System.out.println(androidNewsBean);
                        System.out.println("----------------");
                        mCache.put(Constants.TECH_NEWS_ALL, androidNewsBean);
                        //执行到这一步是在UI线程;
                        request.loadSuccess(androidNewsBean);
                    }
                });*/


        AndroidNewsBean bean = (AndroidNewsBean) mCache.getAsObject(Constants.TECH_NEWS_ALL);
        if (bean != null) {
            request.loadSuccess(bean);
            request.loadComplete();
        } else {
            request.loadFailed();
        }
    }
}
