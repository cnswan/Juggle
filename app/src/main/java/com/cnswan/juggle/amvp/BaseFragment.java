package com.cnswan.juggle.amvp;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cnswan.juggle.R;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public abstract class BaseFragment extends Fragment {

    // fragment是否显示了,叫做isViewCreate更直接吧;
    protected boolean mIsVisible = false;

    // 加载中,一进来是有的,数据加载完设置为gone;
    private LinearLayout mLlProgressBar;

    // 加载失败
    private LinearLayout mLlRefresh;

    // 内容布局
    protected RelativeLayout mRlContainer;

    // 动画
    private AnimationDrawable mAnimationDrawable;

    private ImageView img;


    //rx部分,用来收集所有的subscriber(订阅者),接下来在Activity onPause或者onDestroy时候统一取消订阅，避免造成内存泄漏
    //移除订阅之后,就不能再订阅了,除非是重新创建一个该对象;
    private CompositeSubscription mCompositeSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View baseView = inflater.inflate(R.layout.fragment_base, container, false);

        //先把要准备的加载的布局初始化好;
        mLlProgressBar = (LinearLayout) baseView.findViewById(R.id.ll_progress_bar);
        mLlRefresh = (LinearLayout) baseView.findViewById(R.id.ll_error_refresh);
        mRlContainer = (RelativeLayout) baseView.findViewById(R.id.container);
        img = (ImageView) baseView.findViewById(R.id.img_progress);
        // 加载动画
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        //showLoading(); //先showloading吧;
        //NOTE:不知道这一步有没有问题;
        View contentView = inflater.inflate(getFragmentContent(), null);
        initContentView(contentView);
        mRlContainer.addView(contentView);
        return baseView;
    }

    public abstract void initContentView(View contentView);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // 点击加载失败布局
        mLlRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                onRefresh();
            }
        });
    }

    /**
     * 为BaseFragment设置内同;
     */
    public abstract int getFragmentContent();

    public abstract void onRefresh();

    /**
     * 显示加载中状态,主要是播放动画;什么时候调用,在界面显示后,加载数据之前调用;
     */
    public void showLoading() {
        Log.d("###", "showloading....");
        if (mLlProgressBar.getVisibility() != View.VISIBLE) {
            mLlProgressBar.setVisibility(View.VISIBLE);
        }
        // 开始动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (mRlContainer.getVisibility() != View.GONE) {
            mRlContainer.setVisibility(View.GONE);
        }
        if (mLlRefresh.getVisibility() != View.GONE) {
            mLlRefresh.setVisibility(View.GONE);
        }
    }

    /**
     * 加载完成的状态,加载中==>gone;动画播放停止;无法加载==>gone; bindingView显示出来...
     */
    public void showContent() {
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (mLlRefresh.getVisibility() != View.GONE) {
            mLlRefresh.setVisibility(View.GONE);
        }
        if (mRlContainer.getVisibility() != View.VISIBLE) {
            mRlContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载失败点击重新加载的状态
     */
    public void showError() {
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (mLlRefresh.getVisibility() != View.VISIBLE) {
            mLlRefresh.setVisibility(View.VISIBLE);
        }
        if (mRlContainer.getVisibility() != View.GONE) {
            mRlContainer.setVisibility(View.GONE);
        }
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeSubscription();
    }


    public void removeSubscription() {
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }


    //#################################################################
    //Fragment的View加载完毕的标记,在onCreateView方法调用的最后将该值进行设置;
    private boolean isViewCreated;

    //Fragment对用户可见的标记,在setUserVisibleHint方法中根据传入的参数设置该值
    private boolean isUIVisible;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见,这是系统判断的;
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    protected void loadData() {
        Log.e("###", "我开始显示了");
        mAnimationDrawable.start();
        //showLoading();
    }
}
