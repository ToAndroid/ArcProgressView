package com.example.john.sclaeprogressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static java.lang.Math.PI;

/**
 * Created by john on 2017/7/18.
 */

public class ArcProgressView extends View {
    private Paint mOuterPaint, mProgressPaint, mPointPaint, mOuterPaint2,mInnerPaint,mTextPaint;
    private int mSweepAngle = 0;

    private int mCenterX;
    private int mCenterY;
    private int mRadiusX;
    private int mRadiusY;

    public ArcProgressView(Context context) {
        super(context);
        init();
    }

    public ArcProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ArcProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mOuterPaint = new Paint();
        mOuterPaint.setColor(Color.parseColor("#1e00F3FC"));
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeWidth(10);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint();
        mPointPaint.setColor(0xFF0969D4);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStrokeWidth(30);
        mPointPaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(Color.parseColor("#00F3FC"));
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(20);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mOuterPaint2 = new Paint();
        mOuterPaint2.setColor(Color.parseColor("#00F3FC"));
        mOuterPaint2.setAntiAlias(true);
        mOuterPaint2.setStrokeWidth(5);
        mOuterPaint2.setStyle(Paint.Style.STROKE);

        mInnerPaint = new Paint();
        mInnerPaint.setColor(Color.parseColor("#00F3FC"));
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(10);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#00F3FC"));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setTextSize(80);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        setProgress(1);
        test();

    }
    private void test(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(30);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();
    }

    public void setProgress(int progress) {
        if (progress > 100) {
            throw new RuntimeException("Progress is not greater than 100");
        }
        if (progress < 1) {
            progress = 1;
        }
        mSweepAngle = 240 * progress / 100;
        postInvalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = w / 2;
        mRadiusX = w / 2 - mCenterX * 2 / 30;
        mRadiusY = w / 2 - mCenterX * 2 / 30;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawOutArc(canvas);
        drawProgressArc(canvas);
        drawPoint(canvas);
        drawInnerCircle(canvas);
        drawProgressText(canvas);
    }

    private void drawProgressText(Canvas canvas) {
        canvas.drawText(((int)(mSweepAngle*100/240))+"",mCenterX,mCenterY+40,mTextPaint);
    }

    private void drawBg(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_arcbg);
        canvas.drawBitmap(bitmap,mCenterX-bitmap.getWidth()/2,mCenterY-bitmap.getHeight()/2,new Paint());
    }

    private void drawInnerCircle(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, 100, mInnerPaint);
        //实例化路径
        Point point1 = calculatePoint(mCenterX, mCenterY, mSweepAngle + 150, 100);
        Point point2 = calculatePoint(mCenterX, mCenterY, mSweepAngle + 150, mRadiusX * 5 / 7);
        Point point3 = calculatePoint(mCenterX, mCenterY, mSweepAngle + 150+5, 100);
        Path path = new Path();
        path.moveTo(point1.x, point1.y);// 此点为多边形的起点
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, mInnerPaint);

    }

    private void drawProgressArc(Canvas canvas) {
        canvas.drawArc(getOutArcRectF(), 150, mSweepAngle, false, mProgressPaint);

    }

    private void drawPoint(Canvas canvas) {

        Point point = calculatePoint(mCenterX, mCenterY, 150 + mSweepAngle, mRadiusX - 50);
//        RectF mRectF = new RectF(point.x - 15,point.y - 15,point.x + 15,point.y + 15);
//        canvas.drawOval(mRectF, mPointPaint);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_progress_point);
        canvas.drawBitmap(bitmap,point.x-bitmap.getWidth()/2,point.y-bitmap.getHeight()/2,new Paint());
    }

    private void drawOutArc(Canvas canvas) {
        canvas.drawArc(getOutArcRectF(), 150, 240,
                false, mOuterPaint);
        canvas.drawArc(getOutArc2RectF(), 140, 260,
                false, mOuterPaint2);


    }


    private RectF getOutArcRectF() {
        return new RectF(mCenterX - mRadiusX + 50, mCenterY - mRadiusY + 50, mCenterX + mRadiusX - 50, mCenterY + mRadiusY - 50);
    }

    private RectF getOutArc2RectF() {
        return new RectF(mCenterX - mRadiusX, mCenterY - mRadiusY, mCenterX + mRadiusX, mCenterY + mRadiusY);
    }


    private Point calculatePoint(double x0, double y0, int angle, int r) {
        int x1 = (int) (x0 + r * Math.cos(angle * PI / 180));
        int x2 = (int) (y0 + r * Math.sin(angle * PI / 180));
        return new Point(x1, x2);
    }
}
