package com.cnswan.juggle.widget.ZSplash;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.zx.freetime.R;
import com.zx.freetime.ui.MainActivity;


/**
 * Created by zhangxin on 2017/3/19 0019.
 * <p>
 * Description :
 */
public class WowView extends View {
    private Bitmap mWowSplashBitmap; //通过缓存拿到了splash的bitmap;
    private Bitmap mDstBitmap;      //目标bitmap;
    private float mAlpha;           //整体的透明度,越来越透明;
    private float mScale;           //整体的缩放,越来越大;
    //最最最核心的地方:设置了PorterDuffXfermode,这里使用的是DST_OUT的方式:[Da * (1 - Sa), Dc * (1 - Sa)]
    //上面的公式可能不对,直接一点:desout的方式是:des的区域不显示,直接显示为src的部分;
    private PorterDuffXfermode mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    private Paint mPaint;
    private long mDuration = 2000;

    private Context mContext;

    private OnEndListener mListener;

    public WowView(Context context) {
        this(context, null);
    }

    public WowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);//感觉没什么用吧;
        //进行xfermode的des图片;
        mContext = context;
        mDstBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wow_splash_shade);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mWowSplashBitmap != null) {
            //canvas先将splash图画上去
            canvas.drawBitmap(mWowSplashBitmap, 0, 0, null);
            //接下来canvas就开始放大了;以中心点放大,这样其实就达到了上面的splashbitmap的放大效果;
            canvas.scale(mScale, mScale, mDstBitmap.getWidth() / 2, mDstBitmap.getHeight() / 2);
            mPaint.setXfermode(mMode);
            canvas.drawBitmap(mDstBitmap, 0, 0, mPaint);
        }
        setAlpha(mAlpha);
    }

    public void setWowSplashBitmap(Bitmap bitmap) {
        mWowSplashBitmap = bitmap;
        invalidate();
    }

    public void startAnimate(Bitmap bitmap) {
        setWowSplashBitmap(bitmap);

        getAlphaValueAnimator().start();//透明度减小;
        getScaleValueAnimator().start();//放大增加
    }

    @NonNull
    private ValueAnimator getScaleValueAnimator() {
        ValueAnimator scaleVa = ValueAnimator.ofFloat(0, 6);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mScale = (float) valueAnimator.getAnimatedValue();
            }
        });
        scaleVa.setDuration(mDuration);
        return scaleVa;
    }

    @NonNull
    private ValueAnimator getAlphaValueAnimator() {
        ValueAnimator alphaVa = ValueAnimator.ofFloat(1, 0f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAlpha = (float) valueAnimator.getAnimatedValue();
                postInvalidateDelayed(16);
            }
        });

        alphaVa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setVisibility(GONE);
               /* mContext.startActivity(new Intent(mContext, MainActivity.class));
                mContext.overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);  //我的新作品中颠倒个个
                finish();*/
                if (mListener != null) {
                    mListener.onEnd(WowView.this);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        alphaVa.setDuration(mDuration);
        return alphaVa;
    }

    public interface OnEndListener {
        void onEnd(WowView wowView);
    }

    public void setOnEndListener(OnEndListener listener) {
        mListener = listener;
    }
}

