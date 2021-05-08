package com.cmlanche.core.bus;

public class EventType {
    /**
     * 启动
     */
    public static final int start_task = 1;

    /**
     * 人为暂停
     */
    public static final int pause_byhand = 2;

    /**
     * 非目标页面
     */
    public static final int pause_becauseof_not_destination_page = 3;

    /**
     * 刷新时间
     */
    public static final int refresh_time = 4;

    /**
     * 设置辅助服务对象
     */
    public static final int set_accessiblity = 5;

    /**
     * 没有获取到root的报警
     */
    public static final int no_roots_alert = 6;

    /**
     * root重新准备好
     */
    public static final int roots_ready = 7;

    /**
     * 辅助服务已连接，准备就绪，提示用户ok了
     */
    public static final int accessiblity_connected = 8;

    /**
     * 从停止状态认为启动
     */
    public static final int unpause_byhand = 9;

    /**
     * 程序跳转到目标app
     */
    public static final int goto_target_app = 10;

    /**
     * 目标app任务完成
     */
    public static final int task_finish = 11;
}
