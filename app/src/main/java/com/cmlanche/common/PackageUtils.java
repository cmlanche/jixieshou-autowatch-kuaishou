package com.cmlanche.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.cmlanche.application.MyApplication;

public class PackageUtils {

    /**
     * 启动某应用
     *
     * @param pkg
     */
    public static void startApp(String pkg) {
        Context context = MyApplication.getAppInstance().getApplicationContext();
        PackageManager manager = context.getPackageManager();
        Intent LaunchIntent = manager.getLaunchIntentForPackage(pkg);
        context.startActivity(LaunchIntent);
    }

    public static void restartApp(String pkg) {
        Context context = MyApplication.getAppInstance().getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.restartPackage(pkg);
    }

    /**
     * 获取当前应用的版本号
     * @return
     */
    public static int getVersionCode(){
        int versionCode = 0;
        try {
            Context context = MyApplication.getAppInstance();
            versionCode = context.getPackageManager()//拿到package管理者
                    .getPackageInfo(context.getPackageName(),0).versionCode;
        }catch (Exception e){
        }
        return versionCode;
    }

    /**
     * 获取当前应用的版本号
     *
     * @return
     */
    public static String getVersionName() {
        String versionCode = "";
        try {
            Context context = MyApplication.getAppInstance();
            versionCode = context.getPackageManager()//拿到package管理者
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
        return versionCode;
    }

    public static String getPackageName() {
        return MyApplication.getAppInstance().getPackageName();
    }

    /**
     * 启动快手app
     */
    public static void startKuaishou() {
        startApp("com.kuaishou.nebula");
    }

    /**
     * 启动快手app
     */
    public static void startDouyin() {
        startApp("com.ss.android.ugc.aweme.lite");
    }

    public static void startSelf() {
        startApp(MyApplication.getAppInstance().getPackageName());
    }
}
