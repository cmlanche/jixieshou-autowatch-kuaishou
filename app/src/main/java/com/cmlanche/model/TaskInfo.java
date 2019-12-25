package com.cmlanche.model;

import java.util.List;

public class TaskInfo {

    private List<AppInfo> appInfos;

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    /**
     * 获取任务总时间
     *
     * @return
     */
    public int getHours() {
        int hours = 0;
        for (AppInfo info : appInfos) {
            hours += info.getPeriod();
        }
        return hours;
    }
}
