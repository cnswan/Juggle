package com.cnswan.juggle.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zx.freetime.R;
import com.zx.freetime.base.BaseSkinActivity;
import com.zx.freetime.engine.changeskin.callback.ISkinChangingCallback;
import com.zx.freetime.engine.changeskin.control.SkinConfig;
import com.zx.freetime.engine.changeskin.control.SkinManager;
import com.zx.freetime.widget.ZSwitch.SlideSwitch;

import java.io.File;

public class NavSettingActivity extends BaseSkinActivity {

    SlideSwitch changeSkin;
    SlideSwitch nightMode;

    FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_setting);

        mFrameLayout = (FrameLayout) findViewById(R.id.night_fl);
        changeSkin = (SlideSwitch) findViewById(R.id.changeskin_switch);
        nightMode = (SlideSwitch) findViewById(R.id.night_mode_switch);

        initListener();
    }


    void initListener() {
        String pluginName = getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE).getString(SkinConfig
                .KEY_PLUGIN_PKG, "");
        if (TextUtils.isEmpty(pluginName)) {//所以设置为关闭状态,原始状态;
            changeSkin.setState(false);
        } else {
            //如果不为空,那么就是换肤状态,所以当前状态应该为true,为打开的状态
            changeSkin.setState(true);
        }

        changeSkin.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
//                Toast.makeText(NavSettingActivity.this, "开1", Toast.LENGTH_LONG).show();
                String pluginPath = getFilesDir() + File.separator + SkinConfig.PLUGIN_NAME;
                SkinManager.getInstance().changeSkin(
                        pluginPath,
                        SkinConfig.PLUGIN_PKG_NAME,
                        new ISkinChangingCallback() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(NavSettingActivity.this, "换肤失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(NavSettingActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void close() {
//                Toast.makeText(NavSettingActivity.this, "关1", Toast.LENGTH_LONG).show();
                SkinManager.getInstance().removeAnySkin();
            }
        });

        nightMode.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
//                Toast.makeText(NavSettingActivity.this, "开2", Toast.LENGTH_LONG).show();
                mFrameLayout.setForeground(new ColorDrawable(0x8D080808));
            }

            @Override
            public void close() {
//                Toast.makeText(NavSettingActivity.this, "关2", Toast.LENGTH_LONG).show();
                mFrameLayout.setForeground(new ColorDrawable(0x00000000));
            }
        });
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, NavSettingActivity.class);
        mContext.startActivity(intent);
    }
}
