package com.wangyuelin.downloader.mvp.Home.bean;

/**
 *
 * 下载任务的类型
 */
public enum  DownloadTaskType {
    TUNDER(1, "tunder", "thunder://"),
    FTP(2, "ftp", "ftp://"),
    ED2K(3, "ed2k", "ed2k://"),
    HTTP(4, "http", "http://"),
    HTTPS(5, "https", "https://"),
    MAGNET(6, "magnet", "magnet:?"),
    TORRENT(7, "torrent", ".torrent");


    DownloadTaskType(int type, String name, String protocol) {
        this.type = type;
        this.name = name;
        this.protocol = protocol;
    }

    private int type;
    private String name;
    private String protocol;

    public String getName() {
        return name;
    }

    public String getProtocol() {
        return protocol;
    }
}
