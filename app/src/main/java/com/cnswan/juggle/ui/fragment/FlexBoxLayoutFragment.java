package com.cnswan.juggle.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnswan.juggle.R;
import com.google.android.flexbox.FlexboxLayout;

/**
 * fragment for FlexBoxLayout with Flex Box
 * Created by cnswan on 2017/11/8.
 */

public class FlexBoxLayoutFragment extends Fragment implements View.OnClickListener {

    FlexboxLayout mFlexBoxLayout;

    public static FlexBoxLayoutFragment newInstance() {
        return new FlexBoxLayoutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flex_box_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFlexBoxLayout = view.findViewById(R.id.layout_flex_box);
        Activity activity = getActivity();
        FloatingActionButton addFab = activity.findViewById(R.id.add_fab);
        addFab.setOnClickListener(this);
        FloatingActionButton removeFab = activity.findViewById(R.id.remove_fab);
        removeFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int flexCount = mFlexBoxLayout.getFlexItemCount();
        switch (v.getId()) {
            case R.id.add_fab:
                TextView textView = new TextView(getContext());
                textView.setBackgroundColor(Color.CYAN);
                textView.setText(String.valueOf(flexCount + 1));
                textView.setGravity(Gravity.CENTER);
                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(lp);
                mFlexBoxLayout.addView(textView);
                break;
            case R.id.remove_fab:
                if (flexCount != 0) {
                    mFlexBoxLayout.removeViewAt(flexCount - 1);
                }
                break;
            default:
                break;
        }
    }
}
