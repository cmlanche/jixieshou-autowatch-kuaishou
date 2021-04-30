package com.cmlanche.core.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.File;

import static com.cmlanche.core.utils.Constant.PN_MIAO_KAN;
import static com.cmlanche.core.utils.Constant.PN_TOU_TIAO;

public class MiaoKanUtil {
    private static boolean isKuaiShouInstalled() {
        return isInstallPackage(PN_MIAO_KAN);
    }
    public static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static void openKuaiShou(Context context) {
        if(!isKuaiShouInstalled()){
            showDownLoadDialog(context);
            return;
        }
        PackageUtils.startKuaishou(context);
    }

    public static void showDownLoadDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("秒看领红包未安装");
        builder.setMessage("点击确定前往应用商店下载秒看领红包");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccessibilityUtil.goToAppMarket(context,Constant.PN_MIAO_KAN);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
