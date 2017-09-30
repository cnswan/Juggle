package com.cnswan.juggle.activity.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnswan.juggle.R;
import com.cnswan.juggle.activity.arouter.ARouterActivity;
import com.cnswan.juggle.adapter.LibraryAdapter;
import com.cnswan.juggle.amvp.BaseInnerFragment;
import com.cnswan.juggle.bean.lib.Library;

import java.util.ArrayList;

/**
 * Created by 00013259 on 2017/9/27.
 */

public class LibraryFragment extends BaseInnerFragment {

    RecyclerView   mLibList;
    LibraryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        mLibList = (RecyclerView) root.findViewById(R.id.list_libs);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Library> list = new ArrayList<>();
        list.add(new Library("ARouter"));
        list.add(new Library("ARouter"));
        list.add(new Library("ARouter"));
        list.add(new Library("ARouter"));
        list.add(new Library("ARouter"));
        list.add(new Library("ARouter"));
        mAdapter = new LibraryAdapter(list);
        mLibList.setAdapter(mAdapter);
        mLibList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mLibList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance().build(ARouterActivity.ACT_PATH).navigation();
            }
        });
    }
}
