package com.cmlanche.core.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class PackageUtils {

    /**
     * 启动某应用
     *
     * @param pkg
     */
    public static void startApp(Context context,String pkg) {
        PackageManager manager = context.getPackageManager();
        Intent LaunchIntent = manager.getLaunchIntentForPackage(pkg);
        context.startActivity(LaunchIntent);
    }

    public static void restartApp(Context context,String pkg) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.restartPackage(pkg);
    }

    /**
     * 获取当前应用的版本号
     * @return
     */
    public static int getVersionCode(Context context){
        int versionCode = 0;
        try {
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
    public static String getVersionName(Context context) {
        String versionCode = "";
        try {
            versionCode = context.getPackageManager()//拿到package管理者
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
        return versionCode;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 启动快手app
     */
    public static void startKuaishou(Context context) {
        startApp(context,"com.kuaishou.nebula");
    }

    /**
     * 启动快手app
     */
    public static void startDouyin(Context context) {
        startApp(context,"com.ss.android.ugc.aweme.lite");
    }

    public static void startSelf(Context context) {
        startApp(context,context.getPackageName());
    }
}
