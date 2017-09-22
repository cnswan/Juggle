package com.cnswan.juggle.activity.chat;

import com.cnswan.juggle.bean.chat.ChatBean;
import com.cnswan.juggle.module.http.RequestImpl;
import com.cnswan.juggle.module.internal.ChatModel;

import io.reactivex.disposables.Disposable;


public class ChatPresenter implements ChatContract.Presenter {

    private ChatContract.View mView;
    private ChatModel mModel = new ChatModel();
    private RequestImpl mRequest;
    public  Disposable  mChatDisplsable;


    public ChatPresenter(ChatContract.View view) {
        mView = view;
        mRequest = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mView.load((ChatBean) object);
            }

            @Override
            public void loadFailed() {
                mView.showErrorView();
            }

            @Override
            public void loadComplete() {
            }

            @Override
            public void addSubscription(Disposable disposable) {
                mChatDisplsable = disposable;
            }
        };
    }

    @Override
    public void talk(String content) {
        mModel.talk(content, mRequest);
    }

    @Override
    public void unSubscribe() {
        if (mChatDisplsable != null && !mChatDisplsable.isDisposed()) {
            mChatDisplsable.dispose();
        }
    }

    @Override
    public void start() {
        // 没有start...
    }
}
