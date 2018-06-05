package com.wangyuelin.downloader.app.utils;

public class Constant {
    public static String SAVE_PATH = "/sdcard/lightdownload";

    public interface TaskStatus{
        //1 正在下载  2：下载完成  3：下载失败
        int DOWNLOADING = 1;
        int DONE = 2;
        int FAIL = 3;
    }
}
