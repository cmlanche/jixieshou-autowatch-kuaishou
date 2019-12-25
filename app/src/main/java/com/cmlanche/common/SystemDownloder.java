package com.cmlanche.common;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.cmlanche.application.MyApplication;

public class SystemDownloder {
    private Context mContext;

    public SystemDownloder() {
        mContext = MyApplication.getAppInstance();
    }

    public long download(String url, String title, String desc) {
        Uri uri = Uri.parse(url);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        //设置WIFI下进行更新
        //req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //下载中和下载完后都显示通知栏
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
        String apkName;
        if (url.endsWith(".apk")) {
            apkName = url.substring(url.lastIndexOf("/"));
        } else {
            apkName = "sswl_game_" + System.currentTimeMillis() + ".apk";
        }
        req.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, apkName);
        req.setVisibleInDownloadsUi(true);
        //通知栏标题
        req.setTitle(title);
        //通知栏描述信息
        req.setDescription(desc);
        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive");

        //获取下载任务ID
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        long loadId = dm.enqueue(req);
        Log.i("min77", "loadId = " + loadId);
        return loadId;
    }
}
