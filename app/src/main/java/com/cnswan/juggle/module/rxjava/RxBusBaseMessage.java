package com.cnswan.juggle.module.rxjava;

public class RxBusBaseMessage {

    private int    code;
    private Object object;

    public RxBusBaseMessage(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public RxBusBaseMessage() {
    }

    public int getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }
}
