package com.cnswan.juggle.module.arouter;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * ARouter IProvider实现
 * Created by cnswan on 2017/11/8.
 */

@Route(path = "service/router/hello")
public class ServiceHelloImpl implements ServiceHello {

    private Context mContext;

    @Override
    public void init(Context context) {
        this.mContext = context;
    }

    @Override
    public void sayHello(String name) {
        Toast.makeText(mContext, "hello " + name, Toast.LENGTH_LONG).show();
    }

}
