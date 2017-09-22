package com.cnswan.juggle.activity.picture;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.zx.freetime.R;
import com.zx.freetime.adapter.PictureAdapter;
import com.zx.freetime.base.BaseFragment;
import com.zx.freetime.bean.picture.PictureBean;
import com.zx.freetime.utils.Constants;
import com.zx.freetime.widget.ZRecyclerView.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class PictureFragment extends BaseFragment implements PictureContract.View {
    public ArrayList<String> mUrls = new ArrayList<>(); //刚一开始是空的;
    private XRecyclerView mRecyclerView;
    private PictureAdapter mAdapter;
    PictureContract.Presenter mPresenter;
    private int mPage = 1;


    @Override
    public void initContentView(View contentView) {
        mRecyclerView = (XRecyclerView) contentView.findViewById(R.id.pic_rv);
        initRecyclerView();
        mPresenter = new PicturePresenter(this);
    }

    @Override
    public int getFragmentContent() {
        return R.layout.fragment_pic;
    }

    @Override
    public void onRefresh() {
        //TODO:暂时还未实现图片的缓存功能,图片的缓存功能
        Toast.makeText(getActivity(), "请检查您的网络是否连接", Toast.LENGTH_LONG).show();
//        mPresenter.getPictures(Constants.TECH_NEWS_ALL);
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setPullRefreshEnabled(false); //禁止下拉刷新吧,但是可以上拉加载更多;
        mAdapter = new PictureAdapter(getActivity(), mUrls);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
                //禁止下拉刷新了;
            }

            @Override
            public void onLoadMore() {
                mPresenter.getPictures(++mPage);
            }
        });
    }

    @Override
    public void load(List<PictureBean.ResultBean> datas) {
        for (int i = 0; i < datas.size(); i++) {
            mUrls.add(datas.get(i).getUrl());
        }
    }

    @Override
    public void showErrorView() {
        showError();
    }

    @Override
    public void showNormalView() {
        //NOTE:特别注意:这是在收集收据结束之后回到的,所以,请务必执行
        mRecyclerView.refreshComplete();
        mAdapter.notifyDataSetChanged();
        //
        showContent();
    }

    @Override
    public PictureFragment getFragment() {
        return this;
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.start();
    }
}
