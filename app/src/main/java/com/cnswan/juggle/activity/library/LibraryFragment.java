package com.cnswan.juggle.activity.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnswan.juggle.R;
import com.cnswan.juggle.amvp.BaseInnerFragment;

/**
 * Created by 00013259 on 2017/9/27.
 */

public class LibraryFragment extends BaseInnerFragment {

    RecyclerView mLibList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        mLibList = (RecyclerView) root.findViewById(R.id.list_libs);
        return root;
    }
}
