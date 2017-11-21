package com.cnswan.juggle.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cnswan.juggle.R;

/**
 * Created by cnswan on 2017/11/16.
 */

@Route(path = ViewActivity.ACT_PATH)
public class ViewActivity extends ManagedActivity {

    public static final String ACT_PATH = "/activity/view/main";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);



    }
}
