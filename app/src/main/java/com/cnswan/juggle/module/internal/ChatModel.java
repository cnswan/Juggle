package com.cnswan.juggle.module.internal;

import com.cnswan.juggle.bean.chat.ChatBean;
import com.cnswan.juggle.module.http.HttpUtils;
import com.cnswan.juggle.module.http.RequestImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatModel {

    private static final String appKey = "17419f5e88107690d31bf9fe417b6ba7";

    public void talk(String content, final RequestImpl request) {
        Disposable disposable = HttpUtils.getInstance().getChatClient()
                .talk(content, appKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ChatBean>() {
                    @Override
                    public void accept(ChatBean chatBean) throws Exception {
                        System.out.println(chatBean);
                        request.loadSuccess(chatBean);//执行到这一步是在UI线程;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        request.loadFailed();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        request.loadComplete();
                    }
                });
        request.addSubscription(disposable);
    }
}
