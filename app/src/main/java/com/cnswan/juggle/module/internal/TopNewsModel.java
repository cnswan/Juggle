package com.cnswan.juggle.module.internal;

import android.util.Log;

import com.zx.freetime.App;
import com.zx.freetime.bean.technews.AndroidNewsBean;
import com.zx.freetime.bean.topnews.TopNewsBean;
import com.zx.freetime.http.HttpUtils;
import com.zx.freetime.http.RequestImpl;
import com.zx.freetime.utils.ACache;
import com.zx.freetime.utils.Constants;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 * TODO:处理逻辑是:每次把最新的一组新闻设置到ACache中,但是在此时的界面时,内存中保存着多次请求的对象,保存多少呢???
 */

public class TopNewsModel {
    private ACache mCache;

    public TopNewsModel() {
        mCache = ACache.get(App.getInstance());
    }

    public void setData() {

    }

    //NOTE:这个API是没有提供index page的,所以只能通过下拉刷新,不能上拉加载更多;
    public void getTopNews(final RequestImpl request) {
        Subscription subscription = HttpUtils.getInstance().getTopNewClient()
                .getTopNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TopNewsBean>() {
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
                    public void onNext(TopNewsBean topNewsBean) {
                        System.out.println("----------------");
                        System.out.println(topNewsBean);
                        System.out.println("----------------");
                        mCache.put(Constants.TOP_NEWS_ALL, topNewsBean);
                        //执行到这一步是在UI线程;
                        request.loadSuccess(topNewsBean);
                    }
                });
        request.addSubscription(subscription);
    }

    /***
     * 从缓存中获取新闻,这是在没有网络的情况下执行的,其实也很无奈,毕竟后台不是咱自己写的,所以只能用这种逻辑来处理了;
     *
     * @param name
     */
    public void getTopNewsFromCache(String name, final RequestImpl request) {
        Log.e("###", "执行getTopNewsFromCache");


        AndroidNewsBean bean = (AndroidNewsBean) mCache.getAsObject(Constants.TOP_NEWS_ALL);
        if (bean != null) {
            request.loadSuccess(bean);
            request.loadComplete();
        } else {
            request.loadFailed();
        }
    }
}
