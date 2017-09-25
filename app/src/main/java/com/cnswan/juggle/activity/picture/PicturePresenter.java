package com.cnswan.juggle.activity.picture;

import com.cnswan.juggle.bean.picture.PictureBean;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.module.internal.PictureModel;

import io.reactivex.disposables.Disposable;

public class PicturePresenter implements PictureContract.Presenter {


    private PictureContract.View mView;
    private PictureModel mModel = new PictureModel();

    PicturePresenter(PictureContract.View view) {
        mView = view;
    }

    /*@Override
    public void getGirls(final int page, int size, final boolean isRefresh) {
        mModel.setData("福利", page, size);
        mModel.getGankIoData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                PictureBean pictureBean = (PictureBean) object;
                if (isRefresh) {
                    mView.refresh(pictureBean.getResults());
                } else {
                    mView.load(pictureBean.getResults());
                }
                mView.showNormal();
            }

            @Override
            public void loadFailed() {

            }

            @Override
            public void addSubscription(Subscription subscription) {
//                PictureActivity.addSubscription(subscription);
            }
        });
    }*/

    //开始的时候就加载数据,获取最新的数据;
    @Override
    public void start() {
        getPictures(1);
    }

    @Override
    public void getPictures(int page) {
        if (page < 1) {
            page = 1;
        }
        mModel.getPicture(page, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                //此时已经拿到了url数据了,添加到list中;
                mView.load(((PictureBean) object).getResults());
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
            public void addSubscription(Disposable disposable) {
                mView.getFragment().addSubscription(disposable);
            }
        });
    }
}
