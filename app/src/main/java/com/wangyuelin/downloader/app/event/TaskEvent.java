package com.wangyuelin.downloader.app.event;

import com.xunlei.downloadlib.parameter.XLTaskInfo;

public class TaskEvent {
    private XLTaskInfo taskInfo;
    private String url;

    public TaskEvent(XLTaskInfo taskInfo, String url) {
        this.taskInfo = taskInfo;
        this.url = url;
    }

    public XLTaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(XLTaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
