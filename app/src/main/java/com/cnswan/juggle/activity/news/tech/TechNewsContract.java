package com.cnswan.juggle.activity.news.tech;

import com.zx.freetime.base.BasePresenter;
import com.zx.freetime.base.BaseView;
import com.zx.freetime.bean.technews.AndroidNewsBean;

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
