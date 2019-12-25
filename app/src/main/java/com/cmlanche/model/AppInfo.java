package com.cmlanche.model;

import java.util.UUID;

public class AppInfo {

    private String uuid = UUID.randomUUID().toString();

    /**
     * 中文名
     */
    private String name;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 图标id
     */
    private int icon;

    /**
     * 包名
     */
    private String pkgName;

    /**
     * 执行时长
     */
    private long period;

    /**
     * 是否免费
     */
    private boolean isFree;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
