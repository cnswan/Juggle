package com.cnswan.juggle.amvp;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cnswan.juggle.R;
import com.cnswan.juggle.utils.CommonUtils;
import com.cnswan.juggle.utils.StatusBarUtil;
import com.cnswan.juggle.utils.StatusBarUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;

public abstract class BaseHeaderActivity extends AppCompatActivity {

    private LinearLayout llProgressBar;
    private View refresh;

    // 这个是高斯图背景的高度
    private int imageBgHeight;
    // 滑动多少距离后标题不透明
    private int slidingDistance;



    //    private ImageView imgItemBg;
    private Toolbar tbBaseTitle;
    private ImageView ivBaseTitlebarBg;


    private AnimationDrawable mAnimationDrawable;

    protected View titleView;
    protected View headerView;
    protected View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_header);

        titleView = getLayoutInflater().inflate(R.layout.base_header_title_bar, null, false);
        headerView = getLayoutInflater().inflate(getHeaderLayout(), null, false);
        contentView = getLayoutInflater().inflate(getContentLayout(), null, false);
        initHeaderView(headerView);
        initContentView(contentView);
        //添加标题部分
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleView.setLayoutParams(titleParams);
        RelativeLayout mTitleContainer = (RelativeLayout) findViewById(R.id.title_container);
        mTitleContainer.addView(titleView);

        //添加头部
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(headerParams);
        RelativeLayout mHeaderContainer = (RelativeLayout) findViewById(R.id.header_container);
        mHeaderContainer.addView(headerView);

        //添加内容部分
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout) findViewById(R.id.container);
        mContainer.addView(contentView);

//        imgItemBg = getView(R.id.img_item_bg);

        ImageView img = getView(R.id.img_progress);
        tbBaseTitle = (Toolbar) titleView.findViewById(R.id.tb_base_title);
        ivBaseTitlebarBg = (ImageView) titleView.findViewById(R.id.iv_base_titlebar_bg);

        llProgressBar = getView(R.id.ll_progress_bar);
        refresh = getView(R.id.ll_error_refresh);

        // 初始化滑动渐变
        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());

        // 设置toolbar
        setToolBar();


        // 加载动画
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        // 默认进入页面就开启动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        // 点击加载失败布局
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                onRefresh();
            }
        });
        contentView.setVisibility(View.GONE);
    }

    public void initNestedScrollView(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    }

    protected abstract void initHeaderView(View headerView);

    protected abstract void initContentView(View contentView);


    abstract protected int getContentLayout();

    abstract protected int getHeaderLayout();


    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }


    /**
     * 1. 标题
     */
    public void setTitle(CharSequence text) {
        tbBaseTitle.setTitle(text);
    }

    /**
     * 2. 副标题
     */
    protected void setSubTitle(CharSequence text) {
        tbBaseTitle.setSubtitle(text);
    }


    /**
     * *** 初始化滑动渐变 一定要实现 ******
     *
     * @param imgUrl    header头部的高斯背景imageUrl
     * @param mHeaderBg header头部高斯背景ImageView控件
     */
    protected void initSlideShapeTheme(String imgUrl, ImageView mHeaderBg) {
        setImgHeaderBg(imgUrl);

        // toolbar 的高
        int toolbarHeight = tbBaseTitle.getLayoutParams().height;
        final int headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this);

        // 使背景图向上移动到图片的最低端，保留（titlebar+statusbar）的高度
        ViewGroup.LayoutParams params = ivBaseTitlebarBg.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) ivBaseTitlebarBg
                .getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);

        ivBaseTitlebarBg.setImageAlpha(0);
        StatusBarUtils.setTranslucentImageHeader(this, 0, tbBaseTitle);

        // 上移背景图片，使空白状态栏消失(这样下方就空了状态栏的高度)
        if (mHeaderBg != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mHeaderBg.getLayoutParams();
            layoutParams.setMargins(0, -StatusBarUtil.getStatusBarHeight(this), 0, 0);

            ViewGroup.LayoutParams imgItemBgparams = mHeaderBg.getLayoutParams();
            // 获得高斯图背景的高度
            imageBgHeight = imgItemBgparams.height;
        }

        // 变色
        initScrollViewListener();
        initNewSlidingParams();
    }


    /**
     * 加载titlebar背景
     */
    private void setImgHeaderBg(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {

            // 高斯模糊背景 原来 参数：12,5  23,4
            Glide.with(this).load(imgUrl)
                    .error(R.drawable.stackblur_default)
                    .bitmapTransform(new BlurTransformation(this, 23, 4)).listener(new RequestListener<String,
                    GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
                        isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                               boolean isFromMemoryCache, boolean isFirstResource) {
                    tbBaseTitle.setBackgroundColor(Color.TRANSPARENT);
                    ivBaseTitlebarBg.setImageAlpha(0);
                    ivBaseTitlebarBg.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(ivBaseTitlebarBg);
        }
    }

    private void initScrollViewListener() {
        // 为了兼容23以下
        ((NestedScrollView) findViewById(R.id.mns_base)).setOnScrollChangeListener(new NestedScrollView
                .OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
                initNestedScrollView(v, scrollX, scrollY, oldScrollX, oldScrollY);
            }
        });
    }

    /**
     * 根据页面滑动距离改变Header方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);

        Drawable drawable = ivBaseTitlebarBg.getDrawable();

        if (drawable == null) {
            return;
        }
        if (scrolledY <= slidingDistance) {
            // title部分的渐变
            drawable.mutate().setAlpha((int) (alpha * 255));
            ivBaseTitlebarBg.setImageDrawable(drawable);
        } else {
            drawable.mutate().setAlpha(255);
            ivBaseTitlebarBg.setImageDrawable(drawable);
        }
    }

    private void initNewSlidingParams() {
        int titleBarAndStatusHeight = (int) (CommonUtils.getDimens(R.dimen.nav_bar_height) + StatusBarUtil
                .getStatusBarHeight(this));
        // 减掉后，没到顶部就不透明了
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - (int) (CommonUtils.getDimens(R.dimen
                .base_header_activity_slide_more));
    }


    abstract protected String setHeaderImgUrl();

    abstract protected ImageView setHeaderImageView();

    /**
     * 设置toolbar
     */
    protected void setToolBar() {
        setSupportActionBar(tbBaseTitle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        // 手动设置才有效果
        tbBaseTitle.setTitleTextAppearance(this, R.style.ToolBar_Title);
        tbBaseTitle.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitle);
//        tbBaseTitle.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.actionbar_more));
        tbBaseTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    protected void showLoading() {
        if (llProgressBar.getVisibility() != View.VISIBLE) {
            llProgressBar.setVisibility(View.VISIBLE);
        }
        // 开始动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (contentView.getVisibility() != View.GONE) {
            contentView.setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
        Log.e("###", "加载中ing...");
    }

    protected void showContentView() {
        if (llProgressBar.getVisibility() != View.GONE) {
            llProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
        if (contentView.getVisibility() != View.VISIBLE) {
            contentView.setVisibility(View.VISIBLE);
        }

    }

    protected void showError() {
        if (llProgressBar.getVisibility() != View.GONE) {
            llProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (refresh.getVisibility() != View.VISIBLE) {
            refresh.setVisibility(View.VISIBLE);
        }
        if (contentView.getVisibility() != View.GONE) {
            contentView.setVisibility(View.GONE);
        }

        Log.e("###", "加载失败了");
    }

    /**
     * 失败后点击刷新
     */
    protected void onRefresh() {

    }
}
