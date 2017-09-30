package com.cnswan.juggle.activity.arouter;

import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cnswan.juggle.amvp.BaseActivity;

/**
 * Created by 00013259 on 2017/9/30.
 */

public class SchemaFilterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        ARouter.getInstance().build(uri).navigation();
        finish();
    }
}
