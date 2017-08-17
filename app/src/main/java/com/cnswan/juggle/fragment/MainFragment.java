package com.cnswan.juggle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cnswan.juggle.R;
import com.cnswan.juggle.activity.TinkerActivity;

/**
 * Created by 00013259 on 2017/8/17.
 */

public class MainFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Button button = (Button) rootView.findViewById(R.id.btn_start_tinker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TinkerActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
