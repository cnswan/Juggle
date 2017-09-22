package com.cnswan.juggle.widget.ZRecyclerView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnswan.juggle.R;

/**
 * Created by yangcai on 2016/1/27.
 * 针对云音乐的下拉刷新头
 */
public class YunRefreshHeader extends LinearLayout implements BaseRefreshHeader {
    private Context mContext;
    private AnimationDrawable animationDrawable; //动画属性;
    private TextView msg;
    private int mState = STATE_NORMAL;          //正常状态;
    private int mMeasuredHeight;                //下拉刷新头的高度;
    private LinearLayout mContainer;

    //真正使用的是这个构造方法;下面的两个外部没有使用;
    public YunRefreshHeader(Context context) {
        this(context, null);
    }

    public YunRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YunRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); //这个方法已经创建了一个LinearLayout;
        this.mContext = context;
        initView();
    }

    private void initView() {
        //首先要明确一点:我们这个下拉刷新头是一个LinearLayout;
        //加载子布局,直接添加到我们的LinearLayout(刷新头)上
        LayoutInflater.from(mContext).inflate(R.layout.kaws_refresh_header, this);

        //拿到Img,因为我们要显示一个动画;
        ImageView img = (ImageView) findViewById(R.id.img);

        animationDrawable = (AnimationDrawable) img.getDrawable();
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }

        //拿到文字显示部分,因为我们要针对不同的状态显示不同的文字;
        msg = (TextView) findViewById(R.id.msg);

        //想错了,这一步是在测量自己,拿到作为刷新头的高度;
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();//拿到的是整个header作为LinearLayout的高度;
        setGravity(Gravity.CENTER_HORIZONTAL);
        //将container设置为match_parent,但是第二个参数是0,什么意思,高度是0???
        //get it;这里初始化的时候设置为0,在界面上就不显示了,因为高度是0嘛,然后在下拉刷新的过程中,不断的改变这个高度...妙啊
        mContainer = (LinearLayout) findViewById(R.id.container);
        mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        //将整个下拉刷新头设置为: 宽度:匹配父容器      高度:包裹内容;
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }


    //啥意思?只有可见高度>0的情况才可以移动,那么初始的高度为0,如何改变呢?感觉是在smoothScrollTo;
    //NOTE:问题在这.一定是收缩的时候,不断的调用onMove,在这里更新了状态;
    @Override
    public void onMove(float delta) {
        if (getVisiableHeight() > 0 || delta > 0) {
            setVisiableHeight((int) delta + getVisiableHeight()); //更新高度;
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisiableHeight() > mMeasuredHeight) {  //如果此时高度大于了刷新头的高度,那么状态更新为释放刷新;
                    setState(STATE_RELEASE_TO_REFRESH);
                } else { //如果此时还没有达到刷新头的高度,那么状态依旧为normal;
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    private void setState(int state) {
        //如果状态相同,那么说明也不错;
        if (state == mState) return;
        switch (state) {
            case STATE_NORMAL:
                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                msg.setText(R.string.listview_header_hint_normal);
                //if (mState==STATE_DONE){}//可以在这添加代码避免尴尬;
                break;
            case STATE_RELEASE_TO_REFRESH:
                //此时对应的情况是:mState此时还是normal的状态,之前一直显示ide是下拉刷新;
                //TODO:这里有个优化的地方,原来的版本中刷新完成后自动隐藏,还会显示一个下拉刷新的状态...在哪里优化呢?不如我们每次为0的时候就设置状态为0吧;gl好像是这么做的;;;还是在回滚的时候不更新状态;在这里判断一下,如果mState是刷新完成的,那么就不更新文字吧
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    if (!animationDrawable.isRunning()) {
                        animationDrawable.start();
                    }
                    msg.setText(R.string.listview_header_hint_release);
                }
                break;
            case STATE_REFRESHING:
                msg.setText(R.string.refreshing);
                break;
            case STATE_DONE:
                msg.setText(R.string.refresh_done);
                break;
            default:
        }
        mState = state;//最后才设置mState;
    }

    //释放的动作,我猜着是在RecyclerView中的状态监听里调用的
    // 主要是判断手指释放屏幕的时候是否在刷新;
    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;//首先设置默认当前没有在刷新,然后设置状态;
        int height = getVisiableHeight();//拿到刷新头的高度;如果是0,表明你不是在刷新,返回false;
        if (height == 0) // not visible,此时还是不可见;
            isOnRefresh = false;

        //如果此时的状态是0或者释放刷新,那么设置状态为刷新,状态更新为返回true;
        if (getVisiableHeight() > mMeasuredHeight && mState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.会出现这种情况吗??????????
        if (mState == STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;NOTE:感觉这里应该设置为 mState == STATE_RELEASE_TO_REFRESH,一会儿可以打log看看原来的那句会不会执行;
        }


        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.我们不是已经在上一步设置成了STATE_REFRESHING了嘛,所以会执行这一步;
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        //但是也有可能你状态1,然后你就放开了,那么默认还是回滚
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    //刷新完成,我们需要手动抛出complate()这人的英语水平不行啊.......
    @Override
    public void refreshComplate() {
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, 500);
    }

    public void reset() {
        smoothScrollTo(0);
        setState(STATE_NORMAL);
    }


    //不断滑动的时候;传入的参数是目标高度;那么动画就是从当前可见的高度到目标高度,可能是递增,也可能是递减;
    private void smoothScrollTo(int destHeight) {
        //这里弄了一个动画;
        ValueAnimator animator = ValueAnimator.ofInt(getVisiableHeight(), destHeight);
        animator.setDuration(300).start();//你在这启动干嘛???
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisiableHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();//这又启动了一遍...NOTE:一会儿把这个删掉试试;
    }

    private void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        //
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.height = height;
        //不用担心,在setLayoutparams()方法中,会调用requestLayout()方法的;
        mContainer.setLayoutParams(lp);
    }

    //接口中的方法;
    @Override
    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

    public int getState() {
        return mState;
    }
}
