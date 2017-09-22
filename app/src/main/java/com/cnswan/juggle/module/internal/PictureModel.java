package com.cnswan.juggle.module.internal;



import com.zx.freetime.bean.picture.PictureBean;
import com.zx.freetime.http.HttpUtils;
import com.zx.freetime.http.RequestImpl;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PictureModel {
    private int page = 1;      //当前页面
    private int per_page = 20;  //每页加载20张吧,不提供对外设置的接口了;

    public void setData(String id, int page, int per_page) {
        this.page = page;
        this.per_page = per_page;
    }


    public void getPicture(int page, final RequestImpl request) {
        HttpUtils.getInstance().getPictureClient()
                .getPicture(page, per_page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PictureBean>() {
                    @Override
                    public void onCompleted() {
                        //可以在这里通知加载完成,暂时用不到,如果使用的第三方的RecyclerView的话,可能会有一个刷新完成的通知;
                        request.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        request.loadFailed();
                    }

                    @Override
                    public void onNext(PictureBean pictureBean) {
                        //执行到这一步是在UI线程;
                        request.loadSuccess(pictureBean);
                    }
                });
    }
}
