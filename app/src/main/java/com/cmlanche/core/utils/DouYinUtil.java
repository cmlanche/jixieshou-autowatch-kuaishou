package com.cmlanche.core.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.File;

import static com.cmlanche.core.utils.Constant.PN_DOU_YIN;

public class DouYinUtil {
    private static boolean isKuaiShouInstalled() {
        return isInstallPackage(PN_DOU_YIN);
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
        builder.setTitle("抖音极速版未安装");
        builder.setMessage("点击确定前往应用商店下载抖音极速版");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccessibilityUtil.goToAppMarket(context,Constant.PN_DOU_YIN);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
