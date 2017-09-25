package com.cnswan.juggle.module.internal;

import com.cnswan.juggle.aapp.AppContext;
import com.cnswan.juggle.bean.historytoday.HistoryTodayBean;
import com.cnswan.juggle.module.http.HttpUtils;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.utils.ACache;
import com.cnswan.juggle.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HistoryTodayModel {
    private ACache mCache;

    public HistoryTodayModel() {
        mCache = ACache.get(AppContext.context);
    }

    public void getHistory(final RequestImpl request) {
        System.out.println("开始获取历史今天消息");
        HttpUtils.getInstance().getHistoryClient()
                .getHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HistoryTodayBean>() {
                    @Override
                    public void accept(HistoryTodayBean historyTodayBean) throws Exception {
                        request.loadSuccess(historyTodayBean);
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
