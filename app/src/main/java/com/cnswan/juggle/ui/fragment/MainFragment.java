package com.cnswan.juggle.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnswan.juggle.R;
import com.cnswan.juggle.bean.Library;
import com.cnswan.juggle.ui.activity.ARouterActivity;
import com.cnswan.juggle.ui.activity.FlexBoxActivity;
import com.cnswan.juggle.ui.activity.VLayoutActivity;
import com.cnswan.juggle.ui.activity.ViewActivity;
import com.cnswan.juggle.ui.adapter.MainListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnswan on 2017/11/2.
 */

public class MainFragment extends Fragment {

    private RecyclerView    mRecyclerView;
    private MainListAdapter mListAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.main_recycler_list);
        final List<Library> libraries = parseArrayForLibraries();
        mListAdapter = new MainListAdapter(libraries);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(itemClick);
        super.onViewCreated(view, savedInstanceState);
    }

    private OnItemClickListener itemClick = new OnItemClickListener() {
        @Override
        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            Library library = (Library) adapter.getItem(position);
            if (library != null) {
                switch (library.getName()) {
                    case "CoordinatorLayout":
                        break;
                    case "RxJava":
                        break;
                    case "ARouter":
                        ARouter.getInstance().build(ARouterActivity.ACT_PATH).navigation();
                        break;
                    case "FlexBox":
                        ARouter.getInstance().build(FlexBoxActivity.ACT_PATH).navigation();
                        break;
                    case "VLayout":
                        ARouter.getInstance().build(VLayoutActivity.ACT_PATH).navigation();
                        break;
                    case "ButterKnife":
                        break;
                    case "Glide":
                        break;
                    case "DiskLruCache":
                        break;
                    case "View":
                        ARouter.getInstance().build(ViewActivity.ACT_PATH).navigation();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private List<Library> parseArrayForLibraries() {
        ArrayList<Library> libraries = new ArrayList<>();
        final String[] array = getContext().getResources().getStringArray(R.array.libraries);
        for (String str : array) {
            String[] split = str.split("\\|");
            libraries.add(new Library(split[0], split[1]));
        }
        return libraries;
    }

}
