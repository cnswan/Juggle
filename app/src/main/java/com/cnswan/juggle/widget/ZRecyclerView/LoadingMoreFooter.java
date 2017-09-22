package com.cnswan.juggle.widget.ZRecyclerView;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.freetime.R;


public class LoadingMoreFooter extends LinearLayout {

    public final static int STATE_LOADING = 0;      //正在加载;
    public final static int STATE_COMPLETE = 1;     //加载完成;
    public final static int STATE_NOMORE = 2;       //已经没有更多了;
    private TextView mText;
    private AnimationDrawable mAnimationDrawable;   //加载的动画;
    private ImageView mIvProgress;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    /**
     * 拉到吧,这个构造方法就没有用过...
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.yun_refresh_footer, this);
        mText = (TextView) findViewById(R.id.msg);
        mIvProgress = (ImageView) findViewById(R.id.iv_progress);
        mAnimationDrawable = (AnimationDrawable) mIvProgress.getDrawable();
        //初始化肯定先停止动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                if (!mAnimationDrawable.isRunning()) {
                    mAnimationDrawable.start();
                }
                mIvProgress.setVisibility(View.VISIBLE);
                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                if (mAnimationDrawable.isRunning()) {
                    mAnimationDrawable.stop();
                }
                //还有必要显示文字吗...
                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                if (mAnimationDrawable.isRunning()) {
                    mAnimationDrawable.stop();
                }
                mText.setText(getContext().getText(R.string.nomore_loading));
                mIvProgress.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void reSet() {
        this.setVisibility(GONE);
    }
}
