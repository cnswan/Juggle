package com.cnswan.juggle.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cnswan.juggle.R;

/**
 * Created by cnswan on 2017/11/7.
 */

@Route(path = WebViewActivity.ACT_PATH)
public class WebViewActivity extends ManagedActivity {

    public static final String ACT_PATH = "/activity/webview/page";

    private Toolbar mToolbar;
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mToolbar = findViewById(R.id.web_toolbar);
        mWebView = findViewById(R.id.web_webview);
        mToolbar.setTitle(getIntent().getStringExtra("title"));
        mWebView.loadUrl(getIntent().getStringExtra("url"));

    }
}
