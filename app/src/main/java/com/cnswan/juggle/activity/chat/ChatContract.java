package com.cnswan.juggle.activity.chat;

import com.cnswan.juggle.amvp.BasePresenter;
import com.cnswan.juggle.amvp.BaseView;
import com.cnswan.juggle.bean.chat.ChatBean;

public class ChatContract {
    interface View extends BaseView {

        void load(ChatBean chatBean);

        void showErrorView();

    }

    interface Presenter extends BasePresenter {

        void talk(String content);

        void unSubscribe();

    }
}
