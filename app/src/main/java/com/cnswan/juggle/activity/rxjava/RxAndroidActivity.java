package com.cnswan.juggle.activity.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cnswan.juggle.amvp.BaseActivity;

/**
 *
 * Created by 00013259 on 2017/8/15.
 */
@Route(path = "/rx/main")
public class RxAndroidActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}

// https://github.com/ReactiveX/RxJava
// https://github.com/ReactiveX/RxAndroid

// compile 'io.reactivex:rxjava:1.0.14'
// compile 'io.reactivex:rxandroid:1.0.1'