package com.cnswan.juggle.activity.news.top;


import com.cnswan.juggle.bean.topnews.TopNewsBean;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.module.internal.TopNewsModel;

import rx.Subscription;

public class TopNewsPresenter implements TopNewsContract.Presenter {
    private TopNewsContract.View mView;
    private TopNewsModel mModel = new TopNewsModel();

    private RequestImpl mRequest;

    public TopNewsPresenter(TopNewsContract.View view) {
        mView = view;
        mRequest = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                //NOTE:所有的view中load的参数都是对应的List<item>
                mView.load(((TopNewsBean) object).getResult().getData());
            }

            @Override
            public void loadFailed() {
                mView.showErrorView();
            }

            @Override
            public void loadComplete() {
                mView.showNormalView();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                mView.getFragment().addSubscription(subscription);
            }
        };
    }

    @Override
    public void getTopNewsFromNet(Long lastTime) {
        //延时时间为10分钟吧....
        if (System.currentTimeMillis() - lastTime < 1000 * 60 * 10) {
            mView.sendDelay();
        } else {
            mModel.getTopNews(mRequest);
        }
    }

    @Override
    public void getTopNewsFromCache(String cacheName) {

    }

    @Override
    public void start() {
        //如果是start的话,直接获取,不判断时间;
        mModel.getTopNews(mRequest);
    }
}
