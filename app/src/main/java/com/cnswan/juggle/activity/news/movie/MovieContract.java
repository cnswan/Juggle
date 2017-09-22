package com.cnswan.juggle.activity.news.movie;


import com.cnswan.juggle.amvp.BasePresenter;
import com.cnswan.juggle.amvp.BaseView;
import com.cnswan.juggle.bean.movie.SubjectsBean;

import java.util.List;

/**
 * Created by zhangxin on 2016/11/26.
 * <p>
 * Description :
 */

public interface MovieContract {
    interface View extends BaseView {
        void load(List<SubjectsBean> datas);

        void showErrorView();

        void showNormalView();

        //用来订阅;
        MovieFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getMovie();
    }
}
