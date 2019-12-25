package com.cmlanche.scripts;

import com.cmlanche.application.MyApplication;
import com.cmlanche.common.Constants;
import com.cmlanche.common.PackageUtils;
import com.cmlanche.core.bus.BusEvent;
import com.cmlanche.core.bus.BusManager;
import com.cmlanche.core.bus.EventType;
import com.cmlanche.core.utils.Logger;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.model.AppInfo;
import com.cmlanche.model.TaskInfo;

import java.util.List;

import static com.cmlanche.core.bus.EventType.pause_becauseof_not_destination_page;

/**
 * 任务执行器
 */
public class TaskExecutor {

    private TaskInfo taskInfo;

    private boolean isStarted = false;
    private boolean pause = false;
    private boolean forcePause = false;
    private boolean isFinished = true;
    private AppInfo currentTestApp;
    private IScript currentScript;

    private Thread scriptThread;
    private Thread monitorThread;

    private static class TaskExecutorHolder {
        private static TaskExecutor instance = new TaskExecutor();
    }

    public TaskExecutor() {
    }

    public static TaskExecutor getInstance() {
        return TaskExecutorHolder.instance;
    }

    public void startTask(final TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
        this.initStartFlags();
        if(scriptThread == null) {
            scriptThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<AppInfo> appInfos = taskInfo.getAppInfos();
                        for (AppInfo info : appInfos) {
                            currentTestApp = info;
                            IScript script = null;
                            switch (info.getPkgName()) {
                                case Constants.pkg_douyin_fast:
                                    script = new DouyinFastScript(info);
                                    break;
                                case Constants.pkg_kuaishou_fast:
                                    script = new KuaishouFastScript(info);
                                    break;
                                case Constants.pkg_douyin:
                                    script = new DouyinScript(info);
                                    break;
                                case Constants.pkg_kuaishou:
                                    script = new KuaishouScript(info);
                                    break;
                            }
                            if (script != null) {
                                currentScript = script;
                                script.execute();
                            }
                        }
                    } catch (Exception e) {
                        Logger.e("执行任务异常：" + e.getMessage(), e);
                    } finally {
                        // 执行完成
                        resetFlags();
                        PackageUtils.startSelf();
                        Logger.i("执行完成，回到本程序");
                    }
                }
            });
            scriptThread.start();

            monitorThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final long st = System.currentTimeMillis();
                    final long allTime = taskInfo.getHours() * 60 * 60 * 1000;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BusManager.getBus().post(new BusEvent<>(EventType.start_task, allTime));
                        }
                    });

                    while (System.currentTimeMillis() - st < allTime) {
                        try {
                            if (currentScript != null) {
                                if(isForcePause()) {
                                    setPause(true);
                                } else {
                                    setPause(!currentScript.isDestinationPage());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isPause()) {
                                                BusManager.getBus().post(new BusEvent<>(pause_becauseof_not_destination_page, currentScript.getAppInfo().getAppName()));
                                            } else {
                                                String s = Utils.getTimeDescription(System.currentTimeMillis() - st);
                                                BusManager.getBus().post(new BusEvent<>(EventType.refresh_time, s));
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            Logger.e("监控异常：" + e.getMessage(), e);
                        } finally {
                            Utils.sleep(1000);
                        }
                    }
                }
            });
            monitorThread.start();
        } else {
            if(currentScript != null) {
                currentScript.resetStartTime();
                currentScript.startApp();
            } else {
                Logger.e("不可能走这里，如果走这里，程序出bug了");
            }
        }
    }

    protected void runOnUiThread(Runnable runnable) {
        MyApplication.getAppInstance().getMainActivity().runOnUiThread(runnable);
    }

    /**
     * 初始化标记
     */
    private void initStartFlags() {
        this.isStarted = true;
        this.pause = false;
        this.isFinished = false;
        this.forcePause = false;
    }

    /**
     * 重置所有标记
     */
    private void resetFlags() {
        isFinished = true;
        isStarted = false;
        setPause(true);
        setForcePause(true);
    }

    /**
     * 停止任务
     */
    public void stop(boolean force) {
        setForcePause(force);
        setPause(true);
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isForcePause() {
        return forcePause;
    }

    public void setForcePause(boolean forcePause) {
        this.forcePause = forcePause;
    }

    public AppInfo getCurrentTestApp() {
        return currentTestApp;
    }
}
