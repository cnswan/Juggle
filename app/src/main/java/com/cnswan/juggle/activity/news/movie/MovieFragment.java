package com.cnswan.juggle.activity.news.movie;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cnswan.juggle.R;
import com.cnswan.juggle.amvp.BaseFragment;
import com.cnswan.juggle.bean.movie.SubjectsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class MovieFragment extends BaseFragment implements MovieContract.View {
    RecyclerView mRecyclerView;
    ArrayList<SubjectsBean> list = new ArrayList();
    MovieAdapter mAdapter;
    MoviePresenter mPresenter;

    @Override
    public void initContentView(View contentView) {
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new MovieAdapter(getActivity(), list);
        mRecyclerView.setAdapter(mAdapter);

        //初始化数据
        mPresenter = new MoviePresenter(this);//让Presenter持有了view;
//        mPresenter.start();
    }

    @Override
    public int getFragmentContent() {
        return R.layout.fragment_movie;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void load(List<SubjectsBean> datas) {
        list.addAll(datas);

        System.out.println(datas);
        Log.e("###", "load: 加载到的数据是:" +datas);
    }

    @Override
    public void showErrorView() {
        showError();
    }

    @Override
    public void showNormalView() {
        showContent();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public MovieFragment getFragment() {
        return this;
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.start();
    }
}
