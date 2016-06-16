package com.GF.platform.uikit.widget.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.util.GFUtil;

import java.lang.reflect.Field;

public class GFCircleProgressBar extends ProgressBar {
    private static final float DEFAULT_PROGRESS_STROKE_WIDTH = 1.0f;

    private final RectF mProgressRectF = new RectF();

    private final Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mRadius;
    private float mCenterX;
    private float mCenterY;

    private float mProgressStrokeWidth;

    private int mProgressColor;
    private int mProgressBackgroundColor;

    public GFCircleProgressBar(Context context) {
        this(context, null);
    }

    public GFCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        adjustIndeterminate();
        initFromAttributes(context, attrs);
        initPaint();
    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GFCircleProgressBar);
        mProgressStrokeWidth = a.getDimensionPixelSize(R.styleable.GFCircleProgressBar_progress_stroke_width, GFUtil.dip2px(getContext(), DEFAULT_PROGRESS_STROKE_WIDTH));
        mProgressColor = a.getColor(R.styleable.GFCircleProgressBar_progress_color, Color.WHITE);
        mProgressBackgroundColor = a.getColor(R.styleable.GFCircleProgressBar_progress_background_color, Color.WHITE);
        a.recycle();
    }

    private void initPaint() {
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
    }

    private void adjustIndeterminate() {
        try {
            Field mOnlyIndeterminateField = ProgressBar.class.getDeclaredField("mOnlyIndeterminate");
            mOnlyIndeterminateField.setAccessible(true);
            mOnlyIndeterminateField.set(this, false);

            Field mIndeterminateField = ProgressBar.class.getDeclaredField("mIndeterminate");
            mIndeterminateField.setAccessible(true);
            mIndeterminateField.set(this, false);

            Field mCurrentDrawableField = ProgressBar.class.getDeclaredField("mCurrentDrawable");
            mCurrentDrawableField.setAccessible(true);
            mCurrentDrawableField.set(this, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        drawSolidProgress(canvas);
    }

    private void drawSolidProgress(Canvas canvas) {
        mProgressPaint.setColor(mProgressBackgroundColor);
        canvas.drawArc(mProgressRectF, 0.0f, 360.0f, false, mProgressPaint);

        mProgressPaint.setColor(mProgressColor);
        canvas.drawArc(mProgressRectF, -90.0f, 360.0f * getProgress() / getMax(), false, mProgressPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

        mRadius = Math.min(mCenterX, mCenterY);
        mProgressRectF.top = mCenterY - mRadius;
        mProgressRectF.bottom = mCenterY + mRadius;
        mProgressRectF.left = mCenterX - mRadius;
        mProgressRectF.right = mCenterX + mRadius;

        //Prevent the progress from clipping
        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2);
    }
}
