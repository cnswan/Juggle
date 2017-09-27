package com.cnswan.juggle.aapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.cnswan.juggle.manager.ARouterManager;
import com.cnswan.juggle.manager.TinkerManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by 00013259 on 2017/8/17.
 */

@DefaultLifeCycle(
        application = "com.cnswan.juggle.aapp.MApplication",             //application name to generate
        flags = ShareConstants.TINKER_ENABLE_ALL)
public class MApplicationLike extends DefaultApplicationLike {

    private static final String TAG = "Juggle.MApplicationLike";

    public MApplicationLike(Application application, int tinkerFlags,
                            boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                            long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags,
                tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        AppContext.application = getApplication();
        AppContext.context = getApplication();
        TinkerManager.setTinkerApplicationLike(this);
        TinkerManager.setUpgradeRetryEnable(true);
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");// super.onCreate();父类无操作，省略
        ARouterManager.initArouter(getApplication());
    }
}
