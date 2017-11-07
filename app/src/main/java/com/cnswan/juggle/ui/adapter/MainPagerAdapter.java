package com.cnswan.juggle.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cnswan.juggle.ui.fragment.HelpFragment;
import com.cnswan.juggle.ui.fragment.MainFragment;

/**
 * Created by cnswan on 2017/11/2.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int PAGE_COUNT         = 2;
    public static final int PAGE_POSITION_MAIN = 0;
    public static final int PAGE_POSITION_HELP = 1;


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == PAGE_POSITION_MAIN) {
            return MainFragment.newInstance();
        } else if (position == PAGE_POSITION_HELP) {
            return HelpFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
