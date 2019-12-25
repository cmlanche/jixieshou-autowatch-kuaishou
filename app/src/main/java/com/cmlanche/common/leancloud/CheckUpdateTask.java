package com.cmlanche.common.leancloud;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cmlanche.common.PackageUtils;
import com.cmlanche.common.SystemDownloder;
import com.cmlanche.jixieshou.R;

import androidx.appcompat.app.AlertDialog;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

/**
 * 检查更新的任务
 */
public class CheckUpdateTask extends AsyncTask<Void, Integer, AVObject> {

    private Activity activity;

    public CheckUpdateTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected AVObject doInBackground(Void... voids) {
        try {
            AVQuery<AVObject> query = new AVQuery<>(AVUtils.tb_update);
            query.whereEqualTo("flag", "update");
            AVObject obj = query.getFirst();
            if (obj != null) {
                return obj;
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Class or object doesn't exists")) {
                return initTable();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(final AVObject obj) {
        super.onPostExecute(obj);
        if (obj == null) {
            return;
        }
        if (obj.getInt("versionCode") > PackageUtils.getVersionCode()) {
            // 有新版本，提示更新
            new AlertDialog.Builder(activity)
                    .setTitle("发现新版本")
                    .setMessage(obj.getString("description"))
                    .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            new SystemDownloder().download(obj.getString("url"),
                                    String.format("%s(版本:%s)", activity.getString(R.string.app_name), obj.getString("versionName")),
                                    obj.getString("description"));
                            Toast.makeText(activity, "已开始下载，请在通知栏中查看进度", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private AVObject initTable() {
        AVObject obj = new AVObject(AVUtils.tb_update);
        obj.put("versionCode", PackageUtils.getVersionCode());
        obj.put("versionName", PackageUtils.getVersionName());
        obj.put("flag", "update");
        obj.put("description", "这是一次更新...");
        obj.put("url", "https://baidu.com");
        obj.save();
        return obj;
    }
}
