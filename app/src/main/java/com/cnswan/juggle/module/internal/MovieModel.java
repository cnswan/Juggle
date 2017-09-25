package com.cnswan.juggle.module.internal;


import com.cnswan.juggle.bean.movie.HotMovieBean;
import com.cnswan.juggle.module.http.HttpUtils;
import com.cnswan.juggle.module.http.RequestImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieModel {
    public void getMovie(final RequestImpl request) {
        HttpUtils.getInstance().getMovieClient().getHotMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HotMovieBean>() {
                    @Override
                    public void accept(HotMovieBean hotMovieBean) throws Exception {
                        //执行到这一步是在UI线程;
                        request.loadSuccess(hotMovieBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        request.loadFailed();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //可以在这里通知加载完成,暂时用不到,如果使用的第三方的RecyclerView的话,可能会有一个刷新完成的通知;
                        request.loadComplete();
                    }
                });
    }
}
