package com.wangyuelin.downloader.app.utils;

public enum  Menus {
    SETTING("设置", -1, 0),
    ABOUT("关于", -1, 1),
    FEEDBACK("反馈", -1, 2);

    Menus(String name, int icon, int type) {
        this.name = name;
        this.icon = icon;
        this.type = type;
    }

    private String name;
    private int icon;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
