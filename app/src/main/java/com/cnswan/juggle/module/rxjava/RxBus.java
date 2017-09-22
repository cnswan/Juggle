package com.cnswan.juggle.module.rxjava;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by zhangxin on 16/5/17.
 * 参考:http://www.jianshu.com/p/ca090f6e2fe2/
 */
public class RxBus {
    /**
     * 参考网址: http://hanhailong.com/2015/10/09/RxBus%E2%80%94%E9%80%9A%E8%BF%87RxJava%E6%9D%A5%E6%9B%BF%E6%8D%A2EventBus/
     *          http://www.loongwind.com/archives/264.html
     *          https://theseyears.gitbooks.io/android-architecture-journey/content/rxbus.html
     */
    private static volatile RxBus mDefaultInstance;

    private RxBus() {
    }

    //单例模式
    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }


    /*
    PublishSubject只会把在**订阅发生的时间点之后**(正符合我们的需求)来自原始Observable的数据发射给观察者
    Subject同时充当了Observer和Observable的角色，Subject是非线程安全的，要避免该问题，
    需要将 Subject转换为一个 SerializedSubject ，上述RxBus类中把线程非安全的PublishSubject包装成线程安全的Subject。
     */
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());



    //###############################################################################################
    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return _bus;
    }
    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return _bus.ofType(eventType);
    }
    //###############################################################################################



    /**
     * 提供了一个新的事件,根据code进行分发
     * @param code 事件code码
     * @param o    事件内容
     */
    public void post(int code, Object o){
        _bus.onNext(new RxBusBaseMessage(code,o));
    }


    /**
     * ofType操作符只发射指定类型的数据，这里指定了接受的类型是:RxBusBaseMessage.class;其内部就是filter+cast
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     * 对于注册了code为0，class为voidMessage的观察者，那么就接收不到code为0之外的voidMessage。
     * @param code 事件code
     * @param eventType 事件类型,首先进行一次检测,看你发来的类型是不是RxBusBaseMessage类型的;
     * @param <T>
     * @return
     * 我不明白为什么使用了ofType过滤之后还要使用filter过滤;
     * 使用map,将传入的RxBusBaseMessage有转化为对应的object;
     * 使用cast:做强制类型转换,cast(Class),参数为class类型,这里是做安全监测,如果不能转化为制定的class文件,那么将直接跑出异常;
     *
     * 操作符中的ReBusMessage参数是从
     */
    public <T> Observable<T> toObservable(final int code, final Class<T> eventType) {
        return _bus.ofType(RxBusBaseMessage.class)
                .filter(new Func1<RxBusBaseMessage,Boolean>() {
                    @Override
                    public Boolean call(RxBusBaseMessage msg) {
                        //过滤code和eventType都相同(类型)的事件
                        return msg.getCode() == code && eventType.isInstance(msg.getObject());
                    }
                }).map(new Func1<RxBusBaseMessage,Object>() {
                    @Override
                    public Object call(RxBusBaseMessage msg) {
                        return msg.getObject();
                    }
                }).cast(eventType);
    }



    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return _bus.hasObservers();
    }


}

/*
1、首先创建一个可同时充当Observer和Observable的Subject；

2、在需要接收事件的地方，订阅该Subject（此时Subject是作为Observable），在这之后，一旦Subject接收到事件，立即发射给该订阅者；

3、在我们需要发送事件的地方，将事件post至Subject，此时Subject作为Observer接收到事件（onNext），然后会发射给所有订阅该Subject的订阅者。

 */