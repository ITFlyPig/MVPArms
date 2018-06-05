package com.wangyuelin.downloader.mvp.Home.bean;

public class DownloadTaskBean extends BaseDownloadBean{
    private long taskId;
    private String name;
    private String downloadUrl;
    private String savePath;
    private String playUrl;
    private String totalSize;
    private String speed;
    private int status;//1 正在下载  2：下载完成  3：下载失败
    private String downLoadSize;


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDownLoadSize() {
        return downLoadSize;
    }

    public void setDownLoadSize(String downLoadSize) {
        this.downLoadSize = downLoadSize;
    }
}
