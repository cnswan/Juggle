package com.cnswan.juggle.activity.news.tech;


import com.cnswan.juggle.amvp.BasePresenter;
import com.cnswan.juggle.amvp.BaseView;
import com.cnswan.juggle.bean.technews.AndroidNewsBean;

import java.util.List;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class TechNewsContract {
    interface View extends BaseView {
        void load(List<AndroidNewsBean.ResultBean> datas);

        void showErrorView();

        void showNormalView();

        //用来订阅;
        TechNewsFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getTechNewsFromNet(int page);
        void getTechNewsFromCache();
    }
}
