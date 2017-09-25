package com.cnswan.juggle.activity.HistoryToday;

import com.cnswan.juggle.aapp.AppContext;
import com.cnswan.juggle.bean.historytoday.HistoryTodayBean;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.module.internal.HistoryTodayModel;
import com.cnswan.juggle.utils.ACache;
import com.cnswan.juggle.utils.Constants;
import com.cnswan.juggle.utils.TimeUtil;

import io.reactivex.disposables.Disposable;

public class HistoryTodayPresenter implements HistoryTodayContract.Presenter {

    private HistoryTodayContract.View mView;
    private HistoryTodayModel mModel = new HistoryTodayModel();
    private RequestImpl mRequest;
    public  Disposable  historyDisposable;

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
            public void addSubscription(Disposable disposable) {
                historyDisposable = disposable;
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
        if (historyDisposable != null) {
            historyDisposable.dispose();
        }
    }

    @Override
    public void start() {
        String today = ACache.get(AppContext.context).getAsString(Constants.TODAY);
        System.out.println("today:" + today);
        if (TimeUtil.getTodayTimeStamp().equals(today)) {
            getHistoryFromCache();
        } else {
            getHistoryFromNet();
        }
    }
}
