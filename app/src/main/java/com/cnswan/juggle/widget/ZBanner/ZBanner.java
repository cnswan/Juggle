package com.cnswan.juggle.widget.ZBanner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnswan.juggle.R;

import java.util.ArrayList;
import java.util.List;

public class ZBanner extends FrameLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = "CycleViewPager";

    private Context mContext;

    private ViewPager mViewPager;//实现轮播图的ViewPager

    private TextView mTitle;//标题

    private LinearLayout mIndicatorLayout; // 指示器

    private Handler handler;//每几秒后执行下一张的切换

    private int WHEEL = 100; // 转动

    private int WHEEL_WAIT = 101; // 等待

    private List<View> mViews = new ArrayList<>(); //需要轮播的View，数量为轮播图数量+2

    private ImageView[] mIndicators;    //指示器小圆点.//数量和轮播图数量相等

    private boolean isScrolling = false; // 滚动框是否滚动着

    private boolean isCycle = true; // 是否循环，默认为true(是否从最后一张跳到第一张)

    private boolean isWheel = true; // 是否轮播，默认为true(是否从当前页跳到 下一页)

    private int delay = 4000; // 默认轮播时间

    private int mCurrentPosition = 0; // 轮播当前位置

    private long releaseTime = 0; // 手指松开、页面不滚动时间，防止手机松开后短时间进行切换

    private ViewPagerAdapter mAdapter;

    private ImageCycleViewListener mImageCycleViewListener;  //在外部调用时通过setData传入的;

    private List<ZBannerBean> infos;//数据集合

    private int mIndicatorSelected;//指示器图片，被选择状态

    private int mIndicatorUnselected;//指示器图片，未被选择状态

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mContext != null && isWheel) {
                long now = System.currentTimeMillis();
                // 检测上一次滑动时间与本次之间是否有触击(手滑动)操作，有的话等待下次轮播
                if (now - releaseTime > delay - 500) {
                    handler.sendEmptyMessage(WHEEL);
                } else {
                    handler.sendEmptyMessage(WHEEL_WAIT);
                }
            }
        }
    };

    public ZBanner(Context context) {
        this(context, null);
    }

    public ZBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        //我感觉这个this指的是在MainActivity中的外层布局;TODO:
        LayoutInflater.from(mContext).inflate(R.layout.layout_cycle_view, this, true);
        mViewPager = (ViewPager) findViewById(R.id.cycle_view_pager);
        mTitle = (TextView) findViewById(R.id.cycle_title);
        mIndicatorLayout = (LinearLayout) findViewById(R.id.cycle_indicator);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == WHEEL && mViews.size() > 0) {
                    if (!isScrolling) {
                        //当前为非滚动状态，(可以)切换到下一页
                        int posttion = (mCurrentPosition + 1) % mViews.size();
                        mViewPager.setCurrentItem(posttion, true); //平稳过渡
                    }
                    releaseTime = System.currentTimeMillis();
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, delay);
                    return;
                }
                if (msg.what == WHEEL_WAIT && mViews.size() > 0) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, delay);
                }
            }
        };
    }

    /**
     * 设置指示器图片，在setData之前调用
     *
     * @param select   选中时的图片
     * @param unselect 未选中时的图片
     */
    public void setIndicators(int select, int unselect) {
        mIndicatorSelected = select;
        mIndicatorUnselected = unselect;
    }
    public void setData(List<ZBannerBean> list, ImageCycleViewListener listener) {
        setData(list, listener, 0);
    }

    /**
     * 初始化viewpager
     *
     * @param list         要显示的数据
     * @param showPosition 默认显示位置
     */
    public void setData(List<ZBannerBean> list, ImageCycleViewListener listener,
                        int showPosition) {
        if (list == null || list.size() == 0) {
            //没有数据时隐藏整个布局...
            this.setVisibility(View.GONE);
            return;
        }
        //上来就把mViews清空...,因为现在被调用了setData,传入了数据;
        mViews.clear();
        infos = list;
        if (isCycle) { //循环;
            //添加轮播图View，数量为集合数+2
            // 将最后一个View添加进来
            mViews.add(getImageView(mContext, infos.get(infos.size() - 1).getUrl()));
            for (int i = 0; i < infos.size(); i++) {
                mViews.add(getImageView(mContext, infos.get(i).getUrl()));
            }
            // 将第一个View添加进来
            mViews.add(getImageView(mContext, infos.get(0).getUrl()));
        } else { //如果不循环的话;
            //只添加对应数量的View
            for (int i = 0; i < infos.size(); i++) {
                mViews.add(getImageView(mContext, infos.get(i).getUrl()));
            }
        }
        //证明没有调用setData,没有传入数据
        if (mViews == null || mViews.size() == 0) {
            //没有View时隐藏整个布局
            this.setVisibility(View.GONE);
            return;
        }
        mImageCycleViewListener = listener;
        int ivSize = mViews.size();
        // 设置指示器
        mIndicators = new ImageView[ivSize];
        if (isCycle) { //循环的话,数量-2,本应也是这样;
            mIndicators = new ImageView[ivSize - 2];
        }
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < mIndicators.length; i++) {
            mIndicators[i] = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 0, 10, 0);
            mIndicators[i].setLayoutParams(lp);
            mIndicatorLayout.addView(mIndicators[i]);
        }
        mAdapter = new ViewPagerAdapter();
        // 默认指向第一项，下方viewPager.setCurrentItem将触发重新计算指示器指向
        setIndicator(0);
        mViewPager.setOffscreenPageLimit(3);  //这里的轮播图片在屏幕上时4个,现在设置了缓存是3;有点多了吧
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
        if (showPosition < 0 || showPosition >= mViews.size())
            showPosition = 0;
        if (isCycle) {
            showPosition = showPosition + 1;
        }
        mViewPager.setCurrentItem(showPosition);
        setWheel(true);//设置轮播
    }

    /**
     * 获取轮播图View
     *
     * @param context
     * @param url
     */
    /*private View getImageView(Context context, String url) {
        return SecondActivity.getImageView(context, url);
    }*/

    /**
     * 设置指示器
     *
     * @param selectedPosition 默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        setText(mTitle, infos.get(selectedPosition).getTitle());
        try {
            for (int i = 0; i < mIndicators.length; i++) {
                mIndicators[i]
                        .setBackgroundResource(mIndicatorUnselected);
            }
            if (mIndicators.length > selectedPosition)
                mIndicators[selectedPosition]
                        .setBackgroundResource(mIndicatorSelected);
        } catch (Exception e) {
            Log.i(TAG, "指示器路径不正确");
        }
    }

    /**
     * 页面适配器 返回对应的view
     *
     * @author Yuedong Li
     */
    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViews.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            View v = mViews.get(position);
            if (mImageCycleViewListener != null) {
                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageCycleViewListener.onImageClick(
                                infos.get(mCurrentPosition - 1), mCurrentPosition, v);
                    }
                });
            }
            container.addView(v);
            return v;
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    //手指滑动的时候;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    //滑动到某一页的时候,要不断实时的改变其currentItem;
    @Override
    public void onPageSelected(int arg0) {
        int max = mViews.size() - 1;
        int position = arg0;//当前位置;
        mCurrentPosition = arg0;
        if (isCycle) {
            if (arg0 == 0) {
                //滚动到mView的1个（界面上的最后一个），将mCurrentPosition设置为max - 1
                mCurrentPosition = max - 1;
            } else if (arg0 == max) {
                //滚动到mView的最后一个（界面上的第一个），将mCurrentPosition设置为1
                mCurrentPosition = 1;
            }
            position = mCurrentPosition - 1;
        }
        setIndicator(position);
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) { // viewPager在滚动
            isScrolling = true;
            return;
        } else if (state == 0) { // viewPager滚动结束

            releaseTime = System.currentTimeMillis();
            //跳转到第mCurrentPosition个页面（没有动画效果，实际效果页面上没变化）
            mViewPager.setCurrentItem(mCurrentPosition, false);
        }
        isScrolling = false;
    }

    /**
     * 为textview设置文字
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, String text) {
        if (text != null && textView != null) textView.setText(text);
    }

    /**
     * 为textview设置文字
     *
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, int text) {
        if (textView != null) setText(textView, text + "");
    }

    /**
     * 是否循环，默认开启。必须在setData前调用
     *
     * @param isCycle 是否循环
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 是否处于循环状态
     *
     * @return
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * 设置是否轮播，默认轮播,轮播一定是循环的
     *
     * @param isWheel
     */
    public void setWheel(boolean isWheel) {
        this.isWheel = isWheel;
        isCycle = true;
        if (isWheel) {
            handler.postDelayed(runnable, delay);
        }
    }

    /**
     * 刷新数据，当外部视图更新后，通知刷新数据
     */
    public void refreshData() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    /**
     * 是否处于轮播状态
     *
     * @return
     */
    public boolean isWheel() {
        return isWheel;
    }

    /**
     * 设置轮播暂停时间,单位毫秒（默认4000毫秒）
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public static interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param bean
         * @param position
         * @param imageView
         */
        public void onImageClick(ZBannerBean bean, int position, View imageView);
    }

    public static View getImageView(Context context, String url) {
        RelativeLayout rl = new RelativeLayout(context);
        //添加一个ImageView，并加载图片
        ImageView imageView = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParams);
        //使用Picasso来加载图片
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        //在Imageview前添加一个半透明的黑色背景，防止文字和图片混在一起
        ImageView backGround = new ImageView(context);
        backGround.setLayoutParams(layoutParams);
        backGround.setBackgroundResource(R.color.cycle_image_bg);
        rl.addView(imageView);
        rl.addView(backGround);
        return rl;
    }
}
