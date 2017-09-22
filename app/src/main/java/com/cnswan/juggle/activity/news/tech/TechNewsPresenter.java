package com.cnswan.juggle.activity.news.tech;


import com.cnswan.juggle.bean.technews.AndroidNewsBean;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.module.internal.AndroidNewsModel;
import com.cnswan.juggle.utils.NetWorkUtil;

import rx.Subscription;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class TechNewsPresenter implements TechNewsContract.Presenter {
    private TechNewsContract.View mView;
    private AndroidNewsModel mModel = new AndroidNewsModel();

    private RequestImpl mRequest;

    public TechNewsPresenter(TechNewsContract.View view) {
        mView = view;
        mRequest = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mView.load(((AndroidNewsBean) object).getResults());
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
    public void start() {
        if (NetWorkUtil.isNetworkConnected(App.getInstance())) {
            //TODO: 硬编码了,这里强制获取第一页最新的;
            getTechNewsFromNet(1);
        } else {
            getTechNewsFromCache();
        }
    }

    @Override
    public void getTechNewsFromNet(int page) {
        if (page < 1) {
            page = 1;
        }
        mModel.getNews(page, mRequest);
    }

    @Override
    public void getTechNewsFromCache() {
        mModel.getNewsFromCache(mRequest);

    }
}
