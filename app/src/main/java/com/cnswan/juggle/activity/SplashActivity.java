package com.cnswan.juggle.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.cnswan.juggle.R;
import com.cnswan.juggle.amvp.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 00013259 on 2017/8/18.
 */


public class SplashActivity extends BaseActivity {

    @BindView(R.id.action_settings)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }


}
