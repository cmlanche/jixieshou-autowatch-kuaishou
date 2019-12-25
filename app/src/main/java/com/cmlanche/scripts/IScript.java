package com.cmlanche.scripts;

import com.cmlanche.model.AppInfo;

public interface IScript {

    /**
     * 启动应用
     */
    void startApp();

    /**
     * 执行脚本
     */
    void execute();

    /**
     * 重置开始的时间，这样新任务就可以重新开始了
     */
    void resetStartTime();

    /**
     * 是否是目标页面
     *
     * @return
     */
    boolean isDestinationPage();

    /**
     * 获取测试的应用信息
     *
     * @return
     */
    AppInfo getAppInfo();
}
