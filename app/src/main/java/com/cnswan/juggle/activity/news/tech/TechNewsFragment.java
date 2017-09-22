package com.cnswan.juggle.activity.news.tech;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.zx.freetime.R;
import com.zx.freetime.adapter.TechNewsAdapter;
import com.zx.freetime.base.BaseFragment;
import com.zx.freetime.bean.technews.AndroidNewsBean;
import com.zx.freetime.ui.news.NewsFragment;
import com.zx.freetime.utils.Constants;
import com.zx.freetime.widget.ZRecyclerView.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class TechNewsFragment extends BaseFragment implements TechNewsContract.View {

    private List<AndroidNewsBean.ResultBean> mTechNewsList = new ArrayList<>(); //刚一开始是空的;
    private XRecyclerView mRecyclerView;
    private TechNewsAdapter mAdapter;
    private TechNewsContract.Presenter mPresenter;
    private int mPage = 1;

    @Override
    public void initContentView(View contentView) {
        mRecyclerView = (XRecyclerView) contentView.findViewById(R.id.tech_rv);
        initRecyclerView();
        mPresenter = new TechNewsPresenter(this);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setPullRefreshEnabled(false); //禁止下拉刷新吧,但是可以上拉加载更多;
        mAdapter = new TechNewsAdapter(getActivity(), mTechNewsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.getTechNewsFromNet(++mPage);
            }
        });
    }

    @Override
    public int getFragmentContent() {
        return R.layout.fragment_tech;
    }

    @Override
    public void onRefresh() {
        //这个方法可能不行....
        mPresenter.getTechNewsFromCache();
    }


    @Override
    public void load(List<AndroidNewsBean.ResultBean> datas) {
        for (int i = 0; i < datas.size(); i++) {
            mTechNewsList.add(datas.get(i));
        }

    }

    @Override
    public void showErrorView() {
        showError();
    }

    @Override
    public void showNormalView() {
        showContent();
        //NOTE:特别注意:这是在收集收据结束之后回到的,所以,请务必执行
        mRecyclerView.refreshComplete();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public TechNewsFragment getFragment() {
        return this;
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.start();
    }
}
