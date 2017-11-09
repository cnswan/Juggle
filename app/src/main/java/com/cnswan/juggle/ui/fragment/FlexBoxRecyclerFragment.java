package com.cnswan.juggle.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnswan.juggle.R;
import com.cnswan.juggle.ui.adapter.FlexItemAdapter;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

/**
 * fragment for FlexBoxLayout with RecyclerView
 * Created by cnswan on 2017/11/8.
 */

public class FlexBoxRecyclerFragment extends Fragment implements View.OnClickListener {

    private RecyclerView         mRecyclerView;
    private FlexboxLayoutManager mFlexManager;
    private FlexItemAdapter      mAdapter;

    public static FlexBoxRecyclerFragment newInstance() {
        return new FlexBoxRecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flex_box_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.layout_flex_recycler);
        Activity activity = getActivity();
        mFlexManager = new FlexboxLayoutManager(activity);
        mRecyclerView.setLayoutManager(mFlexManager);
        mAdapter = new FlexItemAdapter(new ArrayList<FlexboxLayoutManager.LayoutParams>());
        mRecyclerView.setAdapter(mAdapter);
        FloatingActionButton addFab = activity.findViewById(R.id.add_fab);
        addFab.setOnClickListener(this);
        FloatingActionButton removeFab = activity.findViewById(R.id.remove_fab);
        removeFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int flexCount = mFlexManager.getFlexItemCount();
        switch (v.getId()) {
            case R.id.add_fab:
                FlexboxLayoutManager.LayoutParams lp = new FlexboxLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mAdapter.addData(lp);
                break;
            case R.id.remove_fab:
                if (flexCount != 0) {
                    mAdapter.remove(flexCount - 1);
                }
                break;
            default:
                break;
        }
    }
}
