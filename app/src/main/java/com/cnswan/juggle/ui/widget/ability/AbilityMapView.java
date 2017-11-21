package com.cnswan.juggle.ui.widget.ability;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * view名称：七星图
 * 巩固自定义控件的基础知识以及正多边形的绘制，熟悉绘制流程。
 * Created by cnswan on 2017/11/16.
 */

public class AbilityMapView extends View {

    private static final String VIEW_TAG = AbilityMapView.class.getSimpleName();

    // 元数据
    private Ability mAbility;
    // 变数量或者能力的个数
    private int n = 7;
    // 最外圈半径，定点到中心点的距离
    private float r;
    // 间隔数量，就是把半径分成几段
    private int intervalCount = 4;
    // 两(顶点线到中点)线之间的角度
    private float angle;

    // 储存多边形定点数的数组
    private ArrayList<List<PointF>> mPointList     = new ArrayList<>();
    // 储存能力点的数据
    private ArrayList<PointF>       mAlibityPoints = new ArrayList<>();

    // 划线的笔
    private Paint mLinePaint;
    // 画文字的笔
    private Paint mTextPaint;


    public AbilityMapView(Context context) {
        this(context, null);
    }

    public AbilityMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbilityMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(dp2px(context, 1f));
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(sp2px(context, 14f));
    }

    public void setData(Ability ability) {
        if (ability == null) {
            return;
        }
        this.mAbility = ability;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(VIEW_TAG, "onMeasure");
        //设置控件的最终视图大小(宽高)
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(VIEW_TAG, "onMesure:widthMode:" + widthMode + "->widthSize:" + widthSize +
                "->heightMode:" + heightMode + "->heightSize:" + heightSize);
        int r = widthSize / 2;
        mPointList.clear();
        final float angle = (float) ((2 * Math.PI) / n);// 计算弧度
        for (int i = 0; i < intervalCount; i++) {
            ArrayList<PointF> points = new ArrayList<>();
            final float intervalR = r * (((float) (intervalCount - i) / intervalCount));// 计算半径最外圈开始4/4 3/4 2/4 1/4
            for (int j = 0; j < n; j++) {
                //这里减去Math.PI / 2 是为了让多边形逆时针旋转90度，所以后面的所有用到cos,sin的都要减
                float x = (float) (intervalR * Math.cos((j + 1) * angle - Math.PI / 2));
                float y = (float) (intervalR * Math.sin((j + 1) * angle - Math.PI / 2));
                points.add(new PointF(x, y));
            }
            mPointList.add(points);
        }
        Log.d(VIEW_TAG, "onMeasure:mPointList:" + mPointList);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(VIEW_TAG, "onDraw");
        final int width = getWidth();
        final int height = getHeight();
        Log.d(VIEW_TAG, "onDraw:width:" + width + "->height:" + height);
        canvas.translate(width / 2, height / 2);
        drawPolygon(canvas);
        drawOutLine(canvas);
        drawAbilityLine(canvas);
        drawabilityText(canvas);
    }

    // 绘制多边形框,每一层都绘制
    private void drawPolygon(Canvas canvas) {
        canvas.save();//保存画布当前状态(平移、放缩、旋转、裁剪等),和canvas.restore()配合使用
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);// 绘制实心的
        Path path = new Path();
        for (int i = 0; i < intervalCount; i++) {
            //每一层的颜色都都不同
            switch (i) {
                case 0:
                    mLinePaint.setColor(Color.parseColor("#D4F0F3"));
                    break;
                case 1:
                    mLinePaint.setColor(Color.parseColor("#99DCE2"));
                    break;
                case 2:
                    mLinePaint.setColor(Color.parseColor("#56C1C7"));
                    break;
                case 3:
                    mLinePaint.setColor(Color.parseColor("#278891"));
                    break;
            }
            for (int j = 0; j < n; j++) {
                float x = mPointList.get(i).get(j).x;
                float y = mPointList.get(i).get(j).y;
                if (j == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mLinePaint);
            path.reset();
        }
        canvas.restore();
    }

    private void drawOutLine(Canvas canvas) {
        canvas.save();//保存当前状态
        mLinePaint.setColor(Color.parseColor("#99dce2"));
        mLinePaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        for (int i = 0; i < n; i++) {
            float x = mPointList.get(0).get(i).x;
            float y = mPointList.get(0).get(i).y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, mLinePaint);
        path.reset();
        for (int i = 0; i < n; i++) {
            float x = mPointList.get(0).get(i).x;
            float y = mPointList.get(0).get(i).y;
            canvas.drawLine(0, 0, x, y, mLinePaint);
        }
        canvas.restore();//恢复到上一次保存的状态
    }

    private void drawAbilityLine(Canvas canvas) {
    }

    private void drawabilityText(Canvas canvas) {

    }

    //DP转PX
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //PX转DP
    public int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
