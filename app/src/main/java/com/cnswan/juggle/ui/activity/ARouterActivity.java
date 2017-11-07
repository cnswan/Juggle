package com.cnswan.juggle.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cnswan.juggle.R;
import com.cnswan.juggle.amvp.BaseActivity;

/**
 * Created by 00013259 on 2017/9/27.
 */

@Route(path = ARouterActivity.ACT_PATH)
public class ARouterActivity extends BaseActivity implements View.OnClickListener {

    public static final String ACT_PATH = "/activity/router/main";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter);
        mToolbar = findViewById(R.id.arouter_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_arouter_open_log:
                ARouter.openLog();
                break;
            //====================//
            case R.id.btn_arouter_option_jump1:
                ARouter.getInstance().build(ARouterJumpActivity.ACT_PATH).navigation();
                break;
            case R.id.btn_arouter_option_jump2:
                break;

            //====================//
            case R.id.btn_arouter_option_url:
                ARouter.getInstance().build(WebViewActivity.ACT_PATH)
                        .withString("title", "Test ARouter")
                        .withString("url", "file:///android_asset/schema-test.html").navigation();
                break;
            case R.id.btn_arouter_option_interceptor:
                ARouter.getInstance().build(ARouterJumpActivity.ACT_PATH).navigation(this, new NavCallback() {

                    @Override
                    public void onArrival(Postcard postcard) {

                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {

                    }
                });

                break;
            //====================//
            default:
                break;
        }
    }
}
