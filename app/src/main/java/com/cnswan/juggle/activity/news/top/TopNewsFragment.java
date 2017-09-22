package com.cnswan.juggle.activity.news.top;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.cnswan.juggle.R;
import com.cnswan.juggle.adapter.TopNewsAdapter;
import com.cnswan.juggle.amvp.BaseFragment;
import com.cnswan.juggle.bean.topnews.TopNewsItem;
import com.cnswan.juggle.widget.ZRecyclerView.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class TopNewsFragment extends BaseFragment implements TopNewsContract.View {
    private List<TopNewsItem> mTopNewsList = new ArrayList<>(); //刚一开始是空的;
    private XRecyclerView             mRecyclerView;
    private TopNewsAdapter            mAdapter;
    private TopNewsContract.Presenter mPresenter;
    private Long lastTime = 0L;


    @Override
    public void initContentView(View contentView) {
        mRecyclerView = (XRecyclerView) contentView.findViewById(R.id.top_rv);
        initRecyclerView();
        mPresenter = new TopNewsPresenter(this);
    }


    void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setLoadingMoreEnabled(false); //禁止下拉加载更多吧,但是可以下拉刷新;
        mAdapter = new TopNewsAdapter(getActivity(), mTopNewsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
                mPresenter.getTopNewsFromNet(lastTime);
            }

            //禁止加载更多;
            @Override
            public void onLoadMore() {
                //mPresenter.getTopNewsFromNet(++mPage);
            }
        });
    }

    @Override
    public int getFragmentContent() {
        return R.layout.fragment_top;
    }

    //还没实现吧...
    @Override
    public void onRefresh() {

    }


    @Override
    public void load(List<TopNewsItem> datas) {
        //将新家在的数据添加到list的头部;
        mTopNewsList.addAll(0, datas);
    }

    @Override
    public void showErrorView() {
        showError();
    }

    @Override
    public void showNormalView() {
        showContent();
        lastTime = System.currentTimeMillis();
        mRecyclerView.refreshComplete();
        mAdapter.notifyDataSetChanged();
    }

    //作为一个延时的发送...
    @Override
    public void sendDelay() {
        new Handler().postDelayed(new Runnable() {
            //这个runnale是在主线程执行的
            @Override
            public void run() {
                mRecyclerView.refreshComplete();
                Toast.makeText(getActivity(), "当前已是最新内容O(∩_∩)O~", Toast.LENGTH_LONG).show();
            }
        }, 1000 * 2);
    }

    @Override
    public TopNewsFragment getFragment() {
        return this;
    }


    //懒加载模块
    @Override
    protected void loadData() {
        super.loadData();
        if (lastTime == 0L) {
            lastTime = System.currentTimeMillis();
        }
        mPresenter.start();
    }
}
