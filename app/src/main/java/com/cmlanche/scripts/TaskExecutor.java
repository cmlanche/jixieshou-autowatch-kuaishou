package com.cmlanche.scripts;

import android.util.Log;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.common.PackageUtils;
import com.cmlanche.core.bus.BusEvent;
import com.cmlanche.core.bus.BusManager;
import com.cmlanche.core.bus.EventType;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.model.AppInfo;
import com.cmlanche.model.TaskInfo;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Calendar;
import java.util.List;

import static com.cmlanche.core.bus.EventType.pause_becauseof_not_destination_page;

/**
 * 任务执行器
 */
public class TaskExecutor {
    private String TAG = this.getClass().getSimpleName();
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
                                case Constant.PN_DOU_YIN:
                                    if(info.getName().equals("抖音极速版-秒杀")){
                                        script = new DouyinFastShopingScript(info);
                                    }else if(info.getName().equals("抖音极速版-刷广告")){
                                        script = new DouyinFastAdvertScript(info);
                                    }else if(info.getName().equals("抖音极速版-刷视频")){

                                    }
                                    break;
                                case Constant.PN_KUAI_SHOU:
                                    script = new KuaishouFastScript(info);
                                    break;
                                case Constant.PN_TOU_TIAO:
                                    if(info.getName().equals("今日头条极速版-刷视频")){
                                        script = new TouTiaoFastScript(info);
                                    }else if(info.getName().equals("今日头条极速版-刷广告")){
                                        script = new TouTiaoAdvertScript(info);
                                    }else if(info.getName().equals("")){

                                    }
                                    break;
                                case Constant.PN_FENG_SHENG:
                                    script = new FengShengFastScript(info);
                                    break;
                                case Constant.PN_DIAN_TAO:
                                    script = new DianTaoFastScript(info);
                                    break;
                                case Constant.PN_YING_KE:
                                    script = new YingKeFastScript(info);
                                    break;
                            }
                            if (script != null) {
                                currentScript = script;
                                script.execute();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG,"执行任务异常：" + e.getMessage());
                        CrashReport.postCatchedException(e);
                    } finally {
                        // 执行完成
                        resetFlags();
                        PackageUtils.startSelf();
                        Log.i(TAG,"执行完成，回到本程序");
                        CrashReport.postCatchedException(new Exception("执行完成，回到本程序"));
                    }
                }
            });
            scriptThread.start();

            monitorThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final long st = System.currentTimeMillis();
                    Log.d(TAG,"st:"+st);
                    final long allTime = taskInfo.getHours() * 60 * 60 * 1000;
//                    final long allTime = 1 * 60* 1000;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BusManager.getBus().post(new BusEvent<>(EventType.start_task, allTime));
                        }
                    });

                    while (System.currentTimeMillis() - st < allTime) {
                        Log.d(TAG,"System.currentTimeMillis() - st:"+(System.currentTimeMillis() - st));
                        try {
                            if (currentScript != null) {
                                if(currentTestApp.getPkgName().equals(Constant.PN_FENG_SHENG)){
                                    Calendar c = Calendar.getInstance();//
                                    int mHour = c.get(Calendar.HOUR_OF_DAY);//时
                                    int mMinute = c.get(Calendar.MINUTE);//分
                                    Log.d(TAG,"mHour:"+mHour +" mMinute:" + mMinute);
                                    if((mHour == 8 && mMinute == 30) || (mHour == 22 && mMinute == 30) || (mHour == 15 && mMinute == 49)){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(!currentScript.isDestinationPage()){
                                                    PackageUtils.startApp(currentTestApp.getPkgName());
                                                }
                                            }
                                         });
                                    }else{
                                        continue;
                                    }
                                }
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
                            Log.e(TAG,"监控异常：" + e.getMessage());
                        } finally {
                            Utils.sleep(1000);
                        }
                    }
                    resetFlags();
                    PackageUtils.startSelf();
                    scriptThread.interrupt();
                    scriptThread = null;
                    monitorThread.interrupt();
                    monitorThread = null;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BusManager.getBus().post(new BusEvent<>(EventType.task_finish, currentTestApp));
                        }
                    });
                    Log.e(TAG,"到期了");
                }
            });
            monitorThread.start();
        } else {
            if(currentScript != null) {
                currentScript.resetStartTime();
                currentScript.startApp();
            } else {
                Log.e(TAG,"不可能走这里，如果走这里，程序出bug了");
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
