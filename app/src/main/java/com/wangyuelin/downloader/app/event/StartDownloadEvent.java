package com.wangyuelin.downloader.app.event;

public class StartDownloadEvent {
    private long taskId;
    private String url;

    public StartDownloadEvent(long taskId, String url) {
        this.taskId = taskId;
        this.url = url;
    }

    public long getTaskId() {
        return taskId;
    }

    public String getUrl() {
        return url;
    }
}
