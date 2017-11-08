package com.cnswan.juggle.module.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * ARouter实现序列化，ARouter会自己发现该对象
 * Created by cnswan on 2017/11/8.
 */

@Route(path = "/service/router/json")
public class ServiceJson implements SerializationService {

    private Gson gson;

    @Override
    public <T> T json2Object(String input, Class<T> clazz) {
        return gson.fromJson(input, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return gson.toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        return gson.fromJson(input, clazz);
    }

    @Override
    public void init(Context context) {
        gson = new Gson();
    }
}
