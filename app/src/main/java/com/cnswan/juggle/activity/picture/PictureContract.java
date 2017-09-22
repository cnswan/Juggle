package com.cnswan.juggle.activity.picture;


import com.cnswan.juggle.amvp.BasePresenter;
import com.cnswan.juggle.amvp.BaseView;
import com.cnswan.juggle.bean.picture.PictureBean;

import java.util.List;

/**
 * Created by zhangxin on 2016/10/28.
 * <p>
 * Description :
 */

public class PictureContract {
    interface View extends BaseView {
        void load(List<PictureBean.ResultBean> datas);

        void showErrorView();

        void showNormalView();

        //用来订阅;
        PictureFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getPictures(int page);
    }
}
