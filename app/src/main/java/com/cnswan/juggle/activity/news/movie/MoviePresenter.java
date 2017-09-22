package com.cnswan.juggle.activity.news.movie;


import android.util.Log;

import com.zx.freetime.bean.movie.HotMovieBean;
import com.zx.freetime.http.RequestImpl;
import com.zx.freetime.model.MovieModel;

import rx.Subscription;

/**
 * Created by zhangxin on 2016/11/26.
 * <p>
 * Description :
 */
public class MoviePresenter implements MovieContract.Presenter{

    private MovieContract.View mView;
    private MovieModel mModel = new MovieModel();

    MoviePresenter(MovieContract.View view) {
        mView = view;
    }
    @Override
    public void getMovie() {
        mModel.getMovie(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                Log.e("###","加载电影成功...");
                mView.load(((HotMovieBean)object).getSubjects());
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
        });
    }

    @Override
    public void start() {
        getMovie();
    }
}
