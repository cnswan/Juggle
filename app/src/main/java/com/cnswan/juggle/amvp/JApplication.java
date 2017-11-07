package com.cnswan.juggle.amvp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.cnswan.juggle.manager.ARouterManager;

/**
 * Created by cnswan on 2017/11/1.
 */

public class JApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Application instance;

    public JApplication() {
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Application getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ARouterManager.getInstance().initRouter(this);
    }
}
