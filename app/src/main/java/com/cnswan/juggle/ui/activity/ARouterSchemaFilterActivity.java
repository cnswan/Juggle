package com.cnswan.juggle.ui.activity;

import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cnswan.juggle.amvp.BaseActivity;

/**
 * 新建一个Activity用于监听Schame事件,之后直接把url传递给ARouter即可
 * Created by 00013259 on 2017/9/30.
 */

public class ARouterSchemaFilterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            ARouter.getInstance().build(uri).navigation();
        }
        finish();
    }
}
