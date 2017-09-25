package com.cnswan.juggle.module.rxjava;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.SerializedSubscriber;

public class RxBus {

    //相当于Rxjava1.x中的Subject
    private final FlowableProcessor<Object> mBus;
    private static volatile RxBus sRxBus = null;

    private RxBus() {
        //调用toSerialized()方法，保证线程安全
        mBus = PublishProcessor.create().toSerialized();
    }

    public static synchronized RxBus getDefault() {
        if (sRxBus == null) {
            synchronized (RxBus.class) {
                if (sRxBus == null) {
                    sRxBus = new RxBus();
                }
            }
        }
        return sRxBus;
    }

    public <T> Flowable<T> toFlowable(Class<T> aClass) {
        return mBus.ofType(aClass);
    }

    public void post(int code, Object o) {
        new SerializedSubscriber<>(mBus).onNext(new RxBusBaseMessage(code, o));
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }
}

/*
1、首先创建一个可同时充当Observer和Observable的Subject；
2、在需要接收事件的地方，订阅该Subject（此时Subject是作为Observable），在这之后，一旦Subject接收到事件，立即发射给该订阅者；
3、在我们需要发送事件的地方，将事件post至Subject，此时Subject作为Observer接收到事件（onNext），然后会发射给所有订阅该Subject的订阅者。
 */