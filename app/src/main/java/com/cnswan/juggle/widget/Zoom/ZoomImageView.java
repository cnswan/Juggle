package com.cnswan.juggle.widget.Zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Description :
 * 扩展的ImageView,实现图片的缩放,移动等功能,下周周末添加进来
 */

public class ZoomImageView extends ImageView implements
        ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private boolean mOnce = false;

    // 初始缩放值(正好适合屏幕的缩放值),双击放大达到的值,放大的最大值
    private float mInitScale;
    private float mMidScale;
    private float mMaxScale;
    private ScaleGestureDetector mScaleGestureDetector;

    private Matrix mScaleMatrix;

    // ---------------------------
    // 手指数量的检测,一旦发生改变,那么中心点的坐标看会发生改变,需要记录上次中心点的坐标
    private int mLastPointCount;
    private float mLastX;
    private float mLastY;

    private float mTouchSlop;

    private boolean isCanDrag;

    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    // --------------双击放大缩小---------------------
    private GestureDetector mGestureDetector;
    private boolean isAutoScale;


    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // init();
        //Bitmap bitmap = readBitMap(getContext(), R.drawable.t3);

        paint = new Paint();
        // path = new Path();

        //this.setImageBitmap(bitmap);

        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (isAutoScale) {
                            return true;
                        }
                        float x = e.getX();
                        float y = e.getY();

                        // 需要注意的问题是:简单这样直接放大或者缩小的话,它的动画效果是瞬间变化的,这样的效果用户很难接受,所以用一个内部类解决
                        if (getScale() < mMidScale) {
                            // 双击一次,放大为midScale
                            postDelayed(new AutoScaleRunnable(mMidScale, x, y), 16);
                            isAutoScale = true;
                        } else {
                            postDelayed(new AutoScaleRunnable(mInitScale, x, y), 16);
                            isAutoScale = true;
                        }
                        return true;
                    }
                });
    }

    private class AutoScaleRunnable implements Runnable {

        // 缩放的目标值
        private float mTargetScale;
        // 缩放的中心点
        private float x, y;

        private final float BIGGER = 1.07f;
        private final float SMALLER = 0.97f;

        private float tmpScale;

        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            }
            if (getScale() > mTargetScale) {
                tmpScale = SMALLER;
            }
        }

        @Override
        public void run() {
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScale();

            if ((tmpScale > 1.0f && currentScale < mTargetScale)
                    || (tmpScale < 1.0f && currentScale > mTargetScale)) {
                // 这里其实并不是一点一点的放大,而是设置了一个时间间隔,等待到达要求后才瞬间放大
                postDelayed(this, 16);
            } else {
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        Log.d("#######", "onGlobalLayout");

        if (!mOnce) {
            // 得到控件的宽和高
            int width = getWidth();
            int height = getHeight();

            // 得到图片以宽高
            Drawable d = getDrawable();
            if (d == null) {
                return;
            }

            // 实际图片和宽高
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            // 先设置个默认的缩放值
            float scale = 1.0f;

            // 如果加载进来的图片比较宽且比较低
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }

            // 如果加载进来的图片比较高且比较窄
            if (dh > height && dw < width) {
                scale = height * 1.0f / dh;
            }

            // 如果图片的宽和高均大于控件的大小,将其缩小
            if (dh > height && dw > width) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            // 如果图片的宽和高均小于控件的大小,将其放大
            if (dh < height && dw < width) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMidScale = mInitScale * 2;

            // 将图片移动到屏幕中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;

            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2,
                    height / 2);
            setImageMatrix(mScaleMatrix);
            mOnce = true;

        }
    }

    /***
     * 获取当前的缩放比例,并与手指触控索要的缩放比例进行对比,注意我们的最大缩放尺度是mMaxScale
     */
    public float getScale() {
        float[] value = new float[9];
        mScaleMatrix.getValues(value);
        return value[Matrix.MSCALE_X];
    }

    /*
     * --------------------------------------------------------------------------
     * ---------------- 与手势相关的操作 注意缩放的区间是init~max
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // 得到缩放值;
        float scaleFactor = detector.getScaleFactor();
        float scale = getScale();
        if (getDrawable() == null) {
            return true;
        }

        if ((scale < mMaxScale && scaleFactor > 1.0)
                || (scale > mInitScale && scaleFactor < 1.0)) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            // 四个参数:
            // 前两个表示的是横向和纵向的缩放比例,我们需要保存原始图片的宽高比,所以横向和纵向的缩放比是一样的
            // 后两个参数是缩放的中心,我们将其设为手指所触的中心点
            // 但是问题是如果将图片放大,那么图片会铺满整个屏幕,然而在某一点一直缩小,那么最终图片的位置的中心点则不是原来的屏幕的中心点了
            // 而是手势所触的中心点
            mScaleMatrix.postScale(scaleFactor, scaleFactor,
                    detector.getFocusX(), detector.getFocusY());

            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    /***
     * 在缩放的时候检测边界和位置的控制 因此需要获取放大的图片的坐标等消息
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        // 拿到控件的宽高
        int width = getWidth();
        int height = getHeight();

        // 前提是:只有在当前图像是放大的,因为缩小的情况下有间隔是正常的

        // 实际的宽度比控件的宽度大
        if (rect.width() >= width) {
            // 实际的左边与控件的左边有空隙,则向左移动
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            // 实际的右边与控件的右边有空隙,则向右移动
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }

        if (rect.height() >= height) {
            // 实际的上边与控件的上边有空隙,向上移动
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            // 实际的下边与控件的下边有空隙,向下移动
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }

        // 如果是缩小的情况下,那么让其居中显示
        if (rect.width() < width) {
            deltaX = width / 2f - rect.right + rect.width() / 2f;
        }
        if (rect.height() < height) {
            deltaY = height / 2f - rect.bottom + rect.height() / 2f;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    /***
     * 获得图片放大缩小以后的宽和高,以及left,right,top,bottom等信息.
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    /*
     * --------------------------------------------------------------------------
     * ---------------- 与触控相关的操作
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 将双击的动作传递给mGestureDetector,接着马上返回,避免与后面的操作冲突;
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        // 使得mScaleGestureDetector可以拿到触控的缩放比例
        mScaleGestureDetector.onTouchEvent(event);

        // ------------------------------------------------------
        // 这段之间的代码是检测手指触摸到屏幕上的那一时刻的中心坐标,还并未涉及到移动
        // 用来保存当前的坐标中心点
        float x = 0;
        float y = 0;
        int pointCount = event.getPointerCount();
        for (int i = 0; i < pointCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= pointCount;
        y /= pointCount;

        if (mLastPointCount != pointCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointCount = pointCount;

        RectF rectf = getMatrixRectF();
        // -----------------------------开始对具体的动作进行检测------------------------------------------------
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*if (rectf.width() > getWidth() + 0.01 || rectf.height() > getHeight() + 0.01) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }*/
                break;
            case MotionEvent.ACTION_MOVE:
                /*if (rectf.width() > getWidth() + 0.01 || rectf.height() > getHeight() + 0.01) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }*/
            /*    if (rectf.left<0||rectf.right>getWidth()){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }*/

               /* if (rectf.left==0||rectf.right==getWidth()){  //如果显示的是边界的话,就交给viewpager来处理;
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else {//其他情况,要求父容器不要拦截,事件交给该zoomImage才处理;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }*/
                // 注意:当手指不停在屏幕上滑动时,其实是有顿挫感的,每顿挫一次就会触发一次onTouch的方法,因此该x值的实时改变的.
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (rectf.left == 0 && rectf.right == getWidth()) {//还没有进行缩放,left和right都有显示,那么不拦
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else if (rectf.left == 0 && dx > 0) {  //如果显示的是边界的话,就交给viewpager来处理;
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else if (rectf.right == getWidth() && dx < 0) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {//其他情况,要求父容器不要拦截,事件交给该zoomImage才处理;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                if (isCanDrag) {
                    RectF rectF = getMatrixRectF();
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = true;
                        isCheckTopAndBottom = true;

                        // 如果宽高小于控件的宽高,不允许移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderAndCenterWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointCount = 0;
                break;
            default:
                break;

        }
        return true;
    }

    /***
     * 当移动时,进行边界检查
     */
    private void checkBorderAndCenterWhenTranslate() {
        RectF rectf = getMatrixRectF();

        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rectf.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectf.top;
        }

        if (rectf.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectf.bottom;
        }

        if (rectf.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectf.left;
        }
        if (rectf.right < width && isCheckLeftAndRight) {
            deltaX = width - rectf.right;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    /***
     * 判断偏移的距离是否足矣触发移动图片
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isMoveAction(float dx, float dy) {

        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    // -------添加覆盖Test-------------------------

    // private Canvas canvas;
    private Paint paint;

    // private Path path;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = getMatrixRectF();
        Log.d("$$$$$$$", "rect" + rect.contains(0f, 0f));
        Log.d("$$$$$$$", "rect" + rect.toString());
        Log.d("$$$$$$$", "Center:" + rect.centerX() + " " + rect.centerY());
        // Log.d("$$$$$$$", "rect"+ rect.);

    }

    public static Bitmap readBitMap(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);

    }


}

/*
关于拦截,其实事件是先被传入到ZoomImageView中的,只是ViewPager先给拦截的左右滑动的操作
* */