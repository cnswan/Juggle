package com.cnswan.juggle.activity.chat;

import com.zx.freetime.bean.chat.ChatBean;
import com.zx.freetime.bean.topnews.TopNewsBean;
import com.zx.freetime.http.RequestImpl;
import com.zx.freetime.model.ChatModel;

import rx.Subscription;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 */

public class ChatPresenter implements ChatContract.Presenter {

    private ChatContract.View mView;
    private ChatModel mModel = new ChatModel();
    private RequestImpl mRequest;
    public Subscription chatSubscription;


    public ChatPresenter(ChatContract.View view) {
        mView = view;
        mRequest = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                //NOTE:所有的view中load的参数都是对应的List<item>
                mView.load((ChatBean) object);
            }

            @Override
            public void loadFailed() {
                mView.showErrorView();
            }

            @Override
            public void loadComplete() {
                //mView.showNormalView();
            }

            @Override
            public void addSubscription(Subscription subscription) {
//                mView.getFragment().addSubscription(subscription);
                chatSubscription = subscription;
            }
        };
    }

    @Override
    public void talk(String content) {
        mModel.talk(content, mRequest);
    }

    @Override
    public void unSubscribe() {
        if (chatSubscription != null) {
            chatSubscription.unsubscribe();
        }
    }

    @Override
    public void start() {
// 没有start...
    }
}
