package com.cnswan.juggle.module.rxjava;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.SerializedSubscriber;

public class RxBus {

    //相当于Rxjava1.x中的Subject
    private final FlowableProcessor<Object> mBus;
    private static volatile RxBus sRxBus = null;

    private static volatile RxBus mDefaultInstance;

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


    /**
     * ofType操作符只发射指定类型的数据，这里指定了接受的类型是:RxBusBaseMessage.class;其内部就是filter+cast
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     * 对于注册了code为0，class为voidMessage的观察者，那么就接收不到code为0之外的voidMessage。
     *
     * @param code      事件code
     * @param eventType 事件类型,首先进行一次检测,看你发来的类型是不是RxBusBaseMessage类型的;
     * @param <T>
     * @return 我不明白为什么使用了ofType过滤之后还要使用filter过滤;
     * 使用map,将传入的RxBusBaseMessage有转化为对应的object;
     * 使用cast:做强制类型转换,cast(Class),参数为class类型,这里是做安全监测,如果不能转化为制定的class文件,那么将直接跑出异常;
     * <p>
     * 操作符中的ReBusMessage参数是从
     */
    public <T> Flowable<T> toObservable(final int code, final Class<T> eventType) {
        return mBus.ofType(RxBusBaseMessage.class)
                .filter(new Predicate<RxBusBaseMessage>() {
                    @Override
                    public boolean test(@NonNull RxBusBaseMessage msg) throws Exception {
                        //过滤code和eventType都相同(类型)的事件
                        return msg.getCode() == code && eventType.isInstance(msg.getObject());
                    }
                }).map(new Function<RxBusBaseMessage, Object>() {
                    @Override
                    public Object apply(@NonNull RxBusBaseMessage msg) throws Exception {
                        return msg.getObject();
                    }
                }).cast(eventType);
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