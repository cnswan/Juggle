package com.cnswan.juggle.activity.chat;

import com.zx.freetime.base.BasePresenter;
import com.zx.freetime.base.BaseView;
import com.zx.freetime.bean.chat.ChatBean;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 */

public class ChatContract {
    interface View extends BaseView {
        void load(ChatBean chatBean);

        void showErrorView();

        //暂时不需要这个showNormalView了,这是在联网结束的时候调用的;
//        void showNormalView();


    }

    interface Presenter extends BasePresenter {
        void talk(String content);

        void unSubscribe();
    }
}
