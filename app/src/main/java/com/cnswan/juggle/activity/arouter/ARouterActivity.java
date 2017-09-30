package com.cnswan.juggle.activity.arouter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cnswan.juggle.amvp.BaseActivity;

/**
 * Created by 00013259 on 2017/9/27.
 */

@Route(path = ARouterActivity.ACT_PATH)
public class ARouterActivity extends BaseActivity {

    public static final String ACT_PATH = "/router/root/router";

}
