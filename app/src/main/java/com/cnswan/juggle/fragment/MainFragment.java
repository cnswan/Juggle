package com.cnswan.juggle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnswan.juggle.R;
import com.cnswan.juggle.adapter.MainAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

/**
 * Created by 00013259 on 2017/8/17.
 */

public class MainFragment extends BaseFragment {

    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.layout_include_recycler, container, false);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity(),FlexDirection.ROW);
        mRecyclerView.setLayoutManager(layoutManager);
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        MainAdapter adapter = new MainAdapter(list);
        mRecyclerView.setAdapter(adapter);
        return mRecyclerView;
    }

}
