package com.cmlanche.common.leancloud;

import android.os.AsyncTask;
import android.widget.Toast;

import com.cmlanche.activity.TaskTypeListActivity;
import com.cmlanche.core.utils.Logger;
import com.cmlanche.model.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

/**
 * 任务列表的任务
 */
public class GetTaskListTask extends AsyncTask<Void, Integer, List<AppInfo>> {

    private TaskTypeListActivity activity;
    private static final String tb_tasklist = "task_list";

    public GetTaskListTask(TaskTypeListActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<AppInfo> doInBackground(Void... voids) {
        try {
            AVQuery<AVObject> query = new AVQuery<>(tb_tasklist);
            List<AVObject> objects = query.find();
            List<AppInfo> list = new ArrayList<>();
            if (objects != null) {
                for (AVObject obj : objects) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.setName(obj.getString("name"));
                    appInfo.setFree(obj.getBoolean("isFree"));
                    appInfo.setPeriod(obj.getInt("period"));
                    appInfo.setPkgName(obj.getString("pkgName"));
                    appInfo.setUuid(obj.getObjectId());
                    list.add(appInfo);
                }
            }
            Collections.sort(list, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    if (o1.isFree()) {
                        return -1;
                    }
                    return 1;
                }
            });
            return list;
        } catch (Exception e) {
            Logger.e("获取任务里边异常:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<AppInfo> appInfos) {
        super.onPostExecute(appInfos);
        if (appInfos == null) {
            Toast.makeText(activity, "获取任务列表异常", Toast.LENGTH_LONG).show();
        } else {
            activity.updateList(appInfos);
        }
    }
}
