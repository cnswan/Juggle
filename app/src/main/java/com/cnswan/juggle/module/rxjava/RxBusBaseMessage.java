package com.cnswan.juggle.module.rxjava;

/**
 * Created by zhangxin on 16/5/17.
 * 用来发送消息的RxBusMessage,只有被观察者发出这个类型的消息后,观察者才能做出响应;
 */
public class RxBusBaseMessage {
    private int code;
    private Object object;
    public RxBusBaseMessage(int code, Object object){
        this.code=code;
        this.object=object;
    }
    public RxBusBaseMessage(){}

    public int getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }
}
