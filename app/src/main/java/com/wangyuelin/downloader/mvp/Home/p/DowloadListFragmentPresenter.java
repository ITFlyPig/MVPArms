package com.wangyuelin.downloader.mvp.Home.p;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.wangyuelin.downloader.app.utils.Constant;
import com.wangyuelin.downloader.app.utils.LoopU;
import com.wangyuelin.downloader.mvp.Home.adapter.ListDownloadAdapter;
import com.wangyuelin.downloader.mvp.Home.bean.DownloadTaskBean;
import com.wangyuelin.downloader.mvp.contract.DowloadListFragmentContract;
import com.xunlei.downloadlib.XLDownloadManager;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.GetFileName;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@FragmentScope
public class DowloadListFragmentPresenter extends BasePresenter<DowloadListFragmentContract.Model, DowloadListFragmentContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    private LoopU mLoopU;



    private ListDownloadAdapter mListDownloadAdapter;

    @Inject
    public DowloadListFragmentPresenter(DowloadListFragmentContract.Model model, DowloadListFragmentContract.View rootView) {
        super(model, rootView);

        mLoopU = new LoopU();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void initAdapter() {
        if (mListDownloadAdapter == null) {
            mListDownloadAdapter = new ListDownloadAdapter(new ArrayList<>());
        }

        mRootView.setAdapter(mListDownloadAdapter);
    }


    /**
     * adapter添加数据
     *
     * @param taskInfo
     * @param url
     */
    public void addTask(XLTaskInfo taskInfo, String url) {
        if (taskInfo == null) {
            return;
        }
        DownloadTaskBean bean = new DownloadTaskBean();
        bean.setTaskId(taskInfo.mTaskId);
        bean.setSpeed(convertFileSize(taskInfo.mDownloadSpeed));
        bean.setDownloadUrl(url);
        bean.setStatus(taskInfo.mTaskStatus);
        bean.setName(taskInfo.mFileName);
        bean.setSavePath(Constant.SAVE_PATH);
        bean.setTotalSize(taskInfo.mFileSize + " ");
        bean.setDownLoadSize(taskInfo.mDownloadSize + " ");
        List<DownloadTaskBean> downs = mListDownloadAdapter.getInfos();
        downs.add(bean);
        Log.d("xk", "list size：" + mListDownloadAdapter
                .getInfos().size());
        mListDownloadAdapter.notifyDataSetChanged();

        mLoopU.loop(mTask, 1000);


    }


    /**
     * 转为直观的下载速度
     *
     * @param size
     * @return
     */
    private static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f M" : "%.1f M", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f K" : "%.1f K", f);
        } else
            return String.format("%d B", size);
    }

    /**
     * 更新状态
     *
     * @return true:表示更新  false：表示没有需要更新的
     */
    private boolean updateItemStatus() {
        List<DownloadTaskBean> downs = mListDownloadAdapter.getInfos();
        if (downs == null) {
            return false;
        }
        boolean isHaveUpdate = false;
        int size = downs.size();
        for (int i = 0; i < size; i++) {
            DownloadTaskBean item = downs.get(i);
            String fileName = XLTaskHelper.instance(mApplication).getFileName(item.getDownloadUrl());
            XLTaskInfo taskInfo = XLTaskHelper.instance(mApplication).getTaskInfo(item.getTaskId());
            if (taskInfo == null) {
                continue;
            }
            if (taskInfo.mDownloadSpeed > 0) {
                item.setSpeed(convertFileSize(taskInfo.mDownloadSpeed) + "/s");
            }
            item.setStatus(taskInfo.mTaskStatus);

            if (taskInfo.mFileSize > 0) {
                item.setTotalSize(getPrintSize(taskInfo.mFileSize));
                item.setProgress(taskInfo.mDownloadSize / (float) taskInfo.mFileSize);
            }
            if (taskInfo.mDownloadSize > 0) {
                item.setDownLoadSize(getPrintSize(taskInfo.mDownloadSize));
            }
            item.setName(fileName);
            Log.d("xl", "名称：" + item.getName() + " 状态：" + taskInfo.mTaskStatus);
            //完成就删除，放到已完成List

            if (TextUtils.isEmpty(item.getPlayUrl())) {
                String url = XLTaskHelper.instance(mApplication).getLoclUrl(item.getSavePath() + File.separator + item.getName());//变下边播的url
                Log.d("pl", "查询到边下边播的url：" + url + "/n  保存路劲：" +  item.getSavePath() + File.separator + item.getName());
                if (!TextUtils.isEmpty(url)) {

                    item.setPlayUrl(url);
                }

            }




            if (item.getStatus() == Constant.TaskStatus.DOWNLOADING) {//有正在下载的任务就需要更新
                isHaveUpdate = true;
            }

        }
        mListDownloadAdapter.notifyDataSetChanged();
        return isHaveUpdate;

    }


    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 暂停
     *
     * @param taskId
     */
    public void pause(long taskId) {
        List<DownloadTaskBean> data = mListDownloadAdapter.getInfos();
        if (data == null) {
            return;
        }
        for (DownloadTaskBean datum : data) {
            if (datum.getTaskId() == taskId) {
                XLTaskHelper.instance(mApplication).stopTask(taskId);
                return;
            }
        }
    }

    /**
     * 开始/继续下载
     *
     * @param taskId
     * @param url
     */
    public void startDownload(long taskId, String url) {
        List<DownloadTaskBean> data = mListDownloadAdapter.getInfos();
        if (data == null) {
            return;
        }
        for (DownloadTaskBean datum : data) {
            if (datum.getTaskId() == taskId) {
                if (datum.getStatus() == Constant.TaskStatus.DOWNLOADING) {//暂停
                    pause(taskId);
                } else {//开始下载
                    try {
                        long newTaskId = XLTaskHelper.instance(mApplication).addThunderTask(url, Constant.SAVE_PATH, null);
                        datum.setTaskId(newTaskId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return;
            }
        }

        mLoopU.loop();
    }

    private Runnable mTask  = new Runnable() {
        @Override
        public void run() {
            if (!updateItemStatus()) {
                mLoopU.stopLoop();
            }
        }
    };
}
