package com.cnswan.juggle.module.http;

import rx.Subscription;

/**
 * Created by zhangxin on 2016/10/28.
 * <p>
 * Description :
 */

public interface RequestImpl {
    //加载成功,直接传入的Object,在调用的时候你必须明确的知道你得到的是什么类型的,然后在强制类型转换;
    void loadSuccess(Object object);

    //加载失败
    void loadFailed();

    //加载成功
    void loadComplete();

    //订阅,然后会在销毁时取消订阅,避免内存泄露;
    void addSubscription(Subscription subscription);
}
