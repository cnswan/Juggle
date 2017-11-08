package com.cnswan.juggle.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cnswan.juggle.R;
import com.cnswan.juggle.amvp.BaseActivity;
import com.cnswan.juggle.module.arouter.ARouterObject;
import com.cnswan.juggle.module.arouter.ARouterObjectParcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 00013259 on 2017/9/27.
 */

@Route(path = ARouterActivity.ACT_PATH)
public class ARouterActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG_LOG  = ARouterActivity.class.getSimpleName();
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
            //====================// 基础功能
            case R.id.btn_arouter_option_jump1:
                ARouter.getInstance().build(ARouterJumpActivity.ACT_PATH).navigation();
                break;
            case R.id.btn_arouter_option_jump2:
                break;

            //====================// 进阶用法
            case R.id.btn_arouter_option_url:
                ARouter.getInstance().build(WebViewActivity.ACT_PATH)
                        .withString("title", "Test ARouter")
                        .withString("url", "file:///android_asset/schema-test.html").navigation();
                break;
            case R.id.btn_arouter_option_interceptor:
                ARouter.getInstance().build(ARouterJumpActivity.ACT_PATH)
                        .withBoolean("intercept", true)
                        .navigation(this, new NavCallback() {

                            @Override
                            public void onArrival(Postcard postcard) {
                                Log.d(TAG_LOG, "跳转过后（start activity方法之后调用）" +
                                        "->postcard:" + postcard);
                            }

                            @Override
                            public void onInterrupt(Postcard postcard) {
                                Log.d(TAG_LOG, "被拦截了（如果声明了拦截器且拦截了之后调用）");
                            }
                        });
                break;
            case R.id.btn_arouter_option_inject:
                ARouterObjectParcelable parcelable = new ARouterObjectParcelable(18, "JACK");
                ARouterObject object = new ARouterObject(20, "nick");
                List<ARouterObject> objectList = new ArrayList<>();
                objectList.add(object);
                Map<String, List<ARouterObject>> map = new HashMap<>();
                map.put("testMap", objectList);
                ARouter.getInstance().build(ARouterJumpActivity.ACT_PATH)
                        .withString("name", "老王")
                        .withInt("age", 18)
                        .withBoolean("boy", true)
                        .withLong("high", 180)
                        .withString("url", "https://a.b.c")
                        .withParcelable("pac", parcelable)
                        .withObject("obj", object)
                        .withObject("objList", objectList)
                        .withObject("map", map)
                        .navigation();
                break;
            //====================//
            default:
                break;
        }
    }
}
