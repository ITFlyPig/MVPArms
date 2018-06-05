package com.wangyuelin.downloader.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.app.utils.DisplayUtil;


public class DotsGrowView extends View {
    private int mDotSize;//尺寸
    private int mDotColor;//颜色
    private int mDotPadding;//dot之间的间距
    private Paint mPaint;
    private int mMaxSize;//一个dot放大的最大尺寸
    private int mCurSize;
    private int mDurtation;//一次动画的时间
    private int mTotlaDots;//总的点数
    private long mStartTime;

    private boolean isStop;
    private int[] sizeArray;

    public DotsGrowView(Context context) {
        this(context, null);
    }

    public DotsGrowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotsGrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DotsGrow);
        mDotSize = a.getDimensionPixelSize(R.styleable.DotsGrow_dotSize, 30);
        mDotColor = a.getColor(R.styleable.DotsGrow_dotColor, context.getResources().getColor(R.color.colorPrimary));
        mDotPadding = a.getDimensionPixelSize(R.styleable.DotsGrow_dotPadding, 20);
        mDurtation = a.getInteger(R.styleable.DotsGrow_durtation, 5000);
        mTotlaDots = a.getInteger(R.styleable.DotsGrow_dotNum, 3);
        a.recycle();

        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mDotColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mMaxSize = mDotSize + DisplayUtil.dip2px(getContext(), 5);
        setBackgroundColor(Color.BLUE);
        Log.d("tt", "初始的尺寸：" + mDotSize);
        initsizearry();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
        for (int i = 1; i <= mTotlaDots; i++) {
            int[] pos = getDotPos(i);
            Log.d("oo", "点：" + i + " 位置：" + pos[0] + " : " + pos[1]);
            int size = sizeArray[i - 1];
            if (pos != null) {
                drawCircle(size, pos[0], pos[1], canvas);
            }
        }

    }

    private void drawCircle(int size, int x, int y, Canvas canvas) {
        canvas.drawCircle(x, y, size / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minW = getMinWidth();
        int minH = getMinHeight();
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int realW = 0;
        int realH = 0;
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        if (modeW == MeasureSpec.AT_MOST) {
            realW = minW + getPaddingLeft() + getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(realW, MeasureSpec.EXACTLY);
        } else {
            if (w < minW) {
                realW = minW + getPaddingLeft() + getPaddingRight();
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(realW, MeasureSpec.EXACTLY);
            }
        }

        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        if (modeH == MeasureSpec.AT_MOST) {
            realH = minH + getPaddingBottom() + getPaddingTop();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(realH, MeasureSpec.EXACTLY);
        } else {
            if (h < minH) {
                realH = minH + getPaddingBottom() + getPaddingTop();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(realH, MeasureSpec.EXACTLY);
            }
        }


        Log.d("mm", "view的宽和高：" + w + ":" + h + "  计算的宽和高：" + minW + " : " + minH);
        Log.d("mm", "view的宽和高：" + w + ":" + h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 最小宽度
     *
     * @return
     */
    private int getMinWidth() {
        return mTotlaDots * mDotSize + (mTotlaDots - 1) * mDotPadding + (mMaxSize - mDotSize) * 2;
    }

    /**
     * 最下高度
     *
     * @return
     */
    private int getMinHeight() {
        return mMaxSize;
    }

    /**
     * 一个点所需要的动画的时间
     *
     * @return
     */
    private float getOneDotAniTime() {
        return mDurtation / (float) mTotlaDots;
    }

    /**
     * 返回点的坐标
     *
     * @param index
     * @return
     */
    private int[] getDotPos(int index) {
        if (index > 0 && index <= mTotlaDots) {
            int x = (index - 1) * mDotSize + (index - 1) * mDotPadding + mDotSize / 2 + getPaddingLeft() + (mMaxSize - mDotSize);
            int y = getHeight() / 2;
            return new int[]{x, y};
        }
        return null;
    }




    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startAni();
        } else {
            stopAni();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAni();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAni();
    }

    private ValueAnimator mValueAnimator;
    private int mCurAniIndexStart = 0;

    private void startAni() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            return;
        }

        mValueAnimator = ValueAnimator.ofFloat(0, 1).setDuration((long) getOneDotAniTime());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = animation.getAnimatedFraction();

                Log.d("kk", "计算之前：" + factor);

                if (mCurAniIndexStart > 0) {
                    //设置下一个dot的尺寸
                    if (mCurAniIndexStart < sizeArray.length) {
                        setNext((float) factor);
                    }


                    //设置当前dot为尺寸
                    sizeArray[mCurAniIndexStart] = (int) (mDotSize + ((mMaxSize - mDotSize) * (1 - factor)));
                    Log.d("gg", "当前index：" + mCurAniIndexStart + " 当前节点的尺寸：" + sizeArray[mCurAniIndexStart]);
                    if (sizeArray[mCurAniIndexStart] == mDotSize) {//下一个节点开始动画
                        mCurAniIndexStart++;
                        if (mCurAniIndexStart >= sizeArray.length) {
                            mCurAniIndexStart = 0;
                        }
                        Log.d("gg", "下一个节点：" + mCurAniIndexStart);
                    }

                } else {
                    Log.d("gg", "当前index：" + mCurAniIndexStart);
                    if (factor > 0.5) {//减小
                        //设置下一个dot的尺寸
                        if (mCurAniIndexStart < sizeArray.length) {
                            setNext((float) ((factor - 0.5) * 2));
                        }

                        //设置当前dot为尺寸
                        sizeArray[mCurAniIndexStart] = (int) (mDotSize + ((mMaxSize - mDotSize) * (1 - factor) * 2));
                        Log.d("gg", "当前节点尺寸：" + sizeArray[mCurAniIndexStart]);
                        if (sizeArray[mCurAniIndexStart] == mDotSize) {//下一个节点开始动画
                            mCurAniIndexStart++;
                            if (mCurAniIndexStart >= sizeArray.length) {
                                mCurAniIndexStart = 0;
                            }
                            Log.d("gg", "下一个节点：" + mCurAniIndexStart);
                        }

                    } else {//增大
                        factor = factor * 2;
                        //设置当前dot为尺寸
                        sizeArray[mCurAniIndexStart] = (int) (mDotSize + ((mMaxSize - mDotSize) * factor));
                        Log.d("gg", "当前节点尺寸：" + sizeArray[mCurAniIndexStart]);
                    }
                }

                invalidate();


            }
        });
        mValueAnimator.start();
    }

    private void stopAni() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
    }

    private void initsizearry() {
        sizeArray = new int[mTotlaDots];
        int size = sizeArray.length;
        for (int i = 0; i < size; i++)
            sizeArray[i] = mDotSize;
    }


    private void setNext(float factor) {
        int next = mCurAniIndexStart + 1;
        if (next < sizeArray.length) {
            sizeArray[next] = (int) (mDotSize + (mMaxSize - mDotSize) * factor);
            Log.d("gg", "下一个节点尺寸：" + sizeArray[next]);
        }

    }
}
