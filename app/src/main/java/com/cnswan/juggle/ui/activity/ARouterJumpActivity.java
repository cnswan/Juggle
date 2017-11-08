package com.cnswan.juggle.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cnswan.juggle.R;
import com.cnswan.juggle.module.arouter.ARouterObject;
import com.cnswan.juggle.module.arouter.ARouterObjectParcelable;
import com.cnswan.juggle.module.arouter.ServiceHello;

import java.util.List;
import java.util.Map;

/**
 * Created by cnswan on 2017/11/7.
 */

@Route(path = ARouterJumpActivity.ACT_PATH)
public class ARouterJumpActivity extends ManagedActivity implements View.OnClickListener {

    public static final String ACT_PATH = "/activity/router/jump";


    @Autowired
    String name = "jack";//['waɪəd] 自动布线

    @Autowired
    int age = 10;

    @Autowired
    int height = 175;

    // Autowired注解中标注name之后，将会使用byName的方式注入对应的字段，
    // 不设置name属性，会默认使用byType的方式发现服务(当同一接口有多个实现的时候，必须使用byName的方式发现服务)
    @Autowired(name = "boy")
    boolean girl;

    @Autowired
    char ch = 'A';

    @Autowired
    float fl = 12f;

    @Autowired
    double dou = 12.01d;

    @Autowired
    ARouterObjectParcelable pac;

    @Autowired
    ARouterObject obj;

    @Autowired
    List<ARouterObject> list;

    @Autowired
    Map<String, List<ARouterObject>> map;

    private long high;

    @Autowired
    String url;

    @Autowired
    ServiceHello serviceHello;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);// inject [ɪn'dʒekt] 注入
        // No more getter ...
        // name = getIntent().getStringExtra("name");
        // age = getIntent().getIntExtra("age", 0);
        // girl = getIntent().getBooleanExtra("girl", false);
        // high = getIntent().getLongExtra("high", 0);
        // url = getIntent().getStringExtra("url");
        String params = String.format(
                "name=%s,\n age=%s, \n height=%s,\n girl=%s,\n high=%s,\n url=%s,\n pac=%s,\n obj=%s \n ch=%s \n fl = %s, \n dou = %s, \n objList=%s, \n map=%s",
                name, age, height, girl, high, url, pac, obj, ch, fl, dou, list, map
        );
        ((TextView) findViewById(R.id.tv_jump_text1)).setText(params);
    }


    @Override
    public void onClick(View v) {
        serviceHello.sayHello("hello cnswan");
    }
}
