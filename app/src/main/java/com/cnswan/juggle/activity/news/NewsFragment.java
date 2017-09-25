package com.cnswan.juggle.activity.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnswan.juggle.R;
import com.cnswan.juggle.activity.news.movie.MovieFragment;
import com.cnswan.juggle.activity.news.overview.OverViewFragment;
import com.cnswan.juggle.activity.news.tech.TechNewsFragment;
import com.cnswan.juggle.activity.news.top.TopNewsFragment;
import com.cnswan.juggle.adapter.MyFragmentPagerAdapter;
import com.cnswan.juggle.module.rxjava.RxBus;
import com.cnswan.juggle.module.rxjava.RxBusBaseMessage;
import com.cnswan.juggle.module.rxjava.RxCodeConstants;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class NewsFragment extends Fragment {
    private ArrayList<String>   mTitleList = new ArrayList<>(4);
    private ArrayList<Fragment> mFragments = new ArrayList<>(4);

    TabLayout gank_tab;
    ViewPager gank_vp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        gank_tab = (TabLayout) view.findViewById(R.id.tab_gank);
        gank_vp = (ViewPager) view.findViewById(R.id.vp_gank);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFragmentList();
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻3个实例，切换时不会卡
         * 但会浪费流量，在显示时加载数据
         */
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments,
                mTitleList);
        gank_vp.setAdapter(myAdapter);
        // 左右预加载页面的个数,没必要吧...
        gank_vp.setOffscreenPageLimit(3);
        myAdapter.notifyDataSetChanged();
        gank_tab.setTabMode(TabLayout.MODE_FIXED);
        gank_tab.setupWithViewPager(gank_vp);
        initRxBus();
    }

    private void initFragmentList() {
        mTitleList.add("每日推荐");
        mTitleList.add("今日头条");
        mTitleList.add("科技范儿");
        mTitleList.add("热映电影");
        mFragments.add(new OverViewFragment());
        mFragments.add(new TopNewsFragment());
        mFragments.add(new TechNewsFragment());
        mFragments.add(new MovieFragment());
    }


    //TODO:需要注意的是:避免内存泄露;
    private void initRxBus() {
        RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TO_SUB, RxBusBaseMessage.class)
                .subscribe(new Consumer<RxBusBaseMessage>() {
                    @Override
                    public void accept(RxBusBaseMessage rxBusBaseMessage) throws Exception {
                        gank_vp.setCurrentItem(rxBusBaseMessage.getCode());
                    }
                });
    }

}
