package com.xunlei.downloadlib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.xunlei.downloadlib.parameter.BtIndexSet;
import com.xunlei.downloadlib.parameter.BtSubTaskDetail;
import com.xunlei.downloadlib.parameter.BtTaskParam;
import com.xunlei.downloadlib.parameter.EmuleTaskParam;
import com.xunlei.downloadlib.parameter.GetFileName;
import com.xunlei.downloadlib.parameter.GetTaskId;
import com.xunlei.downloadlib.parameter.InitParam;
import com.xunlei.downloadlib.parameter.MagnetTaskParam;
import com.xunlei.downloadlib.parameter.P2spTaskParam;
import com.xunlei.downloadlib.parameter.TorrentFileInfo;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfo;
import com.xunlei.downloadlib.parameter.XLTaskLocalUrl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by oceanzhang on 2017/7/27.
 */

public class XLTaskHelper {
    private static final String TAG = "XLTaskHelper";

    public static void init(Context context) {
        XLDownloadManager instance = XLDownloadManager.getInstance();
        InitParam initParam = new InitParam();
//        initParam.mAppKey = "bpIzNjAxNTsxNTA0MDk0ODg4LjQyODAwMA&&OxNw==^a2cec7^10e7f1756b15519e20ffb6cf0fbf671f";
        initParam.mAppKey = "1";
        initParam.mAppVersion = "5.45.2.5080";
        initParam.mStatSavePath = context.getFilesDir().getPath();
        initParam.mStatCfgSavePath = context.getFilesDir().getPath();
        initParam.mPermissionLevel = 2;
        instance.init(context, initParam);
        instance.setOSVersion(Build.VERSION.INCREMENTAL);
        instance.setSpeedLimit(-1, -1);
        XLDownloadManager.getInstance().setUserId("");
    }


    private String innerSDCardPath;
    private Context context;
    private AtomicInteger seq = new AtomicInteger(0);

    private XLTaskHelper(final Context context) {
        this.context = context;
        innerSDCardPath = Environment.getExternalStorageDirectory().getPath();
    }


    private static volatile XLTaskHelper instance = null;

    public static XLTaskHelper instance(Context context) {
        if (instance == null) {
            synchronized (XLTaskHelper.class) {
                if (instance == null) {
                    instance = new XLTaskHelper(context.getApplicationContext());
                }
            }

        }
        return instance;
    }

    /**
     * 获取任务详情， 包含当前状态，下载进度，下载速度，文件大小
     * mDownloadSize:已下载大小  mDownloadSpeed:下载速度 mFileSize:文件总大小 mTaskStatus:当前状态，0连接中1下载中 2下载完成 3失败 mAdditionalResDCDNSpeed DCDN加速 速度
     *
     * @param taskId
     * @return
     */
    public synchronized XLTaskInfo getTaskInfo(long taskId) {
        XLTaskInfo taskInfo = new XLTaskInfo();
        XLDownloadManager.getInstance().getTaskInfo(taskId, 1, taskInfo);
        return taskInfo;
    }

    private String mLocalUrl;

    /**
     * 添加迅雷链接任务 支持thunder:// ftp:// ed2k:// http:// https:// 协议
     *
     * @param url
     * @param savePath 下载文件保存路径
     * @param fileName 下载文件名 可以通过 getFileName(url) 获取到,为空默认为getFileName(url)的值
     * @return
     */
    public synchronized long addThunderTask(String url, String savePath, String fileName) throws Exception {
        if (url.startsWith("thunder://"))
            url = XLDownloadManager.getInstance().parserThunderUrl(url);
        final GetTaskId getTaskId = new GetTaskId();
        GetFileName getFileName = new GetFileName();
        XLDownloadManager.getInstance().getFileNameFromUrl(url, getFileName);
        Log.d("tt", "下载的url：" + url);
        if (url.startsWith("ftp://") || url.startsWith("http")) {
            P2spTaskParam taskParam = new P2spTaskParam();
            taskParam.setCreateMode(1);
            taskParam.setFileName(getFileName.getFileName());
            taskParam.setFilePath(savePath);
            taskParam.setUrl(url);
            taskParam.setSeqId(seq.incrementAndGet());
            taskParam.setCookie("");
            taskParam.setRefUrl("");
            taskParam.setUser("");
            taskParam.setPass("");
            XLDownloadManager.getInstance().createP2spTask(taskParam, getTaskId);
        } else if (url.startsWith("ed2k://")) {
            EmuleTaskParam taskParam = new EmuleTaskParam();
            taskParam.setFilePath(savePath);
            taskParam.setFileName(getFileName.getFileName());
            taskParam.setUrl(url);
            taskParam.setSeqId(seq.incrementAndGet());
            taskParam.setCreateMode(1);
            XLDownloadManager.getInstance().createEmuleTask(taskParam, getTaskId);
        } else if (url.startsWith("magnet:?")){
            MagnetTaskParam magnetTaskParam = new MagnetTaskParam();
            magnetTaskParam.setFileName(getFileName.getFileName());
            magnetTaskParam.setFilePath(savePath);
            magnetTaskParam.setUrl(url);
            XLDownloadManager.getInstance().createBtMagnetTask(magnetTaskParam, getTaskId);
        } else if (url.endsWith(".torrent")) {//bt下载
            /*
            TorrentInfo torrentInfo = getTorrentInfo(url);
            //获取文件的索引
            int[] fileIndexs = null;
            if (torrentInfo != null && torrentInfo.mSubFileInfo != null) {
                int size = torrentInfo.mSubFileInfo.length;
                fileIndexs = new int[size];
                for (int i = 0; i < size; i++) {
                    TorrentFileInfo torrentFileInfo = torrentInfo.mSubFileInfo[i];
                    fileIndexs[i] = torrentFileInfo.mFileIndex;
                    Log.d("tt", "播放的url：" + torrentFileInfo.playUrl + " 名称：" + torrentFileInfo.mFileName);
                }
            }
            if (fileIndexs == null) {
                fileIndexs = new int[1];
                fileIndexs[0] = 1;
            }



            addTorrentTask(url, savePath, fileIndexs);
            */

            int[] indexs = new int[]{0};
            String torrentPath = url;
            TorrentInfo torrentInfo = new TorrentInfo();
            XLDownloadManager.getInstance().getTorrentInfo(torrentPath, torrentInfo);
            TorrentFileInfo[] fileInfos = torrentInfo.mSubFileInfo;
            BtTaskParam taskParam = new BtTaskParam();
            taskParam.setCreateMode(1);
            taskParam.setFilePath(savePath);
            taskParam.setMaxConcurrent(3);
            taskParam.setSeqId(seq.incrementAndGet());
            taskParam.setTorrentPath(torrentPath);
            XLDownloadManager.getInstance().createBtTask(taskParam, getTaskId);
            if (fileInfos.length > 0 && indexs != null && indexs.length > 0) {
                BtIndexSet btIndexSet = new BtIndexSet(indexs.length);
                int i = 0;
                for (int index : indexs) {
                    btIndexSet.mIndexSet[i++] = index;
                }
                XLDownloadManager.getInstance().selectBtSubTask(getTaskId.getTaskId(), btIndexSet);
            }

            mLocalUrl = getLoclUrl(savePath + fileInfos[0].mFileName);



        } else {
            throw new Exception("url illegal.");
        }

        XLDownloadManager.getInstance().setDownloadTaskOrigin(getTaskId.getTaskId(), "out_app/out_app_paste");
        XLDownloadManager.getInstance().setOriginUserAgent(getTaskId.getTaskId(), "AndroidDownloadManager/4.4.4 (Linux; U; Android 4.4.4; Build/KTU84Q)");
        XLDownloadManager.getInstance().startTask(getTaskId.getTaskId(), false);
        XLDownloadManager.getInstance().setTaskLxState(getTaskId.getTaskId(), 0, 1);
        XLDownloadManager.getInstance().startDcdn(getTaskId.getTaskId(), 0, "", "", "");

        return getTaskId.getTaskId();
    }


    public String getmLocalUrl() {
        return mLocalUrl;
    }

    /**
     * 添加磁力链任务
     *
     * @param url      磁力链接 magnet:? 开头
     * @param savePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public synchronized long addMagentTask(final String url, final String savePath, String fileName) throws Exception {
        if (url.startsWith("magnet:?")) {
            if (TextUtils.isEmpty(fileName)) {
                final GetFileName getFileName = new GetFileName();
                XLDownloadManager.getInstance().getFileNameFromUrl(url, getFileName);
                fileName = getFileName.getFileName();
            }
            MagnetTaskParam magnetTaskParam = new MagnetTaskParam();
            magnetTaskParam.setFileName(fileName);
            magnetTaskParam.setFilePath(savePath);
            magnetTaskParam.setUrl(url);
            final GetTaskId getTaskId = new GetTaskId();
            XLDownloadManager.getInstance().createBtMagnetTask(magnetTaskParam, getTaskId);

            XLDownloadManager.getInstance().setTaskLxState(getTaskId.getTaskId(), 0, 1);
            XLDownloadManager.getInstance().startDcdn(getTaskId.getTaskId(), 0, "", "", "");
            XLDownloadManager.getInstance().startTask(getTaskId.getTaskId(), false);
            return getTaskId.getTaskId();
        } else {
            throw new Exception("url illegal.");
        }
    }

    /**
     * 获取种子详情
     *
     * @param torrentPath
     * @return
     */
    public synchronized TorrentInfo getTorrentInfo(String torrentPath) {
        TorrentInfo torrentInfo = new TorrentInfo();
        XLDownloadManager.getInstance().getTorrentInfo(torrentPath, torrentInfo);
        return torrentInfo;
    }

    /**
     * 添加种子下载任务,如果是磁力链需要先通过addMagentTask将种子下载下来
     *
     * @param torrentPath 种子地址
     * @param savePath    保存路径
     * @param indexs      需要下载的文件索引
     * @return
     * @throws Exception
     */
    public synchronized long addTorrentTask(String torrentPath, String savePath, int[] indexs) throws Exception {
        TorrentInfo torrentInfo = new TorrentInfo();
        XLDownloadManager.getInstance().getTorrentInfo(torrentPath, torrentInfo);
        TorrentFileInfo[] fileInfos = torrentInfo.mSubFileInfo;
        BtTaskParam taskParam = new BtTaskParam();
        taskParam.setCreateMode(1);
        taskParam.setFilePath(savePath);
        taskParam.setMaxConcurrent(3);
        taskParam.setSeqId(seq.incrementAndGet());
        taskParam.setTorrentPath(torrentPath);
        GetTaskId getTaskId = new GetTaskId();
        XLDownloadManager.getInstance().createBtTask(taskParam, getTaskId);
        if (fileInfos.length > 1 && indexs != null && indexs.length > 0) {
            BtIndexSet btIndexSet = new BtIndexSet(indexs.length);
            int i = 0;
            for (int index : indexs) {
                btIndexSet.mIndexSet[i++] = index;
            }
            XLDownloadManager.getInstance().selectBtSubTask(getTaskId.getTaskId(), btIndexSet);
        }
        XLDownloadManager.getInstance().setTaskLxState(getTaskId.getTaskId(), 0, 1);
//        XLDownloadManager.getInstance().startDcdn(getTaskId.getTaskId(), currentFileInfo.mRealIndex, "", "", "");
        XLDownloadManager.getInstance().startTask(getTaskId.getTaskId(), false);
//        XLDownloadManager.getInstance().setBtPriorSubTask(getTaskId.getTaskId(),currentFileInfo.mRealIndex);
//        XLTaskLocalUrl localUrl = new XLTaskLocalUrl();
//        XLDownloadManager.getInstance().getLocalUrl(savePath+"/" +(TextUtils.isEmpty(currentFileInfo.mSubPath) ? "" : currentFileInfo.mSubPath+"/")+ currentFileInfo.mFileName,localUrl);
//        currentFileInfo.playUrl = localUrl.mStrUrl;
//        currentFileInfo.hash = torrentInfo.mInfoHash;
//        return currentFileInfo;

        return getTaskId.getTaskId();
    }

    /**
     * 获取某个文件的本地proxy url,如果是音视频文件可以实现变下边播
     *
     * @param filePath
     * @return
     */
    public synchronized String getLoclUrl(String filePath) {
        XLTaskLocalUrl localUrl = new XLTaskLocalUrl();
        XLDownloadManager.getInstance().getLocalUrl(filePath, localUrl);
        return localUrl.mStrUrl;
    }

    /**
     * 停止任务 文件保留
     *
     * @param taskId
     */
    public synchronized void stopTask(long taskId) {
        XLDownloadManager.getInstance().stopTask(taskId);
        XLDownloadManager.getInstance().releaseTask(taskId);
    }

    /**
     * 删除一个任务，会把文件也删掉
     *
     * @param taskId
     * @param savePath
     */
    public synchronized void deleteTask(long taskId, final String savePath) {
        stopTask(taskId);
        new Handler(Daemon.looper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    new LinuxFileCommand(Runtime.getRuntime()).deleteDirectory(savePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 通过链接获取文件名
     *
     * @param url
     * @return
     */
    public synchronized String getFileName(String url) {
        if (url.startsWith("thunder://"))
            url = XLDownloadManager.getInstance().parserThunderUrl(url);
        GetFileName getFileName = new GetFileName();
        XLDownloadManager.getInstance().getFileNameFromUrl(url, getFileName);
        return getFileName.getFileName();
    }

    /**
     * 获取种子文件子任务的详情
     *
     * @param taskId
     * @param fileIndex
     * @return
     */
    public synchronized BtSubTaskDetail getBtSubTaskInfo(long taskId, int fileIndex) {
        BtSubTaskDetail subTaskDetail = new BtSubTaskDetail();
        XLDownloadManager.getInstance().getBtSubTaskInfo(taskId, fileIndex, subTaskDetail);
        return subTaskDetail;
    }

    /**
     * 开启dcdn加速
     *
     * @param taskId
     * @param btFileIndex
     */
    public synchronized void startDcdn(long taskId, int btFileIndex) {
        XLDownloadManager.getInstance().startDcdn(taskId, btFileIndex, "", "", "");
    }


    /**
     * 停止dcdn加速
     *
     * @param taskId
     * @param btFileIndex
     */
    public synchronized void stopDcdn(long taskId, int btFileIndex) {
        XLDownloadManager.getInstance().stopDcdn(taskId, btFileIndex);
    }




}
