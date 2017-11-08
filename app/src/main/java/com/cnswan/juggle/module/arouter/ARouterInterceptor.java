package com.cnswan.juggle.module.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * ARouter拦截器
 * Created by cnswan on 2017/11/7.
 */
@Interceptor(priority = 9)
public class ARouterInterceptor implements IInterceptor {

    private Context mContext;

    @Override
    public void init(Context context) {
        this.mContext = context;
    }

    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        if (postcard.getExtras().getBoolean("intercept")) {
            callback.onInterrupt(null);//拦截回调
        } else {
            callback.onContinue(postcard);// 递交事件
        }
    }

}
