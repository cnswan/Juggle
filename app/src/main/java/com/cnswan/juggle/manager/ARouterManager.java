package com.cnswan.juggle.manager;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cnswan.juggle.BuildConfig;

/**
 * Created by 00013259 on 2017/9/27.
 */

public class ARouterManager implements BaseManagerInterface {

    public static final boolean DEBUG_AROUTER = BuildConfig.DEBUG && BuildConfig.debug_arouter;

    private static ARouterManager instance;

    private ARouterManager() {

    }

    public static ARouterManager getInstance() {
        if (instance == null) {
            instance = new ARouterManager();
        }
        return instance;
    }

    public void initRouter(Application application) {
        if (DEBUG_AROUTER) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
    }

}
