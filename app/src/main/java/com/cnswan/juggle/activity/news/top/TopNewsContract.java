package com.cnswan.juggle.activity.news.top;

import com.zx.freetime.base.BasePresenter;
import com.zx.freetime.base.BaseView;
import com.zx.freetime.bean.topnews.TopNewsItem;

import java.util.List;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 */

public class TopNewsContract {
    interface View extends BaseView {
        void load(List<TopNewsItem> datas);

        void showErrorView();

        void showNormalView();

        //这个方法是用来做延时的,因为客户端并不会马上更新新闻,所以假装加载一下数据,然后修改;
        void sendDelay();

        //用来订阅;
        TopNewsFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getTopNewsFromNet(Long lastTtime);
        void getTopNewsFromCache(String cacheName);
    }
}
