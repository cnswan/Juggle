package com.cnswan.juggle.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * Created by cnswan on 2017/11/7.
 */

@Route(path = ARouterJumpActivity.ACT_PATH)
public class ARouterJumpActivity extends ManagedActivity {

    public static final String ACT_PATH = "/activity/router/jump";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
