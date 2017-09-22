package com.cnswan.juggle.activity.HistoryToday;

import android.text.TextUtils;

import com.zx.freetime.App;
import com.zx.freetime.bean.historytoday.HistoryTodayBean;
import com.zx.freetime.bean.historytoday.ResultBean;
import com.zx.freetime.http.RequestImpl;
import com.zx.freetime.model.HistoryTodayModel;
import com.zx.freetime.utils.ACache;
import com.zx.freetime.utils.Constants;
import com.zx.freetime.utils.TimeUtil;

import rx.Subscription;

/**
 * Created by zhangxin on 2017/3/27 0027.
 * <p>
 * Description :
 */

public class HistoryTodayPresenter implements HistoryTodayContract.Presenter {
    private HistoryTodayContract.View mView;
    private HistoryTodayModel mModel = new HistoryTodayModel();
    private RequestImpl mRequest;
    public Subscription historySubscription;

    public HistoryTodayPresenter(HistoryTodayContract.View view) {
        mView = view;
        mRequest = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mView.load(((HistoryTodayBean) object).getRes());
            }

            @Override
            public void loadFailed() {
                mView.showErrorView();
            }

            @Override
            public void loadComplete() {

            }

            @Override
            public void addSubscription(Subscription subscription) {
                historySubscription = subscription;
            }
        };
    }

    @Override
    public void getHistoryFromNet() {
        mModel.getHistory(mRequest);
    }

    @Override
    public void getHistoryFromCache() {
        mModel.getHistoryFromCache(mRequest);
    }

    @Override
    public void unSubscribe() {
        if (historySubscription != null) {
            historySubscription.unsubscribe();
        }
    }

    @Override
    public void start() {
        String today = ACache.get(App.getInstance()).getAsString(Constants.TODAY);
        System.out.println("today:" + today);
        if (TimeUtil.getTodayTimeStamp().equals(today)) {
            getHistoryFromCache();
        } else {
            getHistoryFromNet();
        }
    }
}
