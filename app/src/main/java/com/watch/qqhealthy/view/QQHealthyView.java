package com.watch.qqhealthy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.watch.qqhealthy.R;

public class QQHealthyView extends View {
    private static final int BG_START_ANGLE = 120;
    private static final int BG_SWEEP_ANGLE = 300;
    private static final int MARGIN = 20;
    private static final String TAG = "zhengyi.wzy";
    private int mTextColor = Color.BLACK;
    private int mLineColor = Color.BLACK;

    /**
     * View的宽度
     */
    private int mWidth;

    /**
     * View的高度
     */
    private int mHeight;

    /**
     * 外层圆弧矩形的画笔
     */
    private Paint mBgPaint;

    /**
     * 外层圆弧矩形
     */
    private Path mBgPath;

    /**
     * 进度背景画笔
     */
    private Paint mArcBgPaint;

    /**
     * 进度矩形
     */
    private RectF mArcRectF;

    /**
     * 进度画笔
     */
    private Paint mArcProgressPaint;

    /**
     * 进度
     */
    private float mProgress = 30;

    /**
     * 步数画笔
     */
    private Paint mWalkNumPaint;

    /**
     * 其它文字画笔
     */
    private Paint mOtherTextPaint;

    /**
     * 走路步数
     */
    private int mWalkNum = 1000;

    /**
     * 名次画笔.
     */
    private Paint mRankingPaint;

    /**
     * 用户排名
     */
    private int mRankNum = 1;

    public QQHealthyView(Context context) {
        this(context, null);
    }

    public QQHealthyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQHealthyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.QQHealthyView,
                defStyleAttr, 0);

        mTextColor = ta.getColor(R.styleable.QQHealthyView_titleColor, mTextColor);
        mLineColor = ta.getColor(R.styleable.QQHealthyView_lineColor, mLineColor);

        ta.recycle();

        init();
    }

    private void init() {
        mBgPath = new Path();
        initPaint();
    }

    private void initPaint() {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStrokeWidth(10);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setColor(Color.BLUE);

        mArcBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcBgPaint.setStyle(Paint.Style.STROKE);
        mArcBgPaint.setDither(true);
        mArcBgPaint.setStrokeJoin(Paint.Join.ROUND);
        mArcBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcBgPaint.setColor(mLineColor);
        mArcBgPaint.setStrokeWidth(50);

        mArcProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcProgressPaint.setStyle(Paint.Style.STROKE);
        mArcProgressPaint.setDither(true);
        mArcProgressPaint.setStrokeJoin(Paint.Join.ROUND);
        mArcProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcProgressPaint.setColor(mTextColor);
        mArcProgressPaint.setStrokeWidth(50);

        mWalkNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWalkNumPaint.setTextSize(120);
        mWalkNumPaint.setColor(mTextColor);
        mWalkNumPaint.setTextAlign(Paint.Align.CENTER);

        mRankingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRankingPaint.setTextSize(50);
        mRankingPaint.setColor(mTextColor);
        mRankingPaint.setTextAlign(Paint.Align.CENTER);

        mOtherTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOtherTextPaint.setTextSize(30);
        mOtherTextPaint.setColor(Color.RED);
        mOtherTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = (int) (metrics.widthPixels * 1.0 / 2);
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = (int) (metrics.heightPixels * 3.0 / 4);
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, heightSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        int len = Math.min(mWidth, mHeight);
        float left = (float) (len * 1.0 / 4);
        float top = (float) (len * 1.0 / 4);
        float right = (float) (len * 3.0 / 4);
        float bottom = (float) (len * 3.0 / 4);

        mArcRectF = new RectF(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw: is here");
        drawBackground(canvas);
        drawArcAndText(canvas);
    }

    private void drawArcAndText(Canvas canvas) {
        // 绘制背景圆弧
        canvas.drawArc(mArcRectF, BG_START_ANGLE, BG_SWEEP_ANGLE, false, mArcBgPaint);

        // 绘制进度圆弧
        canvas.drawArc(mArcRectF, BG_START_ANGLE, mProgress, false, mArcProgressPaint);

        // 绘制圈内数字
        drawWalkNum(canvas);

        // 绘制名次
        drawRank(canvas);
    }

    private void drawRank(Canvas canvas) {
        Log.e(TAG, "drawRank: is here!");
        Paint.FontMetricsInt fm = mRankingPaint.getFontMetricsInt();
        float baselineY = (float) (mWidth * 3.0 / 4 - (fm.ascent + fm.descent) / 2);
        canvas.drawText("第" + String.valueOf(mRankNum) + "名",
                mWidth / 2, baselineY, mRankingPaint);
    }

    private void drawWalkNum(Canvas canvas) {
        // 绘制中间的文本
        Paint.FontMetricsInt fm = mWalkNumPaint.getFontMetricsInt();
        float baselineY = mWidth / 2 - (fm.descent + fm.ascent) / 2;
        canvas.drawText(String.valueOf(mWalkNum), mWidth / 2, baselineY, mWalkNumPaint);

        Paint.FontMetricsInt otherFm = mOtherTextPaint.getFontMetricsInt();
        // 绘制上面的文本
        float top = baselineY + fm.ascent;
        float topBaseLineY = top - otherFm.descent- 20;
        canvas.drawText("截止21:30已走", mWidth / 2, topBaseLineY, mOtherTextPaint);

        // 绘制下面的文本
        float bottom = baselineY + fm.descent;
        float btmBaseLineY = bottom - fm.ascent;
        canvas.drawText("好友平均步数1000步", mWidth / 2, btmBaseLineY, mOtherTextPaint);

    }

    private void drawBackground(Canvas canvas) {
        int radiusBg = mWidth / 20;
        // 绘制左边的直线
        mBgPath.moveTo(MARGIN, mHeight - MARGIN);
        mBgPath.lineTo(MARGIN, radiusBg);

        // 绘制左边圆角
        mBgPath.quadTo(MARGIN, MARGIN, radiusBg + MARGIN, MARGIN);

        // 绘制上方直线
        mBgPath.lineTo(mWidth - radiusBg - MARGIN, MARGIN);

        // 绘制右边圆角
        mBgPath.quadTo(mWidth - MARGIN, MARGIN, mWidth - MARGIN, radiusBg + MARGIN);

        // 绘制右边直线
        mBgPath.lineTo(mWidth - MARGIN, mHeight - MARGIN);

        // 绘制下方直线
        mBgPath.close();

        canvas.drawPath(mBgPath, mBgPaint);
    }
}
