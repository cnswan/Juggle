package com.cnswan.juggle.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.cnswan.juggle.R;
import com.cnswan.juggle.ui.adapter.MainPagerAdapter;
import com.cnswan.juggle.utils.SnackbarUtils;

/**
 * 主页
 * Created by cnswan on 2017/11/1.
 */

public class MainActivity extends ManagedActivity {

    private DrawerLayout         mDrawerLayout;
    private CoordinatorLayout    mCoordinatior;
    private NavigationView       mNavView;
    private Toolbar              mToolbar;
    private ViewPager            mViewPager;
    private BottomNavigationView mNavigationView;
    private FloatingActionButton mFloatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewData();
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        mCoordinatior = findViewById(R.id.main_coordinator);
        mNavView = findViewById(R.id.main_nav_view);
        mToolbar = findViewById(R.id.main_toolbar);
        mViewPager = findViewById(R.id.main_view_pager);
        mNavigationView = findViewById(R.id.main_bottom_navigation);
        mFloatButton = findViewById(R.id.main_float_button);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

    private void initViewData() {
        mNavigationView.setOnNavigationItemSelectedListener(mMainNavChange);
        mViewPager.addOnPageChangeListener(mMainPageChange);
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtils.with(mCoordinatior).setMessage(getString(R.string.stay_tuned)).showSuccess();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mMainNavChange = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.i_home:
                    mViewPager.setCurrentItem(0, true);
                    break;
                case R.id.i_help:
                    mViewPager.setCurrentItem(1, true);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private ViewPager.SimpleOnPageChangeListener mMainPageChange = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mNavigationView.setSelectedItemId(R.id.i_home);
                    break;
                case 1:
                    mNavigationView.setSelectedItemId(R.id.i_help);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
