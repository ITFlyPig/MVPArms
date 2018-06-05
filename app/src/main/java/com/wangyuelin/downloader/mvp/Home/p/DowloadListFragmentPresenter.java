package com.wangyuelin.downloader.mvp.Home.p;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.wangyuelin.downloader.app.utils.Constant;
import com.wangyuelin.downloader.mvp.Home.adapter.ListDownloadAdapter;
import com.wangyuelin.downloader.mvp.Home.bean.DownloadTaskBean;
import com.wangyuelin.downloader.mvp.contract.DowloadListFragmentContract;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

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

    private boolean isLooping;

    private ListDownloadAdapter mListDownloadAdapter;

    @Inject
    public DowloadListFragmentPresenter(DowloadListFragmentContract.Model model, DowloadListFragmentContract.View rootView) {
        super(model, rootView);
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


    }

    //一秒查一下状态
    private Handler getDownloadTaskInfoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            long taskId = (long) msg.obj;
           updateItemStatus();
            getDownloadTaskInfoHandler.sendMessageDelayed(getDownloadTaskInfoHandler.obtainMessage(0,taskId),1000);

        }
    };

    /**
     * adapter添加数据
     * @param taskInfo
     * @param url
     */
    public void addTask(XLTaskInfo taskInfo, String url) {
        if (taskInfo == null) {
            return;
        }

        if (mListDownloadAdapter.getInfos().size() == 0) {
            getDownloadTaskInfoHandler.sendMessage(getDownloadTaskInfoHandler.obtainMessage(0,taskInfo.mTaskId));
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
        mListDownloadAdapter.notifyDataSetChanged();


    }


    /**
     * 转为直观的下载速度
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
     */
    private void updateItemStatus() {
        List<DownloadTaskBean> downs = mListDownloadAdapter.getInfos();
        if (downs == null) {
            return;
        }
        int size = downs.size();
        for (int i = 0; i < size; i++) {
            DownloadTaskBean item = downs.get(i);
            XLTaskInfo taskInfo = XLTaskHelper.instance(mApplication).getTaskInfo(item.getTaskId());
            Log.d("gg", "查询id：" + taskInfo.mTaskId + "名称：" + taskInfo.mFileName + " 速度：" + taskInfo.mDownloadSpeed);
            if (taskInfo == null) {
                continue;
            }
            item.setSpeed(convertFileSize(taskInfo.mDownloadSpeed));
            item.setStatus(taskInfo.mTaskStatus);
            item.setTotalSize(taskInfo.mFileSize + " ");
            item.setDownLoadSize(taskInfo.mDownloadSize + " ");
            //完成就删除，放到已完成List
        }
        mListDownloadAdapter.notifyDataSetChanged();
    }
}
