package com.cmlanche.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.cmlanche.activity.MainActivity;
import com.cmlanche.activity.NewOrEditTaskActivity;
import com.cmlanche.activity.TaskTypeListActivity;
import com.cmlanche.common.PackageUtils;
import com.cmlanche.common.SPService;
import com.cmlanche.common.leancloud.InitTask;
import com.cmlanche.core.bus.BusEvent;
import com.cmlanche.core.bus.BusManager;
import com.cmlanche.core.service.MyAccessbilityService;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.core.utils.Logger;
import com.cmlanche.core.utils.SFUpdaterUtils;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.floatwindow.FloatWindow;
import com.cmlanche.floatwindow.MoveType;
import com.cmlanche.floatwindow.PermissionListener;
import com.cmlanche.floatwindow.ViewStateListener;
import com.cmlanche.jixieshou.BuildConfig;
import com.cmlanche.jixieshou.R;
import com.cmlanche.model.AppInfo;
import com.cmlanche.model.TaskInfo;
import com.cmlanche.scripts.TaskExecutor;
import com.sf.appupdater.log.LogInfo;
import com.sf.appupdater.log.LogWriter;
import com.squareup.otto.Subscribe;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Iterator;
import java.util.List;

import cn.leancloud.AVOSCloud;

import static com.cmlanche.core.bus.EventType.accessiblity_connected;
import static com.cmlanche.core.bus.EventType.goto_target_app;
import static com.cmlanche.core.bus.EventType.no_roots_alert;
import static com.cmlanche.core.bus.EventType.pause_becauseof_not_destination_page;
import static com.cmlanche.core.bus.EventType.pause_byhand;
import static com.cmlanche.core.bus.EventType.refresh_time;
import static com.cmlanche.core.bus.EventType.roots_ready;
import static com.cmlanche.core.bus.EventType.set_accessiblity;
import static com.cmlanche.core.bus.EventType.start_task;
import static com.cmlanche.core.bus.EventType.task_finish;
import static com.cmlanche.core.bus.EventType.unpause_byhand;

//import com.umeng.analytics.MobclickAgent;
//import com.umeng.commonsdk.UMConfigure;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    private MyAccessbilityService accessbilityService;
    protected static MyApplication appInstance;
    private int screenWidth;
    private int screenHeight;
    private boolean isVip = false;
    private View floatView;
    private MainActivity mainActivity;
    private boolean isFirstConnectAccessbilityService = false;
    private boolean isStarted = false;
    private String baby;

    public String getBaby() {
        return baby;
    }

    public void setBaby(String baby) {
        this.baby = baby;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.setDebug(true);
        CrashReport.initCrashReport(getApplicationContext(), "8aa7474c90", BuildConfig.DEBUG);
        initUmeng();
        initLeancloud();
        SPService.init(this);
        appInstance = this;

        LogWriter logWriter = new LogWriter() {
            @Override
            public void write(LogInfo logInfo) {
                Log.d("logInfo", "logInfo: " + logInfo);
            }
        };
        SFUpdaterUtils.setAppUpdaterInfo(this, "f6f24f301189059dbbd1046cfc20e12e", "2e69975d7e0348009687c4cbf7bcf954", true, com.sf.appupdater.Environment.PRODUCTION, false, logWriter);


        Display display = getDisplay(getApplicationContext());
        this.screenWidth = display.getWidth();
        this.screenHeight = display.getHeight();
        BusManager.getBus().register(this);

        showFloatWindow();
    }

    @Subscribe
    public void subscribeEvent(BusEvent event) {
        switch (event.getType()) {
            case set_accessiblity:
                Toast.makeText(getApplicationContext(), "服务启动成功！", Toast.LENGTH_LONG).show();
                this.accessbilityService = (MyAccessbilityService) event.getData();
                break;
            case start_task:
                this.isStarted = true;
                long time = (long) event.getData();
                setFloatText("总执行时间：" + Utils.getTimeDescription(time));
                break;
            case pause_byhand:
                if (isStarted) {
                    setFloatText("已被您暂停");
                }
                break;
            case unpause_byhand:
                if (isStarted) {
                    setFloatText("捡豆子已开始");
                }
                break;
            case pause_becauseof_not_destination_page:
                if (isStarted) {
                    // String reason = (String) event.getData();
                    setFloatText("已暂停(非任务页面)");
                }
                break;
            case goto_target_app:
                TaskInfo taskInfo = SPService.get(SPService.SP_TASK_LIST, TaskInfo.class);
                startTask(taskInfo.getAppInfos());
                break;
            case refresh_time:
                if (!TaskExecutor.getInstance().isForcePause()) {
                    if (TaskExecutor.getInstance().getCurrentTestApp().getPkgName().equals(Constant.PN_FENG_SHENG)) {
                        setFloatText("定时打卡");
                    } else {
                        setFloatText("已执行：" + event.getData());
                    }
                }
                break;
            case no_roots_alert:
//                TaskExecutor.getInstance().setForcePause(true);
                setFloatText("无法获取界面信息，请重启手机！");
                break;
            case roots_ready:
                TaskExecutor.getInstance().setForcePause(false);
                setFloatText("重新准备就绪");
                break;
            case accessiblity_connected:
                this.isFirstConnectAccessbilityService = true;
                setFloatText("准备就绪，点我启动");
                break;
            case task_finish:
                Log.d(TAG, "当前任务完成");
                AppInfo appInfo = (AppInfo) event.getData();
                TaskInfo taskInfo1 = SPService.get(SPService.SP_TASK_LIST, TaskInfo.class);
                List<AppInfo> appInfos = taskInfo1.getAppInfos();
                Iterator<AppInfo> iterator = appInfos.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getPkgName().equals(appInfo.getPkgName())) {
                        iterator.remove();
                        Log.d(TAG, "移除当前任务");
                    }
                }
                taskInfo1.setAppInfos(appInfos);
                SPService.put(SPService.SP_TASK_LIST, taskInfo1);
                if (!appInfos.isEmpty()) {
                    startTask(appInfos);
                } else {
                }
                break;
        }
    }

    private void initUmeng() {
        try {
//            UMConfigure.init(getApplicationContext(), "你的友盟appid", "main", UMConfigure.DEVICE_TYPE_PHONE, null);
//            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        } catch (Exception e) {
        }
    }

    private void initLeancloud() {
        try {
            AVOSCloud.initialize("15IzPzEVyONHdh2Sv6NgaY7N-gzGzoHsz", "FSW0TSuSrQ6sHHLwY4bsIxY7");
            new InitTask().execute();
        } catch (Exception e) {
            Logger.e(e.getMessage(), e);
        }
    }

    /**
     * Get Display
     *
     * @param context Context for get WindowManager
     * @return Display
     */
    private static Display getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            return wm.getDefaultDisplay();
        } else {
            return null;
        }
    }

    public static MyApplication getAppInstance() {
        return appInstance;
    }

    public MyAccessbilityService getAccessbilityService() {
        return accessbilityService;
    }

    public boolean isAccessbilityServiceReady() {
        return accessbilityService != null;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void showFloatWindow() {
        floatView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.floatview, null);
        TextView tv_close = floatView.findViewById(R.id.tv_close);
        TextView tv_option = floatView.findViewById(R.id.tv_option);
        FloatWindow
                .with(getApplicationContext())
                .setView(floatView)
                .setY(150)
                .setX(0)
                .setFilter(false, MainActivity.class, NewOrEditTaskActivity.class, TaskTypeListActivity.class)
                .setMoveType(MoveType.slide)
                .setMoveStyle(500, new BounceInterpolator())
                .setViewStateListener(mViewStateListener)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onSuccess() {
                        Logger.i("悬浮框授权成功");
                    }

                    @Override
                    public void onFail() {
                        Logger.i("悬浮框授权失败");
                    }
                })
                .setDesktopShow(true)
                .build();

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);//正常退出App
                Toast.makeText(getApplicationContext(), "退出捡豆子", Toast.LENGTH_LONG).show();
            }
        });


        tv_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskInfo taskInfo = SPService.get(SPService.SP_TASK_LIST, TaskInfo.class);
                if (taskInfo != null && taskInfo.getAppInfos() != null && taskInfo.getAppInfos().size() > 0 &&
                        isFirstConnectAccessbilityService) {
                    // 服务岗连接上，可以点击快速启动，不需要跳转到捡豆子app去启动
                    isFirstConnectAccessbilityService = false;
                    startTask(taskInfo.getAppInfos());
                } else if (isStarted) {
                    // 已启动，则点击会触发暂停
                    if (TaskExecutor.getInstance().isForcePause()) {
                        TaskExecutor.getInstance().setForcePause(false);
                        BusManager.getBus().post(new BusEvent<>(unpause_byhand));
                    } else {
                        TaskExecutor.getInstance().setForcePause(true);
                        BusManager.getBus().post(new BusEvent<>(pause_byhand));
                    }
                } else {
                    // 未启动状态，单击会打开捡豆子app
                    PackageUtils.startSelf();
                }
            }
        });

        tv_option.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TaskExecutor.getInstance().stop(true);
                Toast.makeText(getApplicationContext(), "捡豆子已暂停", Toast.LENGTH_LONG).show();
                PackageUtils.startSelf();
                return false;
            }
        });
    }

    private void setFloatText(String text) {
        if (floatView != null) {
            TextView textView = floatView.findViewById(R.id.tv_option);
            textView.setText(text);
        }
    }

    private ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d(TAG, "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.d(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d(TAG, "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop");
            FloatWindow.get().show();
        }
    };

    /**
     * 开始执行任务
     */
    public void startTask(List<AppInfo> appInfos) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setAppInfos(appInfos);
        TaskExecutor.getInstance().startTask(taskInfo);
    }


}
