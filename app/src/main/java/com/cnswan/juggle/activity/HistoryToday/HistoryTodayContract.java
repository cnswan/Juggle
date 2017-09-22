package com.cnswan.juggle.activity.HistoryToday;

import com.zx.freetime.base.BasePresenter;
import com.zx.freetime.base.BaseView;
import com.zx.freetime.bean.historytoday.ResultBean;
import com.zx.freetime.ui.news.tech.TechNewsFragment;

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
