package com.cnswan.juggle.module.internal;

import android.util.Log;

import com.cnswan.juggle.bean.chat.ChatBean;
import com.cnswan.juggle.module.http.HttpUtils;
import com.cnswan.juggle.module.http.RequestImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class ChatModel {

    private final String appKey = "17419f5e88107690d31bf9fe417b6ba7";

    public void talk(String content, final RequestImpl request) {
        Subscription subscription = HttpUtils.getInstance().getChatClient()
                .talk(content, appKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChatBean>() {
                    @Override
                    public void onCompleted() {
                        request.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("###", e.getMessage());
                        request.loadFailed();
                    }

                    @Override
                    public void onNext(ChatBean chatBean) {
                        System.out.println("----------------");
                        System.out.println(chatBean);
                        System.out.println("----------------");

                        //执行到这一步是在UI线程;
                        request.loadSuccess(chatBean);
                    }
                });
        request.addSubscription(subscription);
    }
}
