package com.wangyuelin.downloader.mvp.model.entity;

/**
 * 左边菜单的一个item
 */
public class LeftMeunItemBean {
    private String name;//菜单的名称
    private int icon;//菜单的图标
    private int type;//菜单的类型

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
