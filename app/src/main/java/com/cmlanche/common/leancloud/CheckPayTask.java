package com.cmlanche.common.leancloud;

import android.os.AsyncTask;
import android.widget.Toast;

import com.cmlanche.activity.TaskTypeListActivity;
import com.cmlanche.application.MyApplication;
import com.cmlanche.common.DeviceUtils;
import com.cmlanche.core.utils.Logger;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class CheckPayTask extends AsyncTask<Void, Integer, Integer> {

    private TaskTypeListActivity activity;

    public CheckPayTask(TaskTypeListActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            AVQuery<AVObject> query = new AVQuery<>(AVUtils.tb_pay);
            query.whereEqualTo("serial", DeviceUtils.getDeviceSN());
            AVObject obj = query.getFirst();
            if (obj != null) {
                boolean vip = obj.getBoolean("payed");
                return vip ? 1 : -1;
            }
        } catch (Exception e) {
            Logger.e("CheckPayTask异常：" + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer vip) {
        super.onPostExecute(vip);
        if (vip == 0) {
            Toast.makeText(activity, "如果您已开通VIP，但是未生效，您需要确认下您的网络是否正常！", Toast.LENGTH_LONG).show();
        }
        boolean isVip = vip == 1;
        activity.updateVIPBtn(isVip);
        MyApplication.getAppInstance().setVip(isVip);
    }
}
