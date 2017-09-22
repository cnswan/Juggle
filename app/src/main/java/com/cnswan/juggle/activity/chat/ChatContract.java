package com.cnswan.juggle.activity.chat;

import com.cnswan.juggle.amvp.BasePresenter;
import com.cnswan.juggle.amvp.BaseView;
import com.cnswan.juggle.bean.chat.ChatBean;

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
