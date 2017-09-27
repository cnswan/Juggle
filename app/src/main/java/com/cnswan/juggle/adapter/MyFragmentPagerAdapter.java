package com.cnswan.juggle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 使用的ViewPager+Fragment的架构,并为ViewPager添加了标题栏,但是没有使用懒加载技术;
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<?>      mFragments;
    private List<String> mTitles;

    public MyFragmentPagerAdapter(FragmentManager fm, List<?> mFragment) {
        super(fm);
        this.mFragments = mFragment;
    }

    public MyFragmentPagerAdapter(FragmentManager fm, List<?> mFragment, List<String> mTitles) {
        super(fm);
        this.mFragments = mFragment;
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position  );
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
        // super.destroyItem(container, position, object);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return android.support.v4.view.PagerAdapter.POSITION_NONE;
    }

}
