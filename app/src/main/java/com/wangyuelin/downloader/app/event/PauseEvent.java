package com.wangyuelin.downloader.app.event;

public class PauseEvent {
    private long taskId;

    public PauseEvent(long taskId) {
        this.taskId = taskId;
    }

    public long getTaskId() {
        return taskId;
    }

}
