package com.cnswan.juggle.activity.HistoryToday;


import com.cnswan.juggle.amvp.BasePresenter;
import com.cnswan.juggle.amvp.BaseView;
import com.cnswan.juggle.bean.historytoday.ResultBean;

import java.util.List;


/**
 * Created by zhangxin on 2017/3/27 0027.
 * <p>
 * Description :
 */

public class HistoryTodayContract {
    interface View extends BaseView {
        //加载进来的是list,在接收的时候要注意;
        void load(List<ResultBean> datas);

        void showErrorView();


    }

    interface Presenter extends BasePresenter {
        void getHistoryFromNet();

        void getHistoryFromCache();

        void unSubscribe();
    }
}
