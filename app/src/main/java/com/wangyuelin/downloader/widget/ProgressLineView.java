package com.wangyuelin.downloader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.app.utils.DisplayUtil;

/**
 * 表示进度的view
 */
public class ProgressLineView extends View {
    private int mColor;
    private Paint mPaint;
    private int mLineH;
    private float progress;//进度 0~1

    public ProgressLineView(Context context) {
        this(context, null);
    }

    public ProgressLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(R.styleable.ProgressLineView);
        if (a != null) {
            mColor = a.getColor(R.styleable.ProgressLineView_progress_line_color, getResources().getColor(R.color.colorPrimary));
            mLineH = a.getDimensionPixelSize(R.styleable.ProgressLineView_progress_line_h, DisplayUtil.dip2px(getContext(), 1));
        }

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(getHeight() + 10);
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 1) {
            progress = 1;
        }

        int startX = getPaddingLeft();
        int endX = (int) ((getWidth() - getPaddingRight()) * progress);
        int startY = getPaddingTop();
        int endY = getPaddingTop();
        canvas.drawLine(startX, startY, endX, endY, mPaint);
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(float progress) {
        if (this.progress != progress) {
            this.progress = progress;
            invalidate();

        }


    }
}
