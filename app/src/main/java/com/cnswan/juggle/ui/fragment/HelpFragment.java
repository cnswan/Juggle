package com.cnswan.juggle.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnswan.juggle.R;

/**
 * Created by cnswan on 2017/11/2.
 */

public class HelpFragment extends Fragment {


    public static HelpFragment newInstance() {
        return new HelpFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
