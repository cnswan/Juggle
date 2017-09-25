package com.cnswan.juggle.module.internal;

import android.util.Log;

import com.cnswan.juggle.aapp.AppContext;
import com.cnswan.juggle.bean.technews.AndroidNewsBean;
import com.cnswan.juggle.bean.topnews.TopNewsBean;
import com.cnswan.juggle.module.http.HttpUtils;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.utils.ACache;
import com.cnswan.juggle.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO:处理逻辑是:每次把最新的一组新闻设置到ACache中,但是在此时的界面时,内存中保存着多次请求的对象,保存多少呢???
 */

public class TopNewsModel {
    private ACache mCache;

    public TopNewsModel() {
        mCache = ACache.get(AppContext.context);
    }

    public void setData() {

    }

    //NOTE:这个API是没有提供index page的,所以只能通过下拉刷新,不能上拉加载更多;
    public void getTopNews(final RequestImpl request) {
        Disposable disposable = HttpUtils.getInstance().getTopNewClient()
                .getTopNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TopNewsBean>() {
                    @Override
                    public void accept(TopNewsBean topNewsBean) throws Exception {
                        System.out.println("----------------");
                        System.out.println(topNewsBean);
                        System.out.println("----------------");
                        mCache.put(Constants.TOP_NEWS_ALL, topNewsBean);
                        //执行到这一步是在UI线程;
                        request.loadSuccess(topNewsBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        request.loadFailed();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        request.loadComplete();
                    }
                });
        request.addSubscription(disposable);
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
