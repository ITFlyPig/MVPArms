package com.wangyuelin.downloader.app.utils;

import android.os.Handler;
import android.os.Message;

public class LoopU {
    private long mIntervalTime;
    private Runnable mTask;
    private boolean stop = true;

    private  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (stop) {
                return;
            }
            super.handleMessage(msg);
            mTask.run();
            mHandler.sendMessageDelayed(mHandler.obtainMessage(),  mIntervalTime);
        }
    };

    /**
     * 开始循环
     * @param runnable
     * @param intervalTime
     */
    public void loop(Runnable runnable, long intervalTime) {
        if (runnable == null || intervalTime < 0) {
            return;
        }
        if (mTask != runnable) {
            mTask = runnable;
        }
        mIntervalTime = intervalTime;
        mTask = runnable;
        loop();

    }

    /**
     * 开始循环
     */
    public void loop() {
        if (stop) {
            stop = false;
            mHandler.sendEmptyMessage(100);
        }

    }

    public void stopLoop() {
//        stop = true;
    }

}
