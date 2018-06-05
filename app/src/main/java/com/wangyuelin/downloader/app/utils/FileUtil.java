package com.wangyuelin.downloader.app.utils;

import android.text.TextUtils;

import java.io.File;

public class FileUtil {

    /**
     * 检查路径是否存在
     * @param path
     */
    public static void checkDir(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }

    }
}
