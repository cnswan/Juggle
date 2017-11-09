package com.cnswan.juggle.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cnswan.juggle.R;
import com.cnswan.juggle.ui.fragment.FlexBoxLayoutFragment;
import com.cnswan.juggle.ui.fragment.FlexBoxRecyclerFragment;

/**
 * Google flex box layout
 * Created by 00013259 on 2017/8/18.
 */
@Route(path = FlexBoxActivity.ACT_PATH)
public class FlexBoxActivity extends ManagedActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String ACT_PATH = "/activity/flex/main";

    public static final String FLEXBOXLAYOUT_FRAGMENT = "flexboxlayout_fragment";
    public static final String RECYCLERVIEW_FRAGMENT  = "recyclerview_fragment";

    RadioButton[] mImplementation = new RadioButton[2];
    RadioButton[] mFlexDirection  = new RadioButton[4];
    RadioButton[] mFlexWrap       = new RadioButton[3];
    RadioButton[] mJustifyContent = new RadioButton[5];
    RadioButton[] mAlignItems     = new RadioButton[5];
    RadioButton[] mAlignContent   = new RadioButton[6];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flex_box);
        mImplementation[0] = findViewById(R.id.radiobutton_viewgroup);
        mImplementation[1] = findViewById(R.id.radiobutton_recyclerview);

        mFlexDirection[0] = findViewById(R.id.radiobutton_flex_direction_row);
        mFlexDirection[1] = findViewById(R.id.radiobutton_flex_direction_row_reverse);
        mFlexDirection[2] = findViewById(R.id.radiobutton_flex_direction_column);
        mFlexDirection[3] = findViewById(R.id.radiobutton_flex_direction_column_reverse);

        mFlexWrap[0] = findViewById(R.id.radiobutton_nowrap);
        mFlexWrap[1] = findViewById(R.id.radiobutton_wrap);
        mFlexWrap[2] = findViewById(R.id.radiobutton_wrap_reverse);

        mJustifyContent[0] = findViewById(R.id.radiobutton_justify_content_flex_start);
        mJustifyContent[1] = findViewById(R.id.radiobutton_justify_content_flex_end);
        mJustifyContent[2] = findViewById(R.id.radiobutton_justify_content_center);
        mJustifyContent[3] = findViewById(R.id.radiobutton_justify_content_space_between);
        mJustifyContent[4] = findViewById(R.id.radiobutton_justify_content_space_around);

        mAlignItems[0] = findViewById(R.id.radiobutton_align_items_flex_start);
        mAlignItems[1] = findViewById(R.id.radiobutton_align_items_flex_end);
        mAlignItems[2] = findViewById(R.id.radiobutton_align_items_center);
        mAlignItems[3] = findViewById(R.id.radiobutton_align_items_baseline);
        mAlignItems[4] = findViewById(R.id.radiobutton_align_items_stretch);

        mAlignContent[0] = findViewById(R.id.radiobutton_align_content_flex_start);
        mAlignContent[1] = findViewById(R.id.radiobutton_align_content_flex_end);
        mAlignContent[2] = findViewById(R.id.radiobutton_align_content_center);
        mAlignContent[3] = findViewById(R.id.radiobutton_align_content_space_between);
        mAlignContent[4] = findViewById(R.id.radiobutton_align_content_space_around);
        mAlignContent[5] = findViewById(R.id.radiobutton_align_content_stretch);

        setRadioListener(mImplementation, mFlexDirection, mFlexWrap, mJustifyContent, mAlignItems, mAlignContent);
        replaceToFlexBoxLayoutFragment(getSupportFragmentManager());
    }

    protected void replaceToFlexBoxLayoutFragment(FragmentManager fragmentManager) {
        FlexBoxLayoutFragment fragment = (FlexBoxLayoutFragment) fragmentManager.findFragmentByTag(FLEXBOXLAYOUT_FRAGMENT);
        if (fragment == null) {
            fragment = FlexBoxLayoutFragment.newInstance();
        }
        fragmentManager.beginTransaction().replace(R.id.layout_flex_box, fragment, FLEXBOXLAYOUT_FRAGMENT).commit();
    }

    protected void replaceToFlexBoxRecyclerFragment(FragmentManager fragmentManager) {
        FlexBoxRecyclerFragment fragment = (FlexBoxRecyclerFragment) fragmentManager.findFragmentByTag(RECYCLERVIEW_FRAGMENT);
        if (fragment == null) {
            fragment = FlexBoxRecyclerFragment.newInstance();
        }
        fragmentManager.beginTransaction().replace(R.id.layout_flex_box, fragment, RECYCLERVIEW_FRAGMENT).commit();
    }

    protected void setRadioListener(RadioButton[]... radioButtons) {
        for (RadioButton[] radios : radioButtons) {
            for (RadioButton radioButton : radios) {
                radioButton.setOnCheckedChangeListener(this);
            }
        }
    }

    protected void resetRadioChecked(RadioButton[] radioButtons, CompoundButton buttonView) {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.isChecked() && radioButton != buttonView) {
                radioButton.setChecked(false);
                return;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isPressed() && isChecked) {
            switch (buttonView.getId()) {
                case R.id.radiobutton_viewgroup:
                    replaceToFlexBoxLayoutFragment(getSupportFragmentManager());
                    resetRadioChecked(mImplementation, buttonView);
                    break;
                case R.id.radiobutton_recyclerview:
                    replaceToFlexBoxRecyclerFragment(getSupportFragmentManager());
                    resetRadioChecked(mImplementation, buttonView);
                    break;
                case R.id.radiobutton_flex_direction_row:
                    resetRadioChecked(mFlexDirection, buttonView);
                    break;
                case R.id.radiobutton_flex_direction_row_reverse:
                    resetRadioChecked(mFlexDirection, buttonView);
                    break;
                case R.id.radiobutton_flex_direction_column:
                    resetRadioChecked(mFlexDirection, buttonView);
                    break;
                case R.id.radiobutton_flex_direction_column_reverse:
                    resetRadioChecked(mFlexDirection, buttonView);
                    break;
                case R.id.radiobutton_nowrap:
                    resetRadioChecked(mFlexWrap, buttonView);
                    break;
                case R.id.radiobutton_wrap:
                    resetRadioChecked(mFlexWrap, buttonView);
                    break;
                case R.id.radiobutton_wrap_reverse:
                    resetRadioChecked(mFlexWrap, buttonView);
                    break;
                case R.id.radiobutton_justify_content_flex_start:
                    resetRadioChecked(mJustifyContent, buttonView);
                    break;
                case R.id.radiobutton_justify_content_flex_end:
                    resetRadioChecked(mJustifyContent, buttonView);
                    break;
                case R.id.radiobutton_justify_content_center:
                    resetRadioChecked(mJustifyContent, buttonView);
                    break;
                case R.id.radiobutton_justify_content_space_between:
                    resetRadioChecked(mJustifyContent, buttonView);
                    break;
                case R.id.radiobutton_justify_content_space_around:
                    break;
                case R.id.radiobutton_align_items_flex_start:
                    resetRadioChecked(mAlignItems, buttonView);
                    break;
                case R.id.radiobutton_align_items_flex_end:
                    resetRadioChecked(mAlignItems, buttonView);
                    break;
                case R.id.radiobutton_align_items_center:
                    resetRadioChecked(mAlignItems, buttonView);
                    break;
                case R.id.radiobutton_align_items_baseline:
                    resetRadioChecked(mAlignItems, buttonView);
                    break;
                case R.id.radiobutton_align_items_stretch:
                    resetRadioChecked(mAlignItems, buttonView);
                    break;
                case R.id.radiobutton_align_content_flex_start:
                    resetRadioChecked(mAlignContent, buttonView);
                    break;
                case R.id.radiobutton_align_content_flex_end:
                    resetRadioChecked(mAlignContent, buttonView);
                    break;
                case R.id.radiobutton_align_content_center:
                    resetRadioChecked(mAlignContent, buttonView);
                    break;
                case R.id.radiobutton_align_content_space_between:
                    resetRadioChecked(mAlignContent, buttonView);
                    break;
                case R.id.radiobutton_align_content_space_around:
                    resetRadioChecked(mAlignContent, buttonView);
                    break;
                case R.id.radiobutton_align_content_stretch:
                    resetRadioChecked(mAlignContent, buttonView);
                    break;
                default:
                    break;
            }
        }
    }
}
