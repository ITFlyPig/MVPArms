package com.wangyuelin.downloader.app.utils;

import android.text.TextUtils;

import com.wangyuelin.downloader.R;


public class IconUtil {

    /**
     * 桔类型获得名称
     * @param type
     * @return
     */
    public static int getIconByType(String type) {
        if (TextUtils.isEmpty(type)) {
            return -1;
        }
        switch (type) {
            case "3gp":
                return R.mipmap.ic_dl_3gp;
            case "rmvb":
                return R.mipmap.ic_dl_rmvb;
            case "mp4":
                return R.mipmap.ic_dl_mp4;
            case "avi":
                return R.mipmap.ic_dl_avi;
            case "mkv":
                return R.mipmap.ic_dl_mkv;
            default:
                return R.mipmap.ic_dl_other_style1;
        }
    }

    /**
     * 据名称获得类型
     * @param name
     * @return
     */
    public static String getTypeByName(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        if (name.contains("3gp") || name.contains("3GP")) {
            return "3gp";
        } else if (name.contains("rmvb") || name.contains("RMVB")) {
            return "rmvb";
        } else if (name.contains("mp4") || name.contains("MP4")) {
            return "mp4";
        } else if (name.contains("avi") || name.contains("AVI")) {
            return "avi";
        } else if (name.contains("mkv") || name.contains("MKV")) {
            return "mkv";
        }
        return "";
    }

}
