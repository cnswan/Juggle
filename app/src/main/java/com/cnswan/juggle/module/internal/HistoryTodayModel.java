package com.cnswan.juggle.module.internal;


import com.cnswan.juggle.bean.historytoday.HistoryTodayBean;
import com.cnswan.juggle.module.http.HttpUtils;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.utils.ACache;
import com.cnswan.juggle.utils.Constants;
import com.cnswan.juggle.utils.TimeUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by zhangxin on 2017/3/27 0027.
 * <p>
 * Description :
 */

public class HistoryTodayModel {
    private ACache mCache;

    public HistoryTodayModel() {
        mCache = ACache.get(App.getInstance());
    }

    public void getHistory(final RequestImpl request) {
        System.out.println("开始获取历史今天消息");
        HttpUtils.getInstance().getHistoryClient()
                .getHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HistoryTodayBean>() {
                    @Override
                    public void onCompleted() {
                        request.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("获取历史今天数据失败...");
                        e.printStackTrace();
                        request.loadFailed();
                    }

                    @Override
                    public void onNext(HistoryTodayBean historyTodayBean) {
                        System.out.println("历史消息加载成功");
                        mCache.put(Constants.HISTORY_ALL, historyTodayBean);
                        mCache.put(Constants.TODAY, TimeUtil.getTodayTimeStamp());  //将当天的年月日插入进去;
                        request.loadSuccess(historyTodayBean);
                    }
                });
    }

    public void getHistoryFromCache(final RequestImpl request) {
        HistoryTodayBean historyTodayBean = (HistoryTodayBean) mCache.getAsObject(Constants.HISTORY_ALL);
        if (historyTodayBean != null) {
            request.loadSuccess(historyTodayBean);
            request.loadComplete();
        } else {
            System.out.println("缓存获取失败");
            request.loadFailed();
        }
    }
}