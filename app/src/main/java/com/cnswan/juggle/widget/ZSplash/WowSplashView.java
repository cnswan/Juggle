package com.cnswan.juggle.widget.ZSplash;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zx.freetime.R;


/**
 * Created by zhangxin on 2017/3/19 0019.
 * <p>
 * Description :
 * 自定义的splash 的 view
 */
public class WowSplashView extends View {
    private float mLength;  //铁塔的长度???
    private PathMeasure mTowerPathMeasure;  //这个对象中保存着铁塔svg图片的测量信息;eg:path的长度和一个点的切线;
    private Path mTowerPath; //用来画铁塔的路径;看一下是怎么创建的;
    private float mAnimatorValue; //这个值是不断变化的,去的就是铁塔路径的多少;
    private Path mTowerDst; //没什么特别的,就是创建了一个path
    private Paint mPaint;       //画笔
    private boolean isAnimateEnd;       //动画是否结束;
    private String mTitle = "休闲时光";  //在splash页面显示的产品名称;

    public static final float SCALE = 2f;  //扩大倍数;
    public static float translateX;    //水平移动距离;
    public static float translateY;     //垂直移动距离;

    // from the svg file,也就是说是固定的值咯;
    //NOTE:这个大小能调吧;
    private int mTowerHeight = 600;
    private int mTowerWidth = 440;

    //画云的路径;接下来设置移动有几朵云;
    private Path[] mCouldPaths;  //构造方法中创建了长度为4,然后初始化4个path;
    private int couldCount = 4;

    //四朵云的起始位置;
    private float mCouldX[] = {0f, 100f, 350f, 400f};
    private float mCouldY[] = {-5000f, -5000f, -5000f, -5000f};
    private float mCouldFinalY[] = {100f, 80f, 200f, 300f};

    //持续时间;
    private long mDuration = 5000;
    private int mWidth;  //当前view的宽度;
    private float mTitleY;
    private float mFinalTitleY = mTowerHeight + 100;

    //结束时候的监听;
    private OnEndListener mListener;
    private int mAlpha;  //透明度的变化;这个值在铁塔画完之后开始设置,不断的透明;

    public WowSplashView(Context context) {
        this(context, null);
    }

    public WowSplashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WowSplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置背景颜色,我们这个矢量图没有背景色;设置为官方红;
        setBackgroundColor(context.getResources().getColor(R.color.colorTheme));
        setDrawingCacheEnabled(true);//允许缓存;那么下次再使用的时候可以getDrawingCache();

        if (Build.VERSION.SDK_INT < 21) { //小于21的版本;5.0一下;
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mPaint = new Paint();

        //init TowerPath,初始化铁塔的路径;
        mTowerPath = new SvgPathParser().parsePath(context.getResources().getString(R.string.path_00));
        //参数1:通过上一步传入的路径;参数2:是否是闭合的;
        mTowerPathMeasure = new PathMeasure(mTowerPath, true);

        mLength = mTowerPathMeasure.getLength();
        mTowerDst = new Path();

        mCouldPaths = new Path[couldCount];

        for (int i = 0; i < mCouldPaths.length; i++) {
            mCouldPaths[i] = new Path();
        }
    }

    //居然是view中的方法;应该会被不断的调用吧;这里使用该方法的目的就是在view被添加的时候获取到该view的库阿奴;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    //画Cloud 路径的方法;
    private void setupCouldPath(Path path, int pos) {
        path.reset();
        path.moveTo(mCouldX[pos], mCouldY[pos]);
        path.lineTo(mCouldX[pos] + 30, mCouldY[pos]);
        path.quadTo(mCouldX[pos] + 30 + 30, mCouldY[pos] - 50, mCouldX[pos] + 30 + 60, mCouldY[pos]);
        path.lineTo(mCouldX[pos] + 30 + 60 + 30, mCouldY[pos]);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        //这里SVG过小  就临时这样适配一下。。
        canvas.scale(SCALE, SCALE);//将canvas扩大两倍;
        translateX = (mWidth - mTowerWidth * SCALE) / 2 - 80;
        translateY = 100;
        canvas.translate(translateX, translateY); //将canvas移动;

        mTowerDst.reset();//怪不得你每次都reset,因为你每次都是重新画的;
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        //canvas.drawPath(mTowerPath, paint);

        float stop = mLength * mAnimatorValue;//这还没开始初始化吧;每次都是叠加的...
        //给出一个起始和结束的位置,然后就把起始和结束的位置之间的path返回给第三个参数中;这样mTowerDst不就有值了吗;
        //其实是这样的,这个onDraw会被调用多次;
        mTowerPathMeasure.getSegment(0, stop, mTowerDst, true);

        drawTower(canvas);//开始画塔;

        mPaint.setAlpha(255);

        drawCould(canvas);

        drawTitle(canvas);
    }

    private void drawTower(Canvas canvas) {
        canvas.drawPath(mTowerDst, mPaint); //开始画塔,mTowerDst已经有值;从0画到现在的位置;...

        if (isAnimateEnd) { //如果此时塔已经画完了,那么就画从透明度上画塔;
            mPaint.setAlpha(mAlpha);
            canvas.drawPath(mTowerPath, mPaint);
        }
    }

    private void drawTitle(Canvas canvas) {
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(80);
        int length = (int) mPaint.measureText(mTitle);

        int x = (mTowerWidth - length) / 2;

        canvas.drawText(mTitle, x, mTitleY, mPaint);
    }

    private void drawCould(Canvas canvas) {
        for (int i = 0; i < mCouldPaths.length; i++) {
            setupCouldPath(mCouldPaths[i], i);
            canvas.drawPath(mCouldPaths[i], mPaint);
        }
    }

    //刚一开始的时候其实什么都没有;你是onCreate中开始动画,然后才开始的;
    public void startAnimate() {

        restore();
        //start tower animate
        getTowerValueAnimator().start();

        //start could animate
        for (int i = 0; i < mCouldPaths.length; i++) {
            final ValueAnimator couldAnimator = getCouldValueAnimator(i);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    couldAnimator.start();
                }
            }, mDuration / 2);
        }

        getTitleAnimate().start();
    }

    //在开始动画中被调用;
    private void restore() {
        for (int i = 0; i < couldCount; i++) {
            mCouldY[i] = 0;
        }
    }

    //##############下面四个方法是获取titil,cloud,tower,透明度的四个动画##############
    private ValueAnimator getTitleAnimate() {

        ValueAnimator va = ValueAnimator.ofFloat(0, mFinalTitleY);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTitleY = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        va.setInterpolator(new DecelerateInterpolator());
        va.setDuration(mDuration / 3);
        return va;
    }

    private ValueAnimator getCouldValueAnimator(final int pos) {
        ValueAnimator animator = ValueAnimator.ofFloat(getHeight(), mCouldFinalY[pos]);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCouldY[pos] = (float) valueAnimator.getAnimatedValue();
                postInvalidateDelayed(10);
            }
        });
        animator.setDuration(1500);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    @NonNull
    private ValueAnimator getTowerValueAnimator() {
        final ValueAnimator towerAnimator = ValueAnimator.ofFloat(0, 1); //变化从0~1;
        towerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                postInvalidateDelayed(10);//延迟10ms,发布计算值;
            }
        });
        towerAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimateEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                isAnimateEnd = true;
                invalidate();
                //塔画完了再开始透明度的计算;
                getAlphaAnimator().start();
                //移除监听器,避免内存泄露;
                towerAnimator.removeAllUpdateListeners();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        towerAnimator.setInterpolator(new DecelerateInterpolator());
        towerAnimator.setDuration(mDuration);
        return towerAnimator;
    }

    private ValueAnimator getAlphaAnimator() {
        final ValueAnimator va = ValueAnimator.ofInt(0, 255);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAlpha = (int) animation.getAnimatedValue();
                invalidate();//这个你为什么立即刷新呢?
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                va.removeAllUpdateListeners();
                //透明度也画完了,那么就开始通知更新了
                if (mListener != null) {
                    mListener.onEnd(WowSplashView.this);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.setDuration(500);
        return va;
    }

    //动画结束的一个通知,将接口暴露出来;整体动画结束了就开始显示我们的界面了;
    public interface OnEndListener {
        void onEnd(WowSplashView wowSplashView);
    }

    public void setOnEndListener(OnEndListener listener) {
        mListener = listener;
    }
}
