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
 * drawertoggle的三条横线的图标
 */
public class DrawertoggleIconView extends View {
    private int mColor;
    private Paint mPaint;
    private int mLineSize;
    private int mLineW;
    private int mLinePadding;
    private int mLineNum;

    public DrawertoggleIconView(Context context) {
        this(context, null);
    }

    public DrawertoggleIconView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawertoggleIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawertoggleIconView);
        if (a != null) {
            mColor = a.getColor(R.styleable.DrawertoggleIconView_line_color, getResources().getColor(R.color.white));
            mLineSize = a.getDimensionPixelSize(R.styleable.DrawertoggleIconView_line_h, 2);
            mLineW = a.getDimensionPixelSize(R.styleable.DrawertoggleIconView_line_w, 15);
            mLinePadding = a.getDimensionPixelSize(R.styleable.DrawertoggleIconView_line_padding, 3);
        }

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineSize);
        mLinePadding = DisplayUtil.dip2px(getContext(), 2);
        mLineNum = 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mLineNum; i++) {
            drawLine(i, canvas);
        }


    }

    /**
     * 绘制线条
     * @param index
     * @param canvas
     */
    private void drawLine(int index, Canvas canvas) {
        int startX = getPaddingLeft();
        int startY = (index - 1) * (mLineSize + mLinePadding);
        int stopX = getWidth() - getPaddingRight();
        int stopY = (index - 1) * (mLineSize + mLinePadding);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (wMode == MeasureSpec.AT_MOST) {
            w = mLineW + getPaddingLeft() + getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
        }
        if (hMode == MeasureSpec.AT_MOST) {
            h = mLineNum * mLineSize  + mLinePadding * (mLineSize - 1) + getPaddingBottom() + getPaddingTop();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
